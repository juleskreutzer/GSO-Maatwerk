package domain;

import domain.database.DatabaseHandlerStock;
import org.json.JSONObject;
import util.Mapper;
import util.RequestHandler;
import util.markitOnDemand.Element;
import util.markitOnDemand.ElementType;
import util.markitOnDemand.InteractiveChartData;
import util.markitOnDemand.InteractiveChartDataInput;

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

                Element element = new Element(symbol, ElementType.PRICE, new String[] { "ohlc" }) ;
                InteractiveChartDataInput input = new InteractiveChartDataInput(1, new Element[] {element});

                JSONObject result = RequestHandler.requestInteracitveChartDate(Mapper.mapToJson(input));
                InteractiveChartData icd = (InteractiveChartData) Mapper.mapToObject(result, InteractiveChartData.class);
                icd.setFetchDate(date);

                // Store the InteractiveChartData object into the database.
                DatabaseHandlerStock.getInstance().addInteractiveChartDate(icd);

                // Wait 10 seconds until a new request is fired from RequestHandler
                wait(10000);

            } catch(IOException e){
                System.out.println("Failure for stockTask with the following symbol: " + symbol);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
