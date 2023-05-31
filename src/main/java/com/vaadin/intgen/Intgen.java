package com.vaadin.intgen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import javax.imageio.ImageIO;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

public class Intgen {

    private static final Class<?>[] COMPONENTS = {JTextField.class, JButton.class};
    private static final String[] LABEL_TEXTS = {"First name", "Last name", "Email", "City", "Country"};
    private static final String[] BUTTON_TEXTS = {"Submit", "Cancel", "Reset", "Close", "Help"};

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Get the available look and feels
    private static final UIManager.LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo lookAndFeel : lookAndFeels) {
            System.out.println(lookAndFeel.getName());
        }

        new File("target/output").mkdirs();

        for (int i = 0; i < 3; i++) {
            CountDownLatch latch = new CountDownLatch(1);
            int id = i + 1;

            SwingUtilities.invokeLater(() -> {
                try {
                    createAndShowGui(latch, id);  // pass the latch and image id to the method
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            latch.await();  // wait for the latch to count down to 0 before continuing
        }
    }

    private static void createAndShowGui(CountDownLatch latch, int imageId) {
        SwingUtilities.invokeLater(() -> {
            try {
                Random random = new Random();

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
                            File file = new File("target/output/screenshot" + imageId + ".png");
                            ImageIO.write(capturedImage, "png", file);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        // Print component details
                        List<ObjectNode> componentDetailsList = new ArrayList<>();
                        printComponentDetails(componentDetailsList, frame, frame, imageId);
                        writeJsonToFile(componentDetailsList, "target/output/coordinates" + imageId + ".json");
                        frame.dispose();
                        latch.countDown();
                    }
                });

                // Choose a random layout
                if (random.nextBoolean()) {
                    frame.setLayout(new FlowLayout());
                } else {
                    frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
                }

                // Add 2-5 random components
                int componentCount = 2 + random.nextInt(6);
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
            } catch (HeadlessException | ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        });
    }

    private static int idCounter = 0;

    private static void printComponentDetails(List<ObjectNode> componentDetailsList, Component component, JFrame relativeTo, int imageId) {
        Point position = SwingUtilities.convertPoint(component, 0, 0, relativeTo);
        Dimension size = component.getSize();
        float area = size.width * size.height;

        ObjectNode componentDetails = objectMapper.createObjectNode();
        componentDetails.put("image_id", imageId);
        componentDetails.put("id", idCounter++);
        ArrayNode bbox = componentDetails.putArray("bbox");
        bbox.add(position.x);
        bbox.add(position.y);
        bbox.add(size.width);
        bbox.add(size.height);
        componentDetails.put("area", area);

        componentDetailsList.add(componentDetails);

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                printComponentDetails(componentDetailsList, child, relativeTo, imageId);
            }
        }
    }

    public static void writeJsonToFile(List<ObjectNode> componentDetailsList, String filePath) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), componentDetailsList);
        } catch (IOException e) {
            e.printStackTrace();
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
