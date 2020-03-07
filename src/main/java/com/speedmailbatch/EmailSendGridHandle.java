package com.speedmailbatch;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * Created by wangshuai on 2018/1/27.
 */
@Service
public class EmailSendGridHandle implements EmailHandle {

    private static String SendGridUrl = "https://api.sendgrid.com/v3/mail/send";

    private static String SOURCE = "01";

    private static String DOMAIN = "@bosun-mould.com";

    @Override
    public void sendEmail(EmailUserInfo emailUserInfo, EmailInfo emailInfo) throws MessagingException, UnsupportedEncodingException, UnirestException {

    }

    public String sendEmailResult(EmailUserInfo emailUserInfo, EmailInfo emailInfo,String apikey) throws MessagingException, UnsupportedEncodingException, UnirestException {
        String flag = "0";
        String authorization = "Bearer " + apikey;
        System.out.println(authorization);
        String body = "{\"personalizations\":[{\"to\":[{\"email\":\""+ emailInfo.getEmail_to()
                + "\",\"name\":\"" + emailInfo.getEmail_to() + "\"}],"
                + "\"subject\":\"" + emailInfo.getEmail_subject()
                +"\"}],\"from\":{\"email\":\"" + emailInfo.getEmail_from()
                + "\",\"name\":\"" + emailInfo.getEmail_from() +"\"},"
                + "\"reply_to\":{\"email\":\""+ emailInfo.getEmail_reply_to() +"\",\"name\":\" "+ emailInfo.getEmail_reply_to()+"\"},"
                + "\"content\":[{\"type\":\"text/html\",\"value\":\""+ emailInfo.getEmail_content() +"\"}]}";
        HttpResponse<JsonNode> request = Unirest.post(SendGridUrl)
                .header("authorization", authorization)
                .header("content-type", "application/json")
                .body(body)
                .asJson();
        System.out.println(body);
        String respone = request.getBody().toString();
        System.out.println(respone);
        if(respone.indexOf("error")>=0){
            flag = "1";
        }
        return flag;
    }

    @Override
    public String getSource() {
        return SOURCE;
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }

    @Override
    public void receiveEmails(EmailUserInfo emailUserInfo, EmailInfo emailInfo) {

    }

    @Override
    public void sendReceiveEmail(EmailUserInfo emailUserInfo, MimeMessage mimeMessage) throws MessagingException {

    }
}
