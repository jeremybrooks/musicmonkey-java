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

import net.jeremybrooks.musicmonkey.GameState;
import net.jeremybrooks.musicmonkey.MMConstants;
import net.jeremybrooks.musicmonkey.MusicLister;
import net.jeremybrooks.musicmonkey.gui.GameWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetMusicWorker extends SwingWorker<List<Path>, Void> {
    private static final Logger logger = LogManager.getLogger();
    private final GameWindow gameWindow;

    public GetMusicWorker(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    protected List<Path> doInBackground() {
        gameWindow.showMessage("Gathering music....");
        List<Path> availableSongList = null;
        Path musicDir;
        if (GameState.getInstance().getMusicCategory().equalsIgnoreCase("All Music")) {
            musicDir = Paths.get(MMConstants.MUSIC_DIRECTORY);
        } else {
            musicDir = Paths.get(MMConstants.MUSIC_DIRECTORY, GameState.getInstance().getMusicCategory());
        }
        MusicLister lister = new MusicLister();
        try {
            Files.walkFileTree(musicDir, lister);
            availableSongList = lister.getSongList();
        } catch (Exception e) {
            logger.error("Error getting music for {}", musicDir, e);
        }
        return availableSongList;
    }

    @Override
    protected void done() {
        try {
            gameWindow.setAvailableSongList(get());
            gameWindow.showMessage("");
            gameWindow.startGame();
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("Error in music worker", e);
        }
    }
}
