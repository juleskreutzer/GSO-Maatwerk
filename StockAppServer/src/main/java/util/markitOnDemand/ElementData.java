package util.markitOnDemand;

import java.util.Date;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 11-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: util.markitOnDemand
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class ElementData {
    private String currency;
    private Date timeStamp;
    private String symbol;
    private ElementType type;

    /**
     * Y Axis Values of the specified Element, corresponding with the X Axis coordinates in the list of "Positions".
     */
    private Object dataSeries;

    public ElementData(String currency, Date timeStamp, String symbol, ElementType type, Object dataSeries) {
        this.currency = currency;
        this.timeStamp = timeStamp;
        this.symbol = symbol;
        this.type = type;
        this.dataSeries = dataSeries;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public Object getDataSeries() {
        return dataSeries;
    }

    public void setDataSeries(Object dataSeries) {
        this.dataSeries = dataSeries;
    }
}
