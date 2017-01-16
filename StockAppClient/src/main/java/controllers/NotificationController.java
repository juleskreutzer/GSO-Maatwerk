package controllers;

import domain.StockApp;
import exceptions.InvalidStockCodeException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 10-01-17
 * |
 * | Project Info:
 * | Project Name: StockAppClient
 * | Project Package Name: controllers
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class NotificationController implements Initializable {

    private static Stage stage;
    private static String stockName;
    private static String code;

    @FXML
    private TextField txtMinimum;

    @FXML
    private TextField txtMaximum;

    @FXML
    private TextField txtEmail;

    @FXML
    private Label lblName;

    @FXML
    private Button btnCreate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblName.setText("Your notification will be created for " + stockName);

        btnCreate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String minimumString = txtMinimum.getText();
                String maximumString = txtMaximum.getText();
                String email = txtEmail.getText();

                if(email.isEmpty()) {
                    AlertMessage.show("Please enter your email address.", "Your email is used to send you a notification when your preferred stock meets you requirements.", Alert.AlertType.WARNING);
                } else if(minimumString.isEmpty()) {
                    AlertMessage.show("Please enter a minimum value.", "", Alert.AlertType.WARNING);
                } else if(maximumString.isEmpty()) {
                    AlertMessage.show("Please enter a maximum value.", "", Alert.AlertType.WARNING);
                } else {
                    // All values are provided, check if minimum and maximum can be converted to double
                    try{
                        double minimum = Double.valueOf(minimumString.replace(",", "."));
                        double maximum = Double.valueOf(maximumString.replace(",", "."));

                        StockApp.getInstance().getIUserHandlingInterface().createNotification(code, email, minimum, maximum);
                    } catch (NumberFormatException e) {
                        AlertMessage.showException("Please fill in a valid minimum or maximum amount.", e);
                    } catch (InvalidStockCodeException e) {
                        AlertMessage.showException("Couldn't create a new notification. Please close this window and try again.", e);
                    } catch (NullPointerException e) {
                        AlertMessage.showException("Something went wrong. Please close this application and try again.", e);
                    }
                }
            }
        });


    }

    public static void showMenu(String name, String stockCode) {
        Parent root = null;
        try {
            root = FXMLLoader.load(MainScreenController.class.getResource("/Notification.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setTitle("Create new Notification");
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setScene(scene);
        stage.show();

        stockName = name;
        code = stockCode;
    }


}
