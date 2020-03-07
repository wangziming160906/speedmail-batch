package com.speedmailbatch;

public enum EmailSendServerType {

    SENDGRID("01","sendgrid"),
    MAILGUN("02","mailgun");

    public String getTranCode() {
        return tranCode;
    }

    public String getDesc() {
        return desc;
    }

    private String tranCode;

    private String desc;

    EmailSendServerType(String tranCode, String desc){
        this.tranCode = tranCode;
        this.desc = desc;
    }
}
