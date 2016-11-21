package util;

import domain.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 14-11-16
 * |
 * | Project Info:
 * | Project Name: StockAppClient
 * | Project Package Name: util
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class Helper {

    private static Helper _instance;

    /**
     * Field
     */
    private User user;
    private String joinedGroup;
    private ArrayList<String> preferredStock;
    private Set<String> symbols;

    private Helper() {
        _instance = this;
        this.symbols = new HashSet<>();
    }

    public static Helper getInstance() {
        return _instance == null ? new Helper() : _instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getJoinedGroup() {
        return joinedGroup;
    }

    public void setJoinedGroup(String joinedGroup) {
        this.joinedGroup = joinedGroup;
    }

    public ArrayList<String> getPreferredStock() {
        return preferredStock;
    }

    public void setPreferredStock(ArrayList<String> preferredStock) {
        this.preferredStock.addAll(preferredStock);
    }

    public void setPreferredStock(String preferredStock) {
        this.preferredStock.add(preferredStock);
    }

    public Set<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(Set<String> symbols) {
        this.symbols = symbols;
    }
}
