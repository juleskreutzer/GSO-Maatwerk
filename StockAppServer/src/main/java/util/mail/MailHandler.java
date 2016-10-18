package util.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 18-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: util.mail
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class MailHandler {

    public static void sendMail(String subject, String message, String address) {
        Properties properties = new Properties();
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.host", "web.ducohosting.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.connectiontimeout", "20000");
        properties.put("mail.smtp.timeout", "20000");

        SmtpAuthenticator auth = new SmtpAuthenticator();

        Session session = Session.getInstance(properties, auth);

        try{
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress("gso@nujules.nl"));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(address));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            Transport.send(mimeMessage);
            System.out.println("Email address sent to " + address);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
