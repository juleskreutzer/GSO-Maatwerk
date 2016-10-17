package interfaces;

import domain.Stock;
import domain.User;
import exceptions.*;

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
     * @throws StockIsNullException Thrown when the provided Stock object is null
     * @throws GroupNameNotFoundException Thrown when the provided groupName does not belong to any active group.
     * @throws UserIsNullException Thrown when the user object that sends the request is null
     * @throws UserNotFoundException Thrown when the user is not registered with the group based on the provided groupName param.
     */
    public void sendToGroup(Stock stock, String groupName, User user) throws RemoteException, StockIsNullException, GroupNameNotFoundException, UserIsNullException, UserNotFoundException;

    /**
     * Send the stock code to a group. The group has to retrieve the latest stock values from the Markit on Demand API.
     *
     * No params are nullable
     * @param code Unique code to identify the stock (https://en.wikipedia.org/wiki/Ticker_symbol)
     * @param groupName Name of the group the user wants to send the code to
     * @param user User sending the code
     * @throws InvalidStockCodeException Thrown when the provided code is empty
     * @throws GroupNameNotFoundException Thrown when the provided groupName does not belong to any active group.
     * @throws UserIsNullException  Thrown when the user object that sends the request is null
     * @throws UserNotFoundException Thrown when the user is not registered with the group based on the provided groupName param.
     */
    public void sendToGroupByStockCode(String code, String groupName, User user) throws RemoteException, InvalidStockCodeException, GroupNameNotFoundException, UserIsNullException, UserNotFoundException;

    /**
     * Send the stock graph to a user
     *
     * No params are nullable
     * @param stock Stock object that a user wants to send to a group
     * @param username Name of the desired user to send the stock to
     * @param user User sending the graph
     * @throws StockIsNullException Thrown when the provided stock object is null
     * @throws UserIsNullException Thrown when the provided username is null, or when a user with the provided username is not found, or when the provided user object is null.
     */
    public void sendToUser(Stock stock, String username, User user) throws RemoteException, StockIsNullException, UserIsNullException;

    /**
     * Send the stock code to a group. THe group has to retrieve the latest stock values from the Markit on Demand API.
     *
     * No params are nullable
     * @param code Unique code to identify the stock (https://en.wikipedia.org/wiki/Ticker_symbol)
     * @param username Name of the desired user to send the stock code to
     * @param user User sending the code
     * @throws InvalidStockCodeException Thrown when an invalid stock code is provided
     * @throws UserIsNullException Thrown when the provided username is null, or when a user with the provided username is not found, or when the provided user object is null.
     */
    public void sendToUserByStockCode(String code, String username, User user) throws RemoteException, InvalidStockCodeException, UserIsNullException;

    /**
     * Send the stock graph to a group for a stock that is stored in the database
     *
     * No params are nullable
     * @param date Date used to get the stock that belongs to that date from the database
     * @param code Unique code to identify the stock (https://en.wikipedia.org/wiki/Ticker_symbol)
     * @param groupName Name of the group the user wants to send the stock to
     * @param user User sending the request
     * @throws InvalidStockCodeException Thrown when the provided stock code is empty
     * @throws GroupNameNotFoundException Thrown when the provided group name is empty
     * @throws UserIsNullException Thrown when the provided user object is null
     * @throws StockNotFoundException Thrown when no stock could be found with the provided date and code
     */
    public void sendToGroupFromHistory(Date date, String code, String groupName, User user) throws RemoteException, InvalidStockCodeException, GroupNameNotFoundException, UserIsNullException, StockNotFoundException;

    /**
     * Send the stock graph to a user for a stock that is stored in the database
     *
     * No params are nullable
     * @param date Date used to get the stock that belongs to that date from the database
     * @param code Unique code to identify the stock (https://en.wikipedia.org/wiki/Ticker_symbol)
     * @param username Name of the desired user to send the stock to
     * @param user User sending the request
     * @throws InvalidStockCodeException Thrown when the provided stock code is empty
     * @throws UserIsNullException Thrown when the provided user object is null
     * @throws StockNotFoundException Thrown when no stock could be found with the provided date and code
     */
    public void sendToUserFromHistory(Date date, String code, String username, User user) throws RemoteException, InvalidStockCodeException, UserIsNullException, StockNotFoundException;


}
