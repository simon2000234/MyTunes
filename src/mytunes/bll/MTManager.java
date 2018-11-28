/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import mytunes.be.Song;
import mytunes.dal.SongDAO;

/**
 *
 * @author Melchertsen
 */
public class MTManager
{
    private MediaPlayer mediaPlayer;
    private Song song;
    private SongDAO SongDAO = new SongDAO();

    /**
     * creating the given song in the database s stand for song
     *
     * @param song
     */
    public Song CreateSong(Song s)
    {
        try
        {
            Song song;
            song = SongDAO.createSong(s.getTitle(), s.getArtist(), s.getTime(), s.getCategory(),s.getFilePath());
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
    public void DeleteSong(Song s)
    {
        SongDAO.deleteSong(s);
    }

    /**
     * s stand for song
     * this method update a song
     * @param s
     */
    public void updateSong(Song s)
    {
        SongDAO.updateSong(s);
    }

    /**
     * get all songs form the database
     * @param s stand for song
     * @return ArrayList<Song> allsong
     * @throws SQLException
     */
    public ArrayList<Song> getAllSong() throws SQLException
    {
        ArrayList<Song> allSong = new ArrayList<>();
        return allSong = SongDAO.getAllSongs();

    }
/**
 * getting the song with the given id 
 * @param id
 * @return Song 
 * @throws SQLException 
 */
    public Song getSong(int id) throws SQLException
    {
        Song song;
        song = SongDAO.getSong(id);
        return song;
    }
     /**
      * playning the given song 
      * s stand for song
      * @param s 
      */
    public void PlaySong(Song s)
    {
        String bip = s.getFilePath();
        Media hit = new Media(new File(bip).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }
    
    public void StopSong()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
        }
    }

}
