package domain.database;

import domain.User;
import exceptions.*;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import util.Hash;

import java.util.List;

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
public class DatabaseHandlerUser extends DatabaseHandler {
    private static DatabaseHandlerUser _instance;
    private MongoCollection userCollection;

    private DatabaseHandlerUser() {
        super();
        _instance = this;
        userCollection = super.getMongoCollection("user");
    }

    public static DatabaseHandlerUser getInstance() {
        return _instance == null ? new DatabaseHandlerUser() : _instance;
    }

    /**
     * Create a new User document in the database
     *
     * This method will check if the username already exists in the database and if a user object is provided.
     * @param user Instance of user class you want to insert into the database
     * @throws UserIsRegisteredException thrown when there is already a user registered with the given username
     * @throws UserIsNullException thrown when the user param is null
     * @throws NotFoundException Thrown from checkExistence method in DatabaseHandler class when the tableName or requested document couldn't be found
     * @throws MultipleFoundException Thrown from checkExistence method in DatabaseHandler class when the requested document is found multiple times
     */
    public void createUser(User user) throws UserIsRegisteredException, UserIsNullException, NotFoundException, MultipleFoundException {
        if(user == null) { throw new UserIsNullException("Please provide the user object to register"); }
        else if(!super.checkExistence("user", "username", user.getUsername(), 0)) { throw new UserIsRegisteredException("The given username already exists in the database"); }
        else {

            // Some extra security, hash the current password that the user has provided
            String pass = user.getPassword();
            String hashedPassword = Hash.hashString(pass);
            user.setPassword(hashedPassword);

            // User object should be OK now, insert into database.
            userCollection.insert(user);
        }
    }

    /**
     * Check the credentials of the given param with the credentials in the database.
     *
     * Because the username is unique, this method will search for users in the database based on their username.
     *
     * When the username <b>and</b> password match, true will be returned. If they don't match, false will be returned instead.
     * @param user Instance of User class you want to check the credentials for
     * @return True if login succeeds, false if not
     * @throws UserIsNullException Thrown when the user param is null
     */
    public boolean login(User user) throws UserIsNullException, UserNotFoundException {
        if(user == null) { throw new UserIsNullException("Please provide the user object to check the credentials for"); }
        else {
            MongoCursor<User> cursor = userCollection.find("{username: # }", user.getUsername()).as(User.class);

            if(cursor.count() != 1) { throw new UserNotFoundException("Incorrect credentials. Login failed."); }
            else {
                // Hash the provided password. This is also done during registration for extra security
                String hashedPassword = Hash.hashString(user.getPassword());

                // User is found. Check if credentials are correct
                for(User u : cursor) {
                    if(u.getUsername().equals(user.getUsername()) && u.getPassword().equals(hashedPassword)) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        }
    }

    public boolean addPreferredStock(User user, String code) {
        //TODO add preferred stock to user in database

        return false;
    }

    public boolean addPreferredStock(User user, List<String> codes) {
        //TODO add preferred stock to user in database

        return false;
    }
}
