package util;

public enum REQUEST_TYPE {
    /**
     * The 3th party api provides 3 different endpoints:
     * - LOOKUP
     * - STOCK_QUOTE
     * - INTERACTIVE_CHART
     *
     * Each endpoint has different functionality.
     * When sending a request, the sendGet method needs this enum to identify which request has to be used.
     * URL parts will be stored in RequestHandler.java and will only be available to that class.
     */
    LOOKUP, STOCK_QUOTE, INTERACTIVE_CHART
}
