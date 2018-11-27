/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.SQLException;
import mytunes.be.Song;

/**
 *
 * @author Caspe
 */
public class FileTester
{
    public static void main(String[] args) throws IOException, SQLServerException, SQLException
    {
        SongDAO songDAO = new SongDAO();
//        songDAO.createSong("TestSong", "TestArtist", 150, "TestType");
//        songDAO.deleteSong(new Song(2, "", "", 3, ""));
//        songDAO.updateSong(new Song(3,"TESTUPDATE","ARTISTUPDATE",999,"UPDATED"));
//        for (Song song : songDAO.getAllSongs())
//        {
//            System.out.println(""+song.toString());
//        }
        
    }
    
}
