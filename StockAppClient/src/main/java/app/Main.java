package app;

import controllers.LoginController;
import interfaces.IUserHandling;
import javafx.application.Application;
import javafx.stage.Stage;

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
        InetAddress IP= InetAddress.getLocalHost();
        System.out.println("IP of my system is := "+IP.getHostAddress());



        if(args.length == 1 && args[0].toLowerCase().equals("local")) {
            // Program started with local command, expect that server is running on local host
            reg = LocateRegistry.getRegistry("127.0.0.1", 1099);
            System.out.println("Attempting to connect to RMI server over 127.0.0.1");
        } else {
            // Program started without additional commands. Except that "the server" is available;
            reg = LocateRegistry.getRegistry("37.97.223.70", 1099);
            System.out.println("Attempting to connect to RMI server over 37.97.223.70");
        }

        LoginController.showMenu();

        //FileNotFoundException e = new FileNotFoundException("Couldn't find file blabla.txt");
        //AlertMessage.showException("Something went wrong. Please try again later.", e);
    }

    public static IUserHandling getIUserHandling() throws RemoteException, NotBoundException {
        return (IUserHandling) reg.lookup("StockApp");
    }
}
