package com.speedmailbatch;

import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.speedmailbatch.EmailUnit.*;

/**
 * Created by wangshuai on 2018/1/29.
 */
@Component
public class EmailAppRunableForVeriEmail implements  Runnable{

    @Autowired
    private EmailDbOperate emailDbOperate;

    private static final String SENDER_EMAIL = "no-reply@domain.com";//"no-reply@domain.com";

    //private static final String SENDER_EMAIL = "check@verify-email.org";

    private static final String SENDER_EMAIL_SERVER = SENDER_EMAIL.split("@")[1];

    private static int LIMITE_STATIC = 10;

    public boolean checkEmailMethod(String email) {
        if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            System.err.println("邮件格式不合法!");
            return false;
        }

        String log = "";
        String host = "";
        String hostName = email.split("@")[1];
        Record[] result = null;
        SMTPClient client = new SMTPClient();
        client.setConnectTimeout(16000);  //设置连接超时时间,有些服务器比较慢

        try {
            // 查找MX记录
            Lookup lookup = new Lookup(hostName, Type.MX);
            lookup.run();
            if (lookup.getResult() != Lookup.SUCCESSFUL) {
                log += "找不到MX记录\n";
                return false;
            } else {
                result = lookup.getAnswers();
            }

            int count = 0;
            for (int i = 0; i < result.length; i++) {
                log = "";
                host = result[i].getAdditionalName().toString();
                try {
                    client.connect(host);   //连接到接收邮箱地址的邮箱服务器
                } catch (Exception e) {        //捕捉连接超时的抛出的异常
                    count++;
                    if (count >= result.length) {    //如果由MX得到的result服务器都连接不上，则认定email无效
                        log += "Connect mail server timeout...\n";
                        return false;
                    }
                }

                if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {   //服务器通信不成功
                    client.disconnect();
                    continue;
                } else {
                    log += "MX record about " + hostName + " exists.\n";
                    log += "Connection succeeded to " + host + "\n";
                    log += client.getReplyString();

                    // HELO <$SENDER_EMAIL_SERVER>   //domain.com
                    try {
                        client.login(SENDER_EMAIL_SERVER);   //这一步可能会出现空指针异常
                    } catch (Exception e) {
                        return false;
                    }
                    log += ">HELO " + SENDER_EMAIL_SERVER + "\n";
                    log += "=" + client.getReplyString();

                    client.setSender(SENDER_EMAIL);
                    if (client.getReplyCode() != 250) {     //为解决hotmail有的MX可能出现=550 OU-001 (SNT004-MC1F43) Unfortunately, messages from 116.246.2.245 weren't sent.
                        client.disconnect();
                        continue;                           //把client.login 和client.setSender放在循环体内，这样所有的如果某mx不行就换其他mx，但这样会对无效的邮箱进行所有mx遍历，耗时
                    }
                    log += ">MAIL FROM: <" + SENDER_EMAIL + ">\n";
                    log += "=" + client.getReplyString();
                    // RCPT TO: <$email>
                    try {
                        client.addRecipient(email);
                    } catch (Exception e) {
                        return false;
                    }
                    log += ">RCPT TO: <" + email + ">\n";
                    log += "=" + client.getReplyString();

                    //最后从收件邮箱服务器返回true，说明服务器中能够找到此收件地址，邮箱有效
                    if (250 == client.getReplyCode()) {
                        return true;
                    }
                    client.disconnect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
            }
            System.out.println(log);
        }
        return false;
    }

    private static int getRunnableSleep(){
        int max = 4;
        int min = 2;
        Random random = new Random();
        int index = random.nextInt(max)%(max-min+1) + min;
        return index;
    }

    private void checkBatch(){

        SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr1 = dateFormater1.format(new Date());
        datestr1 = "邮件验证模块:" + datestr1+"  ";

        String is_open = "";
        int email_validate_count = 0;
        Map map = emailDbOperate.getValiDateArg();
        boolean flag = false;
        String log = "";
        if(map != null){
            is_open = map.get("IS_OPEN").toString();
            email_validate_count = Integer.parseInt(map.get("EMAIL_VALIDATE_COUNT").toString());
            if("0".equals(is_open)){
                flag = true;
                log= datestr1 + "邮件验证未开启！";
            }
        }else{
            flag = true;
            log = datestr1 + "邮件验证控制参数未配置";
        }
        if(flag){
            System.out.println(log);
            try {
                Thread.sleep(1000 * 60 * 2);
            }catch(Exception e){

            }
            return;
        }
        //检查当天验证数量
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        String datestr = dateFormater.format(new Date());
        int count = emailDbOperate.getValidateEmailCount(datestr);
        System.out.println(datestr1 + " 当天:"+ datestr +",已验证数量：" + count);
        if(count >=email_validate_count){
            try {
                Thread.sleep(1000 * 60 * 3);
            }catch(Exception e){}
            return;
        }

        //检查EMAILS_VALIDATE_INFO是否有当天数据
        int validatecount = emailDbOperate.getEmailValidateDate(datestr);
        if(validatecount<=0){
            emailDbOperate.insertEmailEmailValidate(datestr);
        }

        int limitcount_int = email_validate_count - count;
        if(limitcount_int >= LIMITE_STATIC){
            limitcount_int = LIMITE_STATIC;
        }
        String limitcount = String.valueOf(limitcount_int);
        List<Map<String, Object>> toNeedVriEmails = new ArrayList();

        //判断当前时间配置批量
        Map batchMap = emailDbOperate.getSendBatchArg(datestr);
        boolean isBatchSet = true;
        if(batchMap == null){
            isBatchSet = false;
        }
        toNeedVriEmails = emailDbOperate.getEmailInfoListForNotVari(limitcount,isBatchSet);

        if(toNeedVriEmails == null || toNeedVriEmails.size() <= 0){
            System.out.println(datestr1 + " 当前没有需要验证的邮箱！");
            try {
                Thread.sleep(1000 * 60 * 3);
            }catch(Exception e){}
            return;
        }

        if (toNeedVriEmails != null && toNeedVriEmails.size() > 0) {
            for (Map emaiinfoArrd : toNeedVriEmails) {
                String email_validate_status = "";
                email_validate_status = VALIDATE_STATUS_INIT;
                String email_addr = emaiinfoArrd.get("EMAIL_ADDR").toString();
                String email_batch = emaiinfoArrd.get("EMAIL_BATCH").toString();
                boolean verityResult = checkEmailMethod(email_addr);
                if(verityResult){
                    email_validate_status = VALIDATE_STATUS_SUCCESS;
                }else{
                    email_validate_status = VALIDATE_STATUS_FAIL;
                }
                //更新数据记录
                String  email_validate_status_des = VALIDATE_STATUS.get(email_validate_status);
                emailDbOperate.upDateVerityEmail(email_addr,email_validate_status,email_validate_status_des);
                emailDbOperate.upDateEmailEmailValidate(datestr,email_validate_status);
                emailDbOperate.upDateBatchValidateCount(email_batch,email_validate_status);
                try {
                    int sleepcount = this.getRunnableSleep();
                    Thread.sleep(1000 * sleepcount);
                }catch(Exception e){

                }
            }
        }
    }
    @Override
    public void run() {
        while(true) {
            this.checkBatch();
        }
    }
}
