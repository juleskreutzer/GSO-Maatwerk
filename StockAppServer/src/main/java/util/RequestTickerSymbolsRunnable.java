package util;

import org.json.JSONArray;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

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
public class RequestTickerSymbolsRunnable implements Runnable {
    private String c;
    private final CountDownLatch latch;

    protected RequestTickerSymbolsRunnable(String c, CountDownLatch latch) {
        this.c = c;
        this.latch = latch;
    }


    @Override
    public void run() {

        try{
            JSONArray result = RequestHandler.sendGet(REQUEST_TYPE.LOOKUP, c);

            for(int i = 0; i < result.length(); i++) {
                System.out.println(result.getJSONObject(i).getString("Symbol"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }

    }
}
