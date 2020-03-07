package com.speedmailbatch;

import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangshuai on 2018/1/27.
 */

public class EmailAppRunableForCloudEmailBatch implements  Runnable{

    private EmailDbOperate emailDbOperateForSend;

    private static int LIMITE_STATIC = 10;

    private EmailBatchInfoService emailBatchInfoService;

    private EmailSendSourceControlRepository emailSendSourceControlRepository;

    private EmailAutoSendConfigRepository emailAutoSendConfigRepository;

    private EmailSendServerType emailSendServerType;


    public EmailAppRunableForCloudEmailBatch( EmailDbOperate emailDbOperate, EmailBatchInfoService emailBatchInfoService,
                                              EmailSendSourceControlRepository emailSendSourceControlRepository,
                                              EmailAutoSendConfigRepository emailAutoSendConfigRepository,
                                              EmailSendServerType emailSendServerType){
        this.emailDbOperateForSend = emailDbOperate;
        this.emailBatchInfoService = emailBatchInfoService;
        this.emailAutoSendConfigRepository = emailAutoSendConfigRepository;
        this.emailSendSourceControlRepository = emailSendSourceControlRepository;
        this.emailSendServerType = emailSendServerType;
    }

    public void run() {
        while(true) {
            this.sendEmailBatch();
        }
    }

    private static int getRunnableSend(int mini,int maxi){
        System.out.println("计算发送间隔");
        int max=maxi;
        int min=mini;
        Random random = new Random();
        int index = random.nextInt(max)%(max-min+1) + min;
        return index;
    }

