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
 * Created by JFormDesigner on Fri Jan 07 16:22:08 PST 2022
 */

package net.jeremybrooks.musicmonkey.gui;

import net.jeremybrooks.musicmonkey.GameState;
import net.jeremybrooks.musicmonkey.MMConstants;
import net.jeremybrooks.musicmonkey.db.DbUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.Serial;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

/**
 * @author Jeremy Brooks
 */
public class MainWindow extends MusicMonkeyFrame {

  @Serial
  private static final long serialVersionUID = -5880858723776982447L;

  private static final Logger logger = LogManager.getLogger();




  private final MusicMonkeyFrame musicSelectionWindow;
  private final MusicMonkeyFrame gameWindow;

  public MainWindow(MusicMonkeyFrame ancestor) {
    super(ancestor);
    initComponents();
    setUndecorated(true);
    setSize(1024, 600);
    setLocation(0, 0);
    musicSelectionWindow = new MusicSelectionWindow(this);
    gameWindow = new GameWindow(this);
  }

  @Override
  public void handleKeypress(KeyEvent e) {
    switch (e.getKeyChar()) {

      case '0':
        DbUtil.shutdown();
        System.exit(0);
        break;

      case MMConstants.P1B1:
        GameState.getInstance().setPlayerJoined(GameState.PlayerId.ONE, !GameState.getInstance().isPlayerJoined(GameState.PlayerId.ONE));
        lblP1.setIcon(GameState.getInstance().isPlayerJoined(GameState.PlayerId.ONE)
            ? MMConstants.ICON_P1 : MMConstants.ICON_P1_OFF);
        break;
      case MMConstants.P2B1:
        GameState.getInstance().setPlayerJoined(GameState.PlayerId.TWO, !GameState.getInstance().isPlayerJoined(GameState.PlayerId.TWO));
        lblP2.setIcon(GameState.getInstance().isPlayerJoined(GameState.PlayerId.TWO)
          ? MMConstants.ICON_P2 : MMConstants.ICON_P2_OFF);
        break;
      case MMConstants.P3B1:
        GameState.getInstance().setPlayerJoined(GameState.PlayerId.THREE, !GameState.getInstance().isPlayerJoined(GameState.PlayerId.THREE));
        lblP3.setIcon(GameState.getInstance().isPlayerJoined(GameState.PlayerId.THREE)
            ? MMConstants.ICON_P3 : MMConstants.ICON_P3_OFF);
        break;
      case MMConstants.P4B1:
        GameState.getInstance().setPlayerJoined(GameState.PlayerId.FOUR, !GameState.getInstance().isPlayerJoined(GameState.PlayerId.FOUR));
        lblP4.setIcon(GameState.getInstance().isPlayerJoined(GameState.PlayerId.FOUR)
            ? MMConstants.ICON_P4 : MMConstants.ICON_P4_OFF);
        break;

      case MMConstants.P1B2:
      case MMConstants.P2B2:
      case MMConstants.P3B2:
      case MMConstants.P4B2:
        logger.info("Pushing music selection window");
        musicSelectionWindow.push();
        break;

      case MMConstants.P1B3:
      case MMConstants.P2B3:
      case MMConstants.P3B3:
      case MMConstants.P4B3:
        break;

      case MMConstants.P1B4:
      case MMConstants.P2B4:
      case MMConstants.P3B4:
      case MMConstants.P4B4:
        if (GameState.getInstance().getPlayerCount() == 0) {
          showMessage("No players have joined!", Duration.ofSeconds(5));
        } else {
          gameWindow.push();
        }
        break;
    }
  }

  @Override
  public void push() {
    lblMusicSelection.setText("Current music: " + GameState.getInstance().getMusicCategory());
    super.push();
  }
  
