package server;
import com.github.sarxos.webcam.Webcam;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CameraCapture {

    public static void main(String[] args) {
        // Get the default webcam
        Webcam webcam = Webcam.getDefault();
        if (webcam != null) {
            System.out.println("Webcam: " + webcam.getName());

            // Open the webcam and capture an image
            webcam.open();
            BufferedImage image = webcam.getImage();

            // Save the captured image to a file
            try {
                ImageIO.write(image, "JPG", new File("selfie.jpg"));
                System.out.println("Image captured and saved successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Close the webcam
            webcam.close();
        } else {
            System.out.println("No webcam detected");
        }
    }
}
