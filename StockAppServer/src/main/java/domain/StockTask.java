package domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

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
public class StockTask extends TimerTask {
    private HashSet<String> tickerSymbols;

    public StockTask(Set<String> tickerSymbols) {
        if(tickerSymbols.isEmpty()) { throw new IllegalArgumentException("Please provide tickerSymbols to fetch"); }
        this.tickerSymbols = (HashSet<String>) tickerSymbols;

    }

    @Override
    public void run() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm:ss z");
        Date date = new Date();
        System.out.println("StockTask has run. Execution time: " + formatter.format(date));
    }
}
