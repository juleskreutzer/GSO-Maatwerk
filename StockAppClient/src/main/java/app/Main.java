package app;

import controllers.AlertMessage;
import controllers.LoginController;
import domain.StockApp;
import interfaces.IStockSend;
import interfaces.IUserHandling;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 18-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppClient
 * | Project Package Name: main.java.app
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class Main extends Application {

    private static Registry reg;
    private static String[] args;

    public static void main(String[] arguments) throws Exception {
        args = arguments;
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(-1);
            }
        });
        InetAddress IP= InetAddress.getLocalHost();
        System.out.println("IP of my system is := "+IP.getHostAddress());



        if(args.length == 1 && args[0].toLowerCase().equals("local")) {
            // Program started with local command, expect that server is running on local host
            reg = LocateRegistry.getRegistry(IP.getHostAddress(), 1099);
            System.out.println("Attempting to connect to RMI server over 127.0.0.1");
        } else {
            // Program started without additional commands. Except that "the server" is available;
            reg = LocateRegistry.getRegistry("37.97.223.70", 1099);
            System.out.println("Attempting to connect to RMI server over 37.97.223.70");
        }

        try {
            StockApp.getInstance().setServerInterfaces((IStockSend) reg.lookup("StockApp"), (IUserHandling) reg.lookup("StockApp"));
        } catch(RemoteException e) {
            AlertMessage.showException("Unable to connect to server.", e);
        } catch (NotBoundException e) {
            AlertMessage.showException("No server has been found with the name \"StockApp\" on the remote host.\nPlease try again later", e);
        }

        LoginController.showMenu();

        //FileNotFoundException e = new FileNotFoundException("Couldn't find file blabla.txt");
        //AlertMessage.showException("Something went wrong. Please try again later.", e);
    }
}
