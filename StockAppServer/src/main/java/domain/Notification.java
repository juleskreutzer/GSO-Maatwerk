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
        if(code.equals("")) { throw new IllegalArgumentException("Not allowed to set an empty code"); }
        if(email.equals("")) { throw new IllegalArgumentException("Not allowed to set an empty mail address"); }
        if(minimum > maximum) { throw new IllegalArgumentException("Minimum can't be higher than maximum"); }

        this.code = code;
        this.email = email;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if(code.equals("")) { throw new IllegalArgumentException("Not allowed to set an empty code"); }
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email.equals("")) { throw new IllegalArgumentException("Not allowed to set an empty email address"); }
        this.email = email;
    }

    public double getMinimum() {
        return minimum;
    }

    public void setMinimum(double minimum) {
        if(minimum > maximum) { throw new IllegalArgumentException("Minimum value can't be higher than maximum value"); }
        this.minimum = minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        if(maximum < minimum) { throw new IllegalArgumentException("Maximum value can't be lower than minumum value"); }
        this.maximum = maximum;
    }

    public void sendMail() {
        //TODO: Setup email account and mail client when stock reaches desired value
    }
}
