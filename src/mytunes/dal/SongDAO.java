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
import mytunes.be.Song;

/**
 *
 * @author Melchertsen
 */
public class SongDAO
{

    private DBConnectionProvider dbConnect;

    public SongDAO()
    {
        dbConnect = new DBConnectionProvider();
    }

    public Song createSong(String title, String artist, int time, String category) throws SQLServerException
    {
        String sql = "INSERT INTO Song(title, artist, time, category) VALUES(?,?,?,?);";

        try (Connection con = dbConnect.getConnection())
        {
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            st.setString(1, title);
            st.setString(2, artist);
            st.setInt(3, time);
            st.setString(4, category);

            int rowsAffected = st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            int id = 0;
            if (rs.next())
            {
                id = rs.getInt(1);
            }
            Song song = new Song(id, title, artist, time, category);
            return song;

        } catch (SQLException ex)
        {
            //nothing
        }
        return null;
    }

    public void deleteSong(Song song)
    {
        String sql = "DELETE FROM Song WHERE id=" + song.getSongID();

        try (Connection con = dbConnect.getConnection())
        {
            Statement statement = con.createStatement();
            statement.execute(sql);

        } catch (SQLException ex)
        {
            //nothing
        }
    }

    public void updateSong(Song song)
    {
        String sql = "UPDATE Song SET title = ?, artist = ?, time = ?, category = ? WHERE id = " + song.getSongID();

        try (Connection con = dbConnect.getConnection())
        {
            PreparedStatement st = con.prepareStatement(sql);

            st.setString(1, song.getTitle());
            st.setString(2, song.getArtist());
            st.setInt(3, song.getTime());
            st.setString(4, song.getCategory());

            st.executeUpdate();
            st.close();

        } catch (SQLException ex)
        {
            //nothing
        }
    }

    public ArrayList<Song> getAllSongs() throws SQLException
    {
        ArrayList<Song> allSongs = new ArrayList<>();

        try (Connection con = dbConnect.getConnection())
        {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Song");
            while (rs.next())
            {
                int id = rs.getInt("id");
                String title = rs.getNString("title");
                String artist = rs.getNString("artist");
                int time = rs.getInt("time");
                String category = rs.getNString("category");
                allSongs.add(new Song(id, title, artist, time, category));
            }
        } catch (SQLException ex)
        {
            //nothing
        }
        return allSongs;
    }

    public Song getSong(int songID) throws SQLException
    {
        Song theSong = new Song(0, "", "", 0, "");

        try (Connection con = dbConnect.getConnection())
        {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Song WHERE id=" + songID);
            while (rs.next())
            {
                int id = rs.getInt("id");
                String title = rs.getNString("title");
                String artist = rs.getNString("artist");
                int time = rs.getInt("time");
                String category = rs.getNString("category");
                theSong = new Song(id,title,artist,time,category);
                System.out.println(""+theSong.toString());
            }
            
        } catch (SQLException ex)
        {
            //nothing
        }
        return theSong;

    }

}
