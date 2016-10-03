package domain.database;

import domain.Stock;
import exceptions.StockIsNullException;
import exceptions.StockNotFoundException;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 03-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: domain.database
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class DatabaseHandlerStockTest {

    private Stock stock;
    HashMap<String, Double> values = new HashMap<>();
    Date date = new Date();
    MongoCollection stockCollection = Database.getInstance().getMongoCollection("stock");

    @BeforeClass
    public static void cleanDatabase() {
        MongoCollection collection = Database.getInstance().getMongoCollection("stock");
        collection.remove();
    }

    @Before
    public void setup() {
        values.clear();
        values.put("1", 1.0);
        values.put("2", 1.1);
        values.put("3", 1.2);
        values.put("4", 1.3);
        values.put("5", 1.4);
        values.put("6", 1.5);

        stock = Stock.createNewStock("Apple, Inc.", "AAPL", 1.0, 2.0, values, "USD", date);
    }

    @Test
    public void addStock() throws Exception {
        DatabaseHandlerStock.getInstance().addStock(stock);

        MongoCursor<Stock> stockCursor = stockCollection.find("{ code: #, date: # }", "AAPL", date).as(Stock.class);

        if(stockCursor.count() < 1) {
            fail("Cursor should have at least 1 document");
        }
        for(Stock s : stockCursor) {
            assertEquals(s.getCode(), "AAPL");
            assertEquals(s.getName(), "Apple, Inc.");
            assertEquals(s.getMinimum(), 1.0, 0.0);
            assertEquals(s.getMaximum(), 2.0, 0.0);
            assertEquals(s.getCurrency(), "USD");
            assertEquals(s.getDate(), date);
            assertEquals(s.getValues(), values);
        }

        try{
            DatabaseHandlerStock.getInstance().addStock(null);
            fail("Not allowed to send NULL");
        } catch (StockIsNullException e) { }


    }

    @Test
    public void getStock() throws Exception {
        Stock s = DatabaseHandlerStock.getInstance().getStock(date, "AAPL");

        assertNotNull(s);

        assertEquals(s.getCode(), "AAPL");
        assertEquals(s.getName(), "Apple, Inc.");
        assertEquals(s.getDate(), date);
        assertEquals(s.getValues(), values);
        assertEquals(s.getCurrency(), "USD");
        assertEquals(s.getMinimum(), 1.0, 0.0);
        assertEquals(s.getMaximum(), 2.0, 0.0);

        try{
            Stock stek = DatabaseHandlerStock.getInstance().getStock(date, "");
            fail("Not allowed to have an empty code");
        } catch (IllegalArgumentException e) { }

        try{
            Stock stek = DatabaseHandlerStock.getInstance().getStock(null, "FB");
            fail("Not allowed to have an empty date");
        } catch (IllegalArgumentException e) { }

        try{
            Stock stek = DatabaseHandlerStock.getInstance().getStock(date, "FB");
            fail("There shouldn't be any stock with code \"FB\'");
        } catch (StockNotFoundException e) { }
    }

}