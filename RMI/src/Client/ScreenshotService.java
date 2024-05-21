package Client;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

public class ScreenshotService extends UnicastRemoteObject implements IScreenshotService {

    public ScreenshotService() throws RemoteException {
        super();
    }
@Override
    public BufferedImage requestScreenshot() throws RemoteException {
    try {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        Robot robot = new Robot();
        BufferedImage screenCapture = robot.createScreenCapture(screenRect);
        // You can add code to process or save the screenshot if needed
        return screenCapture;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
    }
}
