import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            while (true){
                Scanner s = new Scanner(System.in);
                int num = s.nextInt();
                if (num==0)
                    break;
                if (num!=1)
                    continue;
                RemoteInterface obj = (RemoteInterface) Naming.lookup("//localhost/RemoteObject");
                System.out.println(obj.RemoteMethod("Hello, server!"));
                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                //
//            Robot robot = new Robot();
//            BufferedImage screenshot = robot.createScreenCapture(screenRect);

                byte[] screenshot = obj.requestScreenshotData();
                BufferedImage screen=RemoteObject.reconstructImageFromByteArray(screenshot);
                if (screen==null)
                    throw new RuntimeException();
                // Display the screenshot in a JFrame or process as needed
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new JLabel(new ImageIcon(screen)));
                frame.pack();
                frame.setVisible(true);
            }} catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
