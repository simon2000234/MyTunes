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
import javafx.animation.Animation;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static javafx.scene.media.MediaPlayer.Status.PAUSED;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.dal.PlaylistDAO;
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
    private boolean isSongPlaying = false;
    private String curPlaySong = "";
    private PlaylistDAO pldao = new PlaylistDAO();

    /**
     * creating the given song in the database s stand for song
     *
     * @param song
     */
    public Song CreateSong(String title, String artist, int time, String category, String filePath)
    {
        try
        {
            Song song;
            song = SongDAO.createSong(title, artist, time, category, filePath);
            return song;
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MTManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; // there was a error 
    }

    /**
     * delete a given songe from the database s stand for song
     *
     * @param song
     */
    public void DeleteSong(Song s)
    {
        SongDAO.deleteSong(s);
    }

    /**
     * s stand for song this method update a song
     *
     * @param s
     */
    public void updateSong(Song s)
    {
        SongDAO.updateSong(s);
    }

    /**
     * get all songs form the database
     *
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
     *
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
     * playning the given song.
     *
     * @param s is the song
     */
    public void PlaySong(Song s)
    {
        String bip = s.getFilePath();
        Media hit = new Media(new File(bip).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
        isSongPlaying = true;
        curPlaySong = bip;
    }

    public String getCurPlaySong()
    {
        return curPlaySong;
    }

    public void StopSong()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
        }
    }

    public void PausePlaySong()
    {

        if (mediaPlayer != null)
        {
            if (isSongPlaying)
            {
                isSongPlaying = false;
                mediaPlayer.pause();
            } else
            {
                isSongPlaying = true;
                mediaPlayer.play();
            }

        }
    }

    public Playlist createPlaylist(String name) throws SQLException
    {
        return pldao.createPlaylist(name);
    }

    public void addSongToPlaylist(Song song, Playlist playlist) throws SQLException
    {
        pldao.addSongToPlaylist(song, playlist);
    }

    public List<Song> getAllSongsOnPlaylist(Playlist playlist) throws SQLException
    {
        return pldao.getAllSongsOnPlaylist(playlist);
    }

    public void removeSong(Playlist playlist, Song song)
    {
        pldao.removeSong(playlist, song);
    }

    public List<Playlist> getAllPlaylists() throws SQLException
    {
        return pldao.getAllPlaylists();
    }

    public ArrayList<Song> searchSong(String searchWord) throws SQLException
    {
        ArrayList<Song> foundedSong = SongDAO.SearchSong(searchWord);
        return foundedSong;
    }
    

     public void deletePlayList(int id) throws SQLException{
           pldao.deletePlayList(id);
         
     }
    

    public Playlist getPlaylist(int playlistId) throws SQLException
    {
        return pldao.getPlaylist(playlistId);
    }
    
    public void playNextSong(Song song)
    {
        mediaPlayer.setOnEndOfMedia(new Runnable() {
        @Override public void run() {
        String bip = song.getFilePath();
        Media hit = new Media(new File(bip).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
        curPlaySong = bip;
        }
      });
    }

}
