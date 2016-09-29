package domain;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 29-09-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: domain
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class UserTest {

    User u;

    @Before
    public void setup() throws Exception {
        u = User.createUser("Useranme", "password", "me@example.com");
    }

    @Test
    public void get_id() throws Exception {
        ObjectId _id = new ObjectId("100000000000000000000000");
        ObjectId _id2 = new ObjectId("200000000000000000000000");
        u.set_id(_id);
        assertEquals(_id.toHexString(), u.get_id().toHexString());
        assertNotEquals(_id2.toHexString(), u.get_id().toHexString());
        u.set_id(_id2);
        assertEquals(_id2.toHexString(), u.get_id().toHexString());

        try{
            u.set_id(null);
            fail("Not allowed to set NULL as ID");
        } catch (IllegalArgumentException e) { }


    }

    @Test
    public void getUsername() throws Exception {
        String newUsername = "Username 2";
        u.setUsername(newUsername);
        assertEquals(newUsername, u.getUsername());

        try{
            u.setUsername("");
            fail("Not allowed to set empty username");
        } catch (IllegalArgumentException e) { }

    }

    @Test
    public void getPassword() throws Exception {
        String newPassword = "Password 2";
        u.setPassword(newPassword);

        assertEquals(newPassword, u.getPassword());

        try{
            u.setPassword("");
            fail("Not allowed to set empty password");
        } catch (IllegalArgumentException e) { }

    }

    @Test
    public void getEmail() throws Exception {
        String newEmail = "juleskreutzer@me.com";
        u.setEmail(newEmail);
        assertEquals(newEmail, u.getEmail());

        try{
            u.setEmail("");
            fail("Not allowed to set empty email address");
        } catch (IllegalArgumentException e) { }

    }

    @Test
    public void getPreferredStock() throws Exception {
        User user = User.createUser("user name", "pass word", "e@mail.com");
        ArrayList<String> stockCodes = new ArrayList<>();

        assertEquals(stockCodes.size(), user.getPreferredStock().size());

        stockCodes.add("AAPL");
        stockCodes.add("FB");

        user.setPreferredStock(stockCodes);

        assertEquals(stockCodes.size(), user.getPreferredStock().size());

        for(int i = 0; i < user.getPreferredStock().size(); i++) {
            assertEquals(stockCodes.get(i), user.getPreferredStock().get(i));
        }

    }

    @Test
    public void createUser() throws Exception {
        ArrayList<String> stockCodes = new ArrayList<>();
        String username = "username";
        String password = "password";
        String email = "me@mail.com";

        User user = User.createUser(username, password, email);

        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());

        stockCodes.add("AAPL");
        stockCodes.add("FB");

        User user2 = User.createUser(username, password, email, stockCodes);

        assertEquals(username, user2.getUsername());
        assertEquals(password, user2.getPassword());
        assertEquals(email, user2.getEmail());

        for(int i = 0; i < user2.getPreferredStock().size(); i++) {
            assertEquals(stockCodes.get(i), user2.getPreferredStock().get(i));
        }

        try{
            User usr = User.createUser("", password, email);
            fail("Empty username not allowed");
        } catch (IllegalArgumentException e) { }

        try{
            User usr = User.createUser(username, "", email);
            fail("Empty password not allowed");
        } catch (IllegalArgumentException e) { }

        try{
            User usr = User.createUser(username, password, "");
            fail("Empty email address not allowed");
        } catch (IllegalArgumentException e) { }

    }

}