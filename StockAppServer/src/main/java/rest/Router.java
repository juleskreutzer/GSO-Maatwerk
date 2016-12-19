package rest;

import domain.StockAppServer;
import exceptions.InvalidStockCodeException;
import org.json.JSONObject;

import static spark.Spark.port;
import static spark.Spark.post;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 16-12-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: rest
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class Router {
    private static Router _instance;

    private Router() {
        _instance = this;
        port(8083);
        this.runEndpoint();
    }

    public static Router getInstance() {
        return _instance == null ? new Router() : _instance;
    }

    private void runEndpoint() {
        System.out.println("Restful service started.");

        post("/notification/add", (request, response) -> {
            String body = request.body();

            JSONObject json = new JSONObject(body);
            String code = json.getString("code");
            String email = json.getString("email");
            double minimum = json.getDouble("minimum");
            double maximum = json.getDouble("maximum");

            try {
                StockAppServer.getInstance().createNotification(code, email, minimum, maximum);
            } catch (InvalidStockCodeException | IllegalArgumentException e) {
                response.status(500);
                e.printStackTrace();
            }

            response.status(200);
            return null;

        }, new JsonTransformer());
    }
}
