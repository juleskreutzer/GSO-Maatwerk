package domain;

import controllers.AlertMessage;
import interfaces.IStockReceive;
import interfaces.IStockSend;
import interfaces.IUserHandling;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import util.REQUEST_TYPE;
import util.RequestHandler;
import util.RequestStockObject;
import util.listeners.ListenerManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 18-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppClient
 * | Project Package Name: main.java.domain
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class StockApp extends UnicastRemoteObject implements IStockReceive {

    private static StockApp _instance;
    private static IStockReceive iStockReceive;

    /**
     * Interfaces to call methods on server
     */
    private IStockSend iStockSend;
    private IUserHandling iUserHandling;

    private StockApp() throws RemoteException {
        super();
        _instance = this;
        iStockReceive = (IStockReceive) this;
    }

    public static StockApp getInstance() {
        try {
            return _instance == null ? new StockApp() : _instance;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setServerInterfaces(IStockSend iStockSend, IUserHandling iUserHandling) {
        this.iStockSend = iStockSend;
        this.iUserHandling = iUserHandling;
    }

    public IStockSend getIStockSendInterface() {
        return this.iStockSend;
    }

    public IUserHandling getIUserHandlingInterface() {
        return this.iUserHandling;
    }

    @Override
    public void receiveStockToDraw(Stock stock) throws RemoteException {
        if(stock == null) {
            AlertMessage.show("Someone wants to send you a company's stock.", "Someone tried to send you a company's stock, but the received object doesn't have any data. Please try again.", Alert.AlertType.WARNING);
        }

        ListenerManager.getInstance().sendDraw(stock);
    }

    @Override
    public void receiveStockToDrawAndSearch(String code) throws RemoteException {
        if(code.equals("")) {
            AlertMessage.show("Someone wants to send you a company's stock.", "Someone tried to send you a compan's stock, but we didn't receive the company's ticker symbol. Please try again.", Alert.AlertType.WARNING);
        }

        try {
            Stock stock = RequestStockObject.requestStock(code);
            JSONArray result = RequestHandler.sendGet(REQUEST_TYPE.LOOKUP, code);

            String name = "";

            for(int i = 0; i < result.length(); i++) {
                name = result.getJSONObject(i).getString("Name");
            }

            stock.setName(name);

            // Now that we have the stock object with the correct name, we can send it to the MainScreenController
            ListenerManager.getInstance().sendDraw(stock);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
