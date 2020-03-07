package com.speedmailbatch;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by wangshuai on 2017/9/7.
 */
public class EmailUnit {


    public static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    public static final int YAHOO_SMTP_PORT = 465;

    public static final String YEAH_SMTP_HOST = "smtp.yeah.net";

    public static final int YEAH_SMTP_PORT = 465;

    public static final String SMTP = "smtp";

    public static final String SOURCE_SENDGRID = "01";

    public static final String SOURCE_MAINGUN = "02";

    public static final String SOURCE_SPARTPOST = "03";


    public static final String VALIDATE_STATUS_INIT = "00";

    public static final String VALIDATE_STATUS_SUCCESS = "01";

    public static final String VALIDATE_STATUS_FAIL = "02";


    public static final String SEND_STATUS_INIT = "00";

    public static final String SEND_STATUS_SUCCESS = "01";

    public static final String SEND_STATUS_FAIL = "02";


    public static final Map<String,String> SOURCE = new HashMap<String,String>(){
        {
            put("01","SENDGRID");
            put("02","MAINGUN");
            put("03","SPARTPOST");
        }
    };

    public static final Map<String,String> SEND_STATUS = new HashMap<String,String>(){
        {
            put("00","初始状态");
            put("01","发送成功");
            put("02","发送失败");
        }
    };

    public static final Map<String,String> VALIDATE_STATUS = new HashMap<String,String>(){
        {
            put("00","初始状态");
            put("01","验证成功");
            put("02","验证失败");
        }
    };



}
