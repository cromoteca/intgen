package com.vaadin.intgen;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.vaadin.intgen.components.Button;
import com.vaadin.intgen.components.ButtonBar;
import com.vaadin.intgen.components.Checkbox;
import com.vaadin.intgen.components.ComboBox;
import com.vaadin.intgen.components.ComboBoxGroupHorizontal;
import com.vaadin.intgen.components.ComboBoxWithLeftLabel;
import com.vaadin.intgen.components.ComboBoxWithTopLabel;
import com.vaadin.intgen.components.FieldGroup;
import com.vaadin.intgen.components.Form;
import com.vaadin.intgen.components.FreeText;
import com.vaadin.intgen.components.Grid;
import com.vaadin.intgen.components.Label;
import com.vaadin.intgen.components.RadioButton;
import com.vaadin.intgen.components.RadioButtonGroupHorizontal;
import com.vaadin.intgen.components.RadioButtonGroupHorizontalWithLeftLabel;
import com.vaadin.intgen.components.RadioButtonGroupVertical;
import com.vaadin.intgen.components.TextArea;
import com.vaadin.intgen.components.TextAreaWithLeftLabel;
import com.vaadin.intgen.components.TextAreaWithTopLabel;
import com.vaadin.intgen.components.TextField;
import com.vaadin.intgen.components.TextFieldWithLeftLabel;
import com.vaadin.intgen.components.TextFieldWithTopLabel;
import com.vaadin.intgen.layouts.LayoutHorizontal;
import com.vaadin.intgen.layouts.LayoutTabs;
import com.vaadin.intgen.layouts.LayoutVertical;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.imgscalr.Scalr;

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

        SwingUtilities.invokeLater(
            () -> {
              createAndShowGui(phase, latch, imageId); // pass the latch and image id to the method
            });

        latch.await(); // wait for the latch to count down to 0 before continuing
      }
    }

    var names = ALL.stream().map(cat -> "'" + cat + "'").collect(Collectors.joining(", "));

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

  public static final Properties CONFIG = new Properties();

  static {
    try (var reader =
        new FileReader(new File(System.getProperty("user.dir"), "intgen.properties"))) {
      CONFIG.load(reader);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static int intConfigParam(String key) {
    return Integer.parseInt(CONFIG.getProperty(key));
  }

  public static boolean booleanConfigParam(String key) {
    return Boolean.parseBoolean(CONFIG.getProperty(key));
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

  public static final File DATASET = new File(CONFIG.getProperty("datasetLocation"));

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
        labelComponent(out, contentPane, contentPane);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }

      frame.dispose();
      latch.countDown();
    }

    private void takeScreenshot(File file, Component frame) throws IOException {
      var image =
          new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
      // call the Component's paint method, using
      // the Graphics object l the image.
      frame.paint(image.getGraphics());
      var capturedImage = image;

      if (booleanConfigParam("resize")) {
        var imageSize = intConfigParam("imageSize");
        capturedImage =
            Scalr.resize(
                capturedImage, Scalr.Mode.FIT_EXACT, imageSize, imageSize, Scalr.OP_ANTIALIAS);
      }

      // Save as PNG
      ImageIO.write(capturedImage, "png", file);
    }
  }

  public static final Random RANDOM = new Random(intConfigParam("seed"));
  public static final List<LayoutGenerator> LAYOUTS =
      List.of(
          // new LayoutGrid(),
          new LayoutHorizontal(),
          new LayoutHorizontal(),
          new LayoutHorizontal(),
          new LayoutTabs(),
          new LayoutVertical(),
          new LayoutVertical(),
          new LayoutVertical(),
          new LayoutVertical());
  public static final List<ComponentGenerator<?>> COMPONENTS =
      List.of(
          new Button(),
          new ButtonBar(),
          new Checkbox(),
          new ComboBox(),
          new ComboBoxGroupHorizontal(),
          new ComboBoxWithLeftLabel(),
          new ComboBoxWithTopLabel(),
          new FieldGroup(),
          new Form(),
          new FreeText(),
          new Grid(),
          new RadioButtonGroupHorizontal(),
          new RadioButtonGroupHorizontalWithLeftLabel(),
          new RadioButtonGroupVertical(),
          new TextAreaWithLeftLabel(),
          new TextAreaWithTopLabel(),
          new TextField(50, 500),
          new TextFieldWithLeftLabel(),
          new TextFieldWithTopLabel());
  public static final List<ComponentGenerator<?>> OTHER_COMPONENTS =
      List.of(
          new Button.DefaultButton(),
          new Label(),
          new LayoutTabs.TabActive(),
          new LayoutTabs.Tab(),
          new RadioButton(),
          new TextArea());
  public static final SortedSet<String> ALL =
      Stream.concat(LAYOUTS.stream(), Stream.concat(COMPONENTS.stream(), OTHER_COMPONENTS.stream()))
          .map(ComponentGenerator::getCategory)
          .filter(Objects::nonNull)
          .collect(Collectors.toCollection(TreeSet::new));

  public static <T extends ComponentGenerator<?>> Container addChild(
      Container layout, List<T> generators) {
    T generator = null;

    while (generator == null) {
      var candidate = pickOne(generators);

      if (!candidate.forbid(layout.getName())) {
        generator = candidate;
      }
    }

    return generator.add(layout).addedContainer();
  }

  private static final Map<String, Integer> categoryMap = new HashMap<>();
  private static final List<String> WORDS;

  private static final UIManager.LookAndFeelInfo[] lookAndFeels;

  static {
    Stream.of(
            // FlatDarculaLaf.class,
            // FlatDarkLaf.class,
            FlatIntelliJLaf.class, FlatLightLaf.class, FlatMacLightLaf.class
            // FlatMacDarkLaf.class,
            // Vaadin.class
            )
        .forEach(
            laf -> {
              UIManager.installLookAndFeel(laf.getSimpleName(), laf.getName());
            });
    lookAndFeels =
        Arrays.stream(UIManager.getInstalledLookAndFeels())
            .filter(laf -> !"CDE/Motif".equals(laf.getName()))
            .toArray(UIManager.LookAndFeelInfo[]::new);

    var index = 0;
    for (var category : ALL) {
      categoryMap.put(category, index++);
    }

    try {
      var uri = Path.of(Intgen.class.getResource("/wordlist.txt").toURI());
      WORDS = Files.readAllLines(uri);
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private static final String[] flatThemes =
      new String[] {
        /* "Arc Dark", "Cobalt_2", */
        "GitHub"
      };

  public static <T> T pickOne(T[] array) {
    return array[RANDOM.nextInt(array.length)];
  }

  public static <T> T pickOne(List<T> list) {
    return list.get(RANDOM.nextInt(list.size()));
  }

  public static String words(int min, int max) {
    return IntStream.rangeClosed(1, min + RANDOM.nextInt(max - min + 1))
        .mapToObj(n -> pickOne(WORDS))
        .collect(Collectors.joining(" "));
  }

  public static String wordLines(int min, int max, int maxWordsPerLine) {
    return IntStream.rangeClosed(1, min + RANDOM.nextInt(max - min + 1))
        .mapToObj(n -> words(1, maxWordsPerLine))
        .collect(Collectors.joining("\n"));
  }

  private static final String[] icons = {
    "crm_mail.gif",
    "g_cancel.gif",
    "g_delete.gif",
    "g_empty.gif",
    "g_events.gif",
    "g_export.gif",
    "g_help.gif",
    "g_import.gif",
    "g_lamp.gif",
    "g_left.gif",
    "g_new.gif",
    "g_no.gif",
    "g_open.gif",
    "g_place.gif",
    "g_preview.gif",
    "g_right.gif",
    "g_save.gif",
    "g_search.gif",
    "g_undo.gif",
    "g_yes.gif",
  };

  public static ImageIcon icon() {
    try {
      var bufferedImage = ImageIO.read(Intgen.class.getResource("/images/" + pickOne(icons)));
      return new ImageIcon(bufferedImage);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void createAndShowGui(Phase phase, CountDownLatch latch, int imageId) {
    try {
      var lafNumber = RANDOM.nextInt(lookAndFeels.length + flatThemes.length);
      if (lafNumber < lookAndFeels.length) {
        var lookAndFeelClassName = lookAndFeels[lafNumber].getClassName();
        UIManager.setLookAndFeel(lookAndFeelClassName);
      } else {
        var themeName = flatThemes[lafNumber - lookAndFeels.length];
        IntelliJTheme.setup(
            Intgen.class.getResourceAsStream("/themes/" + themeName + ".theme.json"));
      }

      var frame = new JFrame("Random Swing App");

      var layouts = new ArrayList<Container>();
      var contentPane = frame.getContentPane();
      layouts.add(contentPane);
      frame.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

      var layoutCount = RANDOM.nextInt(1, intConfigParam("maxLayouts"));
      for (var i = 0; i < layoutCount; i++) {
        var parent = pickOne(layouts);
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

          IntStream.range(0, layout.getComponentCount())
              .forEach(
                  i -> {
                    var component = layout.getComponent(i);
                    if (component instanceof JButton button) {
                      frame.getRootPane().setDefaultButton(button);
                    }
                  });

          Optional.ofNullable(frame.getRootPane().getDefaultButton())
              .ifPresent(b -> b.setName("DefaultButton"));
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
    } catch (HeadlessException
        | ClassNotFoundException
        | IllegalAccessException
        | InstantiationException
        | UnsupportedLookAndFeelException e) {
      throw new RuntimeException(e);
    }
  }

  private static void labelComponent(PrintWriter out, Component component, Component relativeTo) {
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
        labelComponent(out, child, relativeTo);
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