    public void sendEmailBatch(){
        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件发送模块:" + datestr1+"  ";
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        String datestr = dateFormater.format(new Date());
        String is_open = "0";
        String is_open_Test = "0";
        int plan_send_count = 0;
        int fact_send_count = 0;
        int min = 0;
        int max = 0;
        String sendway = "";
        Map map = emailDbOperateForSend.getSendDay(datestr,"01");
        if(map == null){
            map = emailDbOperateForSend.getSendDay(datestr,"02");
            if(map == null){
                EmailAutoSendConfigEntity emailAutoSendConfigEntity = emailAutoSendConfigRepository.findOne("01");
                if (emailAutoSendConfigEntity != null && "1".equalsIgnoreCase(emailAutoSendConfigEntity.getIsopen())) {
                    EmailSendSourceControlEntity emailSendSourceControlEntity = new EmailSendSourceControlEntity();
                    emailSendSourceControlEntity.setPlandate(datestr);
                    emailSendSourceControlEntity.setSendway("02");
                    emailSendSourceControlEntity.setIsopen("1");
                    emailSendSourceControlEntity.setPlan_send_count(emailAutoSendConfigEntity.getSendcount());
                    emailSendSourceControlEntity.setFact_send_count(0);
                    emailSendSourceControlEntity.setSleep_minute_min(5);
                    emailSendSourceControlEntity.setSleep_minute_max(10);
                    emailSendSourceControlEntity.setIsopenTest("1");
                    emailSendSourceControlRepository.save(emailSendSourceControlEntity);
                }
                System.out.println(datestr1 + "当前时间：" + datestr + ",当前没有任务！");
                try {
                    Thread.sleep(1000 * 60 * 2);
                } catch (Exception e) {
                    System.out.println(datestr1 + "当前时间：" + datestr + ",当前没有任务！");
                }
                return;
            }
        }

        if(map != null){
            is_open_Test = map.get("IS_OPEN_TEST").toString();
            is_open = map.get("IS_OPEN").toString();
            plan_send_count = Integer.parseInt(map.get("PLAN_SEND_COUNT").toString());
            fact_send_count = Integer.parseInt(map.get("FACT_SEND_COUNT").toString());
            min = Integer.parseInt(map.get("SLEEP_MINUTE_MIN").toString());
            max = Integer.parseInt(map.get("SLEEP_MINUTE_MAX").toString());
            sendway = map.get("SEND_WAY").toString();
        }

        if("0".equals(is_open)){
            System.out.println(datestr1 + "当前时间：" + datestr + ",任务开关未开启！");
            try {
                Thread.sleep(1000 * 60 * 2);
            }catch(Exception e){
                System.out.println(datestr1 + "当前时间：" + datestr + ",任务开关未开启！");
            }
            return;
        }

        if("1".equals(is_open)) {
            System.out.println(datestr1 + "判断当前时间配置批量");
            Map batchMap = emailDbOperateForSend.getSendBatchArg(datestr);
            boolean isBatchSet = true;
            if(batchMap == null){
                isBatchSet = false;
            }

            int fact_get_count = plan_send_count - fact_send_count;

            if(fact_get_count <= 0){
                System.out.println(datestr1 + "当前时间：" + datestr + ",计划发送条数:" + plan_send_count + ",实际发送条数:" + fact_send_count + ",当天已发送完毕！");
                try {
                    Thread.sleep(1000 * 60 * 2);
                }catch(Exception e){
                    System.out.println(datestr1 + "当前时间：" + datestr + ",计划发送条数:" + plan_send_count + ",实际发送条数:" + fact_send_count + ",当天已发送完毕！");
                }
                return;
            }
            List<Map<String, Object>> toEmails = new ArrayList();
            if(fact_get_count > 0){
                if(fact_get_count >= LIMITE_STATIC){
                    fact_get_count = LIMITE_STATIC;
                }
                String fact_get_count_str = String.valueOf(fact_get_count);
                toEmails = emailDbOperateForSend.getEmailInfoList(fact_get_count_str,isBatchSet);
                String user = "";
                String userEmain = "";
                if(toEmails == null || toEmails.size() <= 0){
                    System.out.println(datestr1 + "当前时间：" + datestr + ",计划发送条数:" + plan_send_count + ",实际发送条数:" + fact_send_count + ",但没有可用发送地址！");
                    try {
                        Thread.sleep(1000 * 60 * 2);
                    }catch(Exception e){
                        System.out.println(datestr1 + "当前时间：" + datestr + ",计划发送条数:" + plan_send_count + ",实际发送条数:" + fact_send_count + ",但没有可用发送地址！");
                    }
                    return;
                }

                if (toEmails != null && toEmails.size() > 0) {
                    System.out.println(datestr1 + "当前时间：" + datestr + ",有可用发送地址！");
                    EmailSourceBean sourceBean = new EmailSourceBean();
                    sourceBean.setPlan_date(datestr);

                    //预处理为待处理状态
                    for (Map emaiinfoArrdT : toEmails) {
                        emailDbOperateForSend.upDateEmailForWait(emaiinfoArrdT.get("EMAIL_ADDR").toString());
                    }

                    for (Map emaiinfoArrd : toEmails) {
                        String apikey = "";
                        String sendsource = "";
                        String postUrl = "";
                        String source_des = "";
                        String apiwebuser = "";
                        int monthCount = 0;
                        Map resultChoose = new HashMap();
                        List<Map<String, Object>> list = emailDbOperateForSend.getAllApiKey(emailSendServerType.getTranCode());
                        if(list == null){
//                            System.out.println(datestr1 + "APIKEY不可用!");
//                            try {
//                                Thread.sleep(1000 * 60 * 2);
//                            }catch(Exception e){
//                                System.out.println(datestr1 + "APIKEY不可用!");
//                            }
//                            return;
                            break;
                        }
                        resultChoose.put("MONTH_API_KEY_COUNT",0);
                        for (Map result : list){
                            apikey = result.get("API_KEY").toString();
                            monthCount = Integer.parseInt(result.get("PLAN_MONTH_COUNT").toString());
                            SimpleDateFormat monthFormat = new SimpleDateFormat("yyyyMM");
                            String month = monthFormat.format(new Date());
                            int apiKeyMonthCount = emailDbOperateForSend.getApiKeyMonthCountByApiKey(apikey,month);

                            if(apiKeyMonthCount < monthCount){
                                int month_apikey_count = monthCount - apiKeyMonthCount;
                                int result_count = Integer.parseInt(resultChoose.get("MONTH_API_KEY_COUNT").toString());
                                if(month_apikey_count >= result_count){
                                    resultChoose = result;
                                    resultChoose.put("MONTH_API_KEY_COUNT",month_apikey_count);
                                }
                            }else {
                                System.out.println(datestr1 + ",APIKEY:" + apikey + ",月份:" + month + ",月发送量:"+ monthCount + ",已发送:"+ apiKeyMonthCount +",已用完!");
                            }
                        }
                        if(Integer.parseInt(resultChoose.get("MONTH_API_KEY_COUNT").toString()) <= 0){
                            System.out.println(datestr1 + ":所有可用APIKEY月使用量已用完!");
                            try {
                                Thread.sleep(1000 * 60 * 2);
                            }catch(Exception e){

                            }

                            break;
                        }else {
                            System.out.println("resultChoose===="  + resultChoose.toString());
                            apikey = resultChoose.get("API_KEY").toString();
                            sendsource = resultChoose.get("SEND_SOURCE").toString();
                            source_des = EmailUnit.SOURCE.get(sendsource).toString();
                            postUrl = resultChoose.get("POST_URL").toString();
                            apiwebuser = resultChoose.get("API_WEB_USER").toString();

                        }

                        sourceBean.setSend_source(sendsource);
                        EmailUserInfo userInfo = new EmailUserInfo();
                        user = EmailBodyTemplateForCloudEmail.getUser();
                        userEmain = user + "@bosun-mould.com";
                        userInfo.setEmail_user(userEmain);
                        userInfo.setAslias_name(userEmain);
                        EmailInfo emailinfo = new EmailInfo();
                        emailinfo.setEmail_from(userEmain);

                        emailinfo.setEmail_subject(EmailBodyTemplateForCloudEmail.getSubject());
                        emailinfo.setEmail_content(EmailBodyTemplateForCloudEmail.getBoay(user));
                        emailinfo.setEmail_reply_to(userEmain);

                        if("1".equalsIgnoreCase(is_open_Test)){
                            SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
                            String day = dayFormat.format(new Date());
                            int apiKeyDayCount = emailDbOperateForSend.getApiKeyDayCount(apikey,sendsource,day);
                            if(apiKeyDayCount % 50 == 0){
                                System.out.println(datestr1 + "开始发送探测邮件!");
                                emailinfo.setEmail_to("wangshuaiws0716@163.com");
                                emailinfo.setEmail_subject("用户：" + apiwebuser + ",发送探测邮件");
                            }else {
                                emailinfo.setEmail_to(emaiinfoArrd.get("EMAIL_ADDR").toString());
                            }
                        }else{
                            emailinfo.setEmail_to(emaiinfoArrd.get("EMAIL_ADDR").toString());
                        }
                        String email_batch = emaiinfoArrd.get("EMAIL_BATCH").toString();
                        String send_status = "";
                        try {
                            String emailStrFilter = emailDbOperateForSend.getFiltrateEmails();
                            String data = emaiinfoArrd.get("EMAIL_ADDR").toString();
                            String serchStr = data.substring(data.indexOf("@")).trim();
                            String flag = "1";
                            if (emailStrFilter.indexOf(serchStr) < 0) {
                                System.out.println(datestr1 + "POSTURL:" + postUrl);
                                HttpResponse response = Unirest.post(postUrl)
                                        .header("content-type", "application/json")
                                        .header("authorization",apikey)
                                        .body(JSON.toJSONString(emailinfo))
                                        .asJson();

                                EmailResult result = JSON.parseObject(response.getBody().toString(), EmailResult.class);
                                if("000000".equalsIgnoreCase(result.getRetCode())){
                                    flag = "0";
                                }
                                int sendSleep = this.getRunnableSend(min, max);
                                Thread.sleep(1000 * sendSleep);
                            }
                            System.out.println(" ");
                            if ("0".equals(flag)) {
                                send_status = EmailUnit.SEND_STATUS_SUCCESS;
                            } else {
                                send_status = EmailUnit.SEND_STATUS_FAIL;
                            }
                        } catch (UnirestException e) {
                            send_status = EmailUnit.SEND_STATUS_FAIL;
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            send_status = EmailUnit.SEND_STATUS_FAIL;
                        }finally {
                            String send_status_des = EmailUnit.SEND_STATUS.get(send_status).toString();
                            emailDbOperateForSend.upDateEmail(emailinfo,send_status,userInfo.getEmail_user(),send_status_des,sendsource,source_des);
                            emailDbOperateForSend.upDateSourceControlFactCountNew(sourceBean,send_status,sendway);
                            emailDbOperateForSend.upDateBatchSendCount(email_batch,send_status);

                            SimpleDateFormat monthFormatTemp = new SimpleDateFormat("yyyyMMdd");
                            String monthDayTemp = monthFormatTemp.format(new Date());
                            emailDbOperateForSend.updateSendSourceApiKeyCount(apikey,sendsource,monthDayTemp,send_status);
                            //更新批次表状态
                            if(emailBatchInfoService == null){
                                System.out.println("emailBatchInfoService is null");
                            }
                            EmailBatchInfoBean emailBatchInfoBean = emailBatchInfoService.getEmailBatchInfoSign(email_batch);
                            if(emailBatchInfoBean != null){
                                int batchcount = emailBatchInfoBean.getEmail_batch_count();
                                int sendfailcount = emailBatchInfoBean.getEmail_send_fail_count();
                                int sendsucccount = emailBatchInfoBean.getEmail_send_succ_count();
                                int sendCount = Integer.parseInt(emailBatchInfoBean.getPriority());
                                sendCount = sendCount  + 1;
                                if(batchcount == (sendfailcount + sendsucccount)){
                                    emailBatchInfoBean.setEmail_batch_status("02");
                                    emailBatchInfoBean.setPriority(String.valueOf(sendCount));
                                    emailBatchInfoService.updateEmailBatchInfo(emailBatchInfoBean);
                                    emailDbOperateForSend.upDateBatchControl(email_batch);
                                }
                                System.out.println("更新完毕");
                            }
                        }
                    }

                    //处理待处理状态数据
                    for (Map emaiinfoArrdRes : toEmails) {
                        emailDbOperateForSend.upDateEmailForRes(emaiinfoArrdRes.get("EMAIL_ADDR").toString());
                    }

                }
            }
        }
    }

}
