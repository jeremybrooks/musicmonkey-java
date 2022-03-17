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

import net.jeremybrooks.musicmonkey.MMConstants;
import net.jeremybrooks.musicmonkey.Song;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.Serial;

public class SongDisplay extends JPanel implements ListCellRenderer<Song> {
  @Serial
  private static final long serialVersionUID = -8929586984880273746L;
  private final JLabel lblSong = new JLabel();
  private final JLabel lblArtist = new JLabel();

  public SongDisplay() {
    super();
    this.setLayout(new GridLayout(2, 1));
    lblSong.setHorizontalAlignment(SwingConstants.CENTER);
    lblSong.setFont(new Font("Monoton", lblSong.getFont().getStyle(), 24));
    this.add(lblSong);
    lblArtist.setHorizontalAlignment(SwingConstants.CENTER);
    lblArtist.setFont(new Font("Monoton", lblArtist.getFont().getStyle(), 24));
    this.add(lblArtist);
    setBorder(new LineBorder(Color.GRAY, 6));
  }

  @Override
  public Component getListCellRendererComponent(JList<? extends Song> list, Song value, int index, boolean isSelected, boolean cellHasFocus) {
    lblSong.setText(value.getTitle());
    lblArtist.setText(value.getArtist());
    lblSong.setVisible(value.isShowTitle());
    lblArtist.setVisible(value.isShowArtist());
    setBackground(MMConstants.SONG_LIST_BG_COLORS.get(index));
    return this;
  }
}
