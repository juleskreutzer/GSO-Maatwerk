package domain;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 27-09-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: domain
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class StockTest {

    Stock stock;
    HashMap<String, Double> values = new HashMap<>();
    @Before
    public void setup() {
        values.clear();
        values.put("1", 1.0);
        values.put("2", 1.1);
        values.put("3", 1.2);
        values.put("4", 1.3);
        values.put("5", 1.4);
        values.put("6", 1.5);

        stock = Stock.createNewStock("Apple, Inc.", "AAPL", 1.0, 2.0, values, "USD", new Date());
    }

    @Test
    public void test_ctor() throws Exception {
        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date newDate = null;

        try{
            newDate = formatter.parse(formatter.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Stock s = new Stock("Facebook, Inc.", "FB", 1.0, 2.0, values, "USD", d);
        assertEquals("Facebook, Inc." , s.getName());
        assertEquals("FB", s.getCode());
        assertEquals(1.0, s.getMinimum(), 0.0);
        assertEquals(2.0, s.getMaximum(), 0.0);
        assertEquals(values, s.getValues());
        assertEquals("USD", s.getCurrency());
        assertEquals(newDate, s.getDate());

        try{
            Stock s2 = new Stock(null, "FB", 1.0, 2.0, values, "USD", d);
            fail("Stock name may not be null");
        } catch( IllegalArgumentException e) { }

        try{
            Stock s3 = new Stock("", "FB", 1.0, 2.0, values, "USD", d);
            fail("Stock name may not be empty");
        } catch( IllegalArgumentException e) { }

        try{
            Stock s4 = new Stock("Facebook", "", 1.0, 2.0, values, "USD", d);
            fail("Code may not be empty");
        } catch( IllegalArgumentException e) { }

        try{
            Stock s5 = new Stock("Facebook", null, 1.0, 2.0, values, "USD", d);
            fail("Code may not be null");
        } catch( IllegalArgumentException e) { }

        try{
            Stock s6 = new Stock("Facebook", "FB", 5.0, 2.0, values, "USD", d);
            fail("Minimum can't be higher than maximum");
        } catch( IllegalArgumentException e) { }

        try{
            Stock s7 = new Stock("Facebook", "FB", 1.0, -1.0, values, "USD", d);
            fail("Maximum can't be lower than minimum");
        } catch( IllegalArgumentException e) { }

        try{
            Stock s8 = new Stock("Facebook", "FB", 1.0, 2.0, values, null, d);
            fail("Currency may not be null");
        } catch( IllegalArgumentException e) { }

        try{
            Stock s9 = new Stock("Facebook", "FB", 1.0, 2.0, values, "", d);
            fail("Currency may not be empty");
        } catch( IllegalArgumentException e) { }

        try{
            Stock s10 = new Stock("Facebook", "FB", 1.0, 2.0, values, "USD", null);
            fail("Date may not be null");
        } catch( IllegalArgumentException e) { }

        try{
            Stock s11 = new Stock("Facebook", "FB", 1.0, 2.0, null, "USD", d);
            fail("Values may not be null");
        } catch( IllegalArgumentException e) { }

        try{
            values.clear();
            Stock s12 = new Stock("Facebook", "FB", 1.0, 2.0, values, "USD", d);
        } catch( IllegalArgumentException e) { }
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_id() throws Exception {
        ObjectId id = new ObjectId("100000000000000000000000");
        stock.set_id(id);

        assertEquals(id.toHexString(), stock.get_id().toHexString());

        /** following method call should give an IllegalArgumentException */
        stock.set_id(null);
    }

    @Test
    public void getName() throws Exception {
        String name = "Apple, Inc.";
        assertEquals(name, stock.getName());

        String name2 = "Facebook, Inc.";
        stock.setName(name2);
        assertEquals(name2, stock.getName());

        try{
            stock.setName(null);
            fail("Not allowed to set <<NULL>> as name");
        } catch(IllegalArgumentException e) {}

        try{
            stock.setName("");
            fail("Not allowed to set an empty name");
        } catch(IllegalArgumentException e) { }
    }

    @Test
    public void getCode() throws Exception {
        String code = "AAPL";
        assertEquals(code, stock.getCode());

        String code2 = "FB";
        stock.setCode(code2);
        assertEquals(code2, stock.getCode());

        try{
            stock.setCode("");
            fail("Not allowed to set an empty code");
        } catch (IllegalArgumentException e) { }

        try {
            stock.setCode(null);
            fail("Not allowed to set NULL as code");
        } catch (IllegalArgumentException e) { }
    }

    @Test
    public void getMinimum() throws Exception {
        double minimum = 1.0;
        assertEquals(minimum, stock.getMinimum(), 0.0);

        double newMinimum = 1.01;
        stock.setMinimum(newMinimum);
        assertEquals(newMinimum, stock.getMinimum(), 0.0);

        try{
            double tooHighMinimum = 5.0;
            stock.setMinimum(tooHighMinimum);
            fail("The minimum of a stock can't be higher than the maximum");
        } catch (IllegalArgumentException e) { }

    }

    @Test
    public void getMaximum() throws Exception {
        double maximum = 2.0;

        assertEquals(maximum, stock.getMaximum(), 0.0);

        double newMaximum = 2.1;
        stock.setMaximum(newMaximum);
        assertEquals(newMaximum, stock.getMaximum(), 0.0);

        try{
            double tooLowMaximum = -2.0;
            stock.setMaximum(tooLowMaximum);
            fail("The maximum of a stock can't be lower than the minimum");
        } catch (IllegalArgumentException e) { }

    }

    @Test
    public void getValues() throws Exception {
        assertEquals(values, stock.getValues());

        values.put("7", 1.7);
        stock.setValues(values);
        assertEquals(values, stock.getValues());

        values.remove("1");
        stock.setValues(values);
        assertEquals(values, stock.getValues());

        try{
            stock.setValues(null);
            fail("Not allowed to set null values");
        } catch (IllegalArgumentException e) { }

    }

    @Test
    public void getCurrency() throws Exception {
        String currency = "USD";
        assertEquals(currency, stock.getCurrency());

        String newCurrency = "EUR";
        stock.setCurrency(newCurrency);
        assertEquals(newCurrency, stock.getCurrency());

        try{
            stock.setCurrency("");
            fail("Not allowed to set empty currency value");
        } catch (IllegalArgumentException e) { }

        try{
            stock.setCurrency(null);
            fail("Not allowed to set NULL as currency value");
        } catch (IllegalArgumentException e) { }
    }

    @Test (expected = IllegalArgumentException.class)
    public void getDate() throws Exception {
        Date date = new Date();
        stock.setDate(date);

        assertEquals(date, stock.getDate());

        stock.setDate(null);
    }

    @Test
    public void createNewStock() throws Exception {

    }

}