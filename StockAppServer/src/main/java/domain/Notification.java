package domain;

import domain.database.DatabaseHandlerStock;
import exceptions.InvalidStockCodeException;
import exceptions.StockNotFoundException;
import util.mail.MailHandler;

import java.util.Calendar;
import java.util.Date;

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

    private void sendMail(Double stockMinimum, Double stockMaximum) {
        String message = "Hello,\'"
                + "You've create a notification for the stock with the ticker symbol " + this.code + "."
                + "\n\n"
                + "This stock has now reached your desired value:"
                + "\n"
                + "Stock minimum: " + stockMinimum + " - Desired minimum: " + this.minimum
                + "Stock maximum: " + stockMaximum + " - Desired maximum: " + this.maximum
                + "\n\n"
                + "Happy Trading!";

        String subject = this.code + " has reached your desired value!";

        MailHandler.sendMail(subject, message, this.email);
    }

    /**
     * Check if the stock has reached the desired value(s).
     * @return return true if the stock has reached the desired value(s), false if not
     */
    public boolean checkStock() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();

        try {
            Stock stock = DatabaseHandlerStock.getInstance().getStock(date, this.code);
            if(stock.getMinimum() < this.minimum || stock.getMaximum() < this.maximum) {
                this.sendMail(stock.getMinimum(), stock.getMaximum());
                return true;
            }
        } catch (StockNotFoundException | InvalidStockCodeException e) {
            System.out.println("No stock found in the database with the code \"" + this.code + "\" for " + date.toString());
        }

        return false;
    }
}
