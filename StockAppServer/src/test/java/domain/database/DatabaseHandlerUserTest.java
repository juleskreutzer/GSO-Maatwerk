package domain.database;

import domain.User;
import exceptions.*;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.junit.Before;
import org.junit.Test;
import util.Hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 04-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: domain.database
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class DatabaseHandlerUserTest {

    User user;

    @Before
    public void setup() throws UserIsRegisteredException, UserIsNullException, NotFoundException, MultipleFoundException {
        MongoCollection collection = Database.getInstance().getMongoCollection("user");
        collection.remove();
        user = User.createUser("username", "password", "email");
        DatabaseHandlerUser.getInstance().createUser(user);
    }

    @Test
    public void createUser() throws Exception {
        String hashedPassword = Hash.hashString("password");
        MongoCursor<User> cursor = Database.getInstance().getMongoCollection("user").find("{username: #, password: # }", "username", hashedPassword).as(User.class);

        if(cursor.count() < 1) {
            fail("There should be at least 1 user document");
        }

        for(User u : cursor) {
            assertEquals("username", u.getUsername());
            assertEquals(hashedPassword, u.getPassword());
            assertEquals("email", u.getEmail());
        }

        try{
            User u = User.createUser("username", "password", "email");
            DatabaseHandlerUser.getInstance().createUser(u);
            fail("It is not allowed to create a user with this username because it already exists in the database");
        } catch (UserIsRegisteredException e) { }

        try{
            User u = User.createUser("", "password", "email");
            DatabaseHandlerUser.getInstance().createUser(u);
            fail("Not allowed to create a new user with empty username");
        } catch (IllegalArgumentException e) { }

        try {
            User u = User.createUser("username", "", "email");
            DatabaseHandlerUser.getInstance().createUser(u);
            fail("Not allowed to create a new user with empty password");
        } catch (IllegalArgumentException e) { }

        try {
            User u = User.createUser("username", "password", "");
            DatabaseHandlerUser.getInstance().createUser(u);
            fail("Not allowed to create a new user with empty email address");
        } catch (IllegalArgumentException e) { }

        try {
            DatabaseHandlerUser.getInstance().createUser(null);
            fail("A user object should be provided");
        } catch (UserIsNullException e) { }

    }

    @Test
    public void login() throws Exception {
        User usr = User.createUser("username", "password", "email");
        boolean loginSucces = DatabaseHandlerUser.getInstance().login(usr);

        assertEquals(true, loginSucces);

        try{
            DatabaseHandlerUser.getInstance().login(null);
            fail("A user object should be provided");
        } catch (UserIsNullException e) { }

        try{
            User u3 = User.createUser("UnknownUsername", "password", "email");
            DatabaseHandlerUser.getInstance().login(u3);
            fail("Login should throw an exception because the provided username isn't stored in the database.");
        } catch (UserNotFoundException e) { }


    }

}