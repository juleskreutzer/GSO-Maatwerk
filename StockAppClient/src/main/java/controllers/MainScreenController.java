package controllers;

import domain.Stock;
import domain.StockApp;
import domain.User;
import exceptions.GroupNameAlreadyExistsException;
import exceptions.GroupNameNotFoundException;
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
import javafx.stage.Stage;
import util.Helper;
import util.RequestStockObject;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
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
public class MainScreenController implements Initializable {

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

    //final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    final CategoryAxis xAxis = new CategoryAxis();

    @FXML
    LineChart<String, Double> lineChart;

    XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();

    private static Stage stage;

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

                    try {
                        Stock stock = RequestStockObject.requestStock(args[0]);
                        draw(stock, args[0], args[1]);
                    } catch (Exception e) {
                        AlertMessage.showException("Something went wrong while fetching the latest info for the selected company. Please try again later.", e);
                    }
                }
            }
        });

    }

    public void addFavorite() {
        String value = cbStock.getValue();
        String code = this.splitArgs(value)[0];

        //TODO: Add ticker symbol from selected value to list of preferred stock
    }

    public void addNotification() {
        String value = cbStock.getValue();

        //TODO: show screen to create new notification

    }

    public void sendStock() {
        String username = txtUsername.getText();
        String value = cbStock.getValue();

        if(username.isEmpty()) {
            AlertMessage.show("Please provide a username", "A username is required to send the stock's information to another user.", Alert.AlertType.ERROR);
        } else {
            try {
                IStockSend iStockSend = StockApp.getInstance().getIStockSendInterface();


                // TODO: Check if user wants to send stock code or stock from history, call send method from iStockSend
            } catch (NullPointerException e) {
                AlertMessage.showException("We couldn't reach the server. Please restart this application and try again.", e);
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

    private void draw(Stock stockToDraw, String code, String name) {
        // Remove current data on line chart if set
        //lineChart.getData().clear();
        series.getData().clear();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stockNameLabel.setText(name + " (" + code + ")");
                taStockInfo.setText("Name: " + name + "\n" +
                        "Code: " + code + "\n" +
                        "Minumum: " + stockToDraw.getMinimum() + "\n" +
                        "Maximum: " + stockToDraw.getMaximum() + "\n" +
                        "Date: " + stockToDraw.getDate().toString() + "\n" +
                        "Currency: " + stockToDraw.getCurrency());
            }
        });

        for(String key : stockToDraw.getValues().keySet()) {
            series.getData().add(new XYChart.Data<String, Double>(key, Double.valueOf(stockToDraw.getValues().get(key))));
        }

        lineChart.getData().add(series);
    }

    private String[] splitArgs(String string) {
        return string.split(" - ");
    }
}
