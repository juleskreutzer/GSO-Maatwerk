package util.listeners;

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
public interface Update {
    /**
     * This method can be used to indicate that all Ticker symbols are received from the 3th party service, and eventually stored to a file.
     */
    public void TickerSymbolsReceived();
}
