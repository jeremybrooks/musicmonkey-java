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

package net.jeremybrooks.musicmonkey.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serial;

/**
 * Classes extending this frame should set their JFrameDesigner fields as follows:
 * Location Policy: Don't set location
 * Size Policy: Don't set size
 *
 */
public abstract class MusicMonkeyFrame extends JFrame {
  @Serial
  private static final long serialVersionUID = -3062016350918195576L;

  private static final Logger logger = LogManager.getLogger();

  private MusicMonkeyFrame ancestor;
  private JDialog messageDialog;

  public MusicMonkeyFrame(MusicMonkeyFrame ancestor) {
    this.ancestor = ancestor;
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        handleKeypress(e);
      }
    });
    setUndecorated(true);
    setSize(1024, 600);
    setLocation(0, 0);
    setResizable(false);
  }

  abstract void handleKeypress(KeyEvent e);

  public void push() {
    SwingUtilities.invokeLater(() -> {
      if (null != ancestor) {
        logger.info("Hiding ancestor");
        ancestor.setVisible(false);
      }
      if (messageDialog != null) {
        messageDialog.setVisible(false);
        messageDialog.dispose();
        messageDialog = null;
      }
      logger.info("Showing current");
      setVisible(true);
    });
  }

  public void pop() {
    if (null != ancestor) {
      setVisible(false);
      ancestor.push();
    }
  }
}
