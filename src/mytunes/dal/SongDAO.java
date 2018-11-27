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

}
