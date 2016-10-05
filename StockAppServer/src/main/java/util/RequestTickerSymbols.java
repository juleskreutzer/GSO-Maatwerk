package util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

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

    private final CountDownLatch latch;
    private final RequestTickerSymbolsRunnable[] childs;


    public RequestTickerSymbols(CountDownLatch latch) {
        this.latch = latch;
        this.childs = new RequestTickerSymbolsRunnable[26];

        for(int x = 0; x < childs.length; x++)
        {
            childs[x] = new RequestTickerSymbolsRunnable(chars[x], this.latch);
        }
    }



    /**
     * Request available ticker symbols from the Markit on Demand API
     *
     * Symbols will be
     * @return
     */
    public Set<String> requestTickerSymbols() {
        Set<String> symbols = new HashSet<>();

        startChilcThreads();
        waitForThreadsToComplete();

        return null;

    }

    private void startChilcThreads() {
        Thread[] threads = new Thread[childs.length];

        for(int i = 0; i < threads.length; i++)
        {
            RequestTickerSymbolsRunnable runner = childs[i];
            threads[i] = new Thread(runner);
            threads[i].start();
        }
    }

    private void waitForThreadsToComplete() {
        try {
            latch.await();
            System.out.println("All child threads have completed.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
