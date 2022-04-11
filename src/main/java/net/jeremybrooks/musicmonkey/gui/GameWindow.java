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
 * Created by JFormDesigner on Tue Feb 01 20:38:50 PST 2022
 */

package net.jeremybrooks.musicmonkey.gui;

import net.jeremybrooks.musicmonkey.GameState;
import net.jeremybrooks.musicmonkey.MMConstants;
import net.jeremybrooks.musicmonkey.Song;
import net.jeremybrooks.musicmonkey.worker.GetMusicWorker;
import net.jeremybrooks.pressplay.FFPlay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.Serial;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Jeremy Brooks
 */
public class GameWindow extends MusicMonkeyFrame {
    @Serial
    private static final long serialVersionUID = -1191363846689192338L;
    private static final Logger logger = LogManager.getLogger();

    private static final int STARTING_SCORE = 100;
    private static final long ROUND_TIME_MILLIS = 10000;

    private final GameState gameState = GameState.getInstance();
    private boolean musicPlaying = false;
    private List<Path> availableSongList;
    private final DefaultListModel<Song> songListModel = new DefaultListModel<>();
    private int selectedSong = 0;
    private FFPlay<Path> songPlayer;
    private Timer scoreTimer;
    private final Map<GameState.PlayerId, JLabel> playerScores = new HashMap<>();
    private final Color scoreColor;

