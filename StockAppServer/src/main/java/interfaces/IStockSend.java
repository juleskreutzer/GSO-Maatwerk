package interfaces;

import domain.Stock;
import domain.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

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
public interface IStockSend extends Remote {

    /**
     * Send the stock graph to a group.
     *
     * No params are nullable
     * @param stock Stock object that a user wants to send to a group
     * @param groupName Name of the group the user wants to send the graph to
     * @param user User sending the graph
     */
    public void sendToGroup(Stock stock, String groupName, User user) throws RemoteException;

    /**
     * Send the stock code to a group. The group has to retrieve the latest stock values from the Markit on Demand API.
     *
     * No params are nullable
     * @param code Unique code to identify the stock (https://en.wikipedia.org/wiki/Ticker_symbol)
     * @param groupName Name of the group the user wants to send the code to
     * @param user User sending the code
     */
    public void sendToGroupByStockCode(String code, String groupName, User user) throws RemoteException;

    /**
     * Send the stock graph to a user
     *
     * No params are nullable
     * @param stock Stock object that a user wants to send to a group
     * @param username Name of the desired user to send the stock to
     * @param user User sending the graph
     */
    public void sendToUser(Stock stock, String username, User user) throws RemoteException;

    /**
     * Send the stock code to a group. THe group has to retrieve the latest stock values from the Markit on Demand API.
     *
     * No params are nullable
     * @param code Unique code to identify the stock (https://en.wikipedia.org/wiki/Ticker_symbol)
     * @param username Name of the desired user to send the stock code to
     * @param user User sending the code
     */
    public void sendToUserByStockCode(String code, String username, User user) throws RemoteException;

    /**
     * Send the stock graph to a group for a stock that is stored in the database
     *
     * No params are nullable
     * @param date Date used to get the stock that belongs to that date from the database
     * @param code Unique code to identify the stock (https://en.wikipedia.org/wiki/Ticker_symbol)
     * @param groupName Name of the group the user wants to send the stock to
     * @param user User sending the request
     */
    public void sendToGroupFromHistory(Date date, String code, String groupName, User user) throws RemoteException;

    /**
     * Send the stock graph to a user for a stock that is stored in the database
     *
     * No params are nullable
     * @param date Date used to get the stock that belongs to that date from the database
     * @param code Unique code to identify the stock (https://en.wikipedia.org/wiki/Ticker_symbol)
     * @param username Name of the desired user to send the stock to
     * @param user User sending the request
     */
    public void sendToUserFromHistory(Date date, String code, String username, User user) throws RemoteException;


}
