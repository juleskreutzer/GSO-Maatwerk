package domain;

import java.util.Calendar;
import java.util.Set;
import java.util.Timer;

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
public class FetchStocks {

    private static FetchStocks _instance;
    private Set<String> tickerSymbols;

    private FetchStocks() {
        _instance = this;
    }

    public static FetchStocks getInstance() {
        return _instance == null ? new FetchStocks() : _instance;
    }


    public void execute(Set<String> tickerSymbols) {
        this.tickerSymbols = tickerSymbols;

        // US Stock markets close at 21:00 UTC (22:00 CEST)

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);



        Timer time = new Timer(); // Instantiate Timer Object

        // Start running the task on Monday at 15:40:00, period is set to 8 hours
        // if you want to run the task immediately, set the 2nd parameter to 0
        //time.schedule(new StockTask(), calendar.getTime(), 1000 * 60 * 60 * 8);
        time.schedule(new StockTask(this.tickerSymbols), calendar.getTime(), 1);
        System.out.println("StockTask has been scheduled.");
    }


}
