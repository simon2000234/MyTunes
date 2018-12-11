/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mytunes.be.Playlist;
import mytunes.be.Song;

/**
 *
 * @author Melchertsen
 */
public class PlaylistDAO
{

    private DBConnectionProvider dbConnect;

    public PlaylistDAO()
    {
        dbConnect = new DBConnectionProvider();
    }

    /**
     * creates a playlist
     *
     * @param name the name of the playlist
     * @return the created playlist
     * @throws SQLException
     */
    public Playlist createPlaylist(String name) throws SQLException
    {
        String sql = "INSERT INTO Playlist(name) VALUES(?);";

        try (Connection con = dbConnect.getConnection())
        {
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            st.setString(1, name);

            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            int id = 0;
            if (rs.next())
            {
                id = rs.getInt(1);
            }

            Playlist playlist = new Playlist(id, name);
            return playlist;
        }
    }

    /**
     * adds a song to the playlist
     *
     * @param song the song that you wish to add
     * @param playlist the playlist that you wish to add the specified song to
     * @throws SQLException
     */
    public void addSongToPlaylist(Song song, Playlist playlist) throws SQLException
    {
        List<Song> songs = getAllSongsOnPlaylist(playlist);
        for (Song song1 : songs)
        {
            if (song1.getSongID() == song.getSongID())
            {
                System.out.println("song is already on playlist, fuck nej");
                return;
            }
        }
        String sql = "INSERT INTO playlistSong(playlistId, songId, trackNumber) VALUES(?,?,?);";

        try (Connection con = dbConnect.getConnection())
        {
            PreparedStatement st = con.prepareStatement(sql);

            st.setInt(1, playlist.getPlaylistID());
            st.setInt(2, song.getSongID());
            st.setInt(3, getNextTrackNumber(playlist));

            st.executeUpdate();
        }
    }

    /**
     * Gets all the songs on a specified playlist
     *
     * @param playlist the playlist that you wish to get all the songs on
     * @return an Arraylist of all the songs on the specified playlist
     * @throws SQLException
     */
    public List<Song> getAllSongsOnPlaylist(Playlist playlist) throws SQLException
    {
        List<Song> songs = new ArrayList<>();

        try (Connection con = dbConnect.getConnection())
        {
            Statement Statement = con.createStatement();
            ResultSet rs = Statement.executeQuery("SELECT * FROM PlaylistSong "
                    + "LEFT JOIN Song ON PlaylistSong.songId=Song.id "
                    + "WHERE playlistId = " + playlist.getPlaylistID() + " ORDER BY trackNumber");
            while (rs.next())
            {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                int time = rs.getInt("time");
                String category = rs.getString("category");
                String filePath = rs.getString("filePath");
                Song song = new Song(id, title, artist, time, category, filePath);
                songs.add(song);
            }
            return songs;
        }
    }

    /**
     * Removes a song from a playlist
     *
     * @param playlist the playlist from where you wish to remove the song
     * @param song the song that you wish to remove from the specified playlist
     * @throws SQLException
     */
    public void removeSong(Playlist playlist, Song song) throws SQLException
    {
        int trackNumber = getSongOnPlaylistTrackNumber(playlist, song);
        String sql2 = "UPDATE PlaylistSong SET trackNumber = trackNumber - 1 WHERE trackNumber>" + trackNumber
                + "AND playlistId=" + playlist.getPlaylistID() + ";";
        String sql = "DELETE FROM PlaylistSong WHERE playlistId = "
                + playlist.getPlaylistID() + "and songId = " + song.getSongID() + ";";
        try (Connection con = dbConnect.getConnection())
        {
            Statement Statement = con.createStatement();

            Statement.executeQuery(sql + sql2);

        } catch (SQLException ex)
        {
            //nothing
        }
    }

