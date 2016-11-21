package util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
public class RequestTickerSymbols {

    //http://stackoverflow.com/questions/9939076/wait-until-child-threads-completed-java

    private static String[] chars = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static Set<String> symbols = new HashSet<>();

    private ExecutorService service;
    private Set<Future<Set<String>>> set;



    public RequestTickerSymbols() {
        service = Executors.newFixedThreadPool(26);
        set = new HashSet<>();

    }

    /**
     * Request available ticker symbols from the Markit on Demand API
     *
     * Symbols will be
     * @return
     */
    public synchronized Set<String> requestTickerSymbols() {
        try {
            for (String c : chars) {
                RequestTickerSymbolsCallable callable = new RequestTickerSymbolsCallable(c);
                Future<Set<String>> future = service.submit(callable);
                set.add(future);
                wait(10000);
            }

            for (Future<Set<String>> f : set) {
                symbols.addAll(f.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return symbols;
    }
}
