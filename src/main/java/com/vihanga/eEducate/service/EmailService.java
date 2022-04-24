package com.vihanga.eEducate.service;

import com.sun.mail.smtp.SMTPTransport;
import org.springframework.stereotype.Service;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import static com.vihanga.eEducate.constant.EmailConstant.*;

@Service
public class EmailService {

    private Session getEmailSession(){
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER); // pass the host, in this case GMAIL_SMTP_SERVER is the host.
        properties.put(SMTP_AUTH, true); // use authentication.
        properties.put(SMTP_PORT, DEFAULT_PORT); // define the default port
        properties.put(SMTP_STARTTLS_ENABLE, true); // enable transport layer security.
        properties.put(SMTP_STARTTLS_REQUIRED, true); // transport layer security should required. otherwise connection will failed.
        return Session.getInstance(properties,null);
    }

    // Create email.
    private Message createEmail(String name, String password, String email) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(CC_EMAIL,false));
        message.setSubject(EMAIL_SUBJECT);
        message.setText("Hello " +name+ ", \n \n Your new password is: " +password+ "\n \n The support team.");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    public void sendNewPasswordEmail(String name, String password, String email)  {
        try {
            Message message = createEmail(name, password, email);


            SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOLE);
            smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
            smtpTransport.sendMessage(message, message.getAllRecipients());
            smtpTransport.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
