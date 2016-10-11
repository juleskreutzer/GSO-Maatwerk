package util;

import org.json.JSONArray;

import javax.xml.ws.http.HTTPException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 03-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: util
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class RequestHandler {
    private static String BASE_URL = "http://dev.markitondemand.com/Api/v2";
    private static String LOOKUP = "http://dev.markitondemand.com/api/v2/lookup/json";
    private static String QUOTE = "http://dev.markitondemand.com/api/v2/quote/json";
    private static String INTERACTIVECHART = "http://dev.markitondemand.com/api/v2/interactivechart";

    public static JSONArray sendGet(REQUEST_TYPE type, String params) throws IOException, HTTPException {
        try {
            URL obj = null;

            switch (type) {
                case LOOKUP:
                    obj = new URL(LOOKUP + "?input=" + params);
                    break;
                case STOCK_QUOTE:
                    obj = new URL(QUOTE);
                    break;
                case INTERACTIVE_CHART:
                    String p = URLEncoder.encode(params);
                    obj = new URL(INTERACTIVECHART + "?parameters=" + p);
                    break;
                default:
                    throw new IllegalArgumentException("The provided REQUEST_TYPE isn't implemented in this method.");
            }

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.75.14 (KHTML, like Gecko) Version/7.0.3 Safari/7046A194A");
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuilder sb = new StringBuilder();
                String response;

                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine + "\n");
                }
                in.close();
                response = sb.toString();

                // print result
                return new JSONArray(response);
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                throw new HTTPException(responseCode);
            } else {
                throw new HTTPException(responseCode);
            }
        } catch (HTTPException ex) {
            System.out.print("HTTP ERROR CODE: " + ex.getStatusCode());
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

