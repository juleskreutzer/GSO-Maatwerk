package controllers;

import domain.Stock;
import domain.StockApp;
import domain.User;
import exceptions.*;
import interfaces.IStockSend;
import interfaces.IUserHandling;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.Helper;
import util.RequestStockObject;
import util.listeners.IDraw;
import util.listeners.ListenerManager;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 08-11-16
 * |
 * | Project Info:
 * | Project Name: StockAppClient
 * | Project Package Name: controllers
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class MainScreenController implements Initializable, IDraw {

    @FXML
    Label stockNameLabel;

    @FXML
    TextField txtUsername, txtGroupname;

    @FXML
    ChoiceBox<String> cbStock;

    @FXML
    Button btnFavorite, btnNotification, btnSendStock, btnJoinGroup, btnCreateGroup;

    @FXML
    TextArea taStockInfo;

    @FXML
    DatePicker datePicker;

    //final NumberAxis xAxis = new NumberAxis();
    //final NumberAxis yAxis = new NumberAxis();
    //final CategoryAxis xAxis = new CategoryAxis();

    @FXML
    LineChart<String, Number> lineChart;

    @FXML
    private Pane chartPane;

    XYChart.Series<String, Number> series = new XYChart.Series<>();

    private static Stage stage;
    private LineChart<String, Number> chartToRemove;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Helper.getInstance().getSymbols().isEmpty()) {
            SetupController.showMenu();
        } else {
            ObservableList<String> observableList = FXCollections.observableArrayList(Helper.getInstance().getSymbolsAsList());
            cbStock.setItems(observableList);
        }

        cbStock.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("cbStock change recognized!");
                String value = cbStock.getValue();
                if (value != null) {
                    String[] args = splitArgs(value);
                    if(datePicker.getValue() != null) {
                        // retrieve stock from database
                        LocalDate localDate = datePicker.getValue();
                        Date date = Helper.getInstance().convertLocalDateToDate(localDate);
                        try {
                            StockApp.getInstance().getIStockSendInterface().sendToUserFromHistory(date, args[0], Helper.getInstance().getUser().getUsername(), Helper.getInstance().getUser());
                        } catch (RemoteException | InvalidStockCodeException | UserIsNullException | StockNotFoundException e) {
                            AlertMessage.showException("Something went wrong while retrieving the requested data from the database. Please try again later.", e);
                        } catch (NullPointerException e) {
                            AlertMessage.showException("We don't know how to connect to our server at this time. Please close this application and try again.", e);
                        }

                        datePicker.setValue(null);

                    } else {
                        // retrieve stock from Markit on Demand
                        try {
                            Stock stock = RequestStockObject.requestStock(args[0]);
                            stock.setName(args[1]);
                            stock.setCode(args[0]);
                            draw(stock);
                        } catch (Exception e) {
                            AlertMessage.showException("Something went wrong while fetching the latest info for the selected company. Please try again later.", e);
                        }
                    }
                }
            }
        });

        ListenerManager.getInstance().addListener(this);

    }

    public void addFavorite() {
        String value = cbStock.getValue();
        String code = this.splitArgs(value)[0];

        //TODO: Add ticker symbol from selected value to list of preferred stock
    }

    public void addNotification() {
        String value = cbStock.getValue();
        if(cbStock.getValue() == null || value.isEmpty()) {
            AlertMessage.show("Please select a stock.", "Please select a stock first before you create a new notification.", Alert.AlertType.WARNING);
        } else {
            String[] array = this.splitArgs(value);
            String code = array[0];
            String name = array[1];

            NotificationController.showMenu(name, code);
        }
    }

    public void sendStock() {
        String username = txtUsername.getText();
        String value = cbStock.getValue();
        LocalDate selectedLocalDate = datePicker.getValue();

        if(username.isEmpty()) {
            AlertMessage.show("Please provide a username", "A username is required to send the stock's information to another user.", Alert.AlertType.ERROR);
        } else if(selectedLocalDate == null) {
            AlertMessage.show("Please select a date", "Please select a date so we know where to start searching.", Alert.AlertType.INFORMATION);
        } else {
            try {
                IStockSend iStockSend = StockApp.getInstance().getIStockSendInterface();
                Date currentDate = new Date();
                Date selectedDate = Date.from(selectedLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                LocalDate currentLocalDate = Helper.getInstance().convertDateToLocalDate(currentDate);
                if(selectedLocalDate.isBefore(currentLocalDate)) {
                    // Retrieve stock from history
                    iStockSend.sendToUserFromHistory(selectedDate, this.splitArgs(value)[0], username, Helper.getInstance().getUser());
                } else {
                    // No stock from history
                    iStockSend.sendToUserByStockCode(this.splitArgs(value)[0], username, Helper.getInstance().getUser());
                }
            } catch (NullPointerException e) {
                AlertMessage.showException("We couldn't reach the server. Please restart this application and try again.", e);
            } catch (RemoteException | UserIsNullException | InvalidStockCodeException | StockNotFoundException e) {
                AlertMessage.showException("Something went wrong when we tried to send the stock to \"" + username + "\". Please try again later.", e);
            }
        }

    }

    public void joinGroup() {
        String groupName = txtGroupname.getText();
        User user = Helper.getInstance().getUser();

        if(groupName.isEmpty()) {
            AlertMessage.show("A group name is required.", "Without a groupname we can't determine which group you would like to join.", Alert.AlertType.ERROR);
        } else if(user == null) {
            AlertMessage.show("Username not set.", "We couldn't get your username. Please restart this application and login again so we can try to set your username again.", Alert.AlertType.ERROR);
        } else {
            try {
                IUserHandling iUserHandling = StockApp.getInstance().getIUserHandlingInterface();
                // TODO: Call method from iUserHandling to join group.

                boolean joined = iUserHandling.joinGroup(groupName, user);
                if(joined) {
                    Helper.getInstance().setJoinedGroup(groupName);
                    AlertMessage.show("Success", "You have successfully \"" + groupName + "\"", Alert.AlertType.INFORMATION);
                }
            } catch(NullPointerException e) {
                AlertMessage.showException("We couldn't reach the server. Please restart this application and try again.", e);
            } catch (GroupNameNotFoundException e) {
                AlertMessage.showException("The provided groupname doesn't exists.", e);
            } catch (RemoteException e) {
                AlertMessage.showException("Something went wrong when processing your request.\nPlease try again later.", e);
            }
        }

    }

    public void createGroup() {
        String groupName = txtGroupname.getText();
        User user = Helper.getInstance().getUser();

        if(groupName.isEmpty()) {
            AlertMessage.show("A group name is required.", "Please fill in a group name for the group you want to create.\nA group name can be used by other users to join your group.", Alert.AlertType.ERROR);
        } else if(user == null) {
            AlertMessage.show("Username not set.", "we couldn't get your username. Please restart this application and login again so we can try to set your username again.", Alert.AlertType.ERROR);
        } else {
            try {
                IUserHandling iUserHandling = StockApp.getInstance().getIUserHandlingInterface();
                String temp = iUserHandling.createGroup(groupName, user);

                if(temp.toLowerCase().equals(groupName.toLowerCase())) {
                    Helper.getInstance().setJoinedGroup(groupName);
                }

                AlertMessage.show("Success!", "You have successfully join the group \"" + groupName + "\"", Alert.AlertType.INFORMATION);
            } catch(NullPointerException e) {
                AlertMessage.showException("We couldn't reach the server. Please restart this application and try again.", e);
            } catch (GroupNameAlreadyExistsException e) {
                AlertMessage.showException("The chosen group name already exists.\nPlease choose another group name.", e);
            } catch (RemoteException e) {
                AlertMessage.showException("Something went wrong when processing your request.\nPlease try again later.", e);
            }
        }

    }

    public static void showMenu() {
        Parent root = null;
        try {
            root = FXMLLoader.load(MainScreenController.class.getResource("/MainScreen.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setTitle("StockApp");
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setScene(scene);
        stage.show();
    }

    private String[] splitArgs(String string) {
        return string.split(" - ");
    }

    @Override
    public void draw(Stock stockToDraw) {
        // Remove current data on line chart if set
//        series.getData().clear();
//
//        lineChart.setPickOnBounds(true);

//        final CategoryAxis categoryAxis = new CategoryAxis();
//        categoryAxis.setAutoRanging(false);
//
//        final NumberAxis numberAxis = new NumberAxis(stockToDraw.getMinimum() - 5, stockToDraw.getMaximum() + 5, 10);
//        numberAxis.setMinorTickVisible(false);
//
//        final LineChart<String,Number> tempChart = new LineChart<String,Number>(categoryAxis, numberAxis);


        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                chartPane.getChildren().remove(chartToRemove);

                Set<String> keys = stockToDraw.getValues().keySet();
                List<String> keysList = new ArrayList<>();
                keysList.addAll(keys);

                final CategoryAxis categoryAxis = new CategoryAxis(FXCollections.observableArrayList(keysList));
                categoryAxis.setAutoRanging(false);

                final NumberAxis numberAxis = new NumberAxis(stockToDraw.getMinimum() - 5, stockToDraw.getMaximum() + 5, 10);
                numberAxis.setMinorTickVisible(false);

                final LineChart<String,Number> tempChart = new LineChart<String,Number>(categoryAxis, numberAxis);


                stockNameLabel.setText(stockToDraw.getName() + " (" + stockToDraw.getCode() + ")");
                taStockInfo.setText("Name: " + stockToDraw.getName() + "\n" +
                        "Code: " + stockToDraw.getCode() + "\n" +
                        "Minumum: " + stockToDraw.getMinimum() + "\n" +
                        "Maximum: " + stockToDraw.getMaximum() + "\n" +
                        "Date: " + stockToDraw.getDate().toString() + "\n" +
                        "Currency: " + stockToDraw.getCurrency());

                for(String key : stockToDraw.getValues().keySet()) {
                    series.getData().add(new XYChart.Data<String, Number>(key, stockToDraw.getValues().get(key)));
                }

                tempChart.getData().add(series);
                chartToRemove = tempChart;

                chartPane.getChildren().add(tempChart);
            }
        });
    }
}
