package com.speedmailbatch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMAIL_SEND_CONFIG_INFO")
public class EmailAutoSendConfigEntity {

    @Id
    @Column(name = "SEND_SOURCE")
    private String sendsource;

    @Column(name = "IS_OPEN")
    private String isopen;

    @Column(name = "SEND_COUNT")
    private int sendcount;

    public String getSendsource() {
        return sendsource;
    }

    public void setSendsource(String sendsource) {
        this.sendsource = sendsource;
    }

    public String getIsopen() {
        return isopen;
    }

    public void setIsopen(String isopen) {
        this.isopen = isopen;
    }

    public int getSendcount() {
        return sendcount;
    }

    public void setSendcount(int sendcount) {
        this.sendcount = sendcount;
    }

}
