package Client;

import Remote.RemoteInterface;
import Remote.RemoteObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientApp {
    static Scanner scanner = new Scanner(System.in);
    private static BufferedReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {


                try {
                    Socket clientSocket = new Socket("192.168.52.1", 8080);
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("connected to server");
                    System.out.println(in.readLine());
                    out.println(scanner.next());
                    String mac = DeviceIdentifier.getMacAddress();
                    makeRMI(mac);
                    out.println(mac);

                    while (!Thread.currentThread().isInterrupted()) {
                        // Send heartbeat signal
                        out.println("HEARTBEAT");
                        out.flush();
                        // Wait for a specified interval before sending the next heartbeat
                        Thread.sleep(5000);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    static void makeRMI(String mac){
        try{
            Registry registry = LocateRegistry.createRegistry(1099);
            // Now bind your remote object here
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            RemoteInterface obj = new RemoteObject();
            Naming.rebind("//localhost/"+mac+".RemoteObject", obj);
        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
