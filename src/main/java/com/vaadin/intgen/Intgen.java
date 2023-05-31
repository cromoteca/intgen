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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import javax.imageio.ImageIO;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

public class Intgen {

    private static final class WindowAdapterExtension extends WindowAdapter {
        private final JFrame frame;
        private final int imageId;
        private final CountDownLatch latch;

        private WindowAdapterExtension(JFrame frame, int imageId, CountDownLatch latch) {
            this.frame = frame;
            this.imageId = imageId;
            this.latch = latch;
        }

        @Override
        public void windowOpened(WindowEvent e) {
            super.windowOpened(e);

            // Take a screenshot
            saveScreenshot(imageId, frame);

            // Print component details
            printComponentDetails(frame, frame, imageId);
            frame.dispose();
            latch.countDown();
        }

        private void saveScreenshot(int imageId, JFrame frame) {
            try {
                BufferedImage capturedImage = takeScreenshot(frame);
                // Save as PNG
                String fileName = "screenshot" + imageId + ".png";
                File file = new File("target/output/" + fileName);
                ImageIO.write(capturedImage, "png", file);

                ObjectNode imageNode = images.addObject();
                imageNode.put("id", imageId);
                imageNode.put("width", capturedImage.getWidth());
                imageNode.put("height", capturedImage.getHeight());
                imageNode.put("file_name", fileName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static final Random RANDOM = new Random();
    public static final ComponentGenerator[] GENERATORS = new ComponentGenerator[] {
            new ButtonGenerator(),
            new TextFieldWithTopLabelGenerator()
    };

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Get the available look and feels
    private static final UIManager.LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();

    private static final ObjectNode rootNode = objectMapper.createObjectNode();
    private static final ArrayNode images = rootNode.putArray("images");
    private static final ArrayNode annotations = rootNode.putArray("annotations");
    private static final ArrayNode categories = rootNode.putArray("categories");
    private static final Map<String, Integer> categoryMap = new HashMap<>();

    static {
        for (int i = 0; i < GENERATORS.length; i++) {
            ComponentGenerator generator = GENERATORS[i];
            int categoryId = i + 1;
            categoryMap.put(generator.getCategory(), categoryId);
            ObjectNode categoryNode = categories.addObject();
            categoryNode.put("supercategory", "Boxes");
            categoryNode.put("id", categoryId);
            categoryNode.put("name", generator.getCategory());
        }
    }

    public static void main(String[] args) throws Exception {

        for (UIManager.LookAndFeelInfo lookAndFeel : lookAndFeels) {
            System.out.println(lookAndFeel.getName());
        }

        new File("target/output").mkdirs();

        for (int i = 0; i < 10; i++) {
            CountDownLatch latch = new CountDownLatch(1);
            int id = i + 1;

            SwingUtilities.invokeLater(() -> {
                try {
                    createAndShowGui(latch, id); // pass the latch and image id to the method
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            latch.await(); // wait for the latch to count down to 0 before continuing
        }

        objectMapper.writeValue(new File("target/output/_annotations.coco.json"), rootNode);
    }

    private static void createAndShowGui(CountDownLatch latch, int imageId) {
        SwingUtilities.invokeLater(() -> {
            try {

                // Choose a random look and feel
                String lookAndFeelClassName = lookAndFeels[RANDOM.nextInt(lookAndFeels.length)].getClassName();
                UIManager.setLookAndFeel(lookAndFeelClassName);

                // If the L&F is Metal, set a random theme
                if (lookAndFeelClassName.equals("javax.swing.plaf.metal.MetalLookAndFeel")) {
                    if (RANDOM.nextBoolean()) {
                        MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                    } else {
                        MetalLookAndFeel.setCurrentTheme(new OceanTheme());
                    }
                }

                // Create the frame
                JFrame frame = new JFrame("Random Swing App");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Add a WindowListener to wait for the windowOpened event
                frame.addWindowListener(new WindowAdapterExtension(frame, imageId, latch));

                addFields(frame);

                // Display the frame
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (HeadlessException | ClassNotFoundException | IllegalAccessException | InstantiationException
                    | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        });
    }

    private static void addFields(JFrame frame) {
        // Choose a random layout
        if (RANDOM.nextBoolean()) {
            frame.setLayout(new FlowLayout());
        } else {
            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        }

        // Add 2-5 random components
        int componentCount = 2 + RANDOM.nextInt(6);
        for (int i = 0; i < componentCount; i++) {
            ComponentGenerator generator = GENERATORS[RANDOM.nextInt(GENERATORS.length)];
            Component component = generator.generate();
            frame.add(component);
            component.setName(generator.getCategory());
        }
    }

    private static int idCounter = 0;

    private static void printComponentDetails(Component component, JFrame relativeTo, int imageId) {
        Point position = SwingUtilities.convertPoint(component, 0, 0, relativeTo);
        Dimension size = component.getSize();
        float area = size.width * size.height;

        ObjectNode componentDetails = annotations.addObject();
        componentDetails.put("image_id", imageId);
        componentDetails.put("id", idCounter++);
        ArrayNode bbox = componentDetails.putArray("bbox");
        bbox.add(position.x);
        bbox.add(position.y);
        bbox.add(size.width);
        bbox.add(size.height);
        componentDetails.put("area", area);
        componentDetails.put("category_id", categoryMap.get(component.getName()));
        System.out.println("NAME:"+component.getName());

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                if (child.getName() != null) {
                    printComponentDetails(child, relativeTo, imageId);
                }
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
                BufferedImage.TYPE_INT_RGB);
        // call the Component's paint method, using
        // the Graphics object of the image.
        component.paint(image.getGraphics()); // alternately use .printAll(..)
        return image;
    }
}
