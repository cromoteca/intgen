package com.vaadin.intgen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.imgscalr.Scalr;

public class DataSet {
  public enum Split {
    TRAIN(Configuration.intParam("train")),
    VALID(Configuration.intParam("valid")),
    TEST(Configuration.intParam("test"));

    private final int count;

    Split(int count) {
      this.count = count;
    }

    public int getCount() {
      return count;
    }

    public File getImages() {
      return new File(Configuration.dataSet(), name().toLowerCase() + "/images");
    }

    public File getLabels() {
      return new File(Configuration.dataSet(), name().toLowerCase() + "/labels");
    }
  }

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private final Split split;

  private final LookAndFeels lookAndFeels = new LookAndFeels();

  private final ObjectNode rootNode = objectMapper.createObjectNode();
  private final ArrayNode images = rootNode.putArray("images");
  private final ArrayNode annotations = rootNode.putArray("annotations");

  private int idCounter = 0;

  public DataSet(Split split) {
    this.split = split;

    var categories = rootNode.putArray("categories");
    var index = 0;
    for (var category : Generators.ALL) {
      var categoryNode = categories.addObject();
      categoryNode.put("supercategory", "Boxes");
      categoryNode.put("id", index++);
      categoryNode.put("name", category);
    }
  }

  public void create() {
    split.getImages().mkdirs();
    split.getLabels().mkdirs();

    for (var i = 0; i < split.getCount(); i++) {
      var latch = new CountDownLatch(1);
      var imageId = i;

      SwingUtilities.invokeLater(
          () -> {
            createAndShowGui(latch, imageId); // pass the latch and image id to the method
          });

      try {
        latch.await(); // wait for the latch to count down to 0 before continuing
      } catch (InterruptedException e) {
        // propagate interruption
        Thread.currentThread().interrupt();
      }

      try {
        objectMapper.writeValue(
            new File(Configuration.dataSet(), "_annotations.coco.json"), rootNode);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    writeDataFile();
  }

  private void writeDataFile() {
    var names =
        Generators.ALL.stream().map(cat -> "'" + cat + "'").collect(Collectors.joining(", "));

    var yaml = new File(Configuration.dataSet(), "data.yaml");
    try (var out = new PrintWriter(new BufferedWriter(new FileWriter(yaml, false)))) {
      out.println("train: ../train/images");
      out.println("val: ../valid/images");
      out.println("test: ../test/images");
      out.println();
      out.println("nc: " + Generators.ALL.size());
      out.println("names: [" + names + "]");
      out.println();

      var i = 0;

      for (var cat : Generators.ALL) {
        out.print("# ");
        out.print(i++);
        out.print('\t');
        out.println(cat);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void createAndShowGui(CountDownLatch latch, int imageId) {
    lookAndFeels.pickOne();

    var frame = new JFrame("Random Swing App");

    var layouts = new ArrayList<Container>();
    var contentPane = frame.getContentPane();
    layouts.add(contentPane);
    frame.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

    var layoutCount = Randoms.nextInt(1, Configuration.intParam("maxLayouts"));
    for (var i = 0; i < layoutCount; i++) {
      var parent = Randoms.pickOne(layouts);
      Container layout = addChild(parent, Generators.LAYOUTS);
      layouts.add(layout);
    }

    for (Container layout : layouts) {
      String name = layout.getName();

      if (name != null && Generators.CATEGORIES.containsKey(name)) {
        var count = Randoms.nextInt(2, Configuration.intParam("maxChildren" + name));

        while (layout.getComponentCount() < count) {
          addChild(layout, Generators.COMPONENTS);
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
      createAndShowGui(latch, imageId);
    } else {
      // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.addWindowListener(new WindowAdapterExtension(frame, imageId, latch));
      frame.setVisible(true);
    }
  }

  private <T extends ComponentGenerator<?>> Container addChild(
      Container layout, List<T> generators) {
    T generator = null;

    while (generator == null) {
      var candidate = Randoms.pickOne(generators);

      if (!candidate.forbid(layout.getName())) {
        generator = candidate;
      }
    }

    return generator.add(layout).addedContainer();
  }

  private final class WindowAdapterExtension extends WindowAdapter {

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

      var contentPane = frame.getContentPane();
      var imageFile = new File(split.getImages(), imageId + ".png");
      var labelFile = new File(split.getLabels(), imageId + ".txt");

      try (var out = new PrintWriter(new BufferedWriter(new FileWriter(labelFile, false)))) {
        takeScreenshot(imageFile, contentPane);
        labelComponent(out, contentPane, contentPane);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }

      frame.dispose();
      latch.countDown();
    }

    private void labelComponent(PrintWriter out, Component component, Component relativeTo) {
      var categoryId = Generators.CATEGORIES.get(component.getName());

      if (categoryId != null) {
        var position = SwingUtilities.convertPoint(component, 0, 0, relativeTo);
        var size = component.getSize();
        float area = size.width * size.height;

        var componentDetails = annotations.addObject();
        componentDetails.put("image_id", 123);
        componentDetails.put("id", idCounter++);
        var bbox = componentDetails.putArray("bbox");
        bbox.add(position.x);
        bbox.add(position.y);
        bbox.add(size.width);
        bbox.add(size.height);
        componentDetails.put("area", area);
        componentDetails.put("category_id", categoryId);

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

    private Rectangle getRelativeBounds(Component inner, Component outer) {
      var innerLocation = inner.getLocationOnScreen();
      var outerLocation = outer.getLocationOnScreen();
      var innerBounds = inner.getBounds();
      // calculate relative location
      var relativeX = innerLocation.x - outerLocation.x + innerBounds.width / 2;
      var relativeY = innerLocation.y - outerLocation.y + innerBounds.height / 2;

      return new Rectangle(relativeX, relativeY, innerBounds.width, innerBounds.height);
    }

    private void takeScreenshot(File file, Component frame) throws IOException {
      var image =
          new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
      frame.paint(image.getGraphics());
      var capturedImage = image;

      if (Configuration.booleanParam("resize")) {
        var imageSize = Configuration.intParam("imageSize");
        capturedImage =
            Scalr.resize(
                capturedImage, Scalr.Mode.FIT_EXACT, imageSize, imageSize, Scalr.OP_ANTIALIAS);
      }

      ImageIO.write(capturedImage, "png", file);

      var imageNode = images.addObject();
      imageNode.put("id", imageId);
      imageNode.put("width", capturedImage.getWidth());
      imageNode.put("height", capturedImage.getHeight());
      imageNode.put("file_name", file.getName());
    }
  }
}
