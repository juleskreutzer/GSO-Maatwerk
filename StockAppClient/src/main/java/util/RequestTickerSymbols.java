package util;

import javafx.concurrent.Task;
import org.json.JSONArray;
import util.listeners.ListenerManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 04-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: util
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class RequestTickerSymbols extends Task<Void> {

    //http://stackoverflow.com/questions/9939076/wait-until-child-threads-completed-java

    private static String[] chars = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static Set<String> symbols = new HashSet<>();

    public RequestTickerSymbols() { }

    @Override
    protected Void call() throws Exception {

        for (int i = 0; i < chars.length; i++) {
            String c = chars[i];
            try{
                JSONArray result = RequestHandler.sendGet(REQUEST_TYPE.LOOKUP, c);

                if(result != null) {
                    for (int j = 0; j < result.length() - 1; j++) {
                        symbols.add(result.getJSONObject(i).getString("Symbol"));
                        updateProgress(i + 1, chars.length);
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

            Thread.sleep(10000);
        }

        Helper.getInstance().setSymbols(symbols);
        ListenerManager.getInstance().tickerSymbolsReceived();

        return null;
    }
}
