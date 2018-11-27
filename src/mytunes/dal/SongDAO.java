/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import mytunes.be.Song;

/**
 *
 * @author Melchertsen
 */
public class SongDAO
{

    public Song createSong(String title, String artist, int time, String category)
    {
        String sql = "INSERT INTO Song(title, artist, time, category) VALUES(?,?,?,?);";
        
//        try(Connection con = WaitingForConnectionClass!)
//        {
//            PreparedStatement st = con.prepareStatement(sql, Statement)
//        }
//      
        return null;
    }
        
}
