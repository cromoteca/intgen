package com.vaadin.intgen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

public class Intgen {

    private static final Class<?>[] COMPONENTS = {JTextField.class, JButton.class};
    private static final String[] LABEL_TEXTS = {"First name", "Last name", "Email", "City", "Country"};
    private static final String[] BUTTON_TEXTS = {"Submit", "Cancel", "Reset", "Close", "Help"};

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            try {
                Random random = new Random();

                // Get the available look and feels
                UIManager.LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();

                for (UIManager.LookAndFeelInfo lookAndFeel : lookAndFeels) {
                    System.out.println(lookAndFeel.getName());
                }

                // Choose a random look and feel
                String lookAndFeelClassName = lookAndFeels[random.nextInt(lookAndFeels.length)].getClassName();
                UIManager.setLookAndFeel(lookAndFeelClassName);

                // If the L&F is Metal, set a random theme
                if (lookAndFeelClassName.equals("javax.swing.plaf.metal.MetalLookAndFeel")) {
                    if (random.nextBoolean()) {
                        MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                    } else {
                        MetalLookAndFeel.setCurrentTheme(new OceanTheme());
                    }
                }

                // Create the frame
                JFrame frame = new JFrame("Random Swing App");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Add a WindowListener to wait for the windowOpened event
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent e) {
                        super.windowOpened(e);

                        // Take a screenshot
                        try {
                            BufferedImage capturedImage = takeScreenshot(frame);
                            // Save as PNG
                            File file = new File("target/screenshot.png");
                            ImageIO.write(capturedImage, "png", file);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        // Print component details
                        printComponentDetails(frame, frame, 1);  // Assuming the image id is 1
                    }
                });

                // Choose a random layout
                if (random.nextBoolean()) {
                    frame.setLayout(new FlowLayout());
                } else {
                    frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
                }

                // Add 2-5 random components
                int componentCount = 2 + random.nextInt(4);
                for (int i = 0; i < componentCount; i++) {
                    Class<?> componentClass = COMPONENTS[random.nextInt(COMPONENTS.length)];
                    if (componentClass == JTextField.class) {
                        String label = LABEL_TEXTS[random.nextInt(LABEL_TEXTS.length)];
                        frame.add(new JLabel(label + ":"));
                        frame.add(new JTextField(15));
                    } else if (componentClass == JButton.class) {
                        String buttonText = BUTTON_TEXTS[random.nextInt(BUTTON_TEXTS.length)];
                        frame.add(new JButton(buttonText));
                    }
                }

                // Display the frame
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static int idCounter = 0;

    private static void printComponentDetails(Component component, JFrame relativeTo, int imageId) {
        Point position = SwingUtilities.convertPoint(component, 0, 0, relativeTo);
        Dimension size = component.getSize();
        float area = size.width * size.height;

        System.out.println("{");
        System.out.println("  \"image_id\": " + imageId + ",");
        System.out.println("  \"id\": " + idCounter++ + ",");
        System.out.println("  \"bbox\": [" + position.x + "," + position.y + "," + size.width + "," + size.height + "],");
        System.out.println("  \"area\": " + area);
        System.out.println("}");

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                printComponentDetails(child, relativeTo, imageId);
            }
        }
    }

    public static BufferedImage takeScreenshot(Component component) {
        BufferedImage image = new BufferedImage(
                component.getWidth(),
                component.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        // call the Component's paint method, using
        // the Graphics object of the image.
        component.paint(image.getGraphics()); // alternately use .printAll(..)
        return image;
    }
}
