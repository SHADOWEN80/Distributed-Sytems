package Client;

import Remote.RemoteInterface;
import Remote.RemoteObject;
import server.ManagerHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManagerApp {
    private static ExecutorService executorService;
    static Scanner scanner = new Scanner(System.in);
    private static BufferedReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket clientSocket = new Socket("192.168.52.1", 8080);
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("connected to server");
                    login();
                    String setString;
                    ArrayList<String> strings;
                    BufferedImage screenshot;
                    while (true) {
                        setString = in.readLine();
                        if (setString.equals("[]")) {
                            System.out.println("no devices connected,'refresh' pls...");
                            out.println(scanner.next());
                            continue;
                        }
                        strings = stringToList(setString);
                        System.out.println("pick a device to take a picture from:");
                        for (int i = 0; i < strings.size(); i++) {
                            System.out.println(i + " : " + strings.get(i));
                        }
                        int x = scanner.nextInt();
                        if (x >= strings.size()) {
                            System.out.println("invalid input try again:");
                            out.println("xxxx");
                            in.readLine();
                            continue;
                        }
                        out.println(strings.get(x));
                        if (in.readLine().equalsIgnoreCase("true")) {
                            System.out.println("device found , pls choose a function 'screenshot' or 'cameracapture':");
                            String answer = scanner.next();
                            if (answer.equalsIgnoreCase("screenshot")) {
                                out.println("true");

                                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                                int imageSize = dataInputStream.readInt();
                                // Allocate a byte array to store the image data
                                byte[] imageBytes = new byte[imageSize];
                                // Read the entire image data into the byte array
                                dataInputStream.readFully(imageBytes);
                                // Convert the byte array to a BufferedImage
                                screenshot = ImageIO.read(new ByteArrayInputStream(imageBytes));

                                String desktopPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "DSImages";
                                File desktop = new File(desktopPath);
                                if (!desktop.exists()) {
                                    desktop.mkdir(); // This will create the NewFolder on the desktop
                                }
                                File outputFile = new File(desktop, "screenshot" + strings.get(x) + LocalTime.now().toString().replace(":", "-") + ".png");
                                // Write the BufferedImage to the specified file as a PNG
                                ImageIO.write(screenshot, "png", outputFile);
                                Desktop desktop1 = Desktop.getDesktop();
                                desktop1.open(outputFile);
                                System.out.println("image saved successfully, write anything to continue:");
                                out.println(scanner.next());
                            } else if (answer.equalsIgnoreCase("cameracapture")) {
                                out.println("false");

                                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                                int imageSize = dataInputStream.readInt();
                                // Allocate a byte array to store the image data
                                byte[] imageBytes = new byte[imageSize];
                                // Read the entire image data into the byte array
                                dataInputStream.readFully(imageBytes);
                                // Convert the byte array to a BufferedImage
                                screenshot = ImageIO.read(new ByteArrayInputStream(imageBytes));
                                String desktopPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "DSImages";
                                File desktop = new File(desktopPath);
                                if (!desktop.exists()) {
                                    desktop.mkdir(); // This will create the NewFolder on the desktop
                                }
                                String filePath="cameracapture" + strings.get(x) + LocalTime.now().toString().replace(":", "-");
                                File outputFile = new File(desktop,filePath + ".png");
                                // Write the BufferedImage to the specified file as a PNG
                                ImageIO.write(screenshot, "png", outputFile);
                                Desktop desktop1 = Desktop.getDesktop();
                                desktop1.open(outputFile);
                                System.out.println("image saved successfully, write anything to continue:");
                                out.println(scanner.next());

                            } else out.println("error");
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void showScreenshot(BufferedImage screen) {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        if (screen == null)
            throw new RuntimeException();
        // Display the screenshot in a JFrame or process as needed
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel(new ImageIcon(screen)));
        frame.pack();
        frame.setVisible(true);
    }

    public static ArrayList<String> stringToList(String input) {
        String[] elements = input.replaceAll("[\\[\\]]", "").split(",\\s*");
        // Create an ArrayList and add the elements
        ArrayList<String> result = new ArrayList<>(Arrays.asList(elements));

        return result;
    }

    static Set stringToSet(String strings) {
        String[] elements = strings.toString().replaceAll("[\\[\\]]", "").split(",\\s*");

        // Create a set and add the parsed integers
        Set<String> set = new HashSet<>();
        for (String element : elements) {
            set.add(element);
        }
        return set;
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

    static void login() {
        try {
            System.out.println(in.readLine());
            out.println(scanner.next());
            System.out.println(in.readLine());
            out.println(scanner.next());
            System.out.println(in.readLine());
            out.println(scanner.next());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
