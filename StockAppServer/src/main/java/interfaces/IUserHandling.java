package interfaces;

import domain.User;
import exceptions.GroupNameAlreadyExistsException;
import exceptions.GroupNameNotFoundException;
import exceptions.InvalidCredentialsException;
import exceptions.UserNotFoundException;

import java.rmi.Remote;
import java.rmi.RemoteException;

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
public interface IUserHandling extends Remote {

    /**
     * Register a new user with the database.
     *
     * The database will check if the user's email and username already exists in the database. If this is the case, the server will return FALSE.
     * When the given credentials don't occur in the database, the database will insert the User object and return TRUE
     * @param user User object containing the credentials of the new user
     * @return True if the user isn't registered, false if user is registered
     */
    public boolean registerUser(User user) throws RemoteException;

    /**
     * Check the given credentials of the user object with the credentials that are stored in the database.
     * @param user User object containing the credentials of the user
     * @return True if the credentials in the User object match the credentials in the database, false if not
     * @throws UserNotFoundException Thrown when there is no user found with the given username
     * @throws InvalidCredentialsException Thrown when the password of the User object doesn't match with the password in the database
     */
    public boolean loginUser(User user) throws UserNotFoundException, InvalidCredentialsException, RemoteException;

    /**
     * Create a new group on the server. A user can then send a stock to this group
     * @param groupName Desired name for the group
     * @param user User object that will create the group
     * @return Return true if the group has been created, false if not
     * @throws GroupNameAlreadyExistsException Thrown when the given groupName is already registered with another group
     */
    public String createGroup(String groupName, User user) throws GroupNameAlreadyExistsException, RemoteException;

    /**
     * Join a group so that the user can receive stock objects that are sent to the group
     * @param groupName Name of the group that the user wants to join
     * @param user User who wants to join
     * @return Returns true when the user has joined the group, false if not
     * @throws GroupNameNotFoundException Thrown when there can't be found any group with the given groupName
     */
    public boolean joinGroup(String groupName, User user) throws GroupNameNotFoundException, RemoteException;

    /**
     * Create a new notification with the server so the user will get an email when a stock reaches the desired value
     * @param code Unique code to identify the stock (https://en.wikipedia.org/wiki/Ticker_symbol)
     * @param email Email address of the user where the message will be sent to
     * @param minimum Minimum desired value of the stock
     * @param maximum Maximum desired value of the stock
     * @return returns true when the notification is created, false if not
     */
    public boolean createNotification(String code, String email, Double minimum, Double maximum) throws RemoteException;
}
