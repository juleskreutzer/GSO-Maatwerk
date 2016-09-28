package domain;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 27-09-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: domain
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class GroupTest {

    List<User> users = new ArrayList<>();

    @BeforeClass
    public void setup() {
        User user1 = User.createUser("Useranme", "password", "me@mail.com");
        User user2 = User.createUser("Username 2", "password", "me@mail.com");
        User user3 = User.createUser("Username 3", "password" ,"me@mail.com");

        users.add(user1);
        users.add(user2);
        users.add(user3);
    }

    @Test
    public void test_ctor() throws Exception {
        String groupname = "name 1";
        Group g = new Group(groupname, users);
        assertEquals(groupname, g.getName());
        assertEquals(users.size(), g.getUsers().size());

        try{
            Group group = new Group("", users);
            fail("An empty groupname isn't allowed");
            throw new NullPointerException();
        } catch( IllegalArgumentException e) { }
    }

    @Test
    public void getName() throws Exception {
        String groupname = "GroupName1";
        Group group = new Group("GroupName1", users);
        assertEquals(groupname, group.getName());
    }

    @Test
    public void getUsers() throws Exception {
        User user4 = User.createUser("Username 4", "password", "me@example.com");
        User user5 = User.createUser("Username 5", "password", "me@example.com");
        User user6 = User.createUser("Username 6", "password", "me@example.com");

        Group group = new Group("Groupname", users);
        group.addUser(user4);

        ArrayList<User> usrs = new ArrayList<>();
        usrs.addAll(users);
        usrs.add(user4);

        assertEquals(usrs.size(), group.getUsers().size());

        List<User> user5and6 = new ArrayList<>();
        user5and6.add(user5);
        user5and6.add(user6);

        group.addUser(user5and6);
        usrs.addAll(user5and6);

        assertEquals(usrs.size(), group.getUsers().size());

    }
}