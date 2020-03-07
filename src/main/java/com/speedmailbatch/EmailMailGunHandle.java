package com.speedmailbatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * Created by wangshuai on 2018/1/31.
 */
@Service
public class EmailMailGunHandle implements EmailHandle {

    private String MAILGUNURL = "https://api.mailgun.net/v3/";

    private static String SOURCE = "02";

    private String domain = "premiermfgco.com";

    @Override
    public void sendEmail(EmailUserInfo emailUserInfo, EmailInfo emailInfo) throws MessagingException, UnsupportedEncodingException, UnirestException {

    }

    @Override
    public void receiveEmails(EmailUserInfo emailUserInfo, EmailInfo emailInfo) {

    }

    @Override
    public void sendReceiveEmail(EmailUserInfo emailUserInfo, MimeMessage mimeMessage) throws MessagingException {

    }

    @Override
    public String sendEmailResult(EmailUserInfo emailUserInfo, EmailInfo emailInfo,String apikey) throws MessagingException, UnsupportedEncodingException, UnirestException {
        String flag = "1";
        HttpResponse<JsonNode> request = Unirest.post(MAILGUNURL + domain + "/messages")
                .basicAuth("api", apikey)
                .queryString("from", emailInfo.getEmail_from() + " <" + emailInfo.getEmail_from() + ">")
                .queryString("to", emailInfo.getEmail_to())
                .queryString("subject", emailInfo.getEmail_subject())
                .queryString("html", emailInfo.getEmail_content())
                .queryString("h:reply-to",emailInfo.getEmail_reply_to())
                .asJson();
        String respone = request.getBody().toString();
        System.out.println("MailGun 发送结果:" + respone);
        JSONObject jsonObject = JSON.parseObject(respone);
        String message = jsonObject.getString("message").toString();

        if("Queued. Thank you.".equalsIgnoreCase(message)){
            flag = "0";
        }
        return flag;
    }

    @Override
    public String getSource() {
        return SOURCE;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
