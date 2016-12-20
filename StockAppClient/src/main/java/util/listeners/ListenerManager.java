package util.listeners;

import domain.Stock;

import java.util.LinkedList;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 13-12-16
 * |
 * | Project Info:
 * | Project Name: StockAppClient
 * | Project Package Name: util.listeners
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class ListenerManager {
    private static ListenerManager _instance;
    private LinkedList<Update> updateListeners;
    private LinkedList<IDraw> drawListeners;

    private ListenerManager() {
        _instance = this;
        updateListeners = new LinkedList<>();
        drawListeners = new LinkedList<>();
    }

    public static ListenerManager getInstance() {
        return _instance == null ? new ListenerManager() : _instance;
    }

    public void addListener(Update listener) {
        updateListeners.add(listener);
    }

    public void addListener(IDraw listener) { drawListeners.add(listener); }

    public void tickerSymbolsReceived() {
        for(Update listener : updateListeners) {
            listener.TickerSymbolsReceived();
        }
    }

    public void sendDraw(Stock stockToDraw) {
        for(IDraw listener : drawListeners) {
            listener.draw(stockToDraw);
        }
    }
}
