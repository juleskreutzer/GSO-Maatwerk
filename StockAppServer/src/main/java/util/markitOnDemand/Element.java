package util.markitOnDemand;

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
public class Element {
    private String symbol;
    private ElementType type;
    private Object[] params;

    /**
     * Create an element object used in InteractiveChartDataInput class
     * @param symbol ticker symbol for the stock
     * @param type possible types: ElementType.PRICE, ElementType.VOLUME, ElementType.SMA
     */
    public Element(String symbol, ElementType type, Object[] params) {
        this.symbol = symbol;
        this.type = type;
        this.params = params;
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

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
