package domain;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 27-09-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: nl.nujules.domain
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class Notification {

    private String code;
    private String email;
    private double minimum;
    private double maximum;

    public Notification(String code, String email, double minimum, double maximum) {
        this.code = code;
        this.email = email;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public void sendMail() {
        //TODO: Setup email account and mail client when stock reaches desired value
    }
}
