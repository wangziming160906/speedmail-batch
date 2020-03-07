package com.speedmailbatch;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * Created by wangshuai on 2017/9/7.
 */
@Component
public interface EmailHandle {

    public void sendEmail(EmailUserInfo emailUserInfo,EmailInfo emailInfo) throws MessagingException, UnsupportedEncodingException, UnirestException;

    public void receiveEmails(EmailUserInfo emailUserInfo,EmailInfo emailInfo);

    public void sendReceiveEmail(EmailUserInfo emailUserInfo,MimeMessage mimeMessage) throws MessagingException;

    public String sendEmailResult(EmailUserInfo emailUserInfo,EmailInfo emailInfo,String apikey) throws MessagingException, UnsupportedEncodingException, UnirestException;

    public String getSource();

    public String getDomain();

}
