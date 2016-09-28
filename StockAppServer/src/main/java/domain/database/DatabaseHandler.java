package domain.database;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import domain.Stock;
import domain.User;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import java.io.IOException;

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
public abstract class DatabaseHandler {

    private static DatabaseHandler _instance;
    private MongoClient client;
    private MongoDatabase db;
    private DB database;
    private Jongo jongo;


    protected DatabaseHandler() {
        _instance = this;

        client = new MongoClient();

        if(client != null) {
            db = client.getDatabase("StockApp");
            database = client.getDB("StockApp");
            jongo = new Jongo(database);
        }
    }

    protected MongoCollection getMongoCollection(String collectionName) {
        return jongo.getCollection(collectionName);
    }

    /**
     * returns true if the specified value is present at the specified fieldName in the specified Table.
     * @param tableName Collection name you want to search in
     * @param fieldName Name of the field you want to search
     * @param value Value you want to search for
     * @param amount Amount of records that may be found. For example: amount = 5 when a user may add a max. of 5 keywords to a marker.
     *               Int may be null
     * @return When amount is null:
     *              true if the value is found, false if not
     *         When amount is not null:
     *              true when records of the same amount as the param amount are found, false if not
     */
    public boolean checkExistence(String tableName, String fieldName, String value, Integer amount) throws Exception {
        MongoCollection collection = jongo.getCollection(tableName);
        MongoCursor cursor;

        switch (tableName.toLowerCase()) {
            case "user":
                if(fieldName.toLowerCase().equals("_id")) {
                    cursor = collection.find("{_id: # }", new ObjectId(value)).as(User.class);
                }
                else {
                    cursor = collection.find("{" + fieldName + ": \"" + value + "\"}").as(User.class);
                }
                break;
            case "stock":
                if(fieldName.toLowerCase().equals("_id")) {
                    cursor = collection.find("{_id: # }", new ObjectId(value)).as(Stock.class);
                }
                else {
                    cursor = collection.find("{" + fieldName + ": #}", value).as(Stock.class);
                }
                break;
            default:
                throw new Exception("tableName not found");
        }

        int count = cursor.count();

        try {
            cursor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(amount == null) {

            if (count > 1) {
                throw new Exception("Multiple documents found");
            }
            if (count < 1) {
                throw new Exception("No documents found.");
            }

            return true;
        }
        else
        {
            if(amount == count) { return true; }
            else { return false; }
        }
    }
}

