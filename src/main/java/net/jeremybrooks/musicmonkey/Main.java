package net.jeremybrooks.musicmonkey;

import net.jeremybrooks.musicmonkey.db.DbUtil;
import net.jeremybrooks.musicmonkey.gui.LibraryUpdateWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String... args) {
        try {
            InputStream fontStream = Main.class.getResourceAsStream(MMConstants.FONT_RESOURCE_PATH + MMConstants.FONT_NAME_DISPLAY);
            if (null == fontStream) {
                throw new IOException("Input stream for font was null.");
            }
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(Font.createFont(Font.TRUETYPE_FONT, fontStream));

            // SET UP DATABASE
            if (Files.notExists(Paths.get(MMConstants.MUSIC_DB))) {
                DbUtil.createDatabase();
            } else {
                logger.info("Starting with DB version {}", DbUtil.getDatabaseVersion());
            }

        } catch (IOException | FontFormatException e) {
            logger.error("Could not find font {} at path {}", MMConstants.FONT_NAME_DISPLAY, MMConstants.FONT_RESOURCE_PATH, e);
            System.exit(1);
        } catch (SQLException sqle) {
            logger.error("Could not create database at path {}", MMConstants.MUSIC_DB, sqle);
            System.exit(2);
        }
        SwingUtilities.invokeLater(() -> new LibraryUpdateWindow(null).push());
    }
}