package interfaces;

import domain.Stock;

import java.rmi.Remote;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 27-09-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: nl.nujules.interfaces
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public interface IStockReceive extends Remote {

    /**
     * When this method is called on the client, the client will read the fields of the object in order to create a graph and will then display
     * the stock in the UI of the client
     * @param stock Stock object containing the values that are needed to draw the graph
     */
    public void receiveStockToDraw(Stock stock);

    /**
     * When this method is called on the client, the client will fetch the latest stock values from the Markit on Demand API in order to create the stock object.
     * The client will than read the fields of the stock object in order to create a graph and will then display the stock in the UI of the client
     * @param code Unique code to identify the stock (https://en.wikipedia.org/wiki/Ticker_symbol)
     */
    public void receiveStockToDrawAndSearch(String code);
}
