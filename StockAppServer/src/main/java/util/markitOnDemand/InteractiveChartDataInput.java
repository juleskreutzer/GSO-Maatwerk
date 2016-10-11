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
    private String startDate;
    private String endDate;
    private int endOffsetDays;
    private int numberOfDays;
    private String dataPeriod; // The type of data requested. Minute, Hour, Day, Week, Month, Quarter, Year

    /**
    For intraday data, specifies the number of periods between data points. e.g. if DataPeriod is Minute and DataInterval is 5, you will get a chart with five minute intervals. Must be 0 or null for interday charts
     */
    private int dataInterval = 0;

    /**
     * The TimePeriod over which to create labels. Control how often you want labels by setting LabelInterval. Minute, Hour, Day, Week, Month, Quarter, Year
     */
    private String labelPeriod;
    private int labelInterval = 1;
    private Element[] elements;

    public InteractiveChartDataInput(String startDate, String endDate, Element[] elements) {
        this.normalized = false; // False shows data in price units, true in percentage
        this.startDate = startDate;
        this.endDate = endDate;
        this.endOffsetDays = 0;
        this.numberOfDays = 1;
        this.dataPeriod = "Day";
        this.labelPeriod = "Minute";
        this.elements = elements;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public void setNormalized(boolean normalized) {
        this.normalized = normalized;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getEndOffsetDays() {
        return endOffsetDays;
    }

    public void setEndOffsetDays(int endOffsetDays) {
        this.endOffsetDays = endOffsetDays;
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

    public int getDataInterval() {
        return dataInterval;
    }

    public void setDataInterval(int dataInterval) {
        this.dataInterval = dataInterval;
    }

    public String getLabelPeriod() {
        return labelPeriod;
    }

    public void setLabelPeriod(String labelPeriod) {
        this.labelPeriod = labelPeriod;
    }

    public int getLabelInterval() {
        return labelInterval;
    }

    public void setLabelInterval(int labelInterval) {
        this.labelInterval = labelInterval;
    }

    public Element[] getElements() {
        return elements;
    }

    public void setElements(Element[] elements) {
        this.elements = elements;
    }
}
