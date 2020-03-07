package com.speedmailbatch;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wangshuai on 2018/2/9.
 */

@Mapper
@Component
public interface EmailSendSourceControlDao {

    int insertEmailSendSourceControl(@Param("emailSendSourceControlBean") EmailSendSourceControlBean emailSendSourceControlBean);

    List<EmailSendSourceControlBean> getSendSourceControlList();

    int updateEmailSendSourceControl(@Param("emailSendSourceControlBean") EmailSendSourceControlBean emailSendSourceControlBean);

    int deleteEmailSendSourceControl(@Param("emailSendSourceControlBean") EmailSendSourceControlBean emailSendSourceControlBean);



}
