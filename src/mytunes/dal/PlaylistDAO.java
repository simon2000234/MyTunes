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
     *
     * @param name
     * @return
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
            st.setInt(3, getNextTackNumber(playlist));

            st.executeUpdate();
        }
    }

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

    public void removeSong(Playlist playlist, Song song) throws SQLException
    {
        int trackNumber = getSongOnPlaylistTackNumber(playlist, song);
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
                allPlaylists.add(playlist);
            }
            return allPlaylists;
        }
    }

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

    public ArrayList<Integer> getTackNumbers(Playlist playlist) throws SQLException
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

    public Integer getNextTackNumber(Playlist playlist) throws SQLException
    {
        List<Integer> trackNumbers = getTackNumbers(playlist);
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
                if (trackNumber >= getTackNumbers(playlist).size())
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

    public Integer getSongOnPlaylistTackNumber(Playlist playlist, Song song) throws SQLException
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
     *
     * @param spotsOfMomvement how much the song should move up or down the
     * playlist should only move up or down by one spot at a time
     * @param playlist
     * @param song
     */
    public void ChangePlaylistOrder(Integer spotsOfMomvement, Playlist playlist, Song song) throws SQLException
    {
        int trackNumber = getSongOnPlaylistTackNumber(playlist, song);
        String sql = "UPDATE PlaylistSong SET trackNumber = trackNumber +"
                + spotsOfMomvement + " WHERE songId =" + song.getSongID()
                + " AND playlistId =" + playlist.getPlaylistID() + ";";
        String sqlDown = "UPDATE PlaylistSong SET trackNumber = trackNumber -1 "
                + "WHERE trackNumber =" + (trackNumber +1) + " AND playlistId = " + playlist.getPlaylistID() + ";";
        String sqlUp = "UPDATE PlaylistSong SET trackNumber = trackNumber +1 "
                + "WHERE trackNumber =" + (trackNumber -1) + " AND playlistId = " + playlist.getPlaylistID() + ";";
        try (Connection con = dbConnect.getConnection())
        {
            Statement statement = con.createStatement();
            if (spotsOfMomvement == -1)
            {
                if (trackNumber -1 == 0)
                {
                    System.out.println("fuck off");
                }
                else
                {
                statement.execute(sqlUp + sql);
                }
            } else if (spotsOfMomvement == +1)
            {
                if(trackNumber + 1 > getAllSongsOnPlaylist(playlist).size())
                {
                    System.out.println("fuck off");
                }
                else
                {
                    statement.execute(sqlDown + sql);
                }
            } else
            {
                System.out.println("fuck off");
            }

        }
    }

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
