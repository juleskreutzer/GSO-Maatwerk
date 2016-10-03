package domain.database;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 03-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: domain
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class Database {

    private static Database _instance;
    private MongoClient client;
    private MongoDatabase db;
    private DB database;
    private Jongo jongo;

    private Database() {
        _instance = this;

        client = new MongoClient();

        if (client != null) {
            db = client.getDatabase("StockApp");
            database = client.getDB("StockApp");
            jongo = new Jongo(database);
        }
    }

    public static Database getInstance() {
        return _instance == null ? new Database() : _instance;
    }

    public MongoCollection getMongoCollection(String collectionName) {
        return jongo.getCollection(collectionName);
    }

}
