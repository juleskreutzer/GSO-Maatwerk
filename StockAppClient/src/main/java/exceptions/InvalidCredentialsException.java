package exceptions;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 27-09-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: nl.nujules.exceptions
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(String message) { super(message); }
    public InvalidCredentialsException() { super(); }
}
