package util.listeners;

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

    private ListenerManager() {
        _instance = this;
        updateListeners = new LinkedList<>();
    }

    public static ListenerManager getInstance() {
        return _instance == null ? new ListenerManager() : _instance;
    }

    public void addListener(Update listener) {
        updateListeners.add(listener);
    }

    public void tickerSymbolsReceived() {
        for(Update listener : updateListeners) {
            listener.TickerSymbolsReceived();
        }
    }
}
