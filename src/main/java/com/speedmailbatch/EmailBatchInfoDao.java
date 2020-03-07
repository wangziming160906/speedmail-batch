package com.speedmailbatch;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wangshuai on 2018/2/14.
 */

@Mapper
@Component
public interface EmailBatchInfoDao {

    int updateEmailBatchInfo(@Param("emailBatchInfoBean") EmailBatchInfoBean emailBatchInfoBean);

    List<EmailBatchInfoBean> getEmailBatchinfoList(@Param("emailBatchInfoBean") EmailBatchInfoBean emailBatchInfoBean);

    List<EmailBatchInfoBean> getEmailBatchinfoListAndWorkStatus();

}
