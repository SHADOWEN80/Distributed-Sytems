import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class RemoteObject extends UnicastRemoteObject implements RemoteInterface {
    protected RemoteObject() throws RemoteException {
        super();
    }

    public String RemoteMethod(String parameter) throws RemoteException {
        return "Response from remote object";
    }
}
