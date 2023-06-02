package com.vaadin.intgen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.vaadin.intgen.components.Button;
import com.vaadin.intgen.components.Checkbox;
import com.vaadin.intgen.components.ComboBox;
import com.vaadin.intgen.components.TextAreaWithTopLabel;
import com.vaadin.intgen.components.TextFieldWithLeftLabel;
import com.vaadin.intgen.components.TextFieldWithTopLabel;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

public class Intgen {

    enum Phase {
        TRAIN(30), VALID(2), TEST(2);

        private final int count;

        Phase(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public File getImages() {
            return new File(DATASET, name().toLowerCase() + "/images");
        }

        public File getLabels() {
            return new File(DATASET, name().toLowerCase() + "/labels");
        }
    }
    public static final int IMAGE_SIZE = 640;
    public static final File DATASET = new File("target/dataset");

    public static void main(String[] args) throws Exception {

        for (var lookAndFeel : lookAndFeels) {
            System.out.println(lookAndFeel.getName());
        }
        for (var phase : Phase.values()) {
            phase.getImages().mkdirs();
            phase.getLabels().mkdirs();

            for (var i = 0; i < phase.getCount(); i++) {
                makeOne(phase, i);
            }
        }
        var names = Arrays.stream(GENERATORS)
                .map(ComponentGenerator::getCategory)
                .map(cat -> "'" + cat + "'")
                .collect(Collectors.joining(", "));
        var yaml = new File(DATASET, "data.yaml");
        try (var out = new PrintWriter(new BufferedWriter(new FileWriter(yaml, false)))) {
            out.println("train: ../train/images");
            out.println("val: ../valid/images");
            out.println("test: ../test/images");
            out.println();
            out.println("nc: " + GENERATORS.length);
            out.println("names: [" + names + "]");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        objectMapper.writeValue(new File(DATASET, "_annotations.coco.json"), rootNode);
    }

    private static void makeOne(Phase phase, int i) throws InterruptedException {
        var latch = new CountDownLatch(1);

        SwingUtilities.invokeLater(() -> {
            try {
                createAndShowGui(phase, latch, i); // pass the latch and image id to the method
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        latch.await(); // wait for the latch to count down to 0 before continuing
    }

    private static final class WindowAdapterExtension extends WindowAdapter {

        private final Phase phase;
        private final JFrame frame;
        private final int imageId;
        private final CountDownLatch latch;

        private WindowAdapterExtension(Phase phase, JFrame frame, int imageId, CountDownLatch latch) {
            this.phase = phase;
            this.frame = frame;
            this.imageId = imageId;
            this.latch = latch;
        }

        @Override
        public void windowOpened(WindowEvent e) {
            super.windowOpened(e);

            // Take a screenshot
            var image = new File(phase.getImages(), imageId + ".png");
            var contentPane = frame.getContentPane();
            saveScreenshot(image, imageId, contentPane);

            // Print component details
            var labels = new File(phase.getLabels(), imageId + ".txt");
            try (var out = new PrintWriter(new BufferedWriter(new FileWriter(labels, false)))) {
                printComponentDetails(out, contentPane, contentPane, imageId);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            frame.dispose();
            latch.countDown();
        }

        private void saveScreenshot(File file, int imageId, Component frame) {
            try {
                var capturedImage = takeScreenshot(frame);
                var resizedImage = resizeImage(capturedImage, IMAGE_SIZE, IMAGE_SIZE);
                // Save as PNG
                var fileName = "screenshot" + imageId + ".png";
                ImageIO.write(resizedImage, "png", file);

                var imageNode = images.addObject();
                imageNode.put("id", imageId);
                imageNode.put("width", capturedImage.getWidth());
                imageNode.put("height", capturedImage.getHeight());
                imageNode.put("file_name", fileName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public static BufferedImage takeScreenshot(Component component) {
            var image = new BufferedImage(
                    component.getWidth(),
                    component.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            // call the Component's paint method, using
            // the Graphics object of the image.
            component.paint(image.getGraphics());
            return image;
        }

        public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
            var resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            var graphics2D = resizedImage.createGraphics();
            graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
            graphics2D.dispose();
            return resizedImage;
        }
    }

    public static final Random RANDOM = new Random(13579);
    public static final ComponentGenerator[] GENERATORS = new ComponentGenerator[]{
        new Button(),
        new TextFieldWithTopLabel(),
        new TextFieldWithLeftLabel(),
        new Checkbox(),
        new ComboBox(),
        new TextAreaWithTopLabel()
    };

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Get the available look and feels
    private static final UIManager.LookAndFeelInfo[] lookAndFeels;

    private static final ObjectNode rootNode = objectMapper.createObjectNode();
    private static final ArrayNode images = rootNode.putArray("images");
    private static final ArrayNode annotations = rootNode.putArray("annotations");
    private static final ArrayNode categories = rootNode.putArray("categories");
    private static final Map<String, Integer> categoryMap = new HashMap<>();

    static {
        Stream.of(FlatDarculaLaf.class, FlatDarkLaf.class, FlatIntelliJLaf.class, FlatLightLaf.class, FlatMacLightLaf.class, FlatMacDarkLaf.class).forEach(laf -> {
            UIManager.installLookAndFeel(laf.getSimpleName(), laf.getName());
        });
        lookAndFeels = UIManager.getInstalledLookAndFeels();
        for (var i = 0; i < GENERATORS.length; i++) {
            var generator = GENERATORS[i];
            categoryMap.put(generator.getCategory(), i);
            var categoryNode = categories.addObject();
            categoryNode.put("supercategory", "Boxes");
            categoryNode.put("id", i);
            categoryNode.put("name", generator.getCategory());
        }
    }

    private static void createAndShowGui(Phase phase, CountDownLatch latch, int imageId) {
        SwingUtilities.invokeLater(() -> {
            try {

                // Choose a random look and feel
                var lookAndFeelClassName = lookAndFeels[RANDOM.nextInt(lookAndFeels.length)].getClassName();
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
                var frame = new JFrame("Random Swing App");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Add a WindowListener to wait for the windowOpened event
                frame.addWindowListener(new WindowAdapterExtension(phase, frame, imageId, latch));

                addFields(frame);

                // Display the frame
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception e) {
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
        var componentCount = 2 + RANDOM.nextInt(7);
        for (var i = 0; i < componentCount; i++) {
            var generator = GENERATORS[RANDOM.nextInt(GENERATORS.length)];
            var component = generator.generate();
            component.setName(generator.getCategory());
            frame.add(component);
        }
    }

    private static int idCounter = 0;

    private static void printComponentDetails(PrintWriter out, Component component, Component relativeTo, int imageId) {
        var categoryId = categoryMap.get(component.getName());

        if (categoryId != null) {
            var position = SwingUtilities.convertPoint(component, 0, 0, relativeTo);
            var size = component.getSize();
            float area = size.width * size.height;

            var componentDetails = annotations.addObject();
            componentDetails.put("image_id", imageId);
            componentDetails.put("id", idCounter++);
            var bbox = componentDetails.putArray("bbox");
            bbox.add(position.x);
            bbox.add(position.y);
            bbox.add(size.width);
            bbox.add(size.height);
            componentDetails.put("area", area);
            componentDetails.put("category_id", categoryId);

            addLine(component, relativeTo, categoryId, out);
        }

        if (component instanceof Container container) {
            for (var child : container.getComponents()) {
                printComponentDetails(out, child, relativeTo, imageId);
            }
        }
    }

    public static Rectangle getRelativeBounds(Component inner, Component outer) {
        var innerLocation = inner.getLocationOnScreen();
        var outerLocation = outer.getLocationOnScreen();
        var innerBounds = inner.getBounds();
        // calculate relative location
        var relativeX = innerLocation.x - outerLocation.x + innerBounds.width / 2;
        var relativeY = innerLocation.y - outerLocation.y + innerBounds.height / 2;

        return new Rectangle(relativeX, relativeY, innerBounds.width, innerBounds.height);
    }

    public static void addLine(Component inner, Component outer, int categoryId, PrintWriter out) {
        var bounds = getRelativeBounds(inner, outer);
        // normalize coordinates and size to be between 0 and 1
        var x = bounds.getX() / outer.getWidth();
        var y = bounds.getY() / outer.getHeight();
        var w = bounds.getWidth() / outer.getWidth();
        var h = bounds.getHeight() / outer.getHeight();

        out.println(categoryId + " " + x + " " + y + " " + w + " " + h);
    }

    public static void writeJsonToFile(List<ObjectNode> componentDetailsList, String filePath) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), componentDetailsList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
