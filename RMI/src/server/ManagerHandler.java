package server;

import Remote.RemoteObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ManagerHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private final String ManagerUsername = new String("manager");
    private final String ManagerPassword = new String("asdf");

    public ManagerHandler(Socket clientSocket) {

        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            if (!AuthenticateManager()) {
                out.println("Error:Unauthorized. Connection will be terminated.");
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Authentication Success");
            while (true) {
                String request;
                //System.out.println(DeviceManager.getAllDevices().toString());
                if (DeviceManager.getConnectedDeviceCount()>0)
                out.println(DeviceManager.getAllDevices().toString());
                else {
                    out.println("[]");
                    if (in.readLine().equalsIgnoreCase("refresh"))
                        continue;
                    else break;
                }
                request = in.readLine();
                if (DeviceManager.getAllDevices().contains(request))
                    out.println("true");
                else {
                    out.println("device is no more connected:");
                    continue;
                }
                System.out.println("found");
                DeviceInfo deviceInfo = DeviceManager.getDevice(request);
                String answer=in.readLine();
                if (answer.equalsIgnoreCase("true")) {
                    byte[] s = deviceInfo.getRemoteInterface().requestScreenshotData();
                    BufferedImage screenshot=RemoteObject.reconstructImageFromByteArray(s);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(screenshot, "png", byteArrayOutputStream);
                    // Convert ByteArrayOutputStream to byte array
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();
                    // Send the size of the byte array to the server
                    clientSocket.getOutputStream().write(ByteBuffer.allocate(4).putInt(imageBytes.length).array());
                    // Send the byte array (image) to the server
                    clientSocket.getOutputStream().write(imageBytes);

                     in.readLine();
                } else if (answer.equalsIgnoreCase("false")){
                    byte[] s = deviceInfo.getRemoteInterface().requestCameraCaptureData();
                    BufferedImage screenshot=RemoteObject.reconstructImageFromByteArray(s);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(screenshot, "png", byteArrayOutputStream);
                    // Convert ByteArrayOutputStream to byte array
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();
                    // Send the size of the byte array to the server
                    clientSocket.getOutputStream().write(ByteBuffer.allocate(4).putInt(imageBytes.length).array());
                    // Send the byte array (image) to the server
                    clientSocket.getOutputStream().write(imageBytes);

                    in.readLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void showScreenshot(BufferedImage screen){
        if (screen==null)
            throw new RuntimeException();
        // Display the screenshot in a JFrame or process as needed
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel(new ImageIcon(screen)));
        frame.pack();
        frame.setVisible(true);
    }
    private boolean AuthenticateManager() {
        try {
            System.out.println("Authenticating...");
            //get username
            out.println("Username:");
            String username = in.readLine();
            System.out.println(username);
            //get password
            out.println("password:");
            String password = in.readLine();
            return (username.equals(ManagerUsername) && password.equals(ManagerPassword));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
