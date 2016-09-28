package domain;

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
public class Group implements Serializable {

    private String name;
    private List<User> users;

    public Group(String name, List<User> users) {
        if(name == null || name.equals("")) { throw new IllegalArgumentException("The name of a group may not be empty"); }
        if(users.size() < 1) { throw new IllegalArgumentException("There has to be at least one user as a member of this group"); }

        this.users = new ArrayList<>();
        for(User u : users) {
            this.users.add((u));
        }
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        if(user == null) { throw new IllegalArgumentException("Did you forgot to provided a user object?"); }
        this.users.add(user);
    }

    public void addUser(List<User> users) {
        if(users.size() < 1) { throw new IllegalArgumentException("The user list you want to add doesn't contain any users"); }
        for(User u : users) {
            this.users.add(u);
        }
    }
}
