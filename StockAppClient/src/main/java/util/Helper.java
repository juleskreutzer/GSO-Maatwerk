package util;

import controllers.AlertMessage;
import domain.User;

import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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
    private List<String> symbolsList;
    private final String tickerSymbolsFileName = "tickersymbols.txt";

    private Helper() {
        _instance = this;
        this.symbols = new TreeSet<>();
        this.symbolsList = new ArrayList<>();
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

    /**
     * Returns all symbols as a set (HashSet)
     * @return
     */
    public Set<String> getSymbols() {
        if(this.symbols.isEmpty()) {
            // Try to read the symbols from file.
            HashSet<String> symb = readTickerSymbolsFromFile(tickerSymbolsFileName);
            this.symbols = symb;
        }

        return symbols;
    }

    /**
     * This method returns an ArrayList of all symbols that can be used in, for example, a choiseBox.
     * @return
     */
    public ArrayList<String> getSymbolsAsList() {
        if(this.symbolsList.isEmpty()) {
            // SymbolsList is empty at this time. Try to get a list from the getSymbols() method
            HashSet<String> temp = (HashSet<String>) this.getSymbols();
            for (String symbol : temp) {
                this.symbolsList.add(symbol);
            }
        }

        return (ArrayList<String>) this.symbolsList;
    }

    public void setSymbols(Set<String> symbols) {
        this.symbols = symbols;
        writeTickerSymblsToFile(tickerSymbolsFileName, symbols);
    }

    private void writeTickerSymblsToFile(String fileName, Set<String> data) {
        try {
            TreeSet<String> temp = new TreeSet<>();
            for(String s : data) {
                temp.add(s);
            }
            BufferedWriter out =  new BufferedWriter(new FileWriter(fileName));
            Iterator it = temp.iterator();
            while(it.hasNext()) {
                out.write(it.next() + "\n");
            }

            out.close();
            System.out.println("Ticker symbols written to file!");

        } catch (IOException e) {
            AlertMessage.showException("Unable to save the requested ticker symbols at this time. Please close this application and try again.", e);
        }

    }

    private HashSet<String> readTickerSymbolsFromFile(String fileName) {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

            HashSet<String> set = new HashSet<>();
            while(in.readLine() != null) {
                set.add(in.readLine());
            }
            in.close();

            return set;
        } catch (IOException e) {
            AlertMessage.showException("Something went wrong while reading the tickers symbols from the file.", e);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Convert a Date object to a LocalDate object.
     * @param dateToConvert Date object to convert to LocalDate
     * @return LocalDate object based on provided dateToConvert object
     */
    public LocalDate convertDateToLocalDate(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dateToConvert);
//
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH) + 1; // without + 1, january would be 0
//        int day = cal.get(Calendar.DAY_OF_MONTH);
//
//        LocalDate date = LocalDate.parse(String.format("%s-%s-%s", year, month, day));
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        formatter = formatter.withLocale(Locale.getDefault());  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
//        LocalDate date = LocalDate.parse(String.format("%s-%s-%sT00:00:00", year, month, day), formatter);
//
//        return date;
    }

    /**
     * Convert a localDate object to a date object
     * @param dateToConvert localDate object to convert to date object
     * @return Date object converted from LocalDate
     */
    public Date convertLocalDateToDate(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
