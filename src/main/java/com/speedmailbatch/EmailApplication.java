package com.speedmailbatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class EmailApplication implements CommandLineRunner {

    @Autowired
    private EmailDbOperate emailDbOperateForSend;

    @Autowired
    private EmailAppRunableForVeriEmail emailAppRunableForVeriEmail;

    @Autowired
    private EmailSendGridHandle emailSendGridHandle;

    @Autowired
    private EmailMailGunHandle emailMailGunHandle;

    @Autowired
    private EmailSparkPostHandle emailSparkPostHandle;

    @Autowired
    private EmailBatchInfoService emailBatchInfoService;

    @Autowired
    private EmailSendSourceControlRepository emailSendSourceControlRepository;

    @Autowired
    private EmailAutoSendConfigRepository emailAutoSendConfigRepository;

    @Autowired
    private EmailAppForCloudEmailBatch emailAppForCloudEmailBatch;

    public static void main(String[] args) {
        SpringApplication.run(EmailApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

//        EmailAppRunableForCloudEmailBatch emailAppRunableForCloudEmailBatchSendGrid =
//                new EmailAppRunableForCloudEmailBatch(emailDbOperateForSend,emailBatchInfoService,
//                        emailSendSourceControlRepository,emailAutoSendConfigRepository, EmailSendServerType.SENDGRID);
//        Thread thread_sendMessageBatchSendGrid = new Thread(emailAppRunableForCloudEmailBatchSendGrid);
//        thread_sendMessageBatchSendGrid.start();
//        System.out.println("thread_sendMessageBatch=" +  thread_sendMessageBatchSendGrid.getPriority());

//        Thread.sleep(10*1000);
//
//        EmailAppRunableForCloudEmailBatch emailAppRunableForCloudEmailBatchMailGun =
//                new EmailAppRunableForCloudEmailBatch(emailDbOperateForSend,emailBatchInfoService,
//                        emailSendSourceControlRepository,emailAutoSendConfigRepository, EmailSendServerType.MAILGUN);
//        Thread thread_sendMessageBatchMailGun = new Thread(emailAppRunableForCloudEmailBatchMailGun);
//        thread_sendMessageBatchMailGun.start();
//        System.out.println("thread_sendMessageBatchMailGun=" +  thread_sendMessageBatchMailGun.getPriority());
        for(int i=0;i<10;i++) {
            System.out.println("system:" + i);
            emailAppForCloudEmailBatch.sendEmailBatchManyThread();
        }



    }
}

