package com.speedmailbatch;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wangshuai on 2017/9/7.
 */
public class EmailAppRunableRepaid implements  Runnable{

    private EmailDbOperate emailDbOperate;

    private List<EmailUserInfo> toEmails = new ArrayList();

    private int sleepTime = 0;

    private EmailHandle handle;

    public EmailAppRunableRepaid(int sleepTime, EmailHandle handle, EmailDbOperate emailDbOperate){
        this.emailDbOperate = emailDbOperate;
        this.sleepTime = sleepTime;
        this.handle = handle;
    }

    public void run() {
        boolean flag = true;
        List<EmailUserInfo> users_sort = emailDbOperate.getEmailUsersList();
        List<EmailInfo> emailinfo_afterSort = new ArrayList<EmailInfo>();
        EmailInfo emailinfo_temp = null;
        int count = 1;
        int sizeOrg = users_sort.size();
        while(flag){
            if(count == sizeOrg){
                flag = false;
            }
            int max=users_sort.size();
            int min=0;
            Random random = new Random();
            int index = random.nextInt(max)%(max-min+1) + min;
            if(count % 2 == 0){
                emailinfo_temp.setEmail_to(users_sort.get(index).getEmail_user());
                emailinfo_afterSort.add(emailinfo_temp);
            }else{
                emailinfo_temp = new EmailInfo();
                emailinfo_temp.setEmail_from(users_sort.get(index).getEmail_user());
                emailinfo_temp.setEmail_pws(users_sort.get(index).getEmail_password());
                emailinfo_temp.setEmail_subject(EmailBoayTemplatestemp.getSubject());
                emailinfo_temp.setEmail_content(EmailBoayTemplatestemp.getBoay());
            }
            count = count + 1;
            users_sort.remove(index);
        }

        EmailUserInfo emailUserInfo = null;

        for(EmailInfo emailInfo:emailinfo_afterSort){
            emailUserInfo = new EmailUserInfo();
            emailUserInfo.setEmail_user(emailInfo.getEmail_from());
            emailUserInfo.setEmail_password(emailInfo.getEmail_pws());
            emailUserInfo.setAslias_name(emailInfo.getEmail_from());
            System.out.println("SEND START-----USER_FROM:" + emailUserInfo.getEmail_user()
                                + "--User_PWS:" + emailUserInfo.getEmail_password() +  "    USER_TO:" + emailInfo.getEmail_to());
            try {
                handle.sendEmail(emailUserInfo, emailInfo);
                System.out.println("SEND OK-----USER_FROM:" + emailUserInfo.getEmail_user() + "    USER_TO:" + emailInfo.getEmail_to());
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
