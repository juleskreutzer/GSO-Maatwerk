package controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.RequestTickerSymbols;
import util.listeners.ListenerManager;
import util.listeners.Update;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 28-11-16
 * |
 * | Project Info:
 * | Project Name: StockAppClient
 * | Project Package Name: controllers
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class SetupController implements Initializable, Update {

    private static Stage stage;
    private ProgressBar progressBar;

    @FXML
    private Pane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ListenerManager.getInstance().addListener(this);
        initializeData();
    }

    public void initializeData() {

        ObservableList<Node> list = pane.getChildren();
        for(Node node : list){
            if(node instanceof ProgressBar){
                progressBar = (ProgressBar)node;
            }
        }

        Task requestTickerSymbols = new RequestTickerSymbols();
        progressBar.progressProperty().bind(requestTickerSymbols.progressProperty());
        new Thread(requestTickerSymbols).start();

    }

    public static void showMenu() {
        Parent root = null;
        try {
            root = FXMLLoader.load(SetupController.class.getResource("/Setup.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(SetupController.class.getName()).log(Level.SEVERE, null, ex);
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

    @Override
    public void TickerSymbolsReceived() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainScreenController.showMenu();
            }
        });
    }
}
