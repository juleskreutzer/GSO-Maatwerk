package app;


import domain.StockAppServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) throws UnknownHostException {

        InetAddress IP= InetAddress.getLocalHost();
        System.out.println("IP of my system is := "+IP.getHostAddress());

        if(args.length == 1 && args[0].toLowerCase().equals("local")) {
            System.out.println("Running on localhost");
            System.setProperty("java.rmi.server.hostname", IP.getHostAddress());
        } else {
            System.out.println("rmi hostname is set to 37.97.223.70");
            System.setProperty("java.rmi.server.hostname", "37.97.223.70");
        }

        try {
            Registry reg = LocateRegistry.createRegistry(1099);
            StockAppServer server = StockAppServer.getInstance();
            reg.rebind("StockApp", server);
            System.out.println("StockApp bound for StockAppServer object.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
