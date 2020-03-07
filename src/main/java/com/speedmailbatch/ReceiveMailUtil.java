package com.speedmailbatch;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;


public class ReceiveMailUtil {

    private static final String dateformat = "yyyy-MM-dd HH:mm";
    private static final String saveAttachPath = "D:\\JS";
    private static final String sendFrom = "******@qq.com";
    private static final String receiveAddress = "******@163.com";
    private static final String smtpHost = "smtp.qq.com";
    private StringBuffer bodyText = new StringBuffer();
    private String host;
    private String username;
    private String password;

    private Message message[];

    private Store store = null;
    private Folder folder = null;

    private MimeMessage mimeMsg = null;

    ReceiveMailUtil(String host,String username,String password) throws Exception{
        this.host = host;
        this.username = username;
        this.password = password;
        init();
    }

    ReceiveMailUtil(MimeMessage msg){
        this.mimeMsg = msg;
    }

    private void forwardMail(Session session,ReceiveMailUtil mail) throws Exception, MessagingException{//转发邮件
        session.setDebug(true);

        MimeMessage fordward = new MimeMessage(session);
        fordward.setFrom(new InternetAddress(sendFrom));
        fordward.setSubject(mail.getSubject());
        fordward.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiveAddress));

        Multipart mp = new MimeMultipart();
        MimeBodyPart mbp = new MimeBodyPart();
        mail.getMailContent((Part)mail.mimeMsg);
        mbp.setContent("<meta http-equiv=Content-Type content=text/html; charset=GBK>"+
                mail.getBodyText(), "text/html;charset=gb2312");
        mp.addBodyPart(mbp);
        fordward.setContent(mp); //Multipart加入到信件
        fordward.setSentDate(new Date());     //设置信件头的发送日期
        //发送信件
        fordward.saveChanges();
        Transport.send(fordward);
    }

    private void replyMail(Session session,ReceiveMailUtil mail) throws Exception{//回复邮件
        MimeMessage msg = (MimeMessage) mail.mimeMsg.reply(false);
        msg.setFrom(new InternetAddress(sendFrom));
        msg.setRecipients(RecipientType.TO, mail.getFrom());
        Multipart mp = new MimeMultipart();
        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setContent("<meta http-equiv=Content-Type content=text/html; charset=GBK>"+
                "您好，您的邮件已经收到，谢谢！", "text/html;charset=gb2312");
        mp.addBodyPart(mbp);
        msg.setContent(mp);
        msg.setSentDate(new Date());
        msg.saveChanges();
        Transport trans = session.getTransport("smtp");
        trans.connect(smtpHost, username, password);
        trans.sendMessage(msg, msg.getAllRecipients());
        trans.close();
    }


    private static class Email_Autherticatorbean extends Authenticator {//认证

        private String m_username = null;

        private String m_userpass = null;

        public Email_Autherticatorbean(String username, String userpass) {
            super();
            setUsername(username);
            setUserpass(userpass);
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(m_username, m_userpass);
        }

        public void setUsername(String username) {
            m_username = username;
        }

        public void setUserpass(String userpass) {
            m_userpass = userpass;
        }
    }

    private static String base64Decoder(String s) throws Exception {
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        byte[] b = decoder.decodeBuffer(s);
        return (new String(b,"GBK"));
    }

    private void init() throws Exception{
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", host);
        props.setProperty("mail.transport.protocol","smtp");
        props.setProperty("mail.smtp.host", smtpHost);
        props.setProperty("mail.smtp.auth", "true");

        System.out.println("1"+username+password);
        Email_Autherticatorbean Auth = new Email_Autherticatorbean(username,password);
        Session session = Session.getDefaultInstance(props,Auth);
        store = session.getStore("imap");
        //session.setDebug(true);
        store.connect(host,username,password);
//      Folder folder1 = store.getDefaultFolder();
//      System.out.println("Default Folder :: " + folder1.getName());
//      Folder []folders = folder1.list();
//      System.out.println(folders.length);
//      for(int i = 0 ;  i < folders.length ; i ++){
//          System.out.println("Folder is ::: " + folders[i].getName());
//          if(folders[i].getName().equals("Sent Messages") == false
//                  &&folders[i].getName().equals("Deleted Messages") == false)
//              System.out.println("Length::" + folders[i].getMessageCount());
//      }
        folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
//      FetchProfile profile = new FetchProfile();
//        profile.add(FetchProfile.Item.ENVELOPE);
        this.message = folder.getMessages();
        //folder.fetch(message, profile);
        System.out.println("收件箱的邮件数：" + message.length);
        System.out.println("未读邮件数：" + folder.getUnreadMessageCount());
        System.out.println("新邮件数：" + folder.getNewMessageCount());
        int len = message.length;
        for(int i = len - 1 ; i < len; i ++){
            ReceiveMailUtil mail = new ReceiveMailUtil((MimeMessage)(message[i]));
            forwardMail(session,mail);//转发邮件
            replyMail(session,mail);//回复邮件
        }
    }

    private String getSubject() throws Exception{
        String tempSubject = "";
        tempSubject = MimeUtility.decodeText(mimeMsg.getSubject());
        return tempSubject;
    }

    private String getSendDate() throws Exception{
        Date sentdate = mimeMsg.getSentDate();
        SimpleDateFormat format = new SimpleDateFormat(dateformat);
        return format.format(sentdate);
    }

    private String getFrom() throws Exception{
        InternetAddress address[] = (InternetAddress[]) mimeMsg.getFrom();
        String from = address[0].getAddress();
        if (from == null)
            from = "";
        return from;
    }

    private String getFromandName() throws Exception{
        InternetAddress address[] = (InternetAddress[]) mimeMsg.getFrom();
        String from = address[0].getAddress();
        if (from == null)
            from = "";
        String personal = address[0].getPersonal();
        if (personal == null)
            personal = "";
        String fromaddr = personal + " <" + from + ">";
        return fromaddr;
    }

    /**
     * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同 * "to"----收件人 "cc"---抄送人
     * 地址 "bcc"---密送人地址
     * @throws Exception
     */
    private String getMailAddress(String type) throws Exception{
        String mailAddr = "";
        String addType = type.toUpperCase();
        InternetAddress[] address = null;
        if(addType.equals("TO") || addType.equals("CC") || addType.equals("BCC")){
            if(addType.equals("TO")){
                address = (InternetAddress[]) mimeMsg.getRecipients(Message.RecipientType.TO);
            }
            else if (addType.equals("CC")) {
                address = (InternetAddress[]) mimeMsg.getRecipients(Message.RecipientType.CC);
            }
            else {
                address = (InternetAddress[]) mimeMsg.getRecipients(Message.RecipientType.BCC);
            }
            if(address != null){
                for(int i = 0 ; i < address.length ; i ++){
                    String email = address[i].getAddress();
                    email = (email == null ? "":MimeUtility.decodeText(email));
                    String personal = address[i].getPersonal();
                    personal = (personal == null ? "" : MimeUtility.decodeText(personal));
                    mailAddr += personal + " <"+email+">";
                }
            }
        }
        return mailAddr;
    }
    /**
     * * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"
     */
    public boolean getReplySign() throws MessagingException {
        boolean replysign = false;
        String needreply[] = mimeMsg.getHeader("Disposition-Notification-To");
        if (needreply != null) {
            replysign = true;
        }
        return replysign;
    }
    /**
     * * 【判断此邮件是否已读，如果未读返回返回false,反之返回true】
     */
    private boolean isNew() throws MessagingException {
        boolean isnew = false;
        Flags flags = ((Message) mimeMsg).getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == Flags.Flag.SEEN) {
                isnew = true;
                break;
            }
        }
        return isnew;
    }
    /**
     * * 判断此邮件是否包含附件
     */
    private boolean isContainAttach(Part part) throws Exception {
        boolean attachflag = false;
        if(part.isMimeType("multipart/*")){
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                /**是否存在附件*/
                String disposition = mpart.getDisposition();
                if ((disposition != null)
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE))))
                    attachflag = true;
                else if (mpart.isMimeType("multipart/*")) {
                    attachflag = isContainAttach((Part) mpart);
                } else {
                    String contype = mpart.getContentType();
                    if (contype.toLowerCase().indexOf("application") != -1)
                        attachflag = true;
                    if (contype.toLowerCase().indexOf("name") != -1)
                        attachflag = true;
                }
            }
        }else if (part.isMimeType("message/rfc822")) {
            attachflag = isContainAttach((Part) part.getContent());
        }
        return attachflag;
    }
    /**
     * * 【保存附件】
     */

    public void saveAttachMent(Part part) throws Exception {
        String fileName = "";
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                System.out.println("multi::  " + i);
                BodyPart mpart = mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null)
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition
                        .equals(Part.INLINE)))) {
                    fileName = mpart.getFileName();
                    System.out.println("mpartFilename::"+fileName);
                    //if (fileName.toLowerCase().indexOf("gb2312") != -1) {
                    fileName = MimeUtility.decodeText(fileName);
                    //}
                    saveFile(fileName, mpart.getInputStream());
                } else if (mpart.isMimeType("multipart/*")) {
                    saveAttachMent(mpart);
                } else {
                    fileName = mpart.getFileName();
                    if(fileName != null){
//                      System.out.println("Filename::"+fileName);
//                      if(fileName.indexOf("?=") != -1){
//                          String s = fileName.substring(8,fileName.indexOf("?="));
//                          s = base64Decoder(s);
//                          System.out.println("SFileName is ::: " + s);
//                      }

                        fileName = MimeUtility.decodeText(fileName);
                        System.out.println("Filename::"+fileName);
                        saveFile(fileName, mpart.getInputStream());
                    }
//                    if ((fileName != null)
//                            && (fileName.toLowerCase().indexOf("gb2312") != -1)) {
//                        fileName = MimeUtility.decodeText(fileName);
//                        System.out.println("NO");
//                        saveFile(fileName, mpart.getInputStream());
//                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachMent((Part) part.getContent());
        }
    }

    /**
     * * 【真正的保存附件到指定目录里】
     */

    private void saveFile(String fileName, InputStream in) throws Exception {
        String osName = System.getProperty("os.name");
        String storedir = saveAttachPath;
        String separator = "";
        if (osName == null)
            osName = "";
        if (osName.toLowerCase().indexOf("win") != -1) {
            separator = "\\";
            if (storedir == null || storedir.equals(""))
                storedir = "c:\\tmp";
        } else {
            separator = "/";
            storedir = "/tmp";
        }
        File storefile = new File(storedir + separator + fileName);
        System.out.println("storefile's path: " + storefile.toString());
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(storefile));
            bis = new BufferedInputStream(in);
            int c;
            while ((c = bis.read()) != -1) {
                bos.write(c);
                bos.flush();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new Exception("文件保存失败!");
        } finally {
            bos.close();
            bis.close();
        }
    }

    public String getBodyText() {
        return bodyText.toString();
    }

    /**
     * * 获得此邮件的Message-ID
     */
    public String getMessageId() throws MessagingException {
        return mimeMsg.getMessageID();
    }

    public void getMailContent(Part part) throws Exception {
        String contenttype = part.getContentType();
        int nameindex = contenttype.indexOf("name");
        boolean conname = false;
        if (nameindex != -1)
            conname = true;
        //System.out.println("CONTENTTYPE: " + contenttype);
        if (part.isMimeType("text/plain") && !conname) {
            /**纯文本邮件*/
            bodyText.append((String) part.getContent());
        } else if (part.isMimeType("text/html") && !conname) {
            /**HTML格式邮件*/
            bodyText.append((String ) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            /**multipart邮件*/
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                getMailContent(multipart.getBodyPart(i));
            }
        } else if (part.isMimeType("message/rfc822")) {
            getMailContent((Part) part.getContent());
        }
    }

    public void receiveMail() throws Exception{
        int len = message.length;
        System.out.println("Size::: "+len);
        for(int i = len - 1 ; i < len; i ++){
            ReceiveMailUtil mail = new ReceiveMailUtil((MimeMessage)(message[i]));
            //fordwardMail(session,mail);
            System.out.println("Message " + i + " subject: " + mail.getSubject());
            System.out.println("Message " + i + " sentdate: "
                    + mail.getSendDate());
            System.out.println("Message " + i + " replysign: "
                    + mail.getReplySign());
            System.out.println("Message " + i + " hasRead: " + mail.isNew());

            boolean attachFlag = mail.isContainAttach((Part) message[i]);
            System.out.println("Message " + i + "  containAttachment: "
                    + attachFlag);
            if(attachFlag == true) {
                System.out.println("attach");
                mail.saveAttachMent((Part) message[i]);
            }
            System.out.println("Message " + i + " from: " + mail.getFromandName());
            System.out.println("Message " + i + " to: "
                    + mail.getMailAddress("to"));
            System.out.println("Message " + i + " cc: "
                    + mail.getMailAddress("cc"));
            System.out.println("Message " + i + " bcc: "
                    + mail.getMailAddress("bcc"));
            System.out.println("Message " + i + " Message-ID: "
                    + mail.getMessageId());
            mail.getMailContent((Part) message[i]);
            System.out.println("Message " + i + " bodycontent: \r\n"
                    + mail.getBodyText());
        }
        store.close();
    }

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
//      String host = "imap.163.com";
//      String username = "******@163.com";
        //String password = "******";
        String host = "imap.qq.com";
        String username = "******@qq.com";
        String password = "******";
        ReceiveMailUtil mail = new ReceiveMailUtil(host,username,password);
        mail.receiveMail();
    }

}