package com.speedmailbatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by wangshuai on 2018/1/27.
 */
@RestController
public class EmailOperateControllerRest {

    @Autowired
    private EmailSendGridHandle emailSendGridHandle;

    @Autowired
    private EmailMailGunHandle emailMailGunHandle;

    @Autowired
    EmailsLoadService emailsLoadService;

    @Autowired
    EmailSendSourceControlService emailSendSourceControlService;

    @Autowired
    EmailBatchInfoService emailBatchInfoService;

    @RequestMapping(value="/test")
    public EmailResult test() throws UnirestException {
        EmailInfo emailInfo = new EmailInfo();
        emailInfo.setEmail_to("wangshuaiws0716@163.com");
        emailInfo.setEmail_from("linda<linda@bosun-map.com>");
        emailInfo.setEmail_content("dfjk");
        emailInfo.setEmail_reply_to("wangshuaiws0716@163.com");
        emailInfo.setEmail_subject("plastic injection moulds/moulding/plastic parts");
        String apiKey = "key-c12ed697321e623871fc4b3cbb0af161";

        HttpResponse response = Unirest.post("http://localhost:8081/mailgun")
                .header("content-type", "application/json")
                .header("authorization",apiKey)
                .body(JSON.toJSONString(emailInfo))
                .asJson();
        String result = response.getBody().toString();

        JSONObject jsonObject = JSON.parseObject(result);

        EmailResult javaBean = JSON.parseObject(result, EmailResult.class);

        System.out.println("======" + javaBean.getRetCode());
        return javaBean;
    }

    @RequestMapping(value="/sendgrid")
    public EmailResult sendMessageForSendgrid(@RequestBody EmailInfo emailInfo,@RequestHeader String authorization){
        System.out.println("EmailInfo emailInfo===" + emailInfo.getEmail_to() + "authorization===" + authorization);
        EmailResult result = new EmailResult();
        System.out.println("content:" + emailInfo.getEmail_content());
        String flag = "1";
        EmailUserInfo emailUserInfo = new EmailUserInfo();
        try {
            flag = emailSendGridHandle.sendEmailResult(emailUserInfo, emailInfo, authorization);
        }catch (Exception e){

        }
        if("0".equalsIgnoreCase(flag)){
            System.out.println("SENDGRID 发送邮件:"+emailInfo.getEmail_to()+ ",发送成功!");
            result.setRetCode("000000");
            result.setRetMessage("SEND MESSAGE SUCCESSFUL");
        }else {
            System.out.println("SENDGRID 发送邮件:" +emailInfo.getEmail_to()+ ",发送失败!");
            result.setRetCode("000001");
            result.setRetMessage("SEND MESSAGE FAIL");
        }
        return result;
    }

    @RequestMapping(value="/mailgun")
    public EmailResult sendMessageForMailgun(@RequestBody EmailInfo emailInfo,
                                             @RequestParam(required=false) String domain,
                                             @RequestHeader String authorization){
        EmailResult resultFlag = new EmailResult();
        String flagSend = "1";
        EmailUserInfo emailUserInfo = new EmailUserInfo();
        try {
            if(domain != null) {
                emailMailGunHandle.setDomain(domain);
            }
            flagSend = emailMailGunHandle.sendEmailResult(emailUserInfo, emailInfo, authorization);
        }catch (Exception e){

        }
        if("0".equalsIgnoreCase(flagSend)){
            System.out.println("MAINGUN 发送邮件:"+emailInfo.getEmail_to()+ ",发送成功!");
            resultFlag.setRetCode("000000");
            resultFlag.setRetMessage("SEND MESSAGE SUCCESSFUL");
        }else {
            System.out.println("MAINGUN 发送邮件:" +emailInfo.getEmail_to()+ ",发送失败!");
            resultFlag.setRetCode("000001");
            resultFlag.setRetMessage("SEND MESSAGE FAIL");
        }
        return resultFlag;
    }

    @RequestMapping(value="/apikey")
    public EmailResult apikey(@RequestBody EmailInfo emailInfo1,@RequestHeader String authorization) {
        System.out.println("EmailInfo emailInfo===" + emailInfo1.getEmail_to() + "authorization===" + authorization);
        EmailResult emailInfo = new EmailResult();
        emailInfo.setRetCode("dfds");
        return emailInfo;
    }

}
