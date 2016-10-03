package domain.database;

import domain.User;
import org.jongo.MongoCollection;

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

    public void createUser(User user) {

        //TODO: Store user object in database
    }

    public boolean login(User user) {
        //TODO: Check if credentials in user object match with data in database

        return false;
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
