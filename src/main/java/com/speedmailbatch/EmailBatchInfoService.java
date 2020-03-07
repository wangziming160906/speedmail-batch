package com.speedmailbatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wangshuai on 2018/2/14.
 */
@Service
@Transactional
public class EmailBatchInfoService {

    @Autowired
    private EmailBatchInfoDao emailBatchInfoDao;

    @Autowired
    private EmailSendInfoDao emailSendInfoDao;

    @Autowired
    private EmailDbOperate emailDbOperate;

    public int updateEmailBatchInfo(EmailBatchInfoBean emailBatchInfoBean){
        return emailBatchInfoDao.updateEmailBatchInfo(emailBatchInfoBean);
    }

    public EmailBatchInfoBean getEmailBatchInfoSign(String emailbatch){
        EmailBatchInfoBean emailBatchInfoBean = new EmailBatchInfoBean();
        emailBatchInfoBean.setEmail_batch(emailbatch);
        emailBatchInfoBean = emailBatchInfoDao.getEmailBatchinfoList(emailBatchInfoBean).get(0);
        return emailBatchInfoBean;
    }

    public List<EmailBatchInfoBean> getEmailBatchInfoList(){
        EmailBatchInfoBean emailBatchInfoBean = new EmailBatchInfoBean();
        return  emailBatchInfoDao.getEmailBatchinfoList(emailBatchInfoBean);
    }

    public List<EmailBatchInfoBean> getEmailBatchinfoListAndWorkStatus(){
        return emailBatchInfoDao.getEmailBatchinfoListAndWorkStatus();
    }

    public boolean isExistBatchInfoCount(String emailBatch){
        boolean flag = false;
        int count =  emailDbOperate.getBatchInfo(emailBatch);
        if (count > 0){
            flag = true;
        }
        return flag;
    }


    public int insertBatchControl(String emailBatch,String plandate,String isopen,String priority){
        return emailDbOperate.insertBatchControl(emailBatch,plandate,isopen,priority);
    }


    public int updateBatchControl(String  emailBatch,String plandate,String isopen,String priority){
        return emailDbOperate.updateBatchControl(emailBatch,plandate,isopen,priority);

    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void getSendAddrInfo(EmailSendAddrBean emailSendAddrBean){
        System.out.println("线程" + Thread.currentThread().getName() + emailSendAddrBean.getEmailAddr());
        List<EmailSendAddrBean> list = emailSendInfoDao.getSendAddrInfo(emailSendAddrBean);
        EmailSendAddrBean emailSendAddrBean1 = list.get(0);
        System.out.println("线程" + Thread.currentThread().getName() + emailSendAddrBean1.getEmailSendStatus());
        System.out.println("线程" + Thread.currentThread().getName() + "query");
        try {
            emailSendInfoDao.updateEmailAddrInfo(emailSendAddrBean);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("线程" + Thread.currentThread().getName() + "update");
    }

}
