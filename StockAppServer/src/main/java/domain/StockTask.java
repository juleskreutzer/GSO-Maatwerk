package domain;

import domain.database.DatabaseHandlerStock;
import exceptions.StockAlreadyExistsException;
import exceptions.StockIsNullException;
import org.json.JSONObject;
import util.Mapper;
import util.RequestHandler;
import util.markitOnDemand.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public synchronized void run() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm:ss z");
        int x = 1;

        /**
         * Fetch for each symbol an InteractiveChartData object, then do the following:
         *
         * - Get all data needed to create a stock object:
         *      x Code
         *      x Minimum
         *      x Maximum
         *      x Values
         *      x Currency
         *      x Date
         *
         *      The code and currency are fields of the InteractiveChartData object, other values are stored in the ElementData array in InteractiveChartData
         *      All values in ElementData are mapped by jackson to a LinkedHashMap<String, Object> except for the last array, this can be casted to ArrayList.
         *
         *      Set a breakpoint in the following code to see the structure of the response from Markit on Demand that is mapped to InteractiveChartData object.
         *      
         *  - Create a new Stock object
         *  - Store the newly created stock object in the database
         */
        for (String symbol : tickerSymbols) {
            try {
                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, -1);
                date = cal.getTime();
                System.out.println("Running stock task for symbol \"" + symbol + "\" at " + formatter.format(date));

                Element element = new Element(symbol, ElementType.PRICE, new String[] { "ohlc" }) ;
                InteractiveChartDataInput input = new InteractiveChartDataInput(30, new Element[] {element});

                // Request a InteractiveChartData object from Markit on Demand
                JSONObject result = RequestHandler.requestInteracitveChartDate(Mapper.mapToJson(input));
                InteractiveChartData icd = (InteractiveChartData) Mapper.mapToObject(result, InteractiveChartData.class);

                // Get all ElementData objects from the icd object
                ElementData[] elementDatas = icd.getElements();

                // Get values used in the ctor for a new Stock object
                String currency = elementDatas[0].getCurrency();
                String code = elementDatas[0].getSymbol();
                LinkedHashMap<String, Object> dataSeries = (LinkedHashMap<String, Object>) elementDatas[0].getDataseries();

                LinkedHashMap<String, Object> low = (LinkedHashMap<String, Object>) dataSeries.get("low");
                Double min = (Double) low.get("min");

                LinkedHashMap<String, Object> high = (LinkedHashMap<String, Object>) dataSeries.get("high");
                Double max = (Double) high.get("max");

                ArrayList<String> dates = (ArrayList<String>) icd.getDates();

                HashMap<String, Double> values = new HashMap<>();

                // Get teh correct values to draw a graph
                LinkedHashMap<String, Object> dataSeriesOpen = (LinkedHashMap<String, Object>) dataSeries.get("open");
                ArrayList<Double> dataSeriesOpenValues = (ArrayList<Double>) dataSeriesOpen.get("values");

                // Amount of positions should be the same as the amount of dates
                if(dataSeriesOpenValues.size() == dates.size()) {
                    for(int i = 0; i < dates.size(); i++) {
                        /**
                         * Combine the positions and dates together
                         *
                         * The key should be the date and the postion should be the value
                         */
                        values.put(dates.get(i), (Double) dataSeriesOpenValues.get(i));
                    }

                    // positions and dates have been matched together
                } else {
                    throw new Exception("The amount of positions and dates do not match.");
                }

                // Create the new stock object
                /**
                 * Another name is used than the correct name. To get the correct name, another request should be made to Markit on Demand.
                 *
                 * Because a client can search for a stock object in the database based on the code and date, the correct name is not needed. The client can perform a lookup
                 * to get the correct name from markit on demand on their side.
                 */

                Stock stock = Stock.createNewStock("NOT FETCHED FROM DATABASE", code , min, max, values, currency, date);

                // Save stock in the database
                DatabaseHandlerStock.getInstance().addStock(stock);

                // Wait 10 seconds until a new request is fired from RequestHandler
                wait(10000);

            } catch(IOException e){
                System.out.println("Failure for stockTask with the following symbol: " + symbol);
            } catch (StockAlreadyExistsException e) {
                System.out.println("The stock already exists in the database. \nTicker Symbol: " + symbol);
            } catch(StockIsNullException e) {
                System.out.println("Tried to store the stock object for " + symbol + " in the database, but this object was null.");
            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println("No array data for \"" + symbol + "\"");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
