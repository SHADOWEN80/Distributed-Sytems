package server;

import server.ClientHandler;
import server.DeviceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainServer {
    private static ExecutorService executorService;
    private static BufferedReader in;
    private static PrintWriter out;
    private static final int PORT = 8080; // Choose an available port

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(10); // Adjust the pool size as needed

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Wait for a new client connection
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                Identify(clientSocket);
                // Create a new client handler thread
//                ClientHandler clientHandler = new ClientHandler(clientSocket);
//                executor.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void Identify(Socket clientSocket){
        System.out.println("identifying...");
        try {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        // Initial message to identify the connection
        out.println("Please identify yourself as 'client' or 'manager':");

        String identity = in.readLine();
        if ("manager".equalsIgnoreCase(identity)) {
            // Handle manager
            executorService = Executors.newCachedThreadPool();
            executorService.execute(new ManagerHandler(clientSocket));

        } else if ("client".equalsIgnoreCase(identity)) {
            // Handle client
            executorService = Executors.newCachedThreadPool();
            executorService.execute(new ClientHandler(clientSocket));

        } else {
            out.println("Invalid identity. Connection will be terminated.");
            clientSocket.close();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }}


}
