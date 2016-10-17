package util.markitOnDemand;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty
    private String currency;

    @JsonProperty
    private Date timestamp;

    @JsonProperty
    private String symbol;

    @JsonProperty
    private ElementType type;

    /**
     * Y Axis Values of the specified Element, corresponding with the X Axis coordinates in the list of "Positions".
     */
    @JsonProperty
    private Object dataseries;

    public ElementData() { }

    public ElementData(String currency, Date timeStamp, String symbol, ElementType type, Object dataSeries) {
        this.currency = currency;
        this.timestamp = timeStamp;
        this.symbol = symbol;
        this.type = type;
        this.dataseries = dataSeries;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timestamp = timeStamp;
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

    public Object getDataseries() {
        return dataseries;
    }

    public void setDataseries(Object dataSeries) {
        this.dataseries = dataSeries;
    }
}