  private void showMessage(String message, Duration timeout) {
    Executors.newSingleThreadExecutor().execute(
            () -> {
              SwingUtilities.invokeLater(() -> lblMessage.setText(message));
              try {
                Thread.sleep(timeout.toMillis());
              } catch (Exception e) {
                // ignore
              } finally {
                SwingUtilities.invokeLater(() -> lblMessage.setText(" "));
              }
            }
    );
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    ResourceBundle bundle = ResourceBundle.getBundle("net.jeremybrooks.musicmonkey.mainwindow");
    label1 = new JLabel();
    lblP1 = new JLabel();
    lblP1.setIcon(MMConstants.ICON_P1_OFF);
    lblP2 = new JLabel();
    lblP2.setIcon(MMConstants.ICON_P2_OFF);
    lblP3 = new JLabel();
    lblP3.setIcon(MMConstants.ICON_P3_OFF);
    lblP4 = new JLabel();
    lblP4.setIcon(MMConstants.ICON_P4_OFF);
    panel1 = new JPanel();
    lblJoin = new JLabel();
    lblJoin.setIcon(MMConstants.BUTTON_0);
    lblMusic = new JLabel();
    lblMusic.setIcon(MMConstants.BUTTON_1);
    lblMusicSelection = new JLabel();
    lblSomething = new JLabel();
    lblSomething.setIcon(MMConstants.BUTTON_3);
    lblMessage = new JLabel();

    //======== this ========
    setFont(new Font("Lucida Grande", Font.PLAIN, 48));
    var contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());
    ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
    ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
    ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 1.0E-4};
    ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};

    //---- label1 ----
    label1.setText("MUSIC MONKEY");
    label1.setHorizontalAlignment(SwingConstants.CENTER);
    label1.setFont(new Font("Monoton", label1.getFont().getStyle(), 70));
    contentPane.add(label1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    contentPane.add(lblP1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        new Insets(10, 10, 10, 10), 0, 0));
    contentPane.add(lblP2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
        new Insets(10, 10, 10, 10), 0, 0));
    contentPane.add(lblP3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
        new Insets(10, 10, 10, 10), 0, 0));
    contentPane.add(lblP4, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
        new Insets(10, 10, 10, 10), 0, 0));

    //======== panel1 ========
    {
        panel1.setLayout(new GridBagLayout());
        ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0};
        ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
        ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
        ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

        //---- lblJoin ----
        lblJoin.setText("Join/Leave Game");
        lblJoin.setFont(new Font("Monoton", lblJoin.getFont().getStyle(), 30));
        panel1.add(lblJoin, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 50, 5, 0), 0, 0));

        //---- lblMusic ----
        lblMusic.setText("Change Music");
        lblMusic.setFont(new Font("Monoton", lblMusic.getFont().getStyle(), 30));
        panel1.add(lblMusic, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 50, 5, 0), 0, 0));

        //---- lblMusicSelection ----
        lblMusicSelection.setText(bundle.getString("MainWindow.lblMusicSelection.text"));
        lblMusicSelection.setFont(new Font("Monoton", lblMusicSelection.getFont().getStyle(), 24));
        panel1.add(lblMusicSelection, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 160, 5, 0), 0, 0));

        //---- lblSomething ----
        lblSomething.setText(bundle.getString("MainWindow.lblSomething.text"));
        lblSomething.setFont(new Font("Monoton", lblSomething.getFont().getStyle(), 30));
        panel1.add(lblSomething, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 50, 5, 0), 0, 0));
    }
    contentPane.add(panel1, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    //---- lblMessage ----
    lblMessage.setText(" ");
    lblMessage.setFont(new Font("Monoton", lblMessage.getFont().getStyle(), 30));
    lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
    contentPane.add(lblMessage, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JLabel label1;
  private JLabel lblP1;
  private JLabel lblP2;
  private JLabel lblP3;
  private JLabel lblP4;
  private JPanel panel1;
  private JLabel lblJoin;
  private JLabel lblMusic;
  private JLabel lblMusicSelection;
  private JLabel lblSomething;
  private JLabel lblMessage;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
