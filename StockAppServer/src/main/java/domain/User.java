package domain;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;
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
    private List<String> preferredStock = new ArrayList<>();

    /**
     * Private ctor is required for Jongo lib to map the data from the database to this class
     */
    private User() { }

    public ObjectId get_id() { return _id; }

    public void set_id(ObjectId _id) {
        if (_id == null) {
            throw new IllegalArgumentException("Null is not allowed as ObjectId");
        } else {
            this._id = _id;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if(username.equals("")) { throw new IllegalArgumentException("Empty username is not allowed"); }
        else {
            this.username = username;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password.equals("")) { throw new IllegalArgumentException("Empty password is not allowed"); }
        {
            this.password = password;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email.equals("")) { throw new IllegalArgumentException("Empty email address is not allowed"); }
        {
            this.email = email;
        }
    }

    public List<String> getPreferredStock() {
        return preferredStock;
    }

    public void setPreferredStock(List<String> preferredStock) {
            this.preferredStock = preferredStock;
    }

    public static User createUser(String username, String password, String email, List<String> preferredStock) {
        if(username.equals("")) { throw new IllegalArgumentException("Empty username not allowed"); }
        if(password.equals("")) { throw new IllegalArgumentException("Empty password not allowed"); }
        if(email.equals("")) { throw new IllegalArgumentException("Empty email address not allowed"); }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        if(!preferredStock.isEmpty()) {
            user.setPreferredStock(preferredStock);
        } else {
            // I have to create a temp list because I can't call the attributes in this class.
            // The instance of those attributes will be different of the user object that is created on line 92
            ArrayList<String> temp = new ArrayList<>();
            user.setPreferredStock(temp);
        }

        return user;
    }

    public static User createUser(String username, String password, String email) {
        if(username.equals("")) { throw new IllegalArgumentException("Empty username not allowed"); }
        if(password.equals("")) { throw new IllegalArgumentException("Empty password not allowed"); }
        if(email.equals("")) { throw new IllegalArgumentException("Empty email address not allowed"); }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // We have to create a list for the preferredStock and add it to the user object.
        // I can't call the attributes in this class because that instance will be different as the user instance I create
        // on line 113
        ArrayList<String> temp = new ArrayList<>();
        user.setPreferredStock(temp);

        return user;
    }
}
