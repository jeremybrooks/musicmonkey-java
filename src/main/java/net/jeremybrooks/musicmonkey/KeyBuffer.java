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

import java.util.concurrent.ConcurrentLinkedQueue;

public class KeyBuffer implements Runnable {
  private final ConcurrentLinkedQueue<String> keyQueue;

  public KeyBuffer(ConcurrentLinkedQueue<String> keyQueue) {
    this.keyQueue = keyQueue;
  }

  public void run() {
      
  }

  public void clearKeys() {
    keyQueue.clear();
  }
}
