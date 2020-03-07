package com.speedmailbatch;
import javax.persistence.*;


@Entity
@Table(name = "EMAILS_SEND_SOURCE_CONTROL")
@IdClass(EmailSendSourceControlPrimaryKey.class)


class EmailSendSourceControlEntity {
    @Id
    @Column(name = "PLAN_DATE")
    private String plandate;

    @Id
    @Column(name = "SEND_WAY")
    private String sendway;

    @Column(name = "IS_OPEN")
    private String isopen;

    @Column(name = "PLAN_SEND_COUNT")
    private int plan_send_count ;

    @Column(name = "FACT_SEND_COUNT")
    private int fact_send_count;

    @Column(name = "FACT_SEND_SUCC_COUNT")
    private int fact_send_succ_count;

    @Column(name = "FACT_SEND_FAIL_COUNT")
    private int fact_send_fail_count;

    @Column(name = "sleep_minute_min")
    private int sleep_minute_min;

    @Column(name = "sleep_minute_max")
    private int sleep_minute_max;

    @Column(name = "IS_OPEN_TEST")
    private String isopenTest;

    public String getPlandate() {
        return plandate;
    }

    public void setPlandate(String plandate) {
        this.plandate = plandate;
    }

    public String getSendway() {
        return sendway;
    }

    public void setSendway(String sendway) {
        this.sendway = sendway;
    }

    public String getIsopen() {
        return isopen;
    }

    public void setIsopen(String isopen) {
        this.isopen = isopen;
    }

    public int getPlan_send_count() {
        return plan_send_count;
    }

    public void setPlan_send_count(int plan_send_count) {
        this.plan_send_count = plan_send_count;
    }

    public int getFact_send_count() {
        return fact_send_count;
    }

    public void setFact_send_count(int fact_send_count) {
        this.fact_send_count = fact_send_count;
    }

    public int getFact_send_succ_count() {
        return fact_send_succ_count;
    }

    public void setFact_send_succ_count(int fact_send_succ_count) {
        this.fact_send_succ_count = fact_send_succ_count;
    }

    public int getFact_send_fail_count() {
        return fact_send_fail_count;
    }

    public void setFact_send_fail_count(int fact_send_fail_count) {
        this.fact_send_fail_count = fact_send_fail_count;
    }

    public int getSleep_minute_min() {
        return sleep_minute_min;
    }

    public void setSleep_minute_min(int sleep_minute_min) {
        this.sleep_minute_min = sleep_minute_min;
    }

    public int getSleep_minute_max() {
        return sleep_minute_max;
    }

    public void setSleep_minute_max(int sleep_minute_max) {
        this.sleep_minute_max = sleep_minute_max;
    }

    public String getIsopenTest() {
        return isopenTest;
    }

    public void setIsopenTest(String isopenTest) {
        this.isopenTest = isopenTest;
    }
}

