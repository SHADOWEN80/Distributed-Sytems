import java.rmi.Remote;
import java.rmi.RemoteException;
public interface RemoteInterface extends Remote {
    public String RemoteMethod(String text) throws RemoteException;
}
