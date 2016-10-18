package util.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

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
public class SmtpAuthenticator extends Authenticator {

    public SmtpAuthenticator() {
        super();
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        // Settings username and password, these can be changed when a new mailserver is being used
        String username = "gso@nujules.nl";
        String password = "GSO-Maatwerk123!";

        return new PasswordAuthentication(username, password);
    }
}