    public GameWindow(MusicMonkeyFrame ancestor) {
        super(ancestor);
        initComponents();
        scoreColor = lblP1Score.getForeground();
        playerScores.put(GameState.PlayerId.ONE, lblP1Score);
        playerScores.put(GameState.PlayerId.TWO, lblP2Score);
        playerScores.put(GameState.PlayerId.THREE, lblP3Score);
        playerScores.put(GameState.PlayerId.FOUR, lblP4Score);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        lblP1 = new JLabel();
        lblP2 = new JLabel();
        lblP3 = new JLabel();
        lblP4 = new JLabel();
        lblP1Score = new JLabel();
        lblP2Score = new JLabel();
        lblP3Score = new JLabel();
        lblP4Score = new JLabel();
        lblTimer = new JLabel();
        panel1 = new JPanel();
        listSongs = new JList();
        lblMessage = new JLabel();

        //======== this ========
        setResizable(false);
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
        ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
        ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
        ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
        contentPane.add(lblP1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
            new Insets(10, 10, 15, 15), 0, 0));
        contentPane.add(lblP2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
            new Insets(10, 10, 15, 10), 0, 0));
        contentPane.add(lblP3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets(10, 10, 10, 15), 0, 0));
        contentPane.add(lblP4, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets(10, 10, 10, 10), 0, 0));

        //---- lblP1Score ----
        lblP1Score.setText("0");
        lblP1Score.setHorizontalAlignment(SwingConstants.CENTER);
        lblP1Score.setFont(new Font("Monoton", lblP1Score.getFont().getStyle(), 32));
        contentPane.add(lblP1Score, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 5, 5), 0, 0));

        //---- lblP2Score ----
        lblP2Score.setText("0");
        lblP2Score.setHorizontalAlignment(SwingConstants.CENTER);
        lblP2Score.setFont(new Font("Monoton", lblP2Score.getFont().getStyle(), 32));
        contentPane.add(lblP2Score, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 5, 0), 0, 0));

        //---- lblP3Score ----
        lblP3Score.setText("0");
        lblP3Score.setHorizontalAlignment(SwingConstants.CENTER);
        lblP3Score.setFont(new Font("Monoton", lblP3Score.getFont().getStyle(), 32));
        contentPane.add(lblP3Score, new GridBagConstraints(0, 2, 1, 1, 0.0, 1.0,
            GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 5, 5), 0, 0));

        //---- lblP4Score ----
        lblP4Score.setText("0");
        lblP4Score.setHorizontalAlignment(SwingConstants.CENTER);
        lblP4Score.setFont(new Font("Monoton", lblP4Score.getFont().getStyle(), 32));
        contentPane.add(lblP4Score, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 5, 0), 0, 0));

        //---- lblTimer ----
        lblTimer.setText("100");
        lblTimer.setFont(new Font("Monoton", lblTimer.getFont().getStyle(), 32));
        contentPane.add(lblTimer, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
            new Insets(0, 0, 5, 5), 0, 0));

        //======== panel1 ========
        {
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0};
            ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0};
            ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
            ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

            //---- listSongs ----
            listSongs.setEnabled(false);
            listSongs.setModel(songListModel);
            listSongs.setCellRenderer(new SongDisplay());
            panel1.add(listSongs, new GridBagConstraints(0, 0, 1, 1, 1.0, 10.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel1, new GridBagConstraints(1, 1, 1, 2, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 5), 0, 0));

        //---- lblMessage ----
        lblMessage.setText(" ");
        lblMessage.setFont(new Font("Monoton", lblMessage.getFont().getStyle(), 32));
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblMessage, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 5), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    @Override
    void handleKeypress(KeyEvent e) {
        char c = e.getKeyChar();
        if (c == '0') {
            scoreTimer.cancel();
            songPlayer.stop();
            pop();
            return;
        }

        int songIndex = switch (c) {
            case MMConstants.P1B1, MMConstants.P2B1, MMConstants.P3B1, MMConstants.P4B1 -> 0;
            case MMConstants.P1B2, MMConstants.P2B2, MMConstants.P3B2, MMConstants.P4B2 -> 1;
            case MMConstants.P1B3, MMConstants.P2B3, MMConstants.P3B3, MMConstants.P4B3 -> 2;
            case MMConstants.P1B4, MMConstants.P2B4, MMConstants.P3B4, MMConstants.P4B4 -> 3;
            default -> -1;
        };

        // only process the player's guess
        // if music is playing AND the guessed song is showing
        if (musicPlaying && songListModel.get(songIndex).isVisible()) {
            switch (c) {
                case MMConstants.P1B1:
                case MMConstants.P1B2:
                case MMConstants.P1B3:
                case MMConstants.P1B4:
                    if (gameState.isPlayerActive(GameState.PlayerId.ONE)) {
                        if (MMConstants.P1_BUTTONS[selectedSong] == e.getKeyChar()) {
                            Executors.newSingleThreadExecutor()
                                    .execute(new CorrectGuess(GameState.PlayerId.ONE));
                        } else {
                            hideWrongAnswer(c);
                            Executors.newSingleThreadExecutor()
                                    .execute(new WrongGuess(GameState.PlayerId.ONE));
                        }
                    }
                    break;
                case MMConstants.P2B1:
                case MMConstants.P2B2:
                case MMConstants.P2B3:
                case MMConstants.P2B4:
                    if (gameState.isPlayerActive(GameState.PlayerId.TWO)) {
                        if (MMConstants.P2_BUTTONS[selectedSong] == e.getKeyChar()) {
                            Executors.newSingleThreadExecutor()
                                    .execute(new CorrectGuess(GameState.PlayerId.TWO));
                        } else {
                            hideWrongAnswer(c);
                            Executors.newSingleThreadExecutor()
                                    .execute(new WrongGuess(GameState.PlayerId.TWO));
                        }
                    }
                    break;
                case MMConstants.P3B1:
                case MMConstants.P3B2:
                case MMConstants.P3B3:
                case MMConstants.P3B4:
                    if (gameState.isPlayerActive(GameState.PlayerId.THREE)) {
                        if (MMConstants.P3_BUTTONS[selectedSong] == e.getKeyChar()) {
                            Executors.newSingleThreadExecutor()
                                    .execute(new CorrectGuess(GameState.PlayerId.THREE));
                        } else {
                            hideWrongAnswer(c);
                            Executors.newSingleThreadExecutor()
                                    .execute(new WrongGuess(GameState.PlayerId.THREE));
                        }
                    }
                    break;
                case MMConstants.P4B1:
                case MMConstants.P4B2:
                case MMConstants.P4B3:
                case MMConstants.P4B4:
                    if (gameState.isPlayerActive(GameState.PlayerId.FOUR)) {
                        if (MMConstants.P4_BUTTONS[selectedSong] == e.getKeyChar()) {
                            Executors.newSingleThreadExecutor()
                                    .execute(new CorrectGuess(GameState.PlayerId.FOUR));
                        } else {
                            hideWrongAnswer(c);
                            Executors.newSingleThreadExecutor()
                                    .execute(new WrongGuess(GameState.PlayerId.FOUR));
                        }
                    }
                    break;
                default:
                    // ignore
                    logger.info("Unexpected key {}", e.getKeyChar());
            }
        }
    }


    @Override
    public void push() {
        // get music list in the background - this can take some time
        new GetMusicWorker(this).execute();

        setPlayerIcons();
        lblP1Score.setEnabled(gameState.isPlayerJoined(GameState.PlayerId.ONE));
        lblP2Score.setEnabled(gameState.isPlayerJoined(GameState.PlayerId.TWO));
        lblP3Score.setEnabled(gameState.isPlayerJoined(GameState.PlayerId.THREE));
        lblP4Score.setEnabled(gameState.isPlayerJoined(GameState.PlayerId.FOUR));
        super.push();
    }

    private void setScores() {
        SwingUtilities.invokeLater(() -> {
            playerScores.forEach((playerId, jLabel) -> {
                jLabel.setForeground(scoreColor);
                jLabel.setText(Integer.toString(gameState.getPlayerScore(playerId)));
            });
        });
    }

    private void setPlayerIcons() {
        SwingUtilities.invokeLater(() -> {
            if (gameState.isPlayerJoined(GameState.PlayerId.ONE)) {
                lblP1.setIcon(MMConstants.ICON_P1);
            } else {
                lblP1.setIcon(MMConstants.ICON_P1_OFF);
            }
            if (gameState.isPlayerJoined(GameState.PlayerId.TWO)) {
                lblP2.setIcon(MMConstants.ICON_P2);
            } else {
                lblP2.setIcon(MMConstants.ICON_P2_OFF);
            }
            if (gameState.isPlayerJoined(GameState.PlayerId.THREE)) {
                lblP3.setIcon(MMConstants.ICON_P3);
            } else {
                lblP3.setIcon(MMConstants.ICON_P3_OFF);
            }
            if (gameState.isPlayerJoined(GameState.PlayerId.FOUR)) {
                lblP4.setIcon(MMConstants.ICON_P4);
            } else {
                lblP4.setIcon(MMConstants.ICON_P4_OFF);
            }
        });
    }

    public void startGame() {
        setScores();
        gameState.resetActiveFlagForJoinedPlayers();
        SwingUtilities.invokeLater(songListModel::clear);
        List<Song> songs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Path songPath = availableSongList.remove(ThreadLocalRandom.current().nextInt(0, availableSongList.size()));
            // TODO  this comes from the database
//            songs.add(new Song(songPath));
        }
        selectedSong = ThreadLocalRandom.current().nextInt(0, 4);

        Path songPath = songs.get(selectedSong).getSongPath();
        songPlayer = new FFPlay.Builder<Path>()
                .media(songPath)
                .build();
        songPlayer.setSeekTime(calculateSeekTime(songPlayer.getMediaMetadata().getDuration()));
        songPlayer.play();
        musicPlaying = true;

        SwingUtilities.invokeLater(() -> {
            songs.forEach(songListModel::addElement);
            songListModel.get(selectedSong).setPlaying(true);
        });

        scoreTimer = new Timer();
        scoreTimer.schedule(new Countdown(), 0, ROUND_TIME_MILLIS / STARTING_SCORE);
    }

    private Duration calculateSeekTime(Duration duration) {
        try {
            // select a random time from the beginning of the song,
            // to ROUND_TIME
            long bound = duration.toMillis() - ROUND_TIME_MILLIS;
            if (bound >= 0) {
                duration = Duration.ofMillis(ThreadLocalRandom.current().nextLong(0, bound));
            } else {
                duration = Duration.ZERO;
            }
        } catch (Exception e) {
            duration = Duration.ZERO;
        }
        return duration;
    }

    private void showCorrectAnswer() {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < 4; i++) {
                Song song = songListModel.get(i);
                song.revealAnswer();
                songListModel.setElementAt(song, i);
            }
        });
    }

    private void hideWrongAnswer(char answer) {
        int songIndex = switch (answer) {
            case MMConstants.P1B1, MMConstants.P2B1, MMConstants.P3B1, MMConstants.P4B1 -> 0;
            case MMConstants.P1B2, MMConstants.P2B2, MMConstants.P3B2, MMConstants.P4B2 -> 1;
            case MMConstants.P1B3, MMConstants.P2B3, MMConstants.P3B3, MMConstants.P4B3 -> 2;
            case MMConstants.P1B4, MMConstants.P2B4, MMConstants.P3B4, MMConstants.P4B4 -> 3;
            default -> -1;
        };
        SwingUtilities.invokeLater(() -> {
            Song song = songListModel.get(songIndex);
            song.hide();
            songListModel.setElementAt(song, songIndex);
        });
    }

    public void showMessage(String message) {
        if (null != message) {
            SwingUtilities.invokeLater(() -> lblMessage.setText(message));
        }
    }

    public void setAvailableSongList(List<Path> availableSongList) {
        this.availableSongList = availableSongList;
    }

    /*
     * A timer task that counts down from STARTING_SCORE to zero.
     * Display changes in this Task must use SwingUtilities to schedule updates
     * on the EDT.
     */
    private class Countdown extends TimerTask {
        private int score = STARTING_SCORE;

        @Override
        public void run() {
            SwingUtilities.invokeLater(() -> lblTimer.setText(Integer.toString(score)));
            score -= 1;
            if (score == 50) {
                // Make the artist names visible
                SwingUtilities.invokeLater(() -> {
                    for (int i = 0; i < songListModel.size(); i++) {
                        Song song = songListModel.get(i);
                        if (song.isVisible()) {
                            song.setShowArtist(true);
                            songListModel.setElementAt(song, i);
                        }
                    }
                });
            } else if (score == 0) {
                musicPlaying = false;
                scoreTimer.cancel();
                showCorrectAnswer();

                SwingUtilities.invokeLater(() -> lblTimer.setText("NO WINNER"));
                for (int i = 5; i > 0; i--) {
                    int finalI = i;
                    SwingUtilities.invokeLater(() -> lblMessage.setText(String.format("Next round in %d...", finalI)));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        logger.error("Interrupted", ie);
                    }
                }
                songPlayer.stop();
                startGame();
                SwingUtilities.invokeLater(() -> lblMessage.setText(" "));
            }
        }
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel lblP1;
    private JLabel lblP2;
    private JLabel lblP3;
    private JLabel lblP4;
    private JLabel lblP1Score;
    private JLabel lblP2Score;
    private JLabel lblP3Score;
    private JLabel lblP4Score;
    private JLabel lblTimer;
    private JPanel panel1;
    private JList listSongs;
    private JLabel lblMessage;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    class CorrectGuess implements Runnable {
        private final GameState.PlayerId player;

        CorrectGuess(GameState.PlayerId player) {
            this.player = player;
        }

        public void run() {
            musicPlaying = false;
            scoreTimer.cancel();
            showCorrectAnswer();
            final int points = Integer.parseInt(lblTimer.getText());
            logger.info("Player {} gets {} points", player, points);
            int newScore = gameState.addPlayerPoints(player, points);
            SwingUtilities.invokeLater(() ->
                    lblTimer.setText(String.format("%d points to player %s", points, player.name())));
            JLabel scoreLabel = playerScores.get(player);
            SwingUtilities.invokeLater(() -> {
                scoreLabel.setForeground(MMConstants.COLOR_CORRECT_GUESS);
                scoreLabel.setText("+" + points);
            });

            for (int i = 5; i > 0; i--) {
                int finalI = i;
                SwingUtilities.invokeLater(() -> lblMessage.setText(String.format("Next round in %d...", finalI)));
                try {
                    Thread.sleep(1000);
                    if (i == 3) {
                        SwingUtilities.invokeLater(() -> {
                            scoreLabel.setForeground(scoreColor);
                            scoreLabel.setText(Integer.toString(newScore));
                        });
                    }
                } catch (InterruptedException ie) {
                    logger.error("Interrupted", ie);
                }
            }
            songPlayer.stop();
            startGame();
            SwingUtilities.invokeLater(() -> lblMessage.setText(" "));
        }
    }

    class WrongGuess implements Runnable {
        private final GameState.PlayerId player;

        WrongGuess(GameState.PlayerId player) {
            this.player = player;
        }

        public void run() {
            gameState.setPlayerActive(player, false);
            final int points = Integer.parseInt(lblTimer.getText());
            final String negativePoints = Integer.toString(-points);
            gameState.subtractPlayerPoints(player, points);
            logger.info("Player {} loses {} points", player, points);
            JLabel scoreLabel = playerScores.get(player);
            SwingUtilities.invokeLater(() -> {
                scoreLabel.setForeground(MMConstants.COLOR_WRONG_GUESS);
                scoreLabel.setText(negativePoints);
            });
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                logger.warn("Interrupted while waiting");
            } finally {
                if (scoreLabel.getText().equals(negativePoints)) {
                    SwingUtilities.invokeLater(() -> {
                        scoreLabel.setForeground(scoreColor);
                        scoreLabel.setText(Integer.toString(gameState.getPlayerScore(player)));
                    });
                }
            }
        }
    }

}
