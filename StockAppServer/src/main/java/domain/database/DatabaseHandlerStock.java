package domain.database;

import domain.Stock;
import exceptions.InvalidStockCodeException;
import exceptions.StockAlreadyExistsException;
import exceptions.StockIsNullException;
import exceptions.StockNotFoundException;
import org.jongo.MongoCollection;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.markitOnDemand.InteractiveChartData;

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


        stockCollection.insert(stock);
    }

    public Stock getStock(Date date, String code) throws StockNotFoundException, InvalidStockCodeException {
        /**
         * This method currently requests it's data from the stockCollection. This needs to be changed to interactiveChartDataCollection instead.
         */
        throw new NotImplementedException();
//        if(date == null) { throw new IllegalArgumentException("Not allowed to provide empty date"); }
//        if(code.equals("")) { throw new InvalidStockCodeException("How can we find a stock when we have an invalid stock code?"); }
//
//        MongoCursor<Stock> cursor = stockCollection.find("{code: #, date: # }", code, date).as(Stock.class);
//
//        if(cursor.count() == 1) {
//            for(Stock s : cursor) {
//                return s;
//            }
//        } else {
//            throw new StockNotFoundException("Couldn't find the requested stock.");
//        }
//
//        return null;
    }

    public void addInteractiveChartDate(InteractiveChartData data) {
        if(data == null) { throw new IllegalArgumentException("No InteractiveChartData object provided."); }

        interactiveChartDataCollection.insert(data);

    }

}
