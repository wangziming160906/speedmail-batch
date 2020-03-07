package com.speedmailbatch;

import com.mashape.unirest.http.exceptions.UnirestException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by wangshuai on 2017/9/21.
 */
public class EmailYeahHandle implements EmailHandle {

    public void sendEmail(EmailUserInfo emailUserInfo, EmailInfo emailInfo) throws MessagingException, UnsupportedEncodingException {
        Session session = EmailYeahSession.getSession(emailUserInfo);

        // 创建邮件消息
        MimeMessage message = new MimeMessage(session);

        //设置自定义发件人昵称
        String nick = javax.mail.internet.MimeUtility.encodeText(emailUserInfo.getAslias_name());

        message.setFrom(new InternetAddress(nick + " <" + emailUserInfo.getEmail_user() + ">"));

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


    public void receiveEmails(EmailUserInfo emailUserInfo, EmailInfo emailInfo) {

    }


    public void sendReceiveEmail(EmailUserInfo emailUserInfo, MimeMessage mimeMessage) throws MessagingException {
        Session session = EmailYeahSession.getSession(emailUserInfo);
        mimeMessage.reply(false);
        mimeMessage.setFrom(new InternetAddress(emailUserInfo.getEmail_user()));
        mimeMessage.setRecipients(Message.RecipientType.TO, mimeMessage.getFrom());
        Multipart mp = new MimeMultipart();
        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setContent(EmailBoayTemplatestemp.getContentReply(), "text/html;charset=UTF-8");
        mp.addBodyPart(mbp);
        mimeMessage.setContent(mp);
        mimeMessage.setSentDate(new Date());
        mimeMessage.saveChanges();
        Transport trans = session.getTransport(EmailUnit.SMTP);
        trans.connect(EmailUnit.YEAH_SMTP_HOST, emailUserInfo.getEmail_user(), emailUserInfo.getEmail_password());
        trans.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        trans.close();
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
