package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

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
public class Mapper {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * Map an object to a JSON string
     * @param instance Object you want to map
     * @return Json string containing the mapped object
     */
    public static String mapToJson(Object instance)
    {
        try {
            return mapper.writeValueAsString(instance);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Map a JSON string to an object
     * @param json Json string you want to map
     * @param type Class you want to map the JSON string to
     * @return Instance of a class
     */
    public static Object mapToObject(String json, Class type)
    {
        try{
            return mapper.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
