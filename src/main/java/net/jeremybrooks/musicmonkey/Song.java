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

import java.io.IOException;
import java.nio.file.Path;

public class Song {
  private MediaMetadata metadata;
  private boolean playing;
  private boolean showTitle = true;
  private boolean showArtist = false;
  private final Path songPath;

  public Song(Path songPath) {
    this.songPath = songPath;
    try {
      this.metadata = FFProbe.getMediaMetadata(songPath.toString());
    } catch (IOException ioe) {
      // todo log warning
      this.metadata = new MediaMetadata();
    }
  }

  public Path getSongPath() {
    return songPath;
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
    return metadata.getArtist();
  }

  public String getTitle() {
    return metadata.getTitle();
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


