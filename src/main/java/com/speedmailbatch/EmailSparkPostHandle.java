package com.speedmailbatch;

import com.mashape.unirest.http.exceptions.UnirestException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.sparkpost.Client;
import com.sparkpost.model.AddressAttributes;
import com.sparkpost.model.RecipientAttributes;
import com.sparkpost.model.TemplateContentAttributes;
import com.sparkpost.model.TransmissionWithRecipientArray;
import com.sparkpost.model.responses.Response;
import com.sparkpost.resources.ResourceTransmissions;
import com.sparkpost.transport.RestConnection;
import org.springframework.stereotype.Service;


/**
 * Created by wangshuai on 2018/2/21.
 */

@Service
public class EmailSparkPostHandle implements EmailHandle {

    private static String APIKEY = "6c6264af215923c4d96545674d1d62fcb43f3b9a";

    private static String SOURCE = "03";

    private static String DOMAIN = "@tomould.com";

    @Override
    public void sendEmail(EmailUserInfo emailUserInfo, EmailInfo emailInfo) throws MessagingException, UnsupportedEncodingException, UnirestException {

    }

    @Override
    public void receiveEmails(EmailUserInfo emailUserInfo, EmailInfo emailInfo) {

    }

    @Override
    public void sendReceiveEmail(EmailUserInfo emailUserInfo, MimeMessage mimeMessage) throws MessagingException {

    }

    @Override
    public String sendEmailResult(EmailUserInfo emailUserInfo, EmailInfo emailInfo,String apikey) throws MessagingException, UnsupportedEncodingException, UnirestException {
        String flag = "0";
        Client client = new Client(apikey);
        try {
            TransmissionWithRecipientArray transmission = new TransmissionWithRecipientArray();
            // Populate Recipients
            List<RecipientAttributes> recipientArray = new ArrayList<RecipientAttributes>();
            RecipientAttributes recipientAttribs = new RecipientAttributes();
            recipientAttribs.setAddress(new AddressAttributes(emailInfo.getEmail_to()));
            recipientArray.add(recipientAttribs);
            transmission.setRecipientArray(recipientArray);

            // Populate Email Body
            TemplateContentAttributes contentAttributes = new TemplateContentAttributes();
            contentAttributes.setFrom(new AddressAttributes(emailInfo.getEmail_from()));
            contentAttributes.setSubject(emailInfo.getEmail_subject());
            contentAttributes.setHtml(emailInfo.getEmail_content());
            contentAttributes.setReplyTo("phyllis@bosun-mould.com");
            transmission.setContentAttributes(contentAttributes);

            // Send the Email
            RestConnection connection = new RestConnection(client);
            Response response = ResourceTransmissions.create(connection, 0, transmission);

        }catch (Exception e){
            flag = "1";
        }
        return flag;
    }

    @Override
    public String getSource() {
        return SOURCE;
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }
}
