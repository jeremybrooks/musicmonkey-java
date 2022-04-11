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
 * Created by JFormDesigner on Wed Mar 30 16:54:55 PDT 2022
 */

package net.jeremybrooks.musicmonkey.gui;

import net.jeremybrooks.musicmonkey.worker.LibraryUpdateWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.Serial;
import java.util.ResourceBundle;

/**
 * @author Jeremy Brooks
 */
public class LibraryUpdateWindow extends MusicMonkeyFrame {

    @Serial
    private static final long serialVersionUID = -2010877062931688917L;
    private static final Logger logger = LogManager.getLogger();

    public LibraryUpdateWindow(MusicMonkeyFrame ancestor) {
        super(ancestor);
        initComponents();
        new LibraryUpdateWorker(this).execute();
    }

    @Override
    void handleKeypress(KeyEvent e) {

    }

    public JProgressBar getProgress() {
        return progress;
    }

    public JLabel getSongCountLabel() {
        return songCountLabel;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("net.jeremybrooks.musicmonkey.libraryupdatewindow");
        progress = new JProgressBar();
        label2 = new JLabel();
        songCountLabel = new JLabel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //---- progress ----
        progress.setStringPainted(true);
        contentPane.add(progress, BorderLayout.CENTER);

        //---- label2 ----
        label2.setText(bundle.getString("LibraryUpdateWindow.label2.text"));
        label2.setFont(new Font("Monoton", label2.getFont().getStyle(), 25));
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(label2, BorderLayout.NORTH);

        //---- songCountLabel ----
        songCountLabel.setText(bundle.getString("LibraryUpdateWindow.songCountLabel.text"));
        songCountLabel.setFont(new Font("Monoton", songCountLabel.getFont().getStyle(), 25));
        songCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(songCountLabel, BorderLayout.SOUTH);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JProgressBar progress;
    private JLabel label2;
    private JLabel songCountLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
