package com.speedmailbatch;

/**
 * Created by wangshuai on 2018/1/29.
 */
public class EmailSourceBean {

    private String send_source;

    private String send_source_des;

    private  String plan_date;

    private  String is_open;

    private int plan_send_count;

    private int fact_send_count;

    private int sleep_minute;

    public String getSend_source() {
        return send_source;
    }

    public void setSend_source(String send_source) {
        this.send_source = send_source;
    }

    public String getSend_source_des() {
        return send_source_des;
    }

    public void setSend_source_des(String send_source_des) {
        this.send_source_des = send_source_des;
    }

    public String getPlan_date() {
        return plan_date;
    }

    public void setPlan_date(String plan_date) {
        this.plan_date = plan_date;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
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

    public int getSleep_minute() {
        return sleep_minute;
    }

    public void setSleep_minute(int sleep_minute) {
        this.sleep_minute = sleep_minute;
    }
}
