package util;

import org.json.JSONArray;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 05-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: util
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class RequestTickerSymbolsCallable implements Callable<Set<String>> {
    private String c;

    protected RequestTickerSymbolsCallable(String c) {
        this.c = c;
    }

    @Override
    public Set<String> call() throws Exception {
        Set<String> set = new HashSet<>();
        try{
            JSONArray result = RequestHandler.sendGet(REQUEST_TYPE.LOOKUP, c);

            if(result != null) {
                for (int i = 0; i < result.length() - 1; i++) {
                    set.add(result.getJSONObject(i).getString("Symbol"));
                }
            } else {
                System.out.println("No ticker symbols found with character " + c);
            }
        } catch (IOException e) {
            System.out.println("Failure with character " + c);
            e.printStackTrace();
        } finally {
            System.out.println("Thread for character " + c + " has finished.");
        }

        return set;
    }
}
