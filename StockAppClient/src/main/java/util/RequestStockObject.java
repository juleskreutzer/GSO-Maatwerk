package util;

import domain.Stock;
import org.json.JSONObject;
import util.markitOnDemand.*;

import java.util.*;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 19-12-16
 * |
 * | Project Info:
 * | Project Name: StockAppClient
 * | Project Package Name: util
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class RequestStockObject {

    public static Stock requestStock(String symbol) throws Exception {

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        date = cal.getTime();

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
        Double min = Double.valueOf(String.valueOf(low.get("min")));

        LinkedHashMap<String, Object> high = (LinkedHashMap<String, Object>) dataSeries.get("high");
        Double max = Double.valueOf(String.valueOf(high.get("max")));

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
                values.put(dates.get(i), Double.valueOf(String.valueOf(dataSeriesOpenValues.get(i))));
            }

            // positions and dates have been matched together
        } else {
            throw new Exception("The amount of positions and dates do not match.");
        }

        Stock stock = Stock.createNewStock("NOT FETCHED FROM DATABASE", code , min, max, values, currency, date);
        return stock;
    }
}
