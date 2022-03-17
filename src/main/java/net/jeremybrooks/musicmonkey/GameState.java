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

import java.util.HashMap;
import java.util.Map;

import static net.jeremybrooks.musicmonkey.GameState.PlayerId.*;

public class GameState {

  public enum PlayerId {
    ONE,
    TWO,
    THREE,
    FOUR
  }

  private static GameState instance;
  private final Map<PlayerId, Player> players = new HashMap<>();
  private boolean player1Joined;
  private boolean player2Joined;
  private boolean player3Joined;
  private boolean player4Joined;
  private String musicCategory = "All Music";

  public GameState() {
    players.put(ONE, new Player());
    players.put(TWO, new Player());
    players.put(THREE, new Player());
    players.put(FOUR, new Player());
  }

  public static GameState getInstance() {
    if (null == instance) {
      instance = new GameState();
    }
    return instance;
  }

  /**
   * Get the number of players that have joined the game.
   *
   * @return number of players that have joined.
   */
  public int getPlayerCount() {
    return (int) players.values().stream().filter(Player::isJoined).count();
  }

  public boolean isPlayerJoined(PlayerId player) {
    return players.get(player).isJoined();
  }
  public boolean isPlayerActive(PlayerId player) {
    return players.get(player).isActive();
  }

  public void setPlayerActive(PlayerId player, boolean active) {
    players.get(player).setActive(active);
  }

  public void setPlayerJoined(PlayerId player, boolean joined) {
    players.get(player).setJoined(joined);
  }

  public void resetActiveFlagForJoinedPlayers() {
    players.values().stream().filter(Player::isJoined).forEach(p -> p.setActive(true));
  }

  public void resetAllPlayers() {
    players.values().forEach(Player::reset);
  }

  public int getPlayerScore(PlayerId player) {
    return players.get(player).getScore();
  }

  public int addPlayerPoints(PlayerId player, int points) {
    int score = players.get(player).getScore() + points;
    players.get(player).setScore(score);
    return score;
  }

  public int subtractPlayerPoints(PlayerId player, int points) {
    int score = players.get(player).getScore() - points;
    players.get(player).setScore(score);
    return score;
  }

  public String getMusicCategory() { return this.musicCategory; }
  public void setMusicCategory(String musicCategory) { this.musicCategory = musicCategory; }

}
