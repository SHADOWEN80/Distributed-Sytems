package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.awt.image.BufferedImage;

public interface IScreenshotService extends Remote {
    BufferedImage requestScreenshot() throws RemoteException;
}
