package com.speedmailbatch;

/**
 * Created by wangshuai on 2018/2/14.
 */
public class EmailBatchInfoBean {

    private String email_batch;

    private String email_batch_des;

    private String email_batch_date;

    private int email_batch_num;

    private int email_batch_count;

    private int email_validate_succ_count;

    private int email_validate_fail_count;

    private int email_send_succ_count;

    private int email_send_fail_count;

    private String email_batch_status;

    private String email_batch_work_status;

    private String email_batch_work_date;

    private String priority;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getEmail_batch_work_status() {
        return email_batch_work_status;
    }

    public void setEmail_batch_work_status(String email_batch_work_status) {
        this.email_batch_work_status = email_batch_work_status;
    }

    public String getEmail_batch_work_date() {
        return email_batch_work_date;
    }

    public void setEmail_batch_work_date(String email_batch_work_date) {
        this.email_batch_work_date = email_batch_work_date;
    }

    public String getEmail_batch() {
        return email_batch;
    }

    public void setEmail_batch(String email_batch) {
        this.email_batch = email_batch;
    }

    public String getEmail_batch_des() {
        return email_batch_des;
    }

    public void setEmail_batch_des(String email_batch_des) {
        this.email_batch_des = email_batch_des;
    }

    public String getEmail_batch_date() {
        return email_batch_date;
    }

    public void setEmail_batch_date(String email_batch_date) {
        this.email_batch_date = email_batch_date;
    }

    public int getEmail_batch_num() {
        return email_batch_num;
    }

    public void setEmail_batch_num(int email_batch_num) {
        this.email_batch_num = email_batch_num;
    }

    public int getEmail_batch_count() {
        return email_batch_count;
    }

    public void setEmail_batch_count(int email_batch_count) {
        this.email_batch_count = email_batch_count;
    }

    public int getEmail_validate_succ_count() {
        return email_validate_succ_count;
    }

    public void setEmail_validate_succ_count(int email_validate_succ_count) {
        this.email_validate_succ_count = email_validate_succ_count;
    }

    public int getEmail_validate_fail_count() {
        return email_validate_fail_count;
    }

    public void setEmail_validate_fail_count(int email_validate_fail_count) {
        this.email_validate_fail_count = email_validate_fail_count;
    }

    public int getEmail_send_succ_count() {
        return email_send_succ_count;
    }

    public void setEmail_send_succ_count(int email_send_succ_count) {
        this.email_send_succ_count = email_send_succ_count;
    }

    public int getEmail_send_fail_count() {
        return email_send_fail_count;
    }

    public void setEmail_send_fail_count(int email_send_fail_count) {
        this.email_send_fail_count = email_send_fail_count;
    }

    public String getEmail_batch_status() {
        return email_batch_status;
    }

    public void setEmail_batch_status(String email_batch_status) {
        this.email_batch_status = email_batch_status;
    }
}
