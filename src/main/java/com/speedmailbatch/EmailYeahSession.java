package com.speedmailbatch;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.security.Security;
import java.util.Properties;

/**
 * Created by wangshuai on 2017/9/22.
 */
public class EmailYeahSession {

    private static Session emailYeahSession;

    public static Session getSession(EmailUserInfo userInfo){

        //设置SSL连接、邮件环境
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        // 配置发送邮件的环境属性
        final Properties props_yeah = new Properties();
        /*
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
         * mail.user / mail.from
         */
        // 表示SMTP发送邮件，需要进行身份验证
        props_yeah.put("mail.smtp.auth", "true");
        props_yeah.put("mail.smtp.host", EmailUnit.YEAH_SMTP_HOST);

        // 发件人的账号
        props_yeah.put("mail.user", userInfo.getEmail_user());
        props_yeah.put("mail.smtp.port", EmailUnit.YEAH_SMTP_HOST);
        // 访问SMTP服务时需要提供的密码
        props_yeah.put("mail.password", userInfo.getEmail_password());

        props_yeah.setProperty("mail.smtp.socketFactory.class", EmailUnit.SSL_FACTORY);
        props_yeah.setProperty("mail.smtp.socketFactory.fallback", "false");
        props_yeah.setProperty("mail.smtp.socketFactory.port", String.valueOf(EmailUnit.YEAH_SMTP_PORT));


        if(emailYeahSession == null){
            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication(){
                    String userName = userInfo.getEmail_user();
                    String password = userInfo.getEmail_password();
                    return new PasswordAuthentication(userName, password);
                }
            };
            emailYeahSession = Session.getInstance(props_yeah, authenticator);
        }
        return emailYeahSession;
    }

}
