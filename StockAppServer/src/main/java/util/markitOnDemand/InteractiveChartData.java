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
public class InteractiveChartData {
    /**
     * Response object form Markit on Demand
     */

    /**
     * X Axis label position, text, and dates. The "dates" are in Microsoft "OA Date" format. The "text" is an ISO timestamp.
     */
    private Object labels;

    /**
     * List of X coordinate positions for each data point returned, between 0 and 1.
     */
    private Object positions;

    /**
     * Dates corresponding to each position represented as ISO8601
     */
    private Object dates;

    /**
     * Chart coordinates and other data for each Element specified in the input "Elements" array.
     */
    private ElementData[] elements;

    public InteractiveChartData(Object labels, Object positions, Object dates, ElementData[] elements) {
        this.labels = labels;
        this.positions = positions;
        this.dates = dates;
        this.elements = elements;
    }

    public Object getLabels() {
        return labels;
    }

    public void setLabels(Object labels) {
        this.labels = labels;
    }

    public Object getPositions() {
        return positions;
    }

    public void setPositions(Object positions) {
        this.positions = positions;
    }

    public Object getDates() {
        return dates;
    }

    public void setDates(Object dates) {
        this.dates = dates;
    }

    public ElementData[] getElements() {
        return elements;
    }

    public void setElements(ElementData[] elements) {
        this.elements = elements;
    }
}
