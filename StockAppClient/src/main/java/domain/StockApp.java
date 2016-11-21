package domain;

import interfaces.IStockReceive;
import interfaces.IStockSend;
import interfaces.IUserHandling;

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

    }

    @Override
    public void receiveStockToDrawAndSearch(String code) throws RemoteException {

    }
}