    /**
     * Gets all playlists
     *
     * @return an Arraylist of all the playlists that have been created
     * @throws SQLException
     */
    public List<Playlist> getAllPlaylists() throws SQLException
    {
        List<Playlist> allPlaylists = new ArrayList<>();
        try (Connection con = dbConnect.getConnection())
        {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Playlist");
            while (rs.next())
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Playlist playlist = new Playlist(id, name);
                if (id != 41) // 41 is the id of the hidden playlist in the songview
                {
                allPlaylists.add(playlist);
                }
            }
            return allPlaylists;
        }
    }

    /**
     * Deletes the songs from a playlist and the playlist from the database
     *
     * @param id the id of the playlist that you want to delete
     * @throws SQLException
     */
    public void deletePlayList(int id) throws SQLException
    {
        try (Connection con = dbConnect.getConnection())
        {
            String sql1 = "DELETE FROM PlaylistSong WHERE playlistId=" + id + ";";
            String sql2 = "DELETE FROM Playlist WHERE id=" + id + ";";

            Statement statement = con.createStatement();

            statement.execute(sql1);
            statement.execute(sql2);

        }
    }

    /**
     * Finds a playlist
     *
     * @param playlistId the id needed to find the playlist
     * @return a playlist if a valid one can be found
     * @throws SQLException
     */
    public Playlist getPlaylist(int playlistId) throws SQLException
    {
        try (Connection con = dbConnect.getConnection())
        {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Song WHERE id=" + playlistId + ";");
            while (rs.next())
            {
                int id = playlistId;
                String name = rs.getString("name");
                Playlist playlist = new Playlist(playlistId, name);
                return playlist;
            }
        }
        return null;

    }

    /**
     * Gets all the track numbers of songs on a specified playlist
     *
     * @param playlist the playlist from where you wish to get all the
     * tracknumbers
     * @return an Arraylist of all the track numbers on the songs of the
     * playlist
     * @throws SQLException
     */
    public ArrayList<Integer> getTrackNumbers(Playlist playlist) throws SQLException
    {
        String sql = "SELECT * FROM PlaylistSong WHERE playlistId = " + playlist.getPlaylistID() + ";";
        ArrayList<Integer> trackNumbers = new ArrayList<>();

        try (Connection con = dbConnect.getConnection())
        {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next())
            {
                int tn = rs.getInt("trackNumber");
                trackNumbers.add(tn);
            }

            return trackNumbers;
        }
    }

    /**
     * Generates a track number so that no two songs on a playlist have the same
     * track number
     *
     * @param playlist the playlist from where you wish to get a new track
     * number for a new song
     * @return the new and unique track number
     * @throws SQLException
     */
    public Integer getNextTrackNumber(Playlist playlist) throws SQLException
    {
        List<Integer> trackNumbers = getTrackNumbers(playlist);
        int curNextTN = 1;
        for (int i = 0; i < trackNumbers.size(); i++)
        {
            if (curNextTN != trackNumbers.get(i))
            {
                return curNextTN;
            } else
            {
                curNextTN++;
            }
        }
        return curNextTN;
    }

    /**
     * Gets the next song that is going the play when the current song has ended
     * on the playlist
     *
     * @param playlist the playlist that you are currently listening to
     * @param curSong the song that is currently playing on the playlist
     * @return the next song that is going to be played, if a valid one can be
     * found
     * @throws SQLException
     */
    public Song getNextSongOnPlaylist(Playlist playlist, Song curSong) throws SQLException
    {
        String sql1 = "SELECT * FROM PlaylistSong WHERE songId = "
                + curSong.getSongID() + " and playlistId = " + playlist.getPlaylistID() + ";";
        int trackNumber = -1;
        try (Connection con = dbConnect.getConnection())
        {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql1);
            while (rs.next())
            {
                trackNumber = rs.getInt("trackNumber");
                if (trackNumber >= getTrackNumbers(playlist).size())
                {
                    trackNumber = 0;
                }
            }

            String sql2 = "SELECT * FROM PlaylistSong LEFT JOIN Song ON "
                    + "PlaylistSong.songId=Song.id WHERE playlistId = " + playlist.getPlaylistID()
                    + " and trackNumber = " + (trackNumber + 1) + ";";

            rs = statement.executeQuery(sql2);
            while (rs.next())
            {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                int time = rs.getInt("time");
                String category = rs.getString("category");
                String filePath = rs.getString("filePath");
                Song song = new Song(id, title, artist, time, category, filePath);
                return song;
            }
        }
        return null;
    }

    /**
     * Gets the track number of a song on a playlist
     *
     * @param playlist the playlist that the song is on
     * @param song the song on the playlist that you wish to get the track
     * number of
     * @return the track number if the song exists on the playlist
     * @throws SQLException
     */
    public Integer getSongOnPlaylistTrackNumber(Playlist playlist, Song song) throws SQLException
    {
        String sql = "SELECT * FROM PlaylistSong WHERE playlistId = "
                + playlist.getPlaylistID() + " and songId = " + song.getSongID() + ";";
        try (Connection con = dbConnect.getConnection())
        {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
            {
                int trackNumber = rs.getInt("trackNumber");
                return trackNumber;
            }
        }
        return null;
    }

    /**
     * Changes the order in which the playlist plays its songs, it does this by
     * changing the track numbers
     *
     * @param spotsOfMomvement how much the song should move up or down the
     * playlist, should only move up or down by one spot at a time
     * @param playlist the playlist on which you wish to change the order
     * @param song the song that you wish to move op or down
     */
    public void ChangePlaylistOrder(Integer spotsOfMomvement, Playlist playlist, Song song) throws SQLException
    {
        int trackNumber = getSongOnPlaylistTrackNumber(playlist, song);
        String sql = "UPDATE PlaylistSong SET trackNumber = trackNumber +"
                + spotsOfMomvement + " WHERE songId =" + song.getSongID()
                + " AND playlistId =" + playlist.getPlaylistID() + ";";
        String sqlDown = "UPDATE PlaylistSong SET trackNumber = trackNumber -1 "
                + "WHERE trackNumber =" + (trackNumber + 1) + " AND playlistId = " + playlist.getPlaylistID() + ";";
        String sqlUp = "UPDATE PlaylistSong SET trackNumber = trackNumber +1 "
                + "WHERE trackNumber =" + (trackNumber - 1) + " AND playlistId = " + playlist.getPlaylistID() + ";";
        try (Connection con = dbConnect.getConnection())
        {
            Statement statement = con.createStatement();
            if (spotsOfMomvement == -1)
            {
                if (trackNumber - 1 == 0)
                {
                    System.out.println("cant go op more");
                } else
                {
                    statement.execute(sqlUp + sql);
                }
            } else if (spotsOfMomvement == +1)
            {
                if (trackNumber + 1 > getAllSongsOnPlaylist(playlist).size())
                {
                    System.out.println("cant go down more");
                } else
                {
                    statement.execute(sqlDown + sql);
                }
            } else
            {
                System.out.println("read the fucking javadoc ye cunt");
            }

        }
    }

    /**
     * Lets you change the name of a playlist
     *
     * @param newName the new name of the playlist
     * @param playlist the playlist the that you wish to change the name of
     * @throws SQLServerException
     */
    public void editPlaylist(String newName, Playlist playlist) throws SQLServerException
    {
        String sql = "UPDATE Playlist SET [name] = '" + newName + "' WHERE id=" + playlist.getPlaylistID() + ";";

        try (Connection con = dbConnect.getConnection())
        {
            Statement statement = con.createStatement();
            statement.execute(sql);

        } catch (SQLException ex)
        {
            Logger.getLogger(PlaylistDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
