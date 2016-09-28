package domain;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

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
public class Stock implements Serializable {

    private ObjectId _id;
    private String name;
    private String code;
    private double minimum;
    private double maximum;
    private HashMap<String, Double> values;
    private String currency;
    private Date date;

    /**
     * Private ctor is required for Jongo lib to be able to map the data from the database to a class
     */
    private Stock() { }

    public Stock(String name, String code, double minimum, double maximum, HashMap<String, Double> values, String currency, Date date) {
        if(name == null || name.equals("")) { throw new IllegalArgumentException("NULL or empty string not allowed as name"); }
        if(code == null || code.equals("")) { throw new IllegalArgumentException("NULL or empty string not allowed as code"); }
        if(minimum > maximum) { throw new IllegalArgumentException("Minimum can't be higher than maximum"); }
        if(values == null || values.isEmpty())  { throw new IllegalArgumentException("Values may not be empty"); }
        if(currency == null || currency.equals("")) { throw new IllegalArgumentException("Currency may not be empty"); }
        if(date == null) { throw new IllegalArgumentException("Date may not be null"); }

        this.name = name;
        this.code = code;
        this.minimum = minimum;
        this.maximum = maximum;
        this.values = values;
        this.currency = currency;
        this.date = date;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        if(_id == null) { throw new IllegalArgumentException("ID may not be null"); }
        else {
            this._id = _id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.equals("")) { throw new IllegalArgumentException("Name may not be empty"); }
        else {
            this.name = name;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if(code == null || code.equals("")) { throw new IllegalArgumentException("Code may not be null"); }
        else {
            this.code = code;
        }
    }

    public double getMinimum() {
        return minimum;
    }

    public void setMinimum(double minimum) {
        if(minimum > this.maximum) { throw new IllegalArgumentException("New minimum can't be higher than maximum"); }
        else {
            this.minimum = minimum;
        }
    }

    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        if(maximum < this.minimum) { throw new IllegalArgumentException("New maximum can't be lower than minimum"); }
        else {
            this.maximum = maximum;
        }
    }

    public HashMap<String, Double> getValues() {
        return values;
    }

    public void setValues(HashMap<String, Double> values) {
        if(values == null || values.isEmpty()) { throw new IllegalArgumentException("Values may not be empty"); }
        else {
            this.values = values;
        }
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        if(currency == null || currency.equals("")) { throw new IllegalArgumentException("Currency may not be null"); }
        else {
            this.currency = currency;
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if(date == null) { throw new IllegalArgumentException("Date may not be empty"); }
        else {
            this.date = date;
        }
    }

    public static Stock createNewStock(String name, String code, double minimum, double maximum, HashMap<String, Double> values, String currency, Date date) {
        if(name == null || name.equals("")) { throw new IllegalArgumentException("NULL or empty string not allowed as name"); }
        if(code == null || code.equals("")) { throw new IllegalArgumentException("NULL or empty string not allowed as code"); }
        if(minimum > maximum) { throw new IllegalArgumentException("Minimum can't be higher than maximum"); }
        if(values == null || values.isEmpty())  { throw new IllegalArgumentException("Values may not be empty"); }
        if(currency == null || currency.equals("")) { throw new IllegalArgumentException("Currency may not be empty"); }
        if(date == null) { throw new IllegalArgumentException("Date may not be null"); }

        Stock stock = new Stock();
        stock.setName(name);
        stock.setCode(code);
        stock.setMaximum(maximum);
        stock.setMinimum(minimum);
        stock.setValues(values);
        stock.setCurrency(currency);
        stock.setDate(date);

        return stock;

    }
}
