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

package net.jeremybrooks.musicmonkey.db;

import net.jeremybrooks.musicmonkey.MMConstants;
import net.jeremybrooks.musicmonkey.Song;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbUtil {
    private static final Logger logger = LogManager.getLogger();

    private static final String SQL_CREATE_METADATA_TABLE =
            "CREATE TABLE METADATA (DB_VERSION INT NOT NULL)";

    private static final String SQL_POPULATE_METADATA =
            "INSERT INTO METADATA (DB_VERSION) VALUES (1)";

    private static final String SQL_CREATE_SONGS_TABLE = "CREATE TABLE SONGS " +
            "(SONG_ID INT NOT NULL PRIMARY KEY, " +
            "SONG_TITLE VARCHAR(1024), " +
            "SONG_ARTIST VARCHAR(1024), " +
            "SONG_CATEGORY VARCHAR(1024), " +
            "SONG_PATH VARCHAR(1024), " +
            "SONG_DURATION BIGINT, " +
            "REFRESHED BOOLEAN, " +
            "PLAYED BOOLEAN)";

    private static final String SQL_INSERT_SONG =
            "INSERT INTO SONGS " +
                    "  (SONG_ID, SONG_TITLE, SONG_ARTIST, SONG_CATEGORY, SONG_PATH, " +
                    "   SONG_DURATION, REFRESHED, PLAYED) " +
                    "  VALUES " +
                    "    (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SET_ALL_REFRESHED =
            "UPDATE SONGS SET REFRESHED = ?";

    private static final String SQL_SET_REFRESHED_BY_SONG_ID =
            "UPDATE SONGS SET REFRESHED = ? WHERE SONG_ID = ?";

    private static final String SQL_GET_BY_SONG_ID =
            "SELECT " +
                    "SONG_ID, SONG_TITLE, SONG_ARTIST, SONG_CATEGORY, SONG_PATH, SONG_DURATION " +
                    "FROM SONGS " +
                    "WHERE SONG_ID = ?";

    private static final String SQL_COUNT_FOR_SONG_ID =
            "SELECT COUNT(*) FROM SONGS WHERE SONG_ID = ?";

    private static final String SQL_UPDATE_DB_VERSION = "UPDATE METADATA SET DB_VERSION = ?";
    private static final String SQL_GET_DB_VERSION = "SELECT DB_VERSION FROM METADATA";
    private static final String SQL_GET_CATEGORIES = "SELECT DISTINCT CATEGORY FROM SONGS ORDER BY CATEGORY";
    private static final String SQL_DELETE_OLD_SONGS = "DELETE FROM SONGS WHERE REFRESHED = false";

    public static void createDatabase() throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(SQL_CREATE_METADATA_TABLE)) {
                ps.execute();
                logger.info("Created metadata table");
            }
            try (PreparedStatement ps = conn.prepareStatement(SQL_CREATE_SONGS_TABLE)) {
                ps.execute();
                logger.info("Created songs table");
            }
            try (PreparedStatement ps = conn.prepareStatement(SQL_POPULATE_METADATA)) {
                int count = ps.executeUpdate();
                logger.info("Update database version, {} row changed", count);
                if (count != 1) {
                    conn.rollback();
                    throw new SQLException("Insert failed. Expected 1 row changed, result was " + count);
                }
            }
            conn.commit();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:derby:" +
                MMConstants.MUSIC_DB + ";create=true");
    }

    public static int getDatabaseVersion() throws SQLException {
        int version = 0;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_DB_VERSION);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                version = rs.getInt("DB_VERSION");
            }
        }
        return version;
    }

    public static boolean songIdExists(final int songId) throws SQLException {
        boolean exists = false;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_FOR_SONG_ID)) {
            ps.setInt(1, songId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exists = (rs.getInt(1)) > 0;
                }
            }
        }
        return exists;
    }

    public static int setAllRefreshed(boolean refreshed) throws SQLException {
        int count;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SET_ALL_REFRESHED)) {
            ps.setBoolean(1, refreshed);
            count = ps.executeUpdate();
        }
        return count;
    }

    public static int setRefreshedForSongId(boolean refreshed, int songId) throws SQLException {
        int count;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SET_REFRESHED_BY_SONG_ID)) {
            ps.setBoolean(1, refreshed);
            ps.setInt(2, songId);
            count = ps.executeUpdate();
            logger.debug("Set refreshed={} for {}. {} row(s) updated", refreshed, songId, count);
        }
        return count;
    }

    public static int insertSong(Song song) throws SQLException {
        int count;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT_SONG)) {
            ps.setInt(1, song.getSongId());
            ps.setString(2, song.getTitle());
            ps.setString(3, song.getArtist());
            ps.setString(4, song.getCategory());
            ps.setString(5, song.getSongPath().toString());
            ps.setLong(6, song.getDuration());
            ps.setBoolean(7, true);
            ps.setBoolean(8, false);

            count = ps.executeUpdate();
            logger.debug("Inserted {} row(s) for songId {}", count, song.getSongId());
        }
        return count;
    }

    public static List<String> getCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        categories.add("All Music");
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_CATEGORIES);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("CATEGORY"));
            }
        }
        return categories;
    }

    public static Song getSong(int songId) throws SQLException {
        Song song = null;
        try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(SQL_GET_BY_SONG_ID)) {
            ps.setInt(1, songId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    song = new Song();
                    song.setSongId(rs.getInt("SONG_ID"));
                    song.setTitle(rs.getString("SONG_TITLE"));
                    song.setArtist(rs.getString("SONG_ARTIST"));
                    song.setCategory(rs.getString("SONG_CATEGORY"));
                    song.setSongPath(Paths.get(rs.getString("SONG_PATH")));
                    song.setDuration(rs.getLong("SONG_DURATION"));
                }
            }
        }
        return song;
    }

    public static int deleteOldSongs() throws SQLException {
        int count;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_OLD_SONGS)) {
            count = ps.executeUpdate();
            logger.debug("Deleted {} songs.", count);
        }
        return count;
    }

    public static void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (Exception e) {
            logger.error("Database has been shut down.");
        }
    }
}
