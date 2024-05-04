import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try{
            Registry registry = LocateRegistry.createRegistry(1099);
            // Now bind your remote object here
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            RemoteInterface obj = new RemoteObject();
            Naming.rebind("//localhost/RemoteObject", obj);
        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
