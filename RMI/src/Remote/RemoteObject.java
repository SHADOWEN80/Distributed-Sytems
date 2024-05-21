package Remote;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import com.github.sarxos.webcam.Webcam;

public class RemoteObject extends UnicastRemoteObject implements RemoteInterface {
    public RemoteObject() throws RemoteException {
        super();
    }

    @Override

    public BufferedImage requestCameraCapture() throws RemoteException {
        Webcam webcam = Webcam.getDefault();
        if (webcam != null) {
            System.out.println("Webcam: " + webcam.getName());

            // Open the webcam and capture an image
            webcam.open();
           BufferedImage capture= webcam.getImage();
            webcam.close();
            return capture;
        }
        return null;
    }
    @Override
    public byte[] requestCameraCaptureData() throws RemoteException {
        BufferedImage image=requestCameraCapture();
        return convertImageToByteArray(image);

    }
    @Override
    public BufferedImage requestScreenshot() throws RemoteException {
        try {
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            Robot robot = new Robot();
            // You can add code to process or save the screenshot if needed
            return robot.createScreenCapture(screenRect);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] convertImageToByteArray(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            // This shouldn't happen with a ByteArrayOutputStream, but if it does, you'll want to handle it
            throw new RuntimeException(e);
        }
        return baos.toByteArray(); // ByteArrayOutputStreams don't need to be closed
    }
    @Override
    public byte[] requestScreenshotData() throws RemoteException {
        // Capture screenshot and convert to byte array
        BufferedImage screenshot = requestScreenshot();
        // Convert BufferedImage to byte array (you'll need to implement this)

        return convertImageToByteArray(screenshot);
    }
    public String RemoteMethod(String parameter) throws RemoteException {
        return "Response from remote object";
    }

    public static BufferedImage reconstructImageFromByteArray(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            // Handle the IOException here
            e.printStackTrace();
            return null;
        }
    }
}
