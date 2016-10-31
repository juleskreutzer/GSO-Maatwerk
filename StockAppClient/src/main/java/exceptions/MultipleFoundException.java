package exceptions;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 04-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: exceptions
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class MultipleFoundException extends Exception {
    public MultipleFoundException(String message) { super(message); }
    public MultipleFoundException() { super(); }
}
