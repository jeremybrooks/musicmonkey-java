package net.jeremybrooks.musicmonkey;

import net.jeremybrooks.musicmonkey.gui.MainWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

public class Main {
  private static final Logger logger = LogManager.getLogger();

  public static void main(String... args) {
    try {
      GraphicsEnvironment ge =
          GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(Font.createFont(
          Font.TRUETYPE_FONT,
          Main.class.getResourceAsStream(MMConstants.FONT_RESOURCE_PATH + MMConstants.FONT_NAME_DISPLAY)));
    } catch (IOException | FontFormatException e) {
      logger.error("Could not find font {} at path {}", MMConstants.FONT_NAME_DISPLAY,
          MMConstants.FONT_RESOURCE_PATH, e);
    }
    new MainWindow(null).push();
  }
}