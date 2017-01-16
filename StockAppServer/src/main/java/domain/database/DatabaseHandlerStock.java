package domain.database;

import com.mongodb.util.JSONParseException;
import domain.Stock;
import exceptions.InvalidStockCodeException;
import exceptions.StockAlreadyExistsException;
import exceptions.StockIsNullException;
import exceptions.StockNotFoundException;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 27-09-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: nl.nujules.domain.database
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class DatabaseHandlerStock extends DatabaseHandler {

    private static DatabaseHandlerStock _instance;
    private MongoCollection stockCollection;
    private MongoCollection interactiveChartDataCollection;

    private DatabaseHandlerStock() {
        super();
        _instance = this;
        stockCollection = super.getMongoCollection("stock");
        interactiveChartDataCollection = super.getMongoCollection("interactiveChartDataCollection");

    }

    public static DatabaseHandlerStock getInstance() {
        return _instance == null ? new DatabaseHandlerStock() : _instance;
    }

    public void addStock(Stock stock) throws StockIsNullException, StockAlreadyExistsException {
        if(stock == null) { throw new StockIsNullException("Not allowed to provide null as stock object"); }
        if(stock.getCode().equals("")) { throw new IllegalArgumentException("Not allowed to have empty code"); }
        if(stock.getDate() == null) { throw new IllegalArgumentException("Not allowed to have null as date"); }

        MongoCursor<Stock> cursor = stockCollection.find("{code: #, date: # }", stock.getCode(), stock.getDate()).as(Stock.class);

        if(cursor.count() == 0) {
            stockCollection.insert("{name: #, code: #, currency: #, date: #, maximum: #, minimum: #, values: #}", stock.getName(), stock.getCode(), stock.getCurrency(), stock.getDate(), stock.getMaximum(), stock.getMinimum(), stock.getValues());
        }
    }

    public Stock getStock(Date date, String code) throws StockNotFoundException, InvalidStockCodeException {

        if(date == null) { throw new IllegalArgumentException("Not allowed to provide empty date"); }
        if(code.equals("")) { throw new InvalidStockCodeException("How can we find a stock when we have an invalid stock code?"); }

        //Because StockTask will be run at 23:00, we have to change the date object to have 23:00 as time.
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, +24);
        Date newDate = cal.getTime();

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String dateAsISO = df.format(newDate);

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date convertedDate = null;
        try {
            convertedDate = formatter.parse(dateAsISO);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
        MongoCursor<Stock> cursor = stockCollection.find("{code: #, date: { $lt: # } }", code.toLowerCase(), convertedDate).as(Stock.class);

        if (cursor.count() >= 1) {
            for (Stock s : cursor) {
                return s;
            }
        } else {
            throw new StockNotFoundException("Couldn't find the requested stock.");
        }
    } catch (JSONParseException e) {
        e.printStackTrace();
    }

        return null;
    }
}
