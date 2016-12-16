package util;

import controllers.AlertMessage;
import domain.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
    private final String tickerSymbolsFileName = "tickersymbols.txt";

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
        writeTickerSymblsToFile(tickerSymbolsFileName, symbols);
    }

    private void writeTickerSymblsToFile(String fileName, Set<String> data) {
        try {
            BufferedWriter out =  new BufferedWriter(new FileWriter(tickerSymbolsFileName));
            Iterator it = data.iterator();
            while(it.hasNext()) {
                out.write(it.next() + "\n");
            }

            out.close();
            System.out.println("Ticker symbols written to file!");

        } catch (IOException e) {
            AlertMessage.showException("Unable to save the requested ticker symbols at this time. Please close this application and try again.", e);
        }

    }
}
