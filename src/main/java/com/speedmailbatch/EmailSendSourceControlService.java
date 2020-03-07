package com.speedmailbatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by wangshuai on 2018/2/12.
 */
@Service
public class EmailSendSourceControlService {


    @Autowired
    private EmailSendSourceControlDao emailSendSourceControlDao;

    public int saveSendSourceControl(EmailSendSourceControlBean emailSendSourceControlBean){
        return emailSendSourceControlDao.insertEmailSendSourceControl(emailSendSourceControlBean);
    }

    public List<EmailSendSourceControlBean> getSendSourceControlList(){
        return emailSendSourceControlDao.getSendSourceControlList();
    }

    public int updateSendSourceControl(EmailSendSourceControlBean emailSendSourceControlBean){
        return emailSendSourceControlDao.updateEmailSendSourceControl(emailSendSourceControlBean);
    }

    public int deleteSendSourceControl(EmailSendSourceControlBean emailSendSourceControlBean){
        return emailSendSourceControlDao.deleteEmailSendSourceControl(emailSendSourceControlBean);
    }

}
