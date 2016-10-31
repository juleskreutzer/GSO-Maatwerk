package exceptions;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 03-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: exceptions
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class StockNotFoundException extends Exception {
    public StockNotFoundException(String message) { super(message); }
    public StockNotFoundException() { super(); }
}
