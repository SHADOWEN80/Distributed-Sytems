import java.awt.image.BufferedImage;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface RemoteInterface extends Remote {
     String RemoteMethod(String text) throws RemoteException;
    BufferedImage requestScreenshot() throws RemoteException;
    byte[] requestScreenshotData() throws RemoteException;
}
