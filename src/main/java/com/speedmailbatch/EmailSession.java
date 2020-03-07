package com.speedmailbatch;

import javax.mail.Session;

/**
 * Created by wangshuai on 2017/9/7.
 */
public interface EmailSession {

    public Session getSession(EmailUserInfo userInfo);
}
