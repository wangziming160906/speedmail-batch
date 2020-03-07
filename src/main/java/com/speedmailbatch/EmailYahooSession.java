package com.speedmailbatch;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.security.Security;
import java.util.Properties;

/**
 * Created by wangshuai on 2017/9/7.
 */
public class EmailYahooSession {

    private static Session emailYahooSession;

    public static Session getSession(EmailUserInfo userInfo){

        //设置SSL连接、邮件环境
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        /*
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
         * mail.user / mail.from
         */
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        //props.put("mail.smtp.host", EmailUnit.YAHOO_SMTP_HOST);
        props.put("mail.smtp.host", EmailUnit.YEAH_SMTP_HOST);
        // 发件人的账号
        props.put("mail.user", userInfo.getEmail_user());
        props.put("mail.smtp.port", EmailUnit.YAHOO_SMTP_PORT);
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", userInfo.getEmail_password());

        props.setProperty("mail.smtp.socketFactory.class", EmailUnit.SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", String.valueOf(EmailUnit.YAHOO_SMTP_PORT));

        // 使用环境属性和授权信息，创建邮件会话
        if(emailYahooSession == null) {
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
            emailYahooSession = Session.getInstance(props, authenticator);
        }
        return emailYahooSession;
    }
}
