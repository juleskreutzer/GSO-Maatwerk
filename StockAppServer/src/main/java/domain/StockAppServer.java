package domain;

import domain.database.DatabaseHandlerStock;
import domain.database.DatabaseHandlerUser;
import exceptions.*;
import interfaces.IStockReceive;
import interfaces.IStockSend;
import interfaces.IUserHandling;
import rest.Router;
import util.RequestTickerSymbols;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

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
public class StockAppServer extends UnicastRemoteObject implements IStockSend, IUserHandling {
    private static StockAppServer _instance;
    private List<User> loggedInUsers;
    private List<Group> activeGroups;
    private List<Notification> registeredNotifications;

    private StockAppServer() throws IOException {
        super(1099);
        _instance = this;

        this.loggedInUsers = new ArrayList<>();
        this.activeGroups = new ArrayList<>();
        this.registeredNotifications = new ArrayList<>();

        this.registerStockTask();

        clearActiveGroups();
        checkForCompletedNotifications();

        // Start the restful framework to allow incoming connections from the NodeJS server to manage new notification
        Router.getInstance();
    }

    public static StockAppServer getInstance() {
        try{
            return _instance == null ? new StockAppServer() : _instance;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void registerStockTask() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Start execution to fetch stocks each day at 23:30
                RequestTickerSymbols requestTickerSymbols = new RequestTickerSymbols();
                Set<String> symbols = requestTickerSymbols.requestTickerSymbols();
                FetchStocks.getInstance().execute(symbols);
            }
        });

        t.start();

    }

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
    @Override
    public void sendToGroup(Stock stock, String groupName, User user) throws StockIsNullException, GroupNameNotFoundException, UserIsNullException, UserNotFoundException, RemoteException {
        if(stock == null) { throw new StockIsNullException("Please provide a stock object."); }
        if(groupName.equals("")) { throw new GroupNameNotFoundException("Please provide a group name."); }
        if(user == null) { throw new UserIsNullException("Please provide the user object that is sending the request."); }

        for(Group g : this.activeGroups) {
            if(g.getName().toLowerCase().equals(groupName)) {
                ArrayList<User> users = (ArrayList<User>) g.getUsers();

                // Check if the user object is registered with the group
                if(!users.contains(user)) {
                    throw new UserNotFoundException("The provided user object that is sending the request is not registered with the provided groupname.");
                }

                for(User u : users) {
                    // The user that sends the request doesn't need to draw the stock object again.
                    // This if-statement will send the stock object to all users that are registered with the group corresponding to the provided group name
                    if(!u.getUsername().toLowerCase().equals(user.getUsername().toLowerCase())) {
                        IStockReceive receiveInterface = u.getReceiveInterface();
                        receiveInterface.receiveStockToDraw(stock);
                    }
                }
            }
        }

    }

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
    @Override
    public void sendToGroupByStockCode(String code, String groupName, User user) throws InvalidStockCodeException, GroupNameNotFoundException, UserIsNullException, UserNotFoundException, RemoteException {
        if(code.equals("")) { throw new InvalidStockCodeException("Please provide a valid stock code."); }
        if(groupName.equals("")) { throw new GroupNameNotFoundException("Please provide a group name."); }
        if(user == null) { throw new UserIsNullException("Please provide the user object that is sending the request."); }

        for(Group g : this.activeGroups) {
            if(g.getName().toLowerCase().equals(groupName)) {
                ArrayList<User> users = (ArrayList<User>) g.getUsers();

                // Check if the user object is registered with the group
                if(!users.contains(user)) {
                    throw new UserNotFoundException("The provided user object that is sending the request is not registered with the provided groupname.");
                }

                for(User u : users) {
                    // The user that sends the request doesn't need to draw the stock object again.
                    // This if-statement will send the stock object to all users that are registered with the group corresponding to the provided group name
                    if(!u.getUsername().toLowerCase().equals(user.getUsername().toLowerCase())) {
                        IStockReceive receiveInterface = u.getReceiveInterface();
                        receiveInterface.receiveStockToDrawAndSearch(code);
                    }
                }
            }
        }
    }

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
    @Override
    public void sendToUser(Stock stock, String username, User user) throws StockIsNullException, UserIsNullException, RemoteException {
        if(stock == null) { throw new StockIsNullException("Please provide a stock object to send to the user"); }
        if(username.equals("")) { throw new UserIsNullException("Please provide a username to send the stock object to."); }
        if(user == null) { throw new UserIsNullException("Please provide a user object"); }

        for(User u : this.loggedInUsers) {
            if(u.getUsername().toLowerCase().equals(username.toLowerCase())) {
                IStockReceive receiveInterface = u.getReceiveInterface();
                receiveInterface.receiveStockToDraw(stock);
            }
        }
    }

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
    @Override
    public void sendToUserByStockCode(String code, String username, User user) throws InvalidStockCodeException, UserIsNullException, RemoteException {
        if(code.equals("")) { throw new InvalidStockCodeException("Please provide a valid stock code."); }
        if(username.equals("")) { throw new UserIsNullException("Please provided a username to send the stock code to."); }
        if(user == null) { throw new UserIsNullException("please provide a user object"); }

        for(User u : this.loggedInUsers) {
            if (u.getUsername().toLowerCase().equals(username.toLowerCase())) {
                IStockReceive receiveInterface = u.getReceiveInterface();
                receiveInterface.receiveStockToDrawAndSearch(code);
            }
        }
    }

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
    @Override
    public void sendToGroupFromHistory(Date date, String code, String groupName, User user) throws InvalidStockCodeException, GroupNameNotFoundException, UserIsNullException, StockNotFoundException, RemoteException {
        if(date == null) { throw new IllegalArgumentException("Please provide a date"); }
        if(code.equals("")) { throw new InvalidStockCodeException("Please provide a valid stock code"); }
        if(groupName.equals("")) { throw new GroupNameNotFoundException("Please provide a groupName"); }
        if(user == null) { throw new UserIsNullException("Please provide a user object that is sending the request."); }

        for(Group g : this.activeGroups) {
            if(g.getName().toLowerCase().equals(groupName.toLowerCase())) {
                ArrayList<User> users = (ArrayList<User>) g.getUsers();

                for(User u : users) {
                    // The user that sends the request doesn't need to draw the stock object again.
                    // This if-statement will send the stock object to all users that are registered with the group corresponding to the provided group name
                    if(!u.getUsername().toLowerCase().equals(user.getUsername().toLowerCase())) {
                        Stock stock = DatabaseHandlerStock.getInstance().getStock(date, code);
                        IStockReceive receiveInterface = u.getReceiveInterface();
                        receiveInterface.receiveStockToDraw(stock);
                    }
                }
            }
        }
    }

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
    @Override
    public void sendToUserFromHistory(Date date, String code, String username, User user) throws InvalidStockCodeException, UserIsNullException, RemoteException, StockNotFoundException {
        if(date == null) { throw new IllegalArgumentException("Please provide a date"); }
        if(code.equals("")) { throw new InvalidStockCodeException("Please provide a valid stock code"); }
        if(username.equals("")) { throw new UserIsNullException("Please provide a username to send the stock object to."); }
        if(user == null) { throw new UserIsNullException("Please provide a user object that is sending the request."); }

        for(User u : this.loggedInUsers) {
            if (u.getUsername().toLowerCase().equals(username.toLowerCase())) {
                Stock stock = DatabaseHandlerStock.getInstance().getStock(date, code);
                IStockReceive receiveInterface = u.getReceiveInterface();
                receiveInterface.receiveStockToDraw(stock);
            }
        }

    }

    /**
     * Register a new user with the database.
     *
     * The database will check if the user's email and username already exists in the database. If this is the case, the server will return FALSE.
     * When the given credentials don't occur in the database, the database will insert the User object and return TRUE
     * @param user User object containing the credentials of the new user
     * @return True if the user isn't registered, false if user is registered
     * @throws MultipleFoundException Thrown when the username exists multiple times in the database
     * @throws UserIsRegisteredException Thrown when the username already exists once in the database
     * @throws NotFoundException Thrown from checkExistence method in DatabaseHandler class, not thrown when storing a user in the database
     * @throws UserIsNullException Thrown when the provided user object to the database method is null
     */
    @Override
    public boolean registerUser(User user) throws MultipleFoundException, UserIsRegisteredException, NotFoundException, UserIsNullException {
        DatabaseHandlerUser.getInstance().createUser(user);

        return true;
    }

    /**
     * Check the given credentials of the user object with the credentials that are stored in the database.
     * @param user User object containing the credentials of the user
     * @return True if the credentials in the User object match the credentials in the database, false if not
     * @throws UserNotFoundException Thrown when there is no user found with the given username
     * @throws InvalidCredentialsException Thrown when the password of the User object doesn't match with the password in the database
     * @throws UserIsNullException Thrown when the provided user object to the database methods is null
     */
    @Override
    public boolean loginUser(User user) throws UserNotFoundException, InvalidCredentialsException, UserIsNullException {
        boolean result = DatabaseHandlerUser.getInstance().login(user);

        if(result) {
            if(!this.loggedInUsers.contains(user)) { this.loggedInUsers.add(user); }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create a new group on the server. A user can then send a stock to this group
     * @param groupName Desired name for the group
     * @param user User object that will create the group
     * @return Return true if the group has been created, false if not
     * @throws GroupNameAlreadyExistsException Thrown when the given groupName is already registered with another group
     */
    @Override
    public String createGroup(String groupName, User user) throws GroupNameAlreadyExistsException {
        for(Group group : this.activeGroups) {
            if(group.getName().toLowerCase().equals(groupName)) {
                throw new GroupNameAlreadyExistsException("The provided group name (" + groupName + ") already exists. You may join this group, or create a group with a different group name.");
            }
        }

        ArrayList<User> tempUserList = new ArrayList<>();
        tempUserList.add(user);
        Group g = new Group(groupName, tempUserList);
        this.activeGroups.add(g);

        return g.getName();

    }

    /**
     * Join a group so that the user can receive stock objects that are sent to the group
     * @param groupName Name of the group that the user wants to join
     * @param user User who wants to join
     * @return Returns true when the user has joined the group, false if not
     * @throws GroupNameNotFoundException Thrown when there can't be found any group with the given groupName
     */
    @Override
    public boolean joinGroup(String groupName, User user) throws GroupNameNotFoundException {

        for(Group g : this.activeGroups) {
            if(g.getName().toLowerCase().equals(groupName)) {
                g.addUser(user);
                return true;
            }
        }

        throw new GroupNameNotFoundException("The group name \"" + groupName + "\" is not registered with the server.");
    }

    /**
     * Create a new notification with the server so the user will get an email when a stock reaches the desired value
     * @param code Unique code to identify the stock (https://en.wikipedia.org/wiki/Ticker_symbol)
     * @param email Email address of the user where the message will be sent to
     * @param minimum Minimum desired value of the stock
     * @param maximum Maximum desired value of the stock
     * @return returns true when the notification is created, false if not
     * @throws InvalidStockCodeException Thrown when the provided stock code is empty
     * @throws IllegalArgumentException Thrown when the provided email address is invalid, or when the minimum value is higher than the maximum value
     */
    @Override
    public boolean createNotification(String code, String email, Double minimum, Double maximum) throws InvalidStockCodeException, IllegalArgumentException, RemoteException {
        if(code.equals("")) { throw new InvalidStockCodeException("Please provide a valid stock code."); }
        if(email.equals("")) { throw new IllegalArgumentException("Please provide a valid email address."); }
        if(minimum > maximum) { throw new IllegalArgumentException("The minimum value can't be greater than the maximum value."); }

        Notification notification = new Notification(code, email, minimum, maximum);
        this.registeredNotifications.add(notification);

        return true;
    }

    /**
     * Check if a group that is stored in the arraylist activeGroups has not registered users. If so, remove the group from activeGroups
     *
     * This method will check all groups in activeGroups every hour
     */
    private synchronized void clearActiveGroups() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int i = 0;
                for(Group g : activeGroups) {
                    ArrayList<User> users = (ArrayList<User>) g.getUsers();

                    if(users.size() == 0) {
                        activeGroups.remove(g);
                        i++;
                    }
                }

                System.out.println(i + " group have been removed.\n" + activeGroups.size() + " group are still active");

            }
        }, 0, 3600000);
    }

    /**
     * Check if a notification that is stored in the arraylist registeredNotifications is completed and send a mail to the user.
     * When the notification is completed, remove the notification form the registeredNotifications list.
     *
     * This method will check all notifications in registeredNotifications every hour.
     */
    private synchronized void checkForCompletedNotifications() {

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int i = 0;

                for (Notification notification : registeredNotifications) {
                    // Check if a notification requirement is reached
                    boolean notificationRemoved = notification.checkStock();

                    // If notification is completed, remove it from the registeredNotifications list
                    if (notificationRemoved) {
                        registeredNotifications.remove(notification);
                        notification = null;
                        i++;
                    }
                }

                System.out.println(i + " Notifications have been completed.\n" + registeredNotifications.size() + " notifications are still active.");

            }
        }, 0, 3600000);
    }
}
