/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mytunes.be.Song;
import mytunes.dal.SongDAO;

/**
 *
 * @author Melchertsen
 */
public class MTManager
{

    private Song song;
    private SongDAO SongDAO = new SongDAO();

    /**
     * creating the given song in the database
     * s stand for song
     * @param song
     */
    private Song CreateSong(Song s)
    {
        try
        {
            Song song;
            song = SongDAO.createSong(s.getTitle(), s.getArtist(), s.getTime(), s.getCategory());
            return song;
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MTManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; // there was a error 
    }

    /**
     * delete a given songe from the database
     * s stand for song
     * @param song
     */
    private void DeleteSong(Song s)
    {
        SongDAO.deleteSong(s);
    }

    /**
     * s stand for song
     * this method update a song
     * @param s
     */
    private void updateSong(Song s)
    {
        SongDAO.updateSong(s);
    }

}
