package Client;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class ImageViewer implements Runnable{
    private BufferedImage image;

    public ImageViewer(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void run() {

        ImageIcon imageIcon = new ImageIcon(image);
        JLabel jLabel = new JLabel(imageIcon);
        JPanel jPanel = new JPanel();
        jPanel.add(jLabel);
        JFrame frame = new JFrame();
        frame.add(jPanel);
        frame.pack(); // Adjusts the frame size to fit the image
        frame.setVisible(true);

    }
}
