/*
 *  MusicMonkey is Copyright 2022 by Jeremy Brooks
 *
 *  This file is part of MusicMonkey.
 *
 *   MusicMonkey is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   MusicMonkey is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with MusicMonkey.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.jeremybrooks.musicmonkey.worker;

import net.jeremybrooks.musicmonkey.MMConstants;
import net.jeremybrooks.musicmonkey.Song;
import net.jeremybrooks.musicmonkey.db.DbUtil;
import net.jeremybrooks.musicmonkey.gui.LibraryUpdateWindow;
import net.jeremybrooks.musicmonkey.gui.MainWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class LibraryUpdateWorker extends SwingWorker<Void, Path> {
    private static final Logger logger = LogManager.getLogger();
    private int count;
    private final LibraryUpdateWindow libraryUpdateWindow;

    public LibraryUpdateWorker(LibraryUpdateWindow libraryUpdateWindow) {
        this.libraryUpdateWindow = libraryUpdateWindow;
    }

    protected Void doInBackground() {
        AtomicBoolean error = new AtomicBoolean(false);
        try {
            logger.debug("Walking music directory.");
            List<Path> songs = Files.walk(Paths.get(MMConstants.MUSIC_DIRECTORY))
                    .filter(path ->
                            path.toString().toLowerCase().endsWith(".mp3") ||
                                    path.toString().toLowerCase().endsWith(".m4a"))
                    .toList();
            logger.debug("Found {} songs", songs.size());
            SwingUtilities.invokeAndWait(() -> {
                libraryUpdateWindow.getProgress().setMaximum(songs.size());
                libraryUpdateWindow.getProgress().setValue(0);
                libraryUpdateWindow.getSongCountLabel().setText(MessageFormat.format("Scanning {0} songs", songs.size()));
            });
            // set refreshed to false for all songs
            DbUtil.setAllRefreshed(false);
            // for each song on disk
            songs.forEach(path -> {
                int songId = path.toString().hashCode();
                logger.debug("Processing {}@{}", songId, path);
                publish(path);
                try {
                    //   if the song exists in the database, set refreshed to true
                    if (DbUtil.songIdExists(songId)) {
                        DbUtil.setRefreshedForSongId(true, songId);
                    } else {
                        //   else create a Song object and add to the database
                        Song song = new Song(path);
                        DbUtil.insertSong(song);
                    }
                } catch (SQLException sqle) {
                    logger.error("Error updating database for {}.", path, sqle);
                    error.set(true);
                } catch (IOException ioe) {
                    logger.error("Error reading file {}", path, ioe);
                    error.set(true);
                }
            });

        } catch (Exception e) {
            logger.error("Error during database update.", e);
            error.set(true);
        }
        if (error.get()) {
            JOptionPane.showMessageDialog(libraryUpdateWindow,
                    """
                            There were errors while updating the music database.
                            Check the logs to figure out what went wrong.
                            Gameplay cannot continue with database errors.""",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            // delete all songs with refreshed = false from the database
            try {
                DbUtil.deleteOldSongs();
                SwingUtilities.invokeLater(() -> new MainWindow(libraryUpdateWindow).push());
            } catch (SQLException sqle) {
                logger.warn("Error deleting old songs.", sqle);
                JOptionPane.showMessageDialog(libraryUpdateWindow,
                        """
                                There was an error while deleting old songs.
                                Check the logs to figure out what went wrong.
                                Gameplay cannot continue with database errors.""",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

    protected void process(List<Path> chunks) {
        count += chunks.size();
        libraryUpdateWindow.getProgress().setValue(count);
    }

}
