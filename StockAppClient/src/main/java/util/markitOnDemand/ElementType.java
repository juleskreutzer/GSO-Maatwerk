package util.markitOnDemand;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public enum ElementType {
    // SMA is Simple Moving Average
    PRICE("price"),
    VOLUME("volume"),
    SMA("sma");

    private static Map<String, ElementType> FORMAT_MAP = Stream.of(ElementType.values()).collect(
            Collectors.toMap(s -> s.formatted, Function.identity()));

    private final String formatted;

    ElementType(String formatted) { this.formatted = formatted; }

    @JsonCreator
    public static ElementType fromString(String s) {
        ElementType elementType = FORMAT_MAP.get(s);
        if(elementType == null) {
            throw new IllegalArgumentException(s + "has no corresponding value.");
        }

        return elementType;
    }
}
