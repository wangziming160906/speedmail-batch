package com.speedmailbatch;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailSendSourceControlRepository extends JpaRepository<EmailSendSourceControlEntity,String> {

}
