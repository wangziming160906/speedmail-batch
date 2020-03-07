package com.speedmailbatch;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Properties;

/**
 * Created by wangshuai on 2017/9/4.
 */
public class EmailSend {

    public  static void  sendemailto() throws MessagingException {

        //设置SSL连接、邮件环境
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        /*
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
         * mail.user / mail.from
         */
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.mail.yahoo.com");
        // 发件人的账号
        props.put("mail.user", "PrestooFitzfh@yahoo.com");
        props.put("mail.smtp.port", "465");
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", "Axp063ud");

        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", "465");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);


        //设置自定义发件人昵称
        String nick="";
        try {
            nick=javax.mail.internet.MimeUtility.encodeText("phyllis@bosum-mould.com");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        message.setFrom(new InternetAddress(nick+" <"+props.getProperty("mail.user")+">"));
        // 设置发件人
        //InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
        //message.setFrom(form);


        // 设置收件人
        InternetAddress to = new InternetAddress("wangshuaiws0716@163.com");
        message.setRecipient(Message.RecipientType.TO, to);

        // 设置抄送
        //InternetAddress cc = new InternetAddress("wangshuaiws0716@163.com");
        //message.setRecipient(MimeMessage.RecipientType.CC, cc);

        // 设置密送，其他的收件人不能看到密送的邮件地址
        //InternetAddress bcc = new InternetAddress("wangshuaiws0716@163.comm");
       // message.setRecipient(MimeMessage.RecipientType.CC, bcc);

        // 设置邮件标题
        message.setSubject(EmailBodyTemplates_r.getSubject());

        // 设置邮件的内容体
        message.setContent(EmailBodyTemplates_r.getBoay(), "text/html;charset=UTF-8");

        message.saveChanges();

        // 发送邮件
        Transport.send(message);


    }


}
