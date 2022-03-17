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

/*
 * Created by JFormDesigner on Wed Jan 12 13:54:52 PST 2022
 */

package net.jeremybrooks.musicmonkey.gui;

import net.jeremybrooks.musicmonkey.GameState;
import net.jeremybrooks.musicmonkey.MMConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Jeremy Brooks
 */
public class MusicSelectionWindow extends MusicMonkeyFrame {
  @Serial
  private static final long serialVersionUID = -3664575885088175372L;

  private static final Logger logger = LogManager.getLogger();

  private static final int LABEL_COUNT = 9;
  // color list MUST be LABEL_COUNT / 2
  private static final List<Color> COLOR_LIST = 
      List.of(Color.RED, Color.ORANGE, Color.YELLOW, Color.BLUE, Color.BLACK);
  private static final int MAX_LABEL_FONT_SIZE = 50;

  private final List<String> musicList = new ArrayList<>();
  private final List<JLabel> displayList = new ArrayList<>();
  private int selectedIndex;
  
  public MusicSelectionWindow(MusicMonkeyFrame ancestor) {
    super(ancestor);
    initComponents();

    // generate JLabels for display
    for (int i = 0; i < LABEL_COUNT; i++) {
      JLabel label = new JLabel();
      displayList.add(label);
      pnlMusic.add(label, new GridBagConstraints(0, i, 1, 1, 1.0, 0.0,
          GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
          new Insets(0, 0, 5, 0), 0, 0));
    }
    int middle = LABEL_COUNT/2;
    int increment = (MAX_LABEL_FONT_SIZE - 10)/middle;
    JLabel label = displayList.get(middle);
    label.setFont(new Font("Monoton", label.getFont().getStyle(), MAX_LABEL_FONT_SIZE));
    label.setForeground(COLOR_LIST.get(0));
    for (int i = 1; i <= middle; i++) {
      int size = MAX_LABEL_FONT_SIZE - i*increment;
      logger.info("font size {}", size);
      label = displayList.get(middle + i);
      label.setFont(new Font("Monoton", label.getFont().getStyle(), size));
      label.setForeground(COLOR_LIST.get(i));
      label = displayList.get(middle - i);
      label.setFont(new Font("Monoton", label.getFont().getStyle(), size));
      label.setForeground(COLOR_LIST.get(i));
    }


    musicList.add("All Music");
    try {
      musicList.addAll(Files.list(Paths.get(MMConstants.MUSIC_FOLDER))
          .filter(Files::isDirectory)
              .map(path -> path.getFileName().toString())
              .sorted()
          .toList());
    } catch (Exception e) {
       logger.warn("Error getting a list from directory {}", MMConstants.MUSIC_FOLDER, e);
    }
    
    for (int i = 0; i < musicList.size(); i++) {
      if (musicList.get(i).equals(GameState.getInstance().getMusicCategory())) {
        selectedIndex = i;
      }
    }
    
    updateSelection();
  }
  
  public void updateSelection() {
    int middle = LABEL_COUNT/2;
    // the center label gets the selected music
    displayList.get(3).setText(musicList.get(selectedIndex));

    // walk down display labels 4-6,
    // setting the items in the music list that come after the selected index
    for (int i = 4; i < LABEL_COUNT; i++) {
      int target = selectedIndex + (i-middle);
      logger.debug("index={} target={}", i, target);
      if (target < musicList.size()) {
        displayList.get(i).setText(musicList.get(target));
      } else {
        displayList.get(i).setText("-");
      }
    }


    // walk up display labels 2-0,
    // setting the items in the music list that come before the selected index
    for (int i = 0; i < middle; i++) {
      int target = selectedIndex - (middle - i);
      if (target >= 0) {
        displayList.get(i).setText(musicList.get(target));
      } else {
        displayList.get(i).setText("-");
      }
    }
  }

  @Override
  void handleKeypress(KeyEvent e) {
    char c = e.getKeyChar();
    switch (c) {
      case MMConstants.P1B1:
      case MMConstants.P2B1:
      case MMConstants.P3B1:
      case MMConstants.P4B1:
        logger.info("Button 1 pressed {}", c);
        if (selectedIndex < musicList.size() -1) {
          selectedIndex++;
          updateSelection();
        }
        break;

      case MMConstants.P1B2:
      case MMConstants.P2B2:
      case MMConstants.P3B2:
      case MMConstants.P4B2:
        logger.info("Button 2 pressed {}", c);
        if (selectedIndex > 0) {
          selectedIndex--;
          updateSelection();
        }
        break;

      case MMConstants.P1B3:
      case MMConstants.P2B3:
      case MMConstants.P3B3:
      case MMConstants.P4B3:
        logger.info("Button 3 pressed {}", c);
        // no function at this time
        break;

      case MMConstants.P1B4:
      case MMConstants.P2B4:
      case MMConstants.P3B4:
      case MMConstants.P4B4:
        logger.info("Button 4 pressed {}", c);
        GameState.getInstance().setMusicCategory(musicList.get(selectedIndex));
        pop();
        break;
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    ResourceBundle bundle = ResourceBundle.getBundle("net.jeremybrooks.musicmonkey.musicselectionwindow");
    lblHeading = new JLabel();
    pnlMusic = new JPanel();
    lblButtonUp = new JLabel();
    lblButtonUp.setIcon(MMConstants.BUTTON_0);
    lblButtonDown = new JLabel();
    lblButtonDown.setIcon(MMConstants.BUTTON_1);
    lblButtonSelect = new JLabel();
    lblButtonSelect.setIcon(MMConstants.BUTTON_3);

    //======== this ========
    var contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());
    ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0, 0};
    ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
    ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
    ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};

    //---- lblHeading ----
    lblHeading.setText(bundle.getString("MusicSelectionWindow.lblHeading.text"));
    lblHeading.setFont(new Font("Monoton", lblHeading.getFont().getStyle(), 48));
    contentPane.add(lblHeading, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(0, 0, 5, 5), 0, 0));

    //======== pnlMusic ========
    {
        pnlMusic.setBorder(new LineBorder(Color.black, 3, true));
        pnlMusic.setLayout(new GridBagLayout());
        ((GridBagLayout)pnlMusic.getLayout()).columnWidths = new int[] {0, 0};
        ((GridBagLayout)pnlMusic.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
        ((GridBagLayout)pnlMusic.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
        ((GridBagLayout)pnlMusic.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
    }
    contentPane.add(pnlMusic, new GridBagConstraints(0, 1, 1, 3, 1.0, 1.0,
        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        new Insets(0, 10, 0, 5), 0, 0));

    //---- lblButtonUp ----
    lblButtonUp.setText(bundle.getString("MusicSelectionWindow.lblButtonUp.text"));
    contentPane.add(lblButtonUp, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 5, 0), 0, 0));

    //---- lblButtonDown ----
    lblButtonDown.setText(bundle.getString("MusicSelectionWindow.lblButtonDown.text"));
    contentPane.add(lblButtonDown, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 5, 0), 0, 0));

    //---- lblButtonSelect ----
    lblButtonSelect.setText(bundle.getString("MusicSelectionWindow.lblButtonSelect.text"));
    contentPane.add(lblButtonSelect, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JLabel lblHeading;
  private JPanel pnlMusic;
  private JLabel lblButtonUp;
  private JLabel lblButtonDown;
  private JLabel lblButtonSelect;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
