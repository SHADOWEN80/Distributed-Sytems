package server;

import Remote.RemoteInterface;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private final String ManagerUsername= new String("manager");
    private final String ManagerPassword= new String("123456");

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        String mac=null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
             mac=in.readLine();
            RemoteInterface obj = (RemoteInterface) Naming.lookup("//localhost/"+mac+".RemoteObject");
            DeviceInfo deviceInfo=new DeviceInfo();
            deviceInfo.setIp(clientSocket.getInetAddress().toString());
            deviceInfo.setRemoteInterface(obj);
            DeviceManager.addDevice(mac,deviceInfo);
            System.out.println("Device "+mac+" added...");
            while (!Thread.currentThread().isInterrupted()){
                if (in.readLine().equalsIgnoreCase("HEARTBEAT")){
                    System.out.println("h");
                    Thread.sleep(5000);
                }else {
                    DeviceManager.removeDevice(mac);
                }
            }
        } catch (SocketException e){
            System.out.println("Device "+mac+" Disconnected");
            DeviceManager.removeDevice(mac);
            System.out.println(DeviceManager.getConnectedDeviceCount());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
