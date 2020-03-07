package com.speedmailbatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wangshuai on 2018/2/2.
 */
@Service
public class EmailsLoadService {

    @Autowired
    private EmailDbOperate emailDbOperate;

    @Autowired
    private EmailBatchInfoService emailBatchInfoService;

    public boolean loadEmailsFiltrate(InputStream fileInputStream) {
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(fileInputStream));
        String data = null;
        try {
            while ((data = br.readLine()) != null) {
                if (data.indexOf("@") >= 0) {
                    data = data.trim();
                    emailDbOperate.inSertFiltrateEmails(data);
                }
            }
            br.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean loadEmailsFromWebFile(InputStream fileInputString, String batchDes) {
        BufferedReader br = null;
        String data = "";
        //创建批次
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        String datestr = dateFormater.format(new Date());
        //EMAILS_BATCH_INFO
        int num = emailDbOperate.getEmailBatch(datestr);
        String email_batch = datestr + "-" + String.format("%03d", num);

        //新增批次
        emailDbOperate.inSertBatchInfo(email_batch, batchDes, num);
        br = new BufferedReader(new InputStreamReader(fileInputString));
        String emailStrFilter = "";
        int count = 0;
        try {
            emailStrFilter = emailDbOperate.getFiltrateEmails();
            System.out.println(emailStrFilter);
            while ((data = br.readLine()) != null) {
                if (data.indexOf("@") >= 0) {
                    String serchStr = data.substring(data.indexOf("@")).trim();
                    if (emailStrFilter.indexOf(serchStr) < 0) {
                        String emailaddr = data;
                        try {
                            emailDbOperate.inSertSendEmails(emailaddr, email_batch, batchDes);
                            count = count + 1;
                        } catch (Exception e) {
                        }
                    }
                }
            }
            br.close();
            //更新批次表总数量
            EmailBatchInfoBean emailBatchInfoBean = new EmailBatchInfoBean();
            emailBatchInfoBean.setEmail_batch(email_batch);
            emailBatchInfoBean.setEmail_batch_count(count);
            emailBatchInfoService.updateEmailBatchInfo(emailBatchInfoBean);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
