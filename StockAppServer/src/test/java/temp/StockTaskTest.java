package temp;

import domain.Stock;
import domain.database.DatabaseHandlerStock;
import exceptions.StockAlreadyExistsException;
import exceptions.StockIsNullException;
import org.json.JSONObject;
import util.Mapper;
import util.RequestHandler;
import util.RequestTickerSymbols;
import util.markitOnDemand.*;

import java.io.IOException;
import java.util.*;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 31-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: temp
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class StockTaskTest {

    public static void main(String[] args) throws Exception {
        //String symbol = "AAPL";

        RequestTickerSymbols requestTickerSymbols = new RequestTickerSymbols();
        Set<String> symbols = requestTickerSymbols.requestTickerSymbols();
        for(String symbol : symbols) {
            try {
                System.out.println("Running stock task for symbol \"" + symbol + "\"");

                Element element = new Element(symbol, ElementType.PRICE, new String[]{"ohlc"});
                InteractiveChartDataInput input = new InteractiveChartDataInput(30, new Element[]{element});

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
                Double min = Double.valueOf(String.valueOf(low.get("min")));

                LinkedHashMap<String, Object> high = (LinkedHashMap<String, Object>) dataSeries.get("high");
                Double max = Double.valueOf(String.valueOf(high.get("max")));

                ArrayList<String> dates = (ArrayList<String>) icd.getDates();

                HashMap<String, Double> values = new HashMap<>();

                // Get teh correct values to draw a graph
                LinkedHashMap<String, Object> dataSeriesOpen = (LinkedHashMap<String, Object>) dataSeries.get("open");
                ArrayList<Double> dataSeriesOpenValues = (ArrayList<Double>) dataSeriesOpen.get("values");

                // Amount of positions should be the same as the amount of dates
                if (dataSeriesOpenValues.size() == dates.size()) {
                    for (int i = 0; i < dates.size(); i++) {
                        /**
                         * Combine the positions and dates together
                         *
                         * The key should be the date and the postion should be the value
                         */
                        values.put(dates.get(i), Double.valueOf(String.valueOf(dataSeriesOpenValues.get(i))));
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

                Stock stock = Stock.createNewStock("NOT FETCHED FROM DATABASE", code, min, max, values, currency, new Date());

                // Save stock in the database
                DatabaseHandlerStock.getInstance().addStock(stock);
            } catch (IOException e) {
                System.out.println("Failure for stockTask with the following symbol: " + symbol);
            } catch (StockAlreadyExistsException e) {
                System.out.println("The stock already exists in the database. \nTicker Symbol: " + symbol);
            } catch (StockIsNullException e) {
                System.out.println("Tried to store the stock object for " + symbol + " in the database, but this object was null.");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("No array data for \"" + symbol + "\"");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
