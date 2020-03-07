package com.speedmailbatch;

import com.mashape.unirest.http.exceptions.UnirestException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * Created by wangshuai on 2017/9/7.
 */
public class EmailYahooHandle implements EmailHandle {

    @Override
    public void sendEmail(EmailUserInfo emailUserInfo, EmailInfo emailInfo) throws MessagingException, UnsupportedEncodingException {
        Session session = EmailYahooSession.getSession(emailUserInfo);

        // 创建邮件消息
        MimeMessage message = new MimeMessage(session);

        //设置自定义发件人昵称
        String nick= javax.mail.internet.MimeUtility.encodeText(emailUserInfo.getAslias_name());

        message.setFrom(new InternetAddress(nick+" <"+emailUserInfo.getEmail_user()+">"));

        // 设置收件人
        InternetAddress to = new InternetAddress(emailInfo.getEmail_to());
        message.setRecipient(Message.RecipientType.TO, to);

        // 设置邮件标题
        message.setSubject(emailInfo.getEmail_subject());

        // 设置邮件的内容体
        message.setContent(emailInfo.getEmail_content(), "text/html;charset=UTF-8");

        message.saveChanges();

        Transport.send(message);

    }

    @Override
    public void receiveEmails(EmailUserInfo emailUserInfo, EmailInfo emailInfo) {

    }

    @Override
    public void sendReceiveEmail(EmailUserInfo emailUserInfo, MimeMessage mimeMessage) {

    }

    @Override
    public String sendEmailResult(EmailUserInfo emailUserInfo, EmailInfo emailInfo,String apikey) throws MessagingException, UnsupportedEncodingException, UnirestException {
        return null;
    }

    @Override
    public String getSource() {
        return null;
    }

    @Override
    public String getDomain() {
        return null;
    }
}
