package com.vaadin.intgen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Randoms {
  private static final Random random = new Random(Configuration.intParam("seed"));
  private static final List<String> words;

  static {
    try {
      var uri = Path.of(Intgen.class.getResource("/wordlist.txt").toURI());
      words = Files.readAllLines(uri);
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T pickOne(T[] array) {
    return array[random.nextInt(array.length)];
  }

  public static <T> T pickOne(List<T> list) {
    return list.get(random.nextInt(list.size()));
  }

  public static int nextInt(int bound) {
    return random.nextInt(bound);
  }

  public static int nextInt(int origin, int bound) {
    return random.nextInt(origin, bound);
  }

  public static boolean nextBoolean() {
    return random.nextBoolean();
  }

  public static double nextDouble() {
    return random.nextDouble();
  }

  public static String words(int min, int max) {
    return IntStream.rangeClosed(1, min + random.nextInt(max - min + 1))
        .mapToObj(n -> pickOne(words))
        .collect(Collectors.joining(" "));
  }

  public static String wordLines(int min, int max, int maxWordsPerLine) {
    return IntStream.rangeClosed(1, min + random.nextInt(max - min + 1))
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
      var bufferedImage = ImageIO.read(Randoms.class.getResource("/images/" + pickOne(icons)));
      return new ImageIcon(bufferedImage);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
