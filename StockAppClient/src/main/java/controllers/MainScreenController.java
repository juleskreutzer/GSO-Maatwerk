package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
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

    @FXML
    LineChart<String, Double> lineChart;

    private static Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
}
