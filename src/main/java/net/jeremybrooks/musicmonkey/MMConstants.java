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

import javax.swing.ImageIcon;
import java.awt.Color;
import java.util.List;

public class MMConstants {
  public static final String IMAGE_RESOURCE_PATH = "/net/jeremybrooks/musicmonkey/images/";

  public static final String FONT_RESOURCE_PATH = "/net/jeremybrooks/musicmonkey/fonts/";
  public static final String FONT_NAME_DISPLAY = "Monoton-Regular.ttf";
  public static final List<Color> SONG_LIST_BG_COLORS =
          List.of(Color.RED, Color.YELLOW, Color.GREEN,
                  new Color(12,20, 215));

  public static final Color COLOR_WRONG_GUESS = new Color(190, 0, 0);

  public static final Color COLOR_CORRECT_GUESS = new Color(0, 203, 0);

  // the keyboard characters that correspond to each controller
  // P1B1 = Player 1, Button 1
  // Button 1 is the top button, button 4 is the bottom button
  public static final char P1B1 = '1';
  public static final char P1B2 = 'q';
  public static final char P1B3 = 'a';
  public static final char P1B4 = 'z';
  public static final char[] P1_BUTTONS = {P1B1, P1B2, P1B3, P1B4};

  public static final char P2B1 = '2';
  public static final char P2B2 = 'w';
  public static final char P2B3 = 's';
  public static final char P2B4 = 'x';
  public static final char[] P2_BUTTONS = {P2B1, P2B2, P2B3, P2B4};

  public static final char P3B1 = '3';
  public static final char P3B2 = 'e';
  public static final char P3B3 = 'd';
  public static final char P3B4 = 'c';
  public static final char[] P3_BUTTONS = {P3B1, P3B2, P3B3, P3B4};

  public static final char P4B1 = '4';
  public static final char P4B2 = 'r';
  public static final char P4B3 = 'f';
  public static final char P4B4 = 'v';
  public static final char[] P4_BUTTONS = {P4B1, P4B2, P4B3, P4B4};


  // player icons
  public static final ImageIcon ICON_P1 = new ImageIcon(MMConstants.class.getResource(
      IMAGE_RESOURCE_PATH + "guitar-100.png"));
  public static final ImageIcon ICON_P1_OFF = new ImageIcon(MMConstants.class.getResource(
      IMAGE_RESOURCE_PATH + "guitar-off-100.png"));
  public static final ImageIcon ICON_P2 = new ImageIcon(MMConstants.class.getResource(
      IMAGE_RESOURCE_PATH + "drum-100.png"));
  public static final ImageIcon ICON_P2_OFF = new ImageIcon(MMConstants.class.getResource(
      IMAGE_RESOURCE_PATH + "drum-off-100.png"));
  public static final ImageIcon ICON_P3 = new ImageIcon(MMConstants.class.getResource(
      IMAGE_RESOURCE_PATH + "keyboard-100.png"));
  public static final ImageIcon ICON_P3_OFF = new ImageIcon(MMConstants.class.getResource(
      IMAGE_RESOURCE_PATH + "keyboard-off-100.png"));
  public static final ImageIcon ICON_P4 = new ImageIcon(MMConstants.class.getResource(
      IMAGE_RESOURCE_PATH + "saxophone-100.png"));
  public static final ImageIcon ICON_P4_OFF = new ImageIcon(MMConstants.class.getResource(
      IMAGE_RESOURCE_PATH + "saxophone-off-100.png"));

  // button icons
  public static final ImageIcon BUTTON_0 = new ImageIcon(MMConstants.class.getResource(
          IMAGE_RESOURCE_PATH + "button-red-100.png"));
  public static final ImageIcon BUTTON_1 = new ImageIcon(MMConstants.class.getResource(
      IMAGE_RESOURCE_PATH + "button-yellow-100.png"));
  public static final ImageIcon BUTTON_2 = new ImageIcon(MMConstants.class.getResource(
          IMAGE_RESOURCE_PATH + "button-green-100.png"));
  public static final ImageIcon BUTTON_3 = new ImageIcon(MMConstants.class.getResource(
          IMAGE_RESOURCE_PATH + "button-dark-blue-100.png"));


  public static final String MUSIC_FOLDER = System.getProperty("user.home") + "/MusicMonkeyMusic";
}
