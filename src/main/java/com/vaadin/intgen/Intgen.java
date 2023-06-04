package com.vaadin.intgen;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.vaadin.intgen.components.Button;
import com.vaadin.intgen.components.Checkbox;
import com.vaadin.intgen.components.ComboBox;
import com.vaadin.intgen.components.RadioButtons;
import com.vaadin.intgen.components.TextAreaWithTopLabel;
import com.vaadin.intgen.components.TextFieldWithLeftLabel;
import com.vaadin.intgen.components.TextFieldWithTopLabel;
import com.vaadin.intgen.layouts.HorizontalLayout;
import com.vaadin.intgen.layouts.TabLayout;
import com.vaadin.intgen.layouts.VerticalLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Intgen {

    public static void main(String[] args) throws Exception {
        for (var lookAndFeel : lookAndFeels) {
            System.out.println(lookAndFeel.getName());
        }

        for (var phase : Phase.values()) {
            phase.getImages().mkdirs();
            phase.getLabels().mkdirs();

            for (var i = 0; i < phase.getCount(); i++) {
                var latch = new CountDownLatch(1);
                var imageId = i;

                SwingUtilities.invokeLater(() -> {
                    createAndShowGui(phase, latch, imageId); // pass the latch and image id to the method
                });

                latch.await(); // wait for the latch to count down to 0 before continuing
            }
        }

        var names = ALL.stream()
                .map(ComponentGenerator::getCategory)
                .map(cat -> "'" + cat + "'")
                .collect(Collectors.joining(", "));

        var yaml = new File(DATASET, "data.yaml");
        try (var out = new PrintWriter(new BufferedWriter(new FileWriter(yaml, false)))) {
            out.println("train: ../train/images");
            out.println("val: ../valid/images");
            out.println("test: ../test/images");
            out.println();
            out.println("nc: " + ALL.size());
            out.println("names: [" + names + "]");
        }
    }
    static final Properties config = new Properties();

    static {
        try (var reader = new FileReader(new File(System.getProperty("user.dir"), "intgen.properties"))) {
            config.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static int intConfigParam(String key) {
        return Integer.parseInt(config.getProperty(key));
    }

    static boolean booleanConfigParam(String key) {
        return Boolean.parseBoolean(key);
    }

    enum Phase {
        TRAIN(intConfigParam("train")),
        VALID(intConfigParam("valid")),
        TEST(intConfigParam("test"));

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
    public static final int IMAGE_SIZE = intConfigParam("imageSize");
    public static final File DATASET = new File(config.getProperty("datasetLocation"));

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

            var contentPane = frame.getContentPane();
            var imageFile = new File(phase.getImages(), imageId + ".png");
            var labelFile = new File(phase.getLabels(), imageId + ".txt");

            try (var out = new PrintWriter(new BufferedWriter(new FileWriter(labelFile, false)))) {
                takeScreenshot(imageFile, contentPane);
                labelComponent(out, contentPane, contentPane, imageId);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            frame.dispose();
            latch.countDown();
        }

        private void takeScreenshot(File file, Component frame) throws IOException {
            var image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
            // call the Component's paint method, using
            // the Graphics object l the image.
            frame.paint(image.getGraphics());
            var capturedImage = image;

            if (booleanConfigParam("resize")) {
                capturedImage = resizeImage(capturedImage, IMAGE_SIZE, IMAGE_SIZE);
            }

            // Save as PNG
            ImageIO.write(capturedImage, "png", file);
        }

        public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
            var resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            var graphics2D = resizedImage.createGraphics();
            graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
            graphics2D.dispose();
            return resizedImage;
        }
    }

    public static final Random RANDOM = new Random(intConfigParam("seed"));
    public static final List<LayoutGenerator> LAYOUTS = List.of(
            new HorizontalLayout(),
            new VerticalLayout(),
            new TabLayout()
    );
    public static final List<ComponentGenerator> COMPONENTS = List.of(
            new Button(),
            new TextFieldWithTopLabel(),
            new TextFieldWithLeftLabel(),
            new Checkbox(),
            new ComboBox(),
            new TextAreaWithTopLabel(),
            new RadioButtons()
    );
    public static final List<ComponentGenerator> OTHER_COMPONENTS = List.of(
            new TabLayout.Tab(),
            new TabLayout.ActiveTab()
    );
    public static final List<ComponentGenerator> ALL
            = Stream.concat(LAYOUTS.stream(), Stream.concat(COMPONENTS.stream(), OTHER_COMPONENTS.stream())).toList();

    public static <T extends ComponentGenerator> Container addChild(Container layout, List<T> generators) {
        T generator = null;

        while (generator == null) {
            var candidate = generators.get(RANDOM.nextInt(generators.size()));

            if (!candidate.forbid(layout.getName())) {
                generator = candidate;
            }
        }

        return generator.add(layout);
    }

    private static final UIManager.LookAndFeelInfo[] lookAndFeels;
    private static final Map<String, Integer> categoryMap = new HashMap<>();
    private static final List<String> WORDS;

    static {
        Stream.of(FlatDarculaLaf.class, FlatDarkLaf.class, FlatIntelliJLaf.class, FlatLightLaf.class, FlatMacLightLaf.class, FlatMacDarkLaf.class).forEach(laf -> {
            UIManager.installLookAndFeel(laf.getSimpleName(), laf.getName());
        });
        lookAndFeels = UIManager.getInstalledLookAndFeels();
        for (var i = 0; i < ALL.size(); i++) {
            categoryMap.put(ALL.get(i).getCategory(), i);
        }

        try {
            var uri = Path.of(Intgen.class.getResource("/wordlist.txt").toURI());
            WORDS = Files.readAllLines(uri);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String words(int min, int max) {
        return IntStream.rangeClosed(min, min + RANDOM.nextInt(max - min + 1))
                .mapToObj(n -> WORDS.get(RANDOM.nextInt(WORDS.size())))
                .collect(Collectors.joining(" "));
    }

    private static final String[] emojis = {
        "\u2705", // Check Mark
        "\u274C", // Cross Mark
        "\u26A0", // Warning
        "\u2757", // Exclamation Mark
        "\uD83D\uDEAB", // No Entry
        "\uD83D\uDE4F", // Clapping Hands
        "\uD83D\uDC4D", // Thumbs Up
        "\uD83D\uDC4E", // Thumbs Down
        "\uD83D\uDCD1", // File Folder
        "\uD83D\uDCE6", // Inbox Tray
        "\uD83D\uDCE8", // Outbox Tray
        "\uD83D\uDCDD", // Notebook
    };

    public static String emoji() {
        return emojis[RANDOM.nextInt(emojis.length)];
    }

    private static void createAndShowGui(Phase phase, CountDownLatch latch, int imageId) {
        try {
            if (RANDOM.nextBoolean()) {
                var lookAndFeelClassName = lookAndFeels[RANDOM.nextInt(lookAndFeels.length)].getClassName();
                UIManager.setLookAndFeel(lookAndFeelClassName);
            } else {
                IntelliJTheme.setup(Intgen.class.getResourceAsStream("/GitHub.theme.json"));
            }

            var frame = new JFrame("Random Swing App");

            var layouts = new ArrayList<Container>();
            var contentPane = frame.getContentPane();
            layouts.add(contentPane);
            frame.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

            var layoutCount = RANDOM.nextInt(1, intConfigParam("maxLayouts"));
            for (var i = 0; i < layoutCount; i++) {
                var parent = layouts.get(RANDOM.nextInt(layouts.size()));
                Container layout = Intgen.addChild(parent, LAYOUTS);
                layouts.add(layout);
            }

            for (Container layout : layouts) {
                String name = layout.getName();

                if (name != null && categoryMap.containsKey(name)) {
                    var count = RANDOM.nextInt(2, intConfigParam("maxChildren" + name));

                    while (layout.getComponentCount() < count) {
                        addChild(layout, COMPONENTS);
                    }
                }
            }

            frame.pack();
            frame.setLocationRelativeTo(null);

            var bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            if (frame.getSize().width > bounds.width || frame.getSize().height > bounds.height) {
                // frame is too big, create another one
                frame.dispose();
                createAndShowGui(phase, latch, imageId);
            } else {
                // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.addWindowListener(new WindowAdapterExtension(phase, frame, imageId, latch));
                frame.setVisible(true);
            }
        } catch (HeadlessException | ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    private static void labelComponent(PrintWriter out, Component component, Component relativeTo, int imageId) {
        var categoryId = categoryMap.get(component.getName());

        if (categoryId != null) {
            var bounds = getRelativeBounds(component, relativeTo);
            // normalize coordinates and size to be between 0 and 1
            var x = bounds.getX() / relativeTo.getWidth();
            var y = bounds.getY() / relativeTo.getHeight();
            var w = bounds.getWidth() / relativeTo.getWidth();
            var h = bounds.getHeight() / relativeTo.getHeight();
            out.println(categoryId + " " + x + " " + y + " " + w + " " + h);
        }

        if (component instanceof Container container) {
            for (var child : container.getComponents()) {
                labelComponent(out, child, relativeTo, imageId);
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

}
