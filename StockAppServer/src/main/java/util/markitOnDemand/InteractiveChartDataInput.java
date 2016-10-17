package util.markitOnDemand;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 11-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: util
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class InteractiveChartDataInput {
    private boolean normalized;
    private int numberOfDays;
    private String dataPeriod; // The type of data requested. Minute, Hour, Day, Week, Month, Quarter, Year


    private Element[] elements;

    public InteractiveChartDataInput(int numberOfDays, Element[] elements) {
        this.normalized = false; // False shows data in price units, true in percentage
        this.numberOfDays = numberOfDays;
        this.dataPeriod = "Day";
        this.elements = elements;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public void setNormalized(boolean normalized) {
        this.normalized = normalized;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getDataPeriod() {
        return dataPeriod;
    }

    public void setDataPeriod(String dataPeriod) {
        this.dataPeriod = dataPeriod;
    }

    public Element[] getElements() {
        return elements;
    }

    public void setElements(Element[] elements) {
        this.elements = elements;
    }
}
