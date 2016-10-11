package domain;

import org.json.JSONArray;
import util.REQUEST_TYPE;
import util.RequestHandler;

import java.io.IOException;
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

        for (String symbol : tickerSymbols) {
            try {
                JSONArray result = RequestHandler.sendGet(REQUEST_TYPE.STOCK_QUOTE, symbol);
                for(int i = 0; i < result.length(); i++) {
                    String name = result.getJSONObject(i).getString("Name");
                    String code = result.getJSONObject(i).getString("Symbol");
                    double min = result.getJSONObject(i).getDouble("Low");
                    double max = result.getJSONObject(i).getDouble("High");
                    String currency = "USD";

                    Stock.createNewStock(name, code, min, max, null, currency, date);
                }

            } catch(IOException e){
                System.out.println("Failure for stockTask with the following symbol: " + symbol);
            }
        }
    }
}
