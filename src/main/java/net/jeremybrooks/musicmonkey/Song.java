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

package net.jeremybrooks.musicmonkey;

import net.jeremybrooks.pressplay.FFProbe;
import net.jeremybrooks.pressplay.MediaMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class Song {
    private static final Logger logger = LogManager.getLogger();
    private boolean playing;
    private boolean showTitle = true;
    private boolean showArtist = false;
    private Path songPath;
    private String category;
    private String artist;
    private String title;
    private long duration;
    private int songId;


    public Song() {
    }

    public Song(Path songPath) throws IOException {
        this.songPath = songPath;
        this.songId = songPath.toString().hashCode();
        category = songPath.getName(songPath.getNameCount() - 2).toString();
        try {
            MediaMetadata metadata = FFProbe.getMediaMetadata(songPath.toString());
            artist = metadata.getArtist();
            title = metadata.getTitle();
            duration = metadata.getDuration().toMillis();
        } catch (IOException ioe) {
            logger.warn("Error getting metadata for song {}", songPath);
            throw ioe;
        }
    }

    public int getSongId() {
        return songId;
    }
    public void setSongId(int songId) {
        this.songId = songId;
    }

    public Path getSongPath() {
        return songPath;
    }
    public void setSongPath(Path songPath) {
        this.songPath = songPath;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isShowArtist() {
        return showArtist;
    }

    public void setShowArtist(boolean showArtist) {
        this.showArtist = showArtist;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void revealAnswer() {
        showArtist = isPlaying();
        showTitle = isPlaying();
    }

    public void hide() {
        showArtist = false;
        showTitle = false;
    }

    public boolean isVisible() {
        return showTitle || showArtist;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }
}


