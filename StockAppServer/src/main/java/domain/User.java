package domain;

import org.bson.types.ObjectId;

import java.io.Serializable;
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
public class User implements Serializable {

    private ObjectId _id;
    private String username;
    private String password;
    private String email;
    private List<String> preferredStock;

    /**
     * Private ctor is required for Jongo lib to map the data from the database to this class
     */
    private User() { }

    public ObjectId get_id() { return _id; }

    public void set_id(ObjectId _id) { this._id = _id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getPreferredStock() {
        return preferredStock;
    }

    public void setPreferredStock(List<String> preferredStock) {
        this.preferredStock = preferredStock;
    }

    public static User createUser(String username, String password, String email, List<String> preferredStock) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPreferredStock(preferredStock);

        return user;
    }

    public static User createUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        return user;
    }
}
