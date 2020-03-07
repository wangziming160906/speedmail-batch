package com.speedmailbatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by wangshuai on 2017/9/21.
 */

@Repository
public class EmailDbOperate {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public EmailUserInfo getEmailUser(){
        EmailUserInfo emailUserInfo = new EmailUserInfo();
        String sql = "SELECT * FROM EMAIL_USERS_TODAY WHERE USE_STATUS IN('00') AND USE_TIME<50";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        int max=list.size();
        if(list.size()<=0){
            return null;
        }
        int min=0;
        Random random = new Random();
        int index = random.nextInt(max)%(max-min+1) + min;
        Map map = list.get(index);
        emailUserInfo.setEmail_user((String) map.get("USER_EMAIL_ADDR"));
        emailUserInfo.setEmail_password((String) map.get("USER_EMAIL_PWS"));
        emailUserInfo.setAslias_name((String) map.get("USER_EMAIL_ASLIAS"));
        return emailUserInfo;
    }

    public EmailInfo getEmailInfo(){
        EmailInfo emailInfo = new EmailInfo();
        String sql = "SELECT * FROM EMAILS_SEND_TODAY WHERE SEND_STATUS IN ('00','02') limit 1";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return null;
        }
        Map map = list.get(0);
        emailInfo.setEmail_to((String) map.get("EMAIL_ADDR"));
        return emailInfo;
    }

    public int getEmailsCount(){
        String sql = "SELECT COUNT(*) AS COUNTNUM FROM EMAILS_SEND_TODAY WHERE SEND_STATUS IN ('00','02')";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return 0;
        }
        Map<?, ?> map = list.get(0);
        int count = Integer.parseInt(map.get("COUNTNUM").toString());
        return count;
    }

    public int getEmailUsersCount(){
        String sql = "SELECT COUNT(*) AS COUNTNUM FROM EMAIL_USERS_TODAY WHERE USE_STATUS IN ('00','01')";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return 0;
        }
        Map<?, ?> map = list.get(0);
        int count = Integer.parseInt(map.get("COUNTNUM").toString());
        return count;
    }

    public List<Map<String, Object>> getEmailInfoList(){
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送控制模块:" + datestr1+"  ";

        List<String> toEmails = new ArrayList();
        String sql = "SELECT * FROM EMAILS_SEND_TODAY WHERE SEND_STATUS IN ('00')";
        System.out.println(datestr1 + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return null;
        }
        return list;
    }

    public List<Map<String, Object>> getEmailInfoList(String limitcount,boolean isBatchSet){
        limitcount = "1";
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送控制模块:" + datestr1+"  ";

        SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
        String datestr2 = dateFormater2.format(new Date());

        List<String> toEmails = new ArrayList();
        String sql = "SELECT * FROM EMAILS_SEND_TODAY WHERE SEND_STATUS IN ('00') " +
                "AND EMAIL_VALIDATE_STATUS='01' ORDER BY EMAIL_LOAD_TIME ASC limit " + limitcount;

        if(isBatchSet){
            sql = "SELECT e.*,b.PRIORITY FROM EMAILS_SEND_TODAY e,EMAILS_BATCH_CONTROL b " +
                    "WHERE e.SEND_STATUS IN ('00') AND e.EMAIL_BATCH = b.EMAIL_BATCH AND b.IS_OPEN='1' " +
                    "ORDER BY PRIORITY DESC,EMAIL_LOAD_TIME ASC limit "
                    + limitcount ;
        }
        System.out.println(datestr1 + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return null;
        }
        return list;
    }

    public List<Map<String, Object>> getEmailInfoListForNotVari(String limitcount,Boolean batch) {
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件验证控制模块:" + datestr1+"  ";

        SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
        String datestr2 = dateFormater2.format(new Date());

        String sql = "SELECT * FROM EMAILS_SEND_TODAY WHERE EMAIL_VALIDATE_STATUS='00' ORDER BY EMAIL_LOAD_TIME ASC limit " + limitcount;
        if(batch){
            sql = "SELECT e.*,b.PRIORITY FROM EMAILS_SEND_TODAY e,EMAILS_BATCH_CONTROL b " +
                    "WHERE e.EMAIL_VALIDATE_STATUS IN ('00') " +
                    "AND e.EMAIL_BATCH = b.EMAIL_BATCH AND b.PLAN_DATE='" + datestr2 + "' ORDER BY PRIORITY DESC,EMAIL_LOAD_TIME ASC limit "
                    + limitcount ;
        }
        System.out.println(datestr1 + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list.size() <= 0) return null;
        return list;
    }


    public int upDateUser(EmailUserInfo emailUserInfo,String status){
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送控制模块:" + datestr1+"  ";

        String sql = "UPDATE EMAIL_USERS_TODAY SET USE_TIME = USE_TIME + 1,USE_STATUS='"+ status
                +"' WHERE USER_EMAIL_ADDR='" + emailUserInfo.getEmail_user()
                +"' AND USER_EMAIL_PWS='" + emailUserInfo.getEmail_password() + "'";
        System.out.println(datestr1 + sql);
        return jdbcTemplate.update(sql);
    }

    public int upDateEmail(EmailInfo emailInfo,String status,String from_email,String statusDes,String sendsource,String sendsourceDES){
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送控制模块:" + datestr1+"  ";

        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String datestr = dateFormater.format(new Date());
        String sql = "UPDATE EMAILS_SEND_TODAY SET SEND_STATUS ='" + status + "',EMAIL_SEND_ADDR='"
                + from_email+"',SEND_STATUS_DES='"+ statusDes
                + "',UPDATE_DATE='"+ datestr + "',SEND_SOURCE='"
                + sendsource+"',SEND_SOURCE_DES='" + sendsourceDES
                +"',SEND_TIME = SEND_TIME + 1 WHERE EMAIL_ADDR='"
                + emailInfo.getEmail_to() + "'";
        System.out.println(datestr1 + sql);
        return jdbcTemplate.update(sql);
    }

    public List<EmailUserInfo> getEmailUsersList(){
    	 List<EmailUserInfo> emailUsersList = new ArrayList<EmailUserInfo>();
    	 String sql = "SELECT * FROM EMAIL_USERS_TODAY WHERE USE_STATUS IN('00','01')";
         List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
         for (Map<String, Object> map : list) {
        	 EmailUserInfo emailUser = new EmailUserInfo();
             emailUser.setEmail_user(map.get("USER_EMAIL_ADDR").toString());
             emailUser.setEmail_password(map.get("USER_EMAIL_PWS").toString());
             emailUser.setAslias_name(map.get("USER_EMAIL_ASLIAS").toString());
             emailUsersList.add(emailUser);
         }
    	 return emailUsersList;
    }

    public int insertEmailResult(EmailReceiveEmailInfo emailReceiveEmailInfo){
        String sql = "INSERT INTO EMAIL_ANALY_RESULT VALUES('" + emailReceiveEmailInfo.getSendUser() + "','"
                + emailReceiveEmailInfo.getReceiverUser() + "','"  + emailReceiveEmailInfo.getSendDate().substring(0,11) + "','"
                + emailReceiveEmailInfo.getSendDate() + "','" + emailReceiveEmailInfo.getSubject() + "','"
                + emailReceiveEmailInfo.getUserAddr() + "','" + emailReceiveEmailInfo.getUserPws() + "');";
        return jdbcTemplate.update(sql);
    }

    public List<EmailUserInfo> getEmailUsersListRepair(){
        List<EmailUserInfo> emailUsersList = new ArrayList<EmailUserInfo>();
        String sql = "SELECT * FROM EMAIL_USERS_REPAIR";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : list) {
            EmailUserInfo emailUser = new EmailUserInfo();
            emailUser.setEmail_user(map.get("USER_EMAIL_ADDR").toString());
            emailUser.setEmail_password(map.get("USER_EMAIL_PWS").toString());
            emailUser.setAslias_name(map.get("USER_EMAIL_ASLIAS").toString());
            emailUsersList.add(emailUser);
        }
        return emailUsersList;
    }


    public Map<String, Object> getSendSourceArg(String sendsource,String datestr){
        String sql = "SELECT * FROM EMAILS_SEND_SOURCE_CONTROL WHERE SEND_SOURCE='"
                + sendsource + "' AND PLAN_DATE='" + datestr +"'";
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送控制模块:" + datestr1+"  ";
        System.out.println(datestr1  + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return null;
        }
        return list.get(0);
    }


    public Map<String, Object> getSendDay(String datestr,String sendway){
        String sql = "SELECT * FROM EMAILS_SEND_SOURCE_CONTROL WHERE PLAN_DATE='" + datestr +"' AND SEND_WAY='" + sendway + "'";
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送控制模块:" + datestr1+"  ";
        System.out.println(datestr1  + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return null;
        }
        return list.get(0);
    }

    public Map<String, Object> getSendBatchArg(String datestr){
        String sql = "SELECT * FROM EMAILS_BATCH_CONTROL WHERE IS_OPEN='1'";
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送控制模块:" + datestr1+"  ";
        System.out.println(datestr1  + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return null;
        }
        return list.get(0);
    }


    public int upDateSourceControlFactCount(EmailSourceBean emailSourceBean,String sendStatus) {
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送控制模块:  " + datestr1+"  ";
        String sql_status = "";
        if(sendStatus.equals(EmailUnit.SEND_STATUS_SUCCESS)){
            sql_status = ",FACT_SEND_SUCC_COUNT= FACT_SEND_SUCC_COUNT + 1 ";
        }
        if(sendStatus.equals(EmailUnit.SEND_STATUS_FAIL)){
            sql_status = ",FACT_SEND_FAIL_COUNT= FACT_SEND_FAIL_COUNT + 1 ";
        }
        String sql = "UPDATE EMAILS_SEND_SOURCE_CONTROL SET FACT_SEND_COUNT = FACT_SEND_COUNT + 1 " + sql_status
                + " WHERE SEND_SOURCE='" + emailSourceBean.getSend_source()
                + "' AND PLAN_DATE='" + emailSourceBean.getPlan_date() + "'";
        System.out.println(datestr1 + sql);
        return jdbcTemplate.update(sql);
    }


    public int upDateSourceControlFactCountNew(EmailSourceBean emailSourceBean,String sendStatus,String sendway) {
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "========邮件发送控制模块:" + datestr1+"  ";
        String sql_status = "";
        if(sendStatus.equals(EmailUnit.SEND_STATUS_SUCCESS)){
            sql_status = ",FACT_SEND_SUCC_COUNT= FACT_SEND_SUCC_COUNT + 1 ";
        }
        if(sendStatus.equals(EmailUnit.SEND_STATUS_FAIL)){
            sql_status = ",FACT_SEND_FAIL_COUNT= FACT_SEND_FAIL_COUNT + 1 ";
        }
        String sql = "UPDATE EMAILS_SEND_SOURCE_CONTROL SET FACT_SEND_COUNT = FACT_SEND_COUNT + 1 " + sql_status
                + " WHERE PLAN_DATE='" + emailSourceBean.getPlan_date() + "' AND SEND_WAY='" + sendway + "'";
        System.out.println(datestr1 + sql);
        return jdbcTemplate.update(sql);
    }

    public int upDateVerityEmail(String emailaddr,String email_validate_status,String email_validate_status_des) {
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件验证模块:" + datestr1+"  ";

        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String datestr = dateFormater.format(new Date());
        String sql = "UPDATE EMAILS_SEND_TODAY SET EMAIL_VALIDATE_STATUS ='"
                + email_validate_status + "',EMAIL_VALIDATE_STATUS_DES='"
                + email_validate_status_des + "',VALIDATE_DATE='"+ datestr
                + "',VALIDATE_TRY_TIMES=VALIDATE_TRY_TIMES + 1"
                +" WHERE EMAIL_ADDR='" + emailaddr + "'";
        System.out.println(datestr1 + sql);
        return jdbcTemplate.update(sql);
    }

    public int inSertSendEmails(String emailsAddr,String email_batch,String email_batch_des){
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件批量导入模块:" + datestr1+"  ";

        SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String datestr2 = dateFormater2.format(new Date());

        String sql = "INSERT INTO EMAILS_SEND_TODAY(EMAIL_ADDR,EMAIL_LOAD_TIME,EMAIL_BATCH,EMAIL_BATCH_DES) " +
                "VALUES('" + emailsAddr + "','" + datestr2 +"','"+ email_batch+"','"+ email_batch_des +"')";
        System.out.println(datestr1 + sql);
        return jdbcTemplate.update(sql);
    }

    public int getValidateEmailCount(String validateDate){

        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件验证模块:" + datestr1+"  ";

        String sql = "SELECT COUNT(*) AS COUNTNUM FROM EMAILS_SEND_TODAY WHERE VALIDATE_DATE='" + validateDate + "'";
        System.out.println(datestr1 + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return 0;
        }
        Map<?, ?> map = list.get(0);
        int count = Integer.parseInt(map.get("COUNTNUM").toString());
        return count;
    }

    public Map<String, Object> getValiDateArg(){

        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件验证模块:" + datestr1+"  ";
        String sql = "SELECT * FROM EMAILS_VALIDATE_CONTROL";
        System.out.println(datestr1 + sql);

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return null;
        }
        return list.get(0);
    }

    public int getEmailValidateDate(String validate){
        String sql = "SELECT COUNT(*) AS COUNTNUM FROM EMAILS_VALIDATE_INFO WHERE VALIDATE_DATE='" + validate + "'" ;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return 0;
        }
        Map<?, ?> map = list.get(0);
        int count = Integer.parseInt(map.get("COUNTNUM").toString());
        return count;
    }

    public int insertEmailEmailValidate(String validate){
        String sql = "INSERT INTO EMAILS_VALIDATE_INFO(VALIDATE_DATE) VALUES('" + validate + "');";
        return jdbcTemplate.update(sql);
    }


    public int upDateEmailEmailValidate(String validate,String valistatus) {
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件验证模块:" + datestr1+"  ";
        String sql_status = "";
        if(valistatus.equals(EmailUnit.VALIDATE_STATUS_SUCCESS)){
            sql_status = "SET EMAIL_VALIDATE_SUCCESS_COUNT= EMAIL_VALIDATE_SUCCESS_COUNT + 1 ";
        }
        if(valistatus.equals(EmailUnit.VALIDATE_STATUS_FAIL)){
            sql_status = "SET EMAIL_VALIDATE_FAIL_COUNT= EMAIL_VALIDATE_FAIL_COUNT + 1 ";
        }
        String sql = "UPDATE EMAILS_VALIDATE_INFO " + sql_status
                + " WHERE VALIDATE_DATE='" + validate + "'";
        System.out.println(datestr1 + sql);
        return jdbcTemplate.update(sql);
    }


    public int getEmailBatch(String validate){
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件批次导入模块:" + datestr1+"  ";

        String sql = "SELECT MAX(EMAIL_BATCH_NUM) AS EMAIL_BATCH_NUM FROM EMAILS_BATCH_INFO WHERE EMAIL_BATCH_DATE='" + validate + "'" ;

        System.out.println(datestr1 + sql);

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        Map<?, ?> map = list.get(0);
        if(map.get("EMAIL_BATCH_NUM") == null){
            return 1;
        }
        int count = Integer.parseInt(map.get("EMAIL_BATCH_NUM").toString());
        count = count + 1;
        return count;
    }


    public int inSertBatchInfo(String email_batch,String email_batch_desc,int num){
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件批次导入模块:" + datestr1+"  ";

        SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
        String datestr2 = dateFormater2.format(new Date());

        String sql = "INSERT INTO EMAILS_BATCH_INFO(EMAIL_BATCH,EMAIL_BATCH_DES,EMAIL_BATCH_DATE,EMAIL_BATCH_NUM) " +
                "VALUES('" + email_batch + "','" + email_batch_desc +"','" + datestr2+ "',"+ num +")";
        System.out.println(datestr1 + sql);

        return jdbcTemplate.update(sql);
    }

    public int upDateBatchValidateCount(String email_batch,String email_validate_status) {
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送控制模块:" + datestr1+"  ";
        String sql_status = "";
        if(email_validate_status.equals(EmailUnit.VALIDATE_STATUS_SUCCESS)){
            sql_status = " EMAIL_VALIDATE_SUCC_COUNT= EMAIL_VALIDATE_SUCC_COUNT + 1 ";
        }
        if(email_validate_status.equals(EmailUnit.VALIDATE_STATUS_FAIL)){
            sql_status = " EMAIL_VALIDATE_FAIL_COUNT= EMAIL_VALIDATE_FAIL_COUNT + 1 ";
        }
        String sql = "UPDATE EMAILS_BATCH_INFO SET  " + sql_status
                + " WHERE EMAIL_BATCH='" + email_batch + "'";
        System.out.println(datestr1 + sql);
        return jdbcTemplate.update(sql);
    }


    public int upDateBatchSendCount(String email_batch,String email_send_status) {
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送控制模块:" + datestr1+"  ";
        String sql_status = "";
        if(email_send_status.equals(EmailUnit.SEND_STATUS_SUCCESS)){
            sql_status = " EMAIL_SEND_SUCC_COUNT= EMAIL_SEND_SUCC_COUNT + 1 ";
        }
        if(email_send_status.equals(EmailUnit.SEND_STATUS_FAIL)){
            sql_status = " EMAIL_SEND_FAIL_COUNT= EMAIL_SEND_FAIL_COUNT + 1 ";
        }
        String sql = "UPDATE EMAILS_BATCH_INFO SET  " + sql_status
                + " WHERE EMAIL_BATCH='" + email_batch + "'";
        System.out.println(datestr1 + sql);
        return jdbcTemplate.update(sql);
    }


    public int getBatchInfo(String emailBatch){
        String sql = "SELECT COUNT(*) AS COUNTNUM FROM EMAILS_BATCH_CONTROL WHERE EMAIL_BATCH='" + emailBatch + "'" ;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return 0;
        }
        Map<?, ?> map = list.get(0);
        int count = Integer.parseInt(map.get("COUNTNUM").toString());
        return count;
    }

    public int insertBatchControl(String emailBatch,String plandate,String isopen,String priority){
        String sql = "";
        sql = "INSERT INTO EMAILS_BATCH_CONTROL VALUES('" + emailBatch + "','" + plandate + "','"
                + isopen + "','" + priority + "')";
        System.out.println(sql);
        return jdbcTemplate.update(sql);
    }


    public int updateBatchControl(String  emailBatch,String plandate,String isopen,String priority){
        String sql = "";
        sql = "UPDATE EMAILS_BATCH_CONTROL SET PLAN_DATE='"
                + plandate +"',IS_OPEN='" + isopen + "',PRIORITY='" + priority
                +"' WHERE EMAIL_BATCH='" +  emailBatch + "'";
        System.out.println(sql);
        return jdbcTemplate.update(sql);
    }


    public int inSertFiltrateEmails(String emailsAddr){
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件过滤导入模块:" + datestr1+"  ";

        SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
        String datestr2 = dateFormater2.format(new Date());

        String sql = "INSERT INTO EMAIL_FILTRATE_INFO(EMAIL_ADDR,EMAIL_IMPORT_DATE) " +
                "VALUES('" + emailsAddr + "','" + datestr2 +"')";
        System.out.println(datestr1 + sql);
        return jdbcTemplate.update(sql);
    }


    public String getFiltrateEmails(){
        String data = "";
        String sql = "SELECT  EMAIL_ADDR FROM EMAIL_FILTRATE_INFO";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : list) {
            String temp = map.get("EMAIL_ADDR").toString();
            data = data + temp + "|";
        }
        return data;
    }

    public List getApiKey(String sendsource){
        String apikey ="";
        String sql = "SELECT * FROM EMAIL_APIKEY_INFO WHERE SEND_SOURCE='" + sendsource
                + "' AND APIKEY_STATUS='01' ORDER BY API_KEY";
        System.out.println("邮件发送模块SENDSOURCE获取AIPKEY " + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()==0){
            return null;
        }
        return list;
    }

    public List getAllApiKey(String sendsource){
        String apikey ="";
        String sql = "SELECT * FROM EMAIL_APIKEY_INFO WHERE APIKEY_STATUS='01' AND SEND_SOURCE='" + sendsource + "' ORDER BY API_KEY";
        System.out.println("邮件发送模块ALL获取AIPKEY " + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()==0){
            return null;
        }
        return list;
    }

    public int updateSendSourceApiKey(String apikey,String sendsource){
        String sql = "";
        sql = "UPDATE EMAIL_APIKEY_INFO SET FACT_DAY_COUNT=FACT_DAY_COUNT + 1 WHERE API_KEY='"
                +  apikey + "' AND SEND_SOURCE='" + sendsource + "'";
        System.out.println("邮件发送模块 更新AIPKEY发送条数 " + sql);
        return jdbcTemplate.update(sql);
    }


    public int getApiKeyMonthCount(String apikey,String sendsource,String month){
        String sql = "SELECT SUM(FACT_DAY_SUCESS_COUNT + FACT_DAY_FAIL_COUNT) AS COUNTNUM FROM EMAIL_APIKEY_SEND_DAY_INFO WHERE API_KEY='"
                + apikey + "' AND SEND_SOURCE='" +  sendsource + "' AND SEND_DATE like '" + month +"%'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return 0;
        }
        Map<?, ?> map = list.get(0);
        int count = 0;
        if(map.get("COUNTNUM") != null) {
            count = Integer.parseInt(map.get("COUNTNUM").toString());
        }
        System.out.println("邮件发送模块,   获取EMAIL_APIKEY_SEND_DAY_INFO发送条数：" + sql);
        return count;
    }


    public int getApiKeyMonthCountByApiKey(String apikey,String month){
        String sql = "SELECT SUM(FACT_DAY_SUCESS_COUNT + FACT_DAY_FAIL_COUNT) AS COUNTNUM FROM EMAIL_APIKEY_SEND_DAY_INFO WHERE API_KEY='"
                + apikey + "' AND SEND_DATE like '" + month +"%'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return 0;
        }
        Map<?, ?> map = list.get(0);
        int count = 0;
        if(map.get("COUNTNUM") != null) {
            count = Integer.parseInt(map.get("COUNTNUM").toString());
        }
        System.out.println("邮件发送模块, 获取EMAIL_APIKEY_SEND_DAY_INFO发送条数：" + sql);
        return count;
    }

    public int getApiKeyDayCount(String apikey,String sendsource,String day){
        String sql = "SELECT SUM(FACT_DAY_SUCESS_COUNT + FACT_DAY_FAIL_COUNT) AS COUNTNUM FROM EMAIL_APIKEY_SEND_DAY_INFO WHERE API_KEY='"
                + apikey + "' AND SEND_SOURCE='" +  sendsource + "' AND SEND_DATE = '" + day +"'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return 0;
        }
        Map<?, ?> map = list.get(0);
        int count = 0;
        if(map.get("COUNTNUM") != null) {
            count = Integer.parseInt(map.get("COUNTNUM").toString());
        }
        System.out.println("邮件发送模块,获取EMAIL_APIKEY_SEND_DAY_INFO 当天发送条数：" + sql);
        return count;
    }

    public int updateSendSourceApiKeyCount(String apikey,String sendsource,String monthDay,String send_status){
        String sql = "";
        if(EmailUnit.SEND_STATUS_SUCCESS.equals(send_status)){
            sql = "UPDATE EMAIL_APIKEY_SEND_DAY_INFO SET FACT_DAY_SUCESS_COUNT=FACT_DAY_SUCESS_COUNT + 1 WHERE API_KEY='"
                    +  apikey + "' AND SEND_SOURCE='" + sendsource + "' AND SEND_DATE='" + monthDay + "'";
        }else if(EmailUnit.SEND_STATUS_FAIL.equals(send_status)){
            sql = "UPDATE EMAIL_APIKEY_SEND_DAY_INFO SET FACT_DAY_FAIL_COUNT=FACT_DAY_FAIL_COUNT + 1 WHERE API_KEY='"
                    +  apikey + "' AND SEND_SOURCE='" + sendsource + "' AND SEND_DATE='" + monthDay + "'";
        }
        int result = jdbcTemplate.update(sql);
        if(result <= 0) {
            if(EmailUnit.SEND_STATUS_SUCCESS.equals(send_status)){
                sql = "INSERT INTO EMAIL_APIKEY_SEND_DAY_INFO VALUES ('" + apikey + "','"+ sendsource +"','"+ monthDay + "',1,0)";
            }else if(EmailUnit.SEND_STATUS_FAIL.equals(send_status)){
                sql = "INSERT INTO EMAIL_APIKEY_SEND_DAY_INFO VALUES ('" + apikey + "','"+ sendsource +"','"+ monthDay + "',0,1)";
            }else {
                sql = "INSERT INTO EMAIL_APIKEY_SEND_DAY_INFO VALUES ('" + apikey + "','"+ sendsource +"','"+ monthDay + "',0,0)";
            }

            result = jdbcTemplate.update(sql);
        }
        System.out.println("邮件发送模块 更新 EMAIL_APIKEY_SEND_DAY_INFO发送条数 " + sql);
        return result;
    }


    public int upDateBatchControl(String email_batch){
        String sql = "";
        sql = "UPDATE EMAILS_BATCH_CONTROL SET IS_OPEN='0' WHERE EMAIL_BATCH='" + email_batch + "'";
        int result = jdbcTemplate.update(sql);
        System.out.println("邮件发送模块 更新 EMAIL_Batch_Control " + sql);
        return result;
    }

    public int upDateEmailForWait(String emailaddr){
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送控制模块:" + datestr1+"  ";

        String sql = "UPDATE EMAILS_SEND_TODAY SET SEND_STATUS ='03',SEND_STATUS_DES='待处理' WHERE EMAIL_ADDR='"
                + emailaddr + "' AND SEND_STATUS='00'";
        System.out.println(datestr1 + sql);

        return jdbcTemplate.update(sql);
    }

    public int upDateEmailForRes(String emailaddr){
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送控制模块:" + datestr1+"  ";

        String sql = "UPDATE EMAILS_SEND_TODAY SET SEND_STATUS ='00',SEND_STATUS_DES='初始化' WHERE EMAIL_ADDR='"
                + emailaddr + "' AND SEND_STATUS='03'";
        System.out.println(datestr1 + sql);

        return jdbcTemplate.update(sql);
    }


    public Map getEmailInfoOne(String emailaddr){
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送控制模块:" + datestr1+"  ";

        List<String> toEmails = new ArrayList();
        String sql = "SELECT * FROM EMAILS_SEND_TODAY WHERE EMAIL_ADDR='" + emailaddr + "'";
        System.out.println(datestr1 + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()<=0){
            return null;
        }
        Map<?, ?> map = list.get(0);
        return map;
    }

    public String upDateEmailTransaction(String emailaddr){
        String result = "normal";
        Connection con = null;
        try {
            con=jdbcTemplate.getDataSource().getConnection();//设置不自动提交
            con.setAutoCommit(false);
            List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM EMAILS_SEND_TODAY WHERE EMAIL_ADDR='" + emailaddr + "' FOR UPDATE;");
            if(list.size()>0){
                Map<?, ?> map = list.get(0);
                if(map.get("SEND_STATUS") != null) {
                    String send_status = map.get("SEND_STATUS").toString();
                    if("00".equalsIgnoreCase(send_status)) {
                        String sql = "UPDATE EMAILS_SEND_TODAY SET SEND_STATUS ='03',SEND_STATUS_DES='待处理' WHERE EMAIL_ADDR='"
                                + emailaddr + "'";
                        jdbcTemplate.update(sql);
                        return "normal";
                    }else {
                        return emailaddr;
                    }
                }

            }
            con.commit();
            con.setAutoCommit(true);//还原
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }


    public int upDateSourceControlFactCountTemp(EmailSourceBean emailSourceBean,String count,String sendway) {
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "========邮件发送控制模块:" + datestr1+"  ";
        String sql = "UPDATE EMAILS_SEND_SOURCE_CONTROL SET FACT_SEND_TEMP_COUNT = FACT_SEND_TEMP_COUNT +" + count
                + " WHERE PLAN_DATE='" + emailSourceBean.getPlan_date() + "' AND SEND_WAY='" + sendway + "'";
        System.out.println(datestr1 + sql);
        return jdbcTemplate.update(sql);
    }


    public int upDateSourceControlFactCountTempAll(EmailSourceBean emailSourceBean,String count,String sendway) {
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "========邮件发送控制模块:" + datestr1+"  ";
        String sql = "UPDATE EMAILS_SEND_SOURCE_CONTROL SET FACT_SEND_TEMP_COUNT = " + count
                + " WHERE PLAN_DATE='" + emailSourceBean.getPlan_date() + "' AND SEND_WAY='" + sendway + "'";
        System.out.println(datestr1 + sql);
        return jdbcTemplate.update(sql);
    }

}
