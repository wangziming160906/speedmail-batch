package com.speedmailbatch;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
@Component
public interface EmailSendInfoDao {
    List<EmailSendAddrBean> getSendAddrInfo(@Param("emailSendAddrBean") EmailSendAddrBean emailSendAddrBean);

    int updateEmailAddrInfo(@Param("emailSendAddrBean") EmailSendAddrBean emailSendAddrBean);
}




