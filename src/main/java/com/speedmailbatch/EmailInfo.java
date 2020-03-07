package com.speedmailbatch;

import java.io.Serializable;

/**
 * Created by wangshuai on 2017/9/7.
 */
public class EmailInfo implements Serializable {

    private String email_from;

    private String email_to;

    private String email_subject;

    private String email_content;

    private String email_status;

    private String email_pws;

    private String email_reply_to;

    public String getEmail_pws() {
        return email_pws;
    }

    public void setEmail_pws(String email_pws) {
        this.email_pws = email_pws;
    }

    public String getEmail_from() {
        return email_from;
    }

    public void setEmail_from(String email_from) {
        this.email_from = email_from;
    }

    public String getEmail_to() {
        return email_to;
    }

    public void setEmail_to(String email_to) {
        this.email_to = email_to;
    }

    public void setEmail_reply_to(String email_reply_to) {
        this.email_reply_to = email_reply_to;
    }

    public String getEmail_reply_to() {

        return email_reply_to;
    }

    public String getEmail_subject() {
        return email_subject;
    }

    public void setEmail_subject(String email_subject) {
        this.email_subject = email_subject;
    }

    public String getEmail_content() {
        return email_content;
    }

    public void setEmail_content(String email_content) {
        this.email_content = email_content;
    }

    public String getEmail_status() {
        return email_status;
    }

    public void setEmail_status(String email_status) {
        this.email_status = email_status;
    }
}
