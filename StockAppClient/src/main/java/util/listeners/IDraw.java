package util.listeners;

import domain.Stock;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 20-12-16
 * |
 * | Project Info:
 * | Project Name: StockAppClient
 * | Project Package Name: util.listeners
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public interface IDraw {

    /**
     * Draw a stock object in a chart
     * @param stock the stock object that needs to be drawn in the line chart
     */
    void draw(Stock stockToDraw);
}
