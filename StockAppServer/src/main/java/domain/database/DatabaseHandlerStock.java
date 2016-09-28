package domain.database;

import domain.Stock;
import org.jongo.MongoCollection;

import java.util.Date;

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

    private DatabaseHandlerStock() {
        super();
        _instance = this;
        stockCollection = super.getMongoCollection("stock");
    }

    public static DatabaseHandlerStock getInstance() {
        return _instance == null ? new DatabaseHandlerStock() : _instance;
    }

    public void addStock(Stock stock) {
        //TODO: Insert Stock object in database
    }

    public Stock getStock(Date date, String code) {
        //TODO retrieve stock from database

        return null;
    }

}
