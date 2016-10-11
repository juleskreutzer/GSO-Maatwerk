package domain;

import exceptions.GroupNameAlreadyExistsException;
import exceptions.GroupNameNotFoundException;
import exceptions.InvalidCredentialsException;
import exceptions.UserNotFoundException;
import interfaces.IStockSend;
import interfaces.IUserHandling;
import org.json.JSONArray;
import util.Mapper;
import util.REQUEST_TYPE;
import util.RequestHandler;
import util.markitOnDemand.Element;
import util.markitOnDemand.ElementType;
import util.markitOnDemand.InteractiveChartDataInput;

import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 27-09-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: nl.nujules.domain
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class StockAppServer extends UnicastRemoteObject implements IStockSend, IUserHandling {
    private static StockAppServer _instance;

    private StockAppServer() throws IOException {
        _instance = this;

        // Start execution to fetch stocks each day at 23:30
//        RequestTickerSymbols requestTickerSymbols = new RequestTickerSymbols();
//        Set<String> symbols = requestTickerSymbols.requestTickerSymbols();
//        FetchStocks.getInstance().execute(symbols);

        Element element = new Element("AAPL", ElementType.PRICE, null);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SS");
        String dateString = formatter.format(date);
        InteractiveChartDataInput input = new InteractiveChartDataInput(dateString, dateString, new Element[] {element});

        JSONArray result = RequestHandler.sendGet(REQUEST_TYPE.INTERACTIVE_CHART, Mapper.mapToJson(input));
    }

    public static StockAppServer getInstance() {
        try{
            return _instance == null ? new StockAppServer() : _instance;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void sendToGroup(Stock stock, String groupName, User user) {

    }

    @Override
    public void sendToGroupByStockCode(String code, String groupName, User user) {

    }

    @Override
    public void sendToUser(Stock stock, String username, User user) {

    }

    @Override
    public void sendToUserByStockCode(String code, String username, User user) {

    }

    @Override
    public void sendToGroupFromHistory(Date date, String code, String groupName, User user) {

    }

    @Override
    public void sendToUserFromHistory(Date date, String code, String username, User user) {

    }

    @Override
    public boolean registerUser(User user) {
        return false;
    }

    @Override
    public boolean loginUser(User user) throws UserNotFoundException, InvalidCredentialsException {
        return false;
    }

    @Override
    public String createGroup(String groupName, User user) throws GroupNameAlreadyExistsException {
        return null;
    }

    @Override
    public boolean joinGroup(String groupName, User user) throws GroupNameNotFoundException {
        return false;
    }

    @Override
    public boolean createNotification(String code, String email, Double minimum, Double maximum) {
        return false;
    }
}
