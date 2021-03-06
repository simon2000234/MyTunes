/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.bll.MTManager;

/**
 *
 * @author Melchertsen
 */
public class MyTunesModel
{
    
    private MTManager mtm;
    private ObservableList<Song> songs; // List of songs for the main song view
    private ObservableList<Song> songsOnPlayList;
    private Song selectedSong; //Contains the last selected song
    private Playlist selectedPlaylist; //Contains the last selected playlist
    private SimpleStringProperty curPlaySongString;
    private ObservableList<Playlist> playlists;

    /**
     *
     * @return the last selected playlist
     */
    public Playlist getSelectedPlaylist()
    {
        return selectedPlaylist;
    }

    /**
     * Gets the volume slider
     * @return the volume slider
     */
    public Slider getVolumeSlider()
    {
        return mtm.getVolumeSlider();
    }

    /**
     * Gets the currently playing song as a string (its title)
     * @return the currently playing song as a string (its title)
     */
    public SimpleStringProperty getCurPlaySongString()
    {
        return curPlaySongString;
    }
    
    /**
     * Allows you to set the volume slider
     * @param volumeSlider the volume slider that you wish to set
     */
    public void setVolumeSlider(Slider volumeSlider)
    {
        mtm.setVolumeSlider(volumeSlider);
    }

    /**
     * Set the last selected playlist to the given parameter
     *
     * @param selectedPlaylist
     */
    public void setSelectedPlaylist(Playlist selectedPlaylist)
    {
        this.selectedPlaylist = selectedPlaylist;
    }

    /**
     *
     * @return last selected song
     */
    public Song getSelectedSong()
    {
        return selectedSong;
    }

    /**
     * Sets the last selected song to the given parameter.
     *
     * @param selectedSong
     */
    public void setSelectedSong(Song selectedSong)
    {
        this.selectedSong = selectedSong;
    }
    
    public MyTunesModel() throws SQLException
    {
        this.mtm = new MTManager();
        this.songs = FXCollections.observableArrayList();
        songs.addAll(mtm.getAllSong());
        this.playlists = FXCollections.observableArrayList();
        playlists.addAll(mtm.getAllPlaylists());
        this.songsOnPlayList = FXCollections.observableArrayList();
        curPlaySongString = new SimpleStringProperty();
        curPlaySongString.set("");
        
    }

    /**
     *
     * @return an ObservableList of all songs in the database
     * @throws SQLException
     */
    public ObservableList<Song> getSongs() throws SQLException
    {
        return songs;
    }

    /**
     * Starts playing the last selected song
     */
    public void PlaySong()
    {
        mtm.PlaySong(selectedSong);
        curPlaySongString.setValue("Currently playing: " + selectedSong.getTitle());
    }

    /**
     * Stops the song from playing
     */
    public void StopSong()
    {
        mtm.StopSong();
    }

    /**
     * Puts the currently playing song on pause
     */
    public void PausePlaySong()
    {
        mtm.PausePlaySong();
    }

    /**
     *
     * @return the song that is currently playing
     */
    public Song getCurPlaySong()
    {
        return mtm.getCurPlaySong();
    }

    /**
     * Creates a song with the given parameters and adds it to the playlist,
     * then adds the newly created song to the main songview.
     *
     * @param title
     * @param artist
     * @param time
     * @param category
     * @param filePath
     */
    public void createSong(String title, String artist, int time, String category, String filePath)
    {
        try
        {
            Song newsong = mtm.CreateSong(title, artist, time, category, filePath);
            songs.add(newsong);
            addSongToPlaylist(newsong, new Playlist(41, "hiddenPL"));
        } catch (SQLException ex)
        {
            Logger.getLogger(MyTunesModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Updates a song in the database and updates the view by removing the
     * outdated song and adding the updated song.
     *
     * @param updatedSong
     * @param oldSong
     */
    public void updateSong(Song updatedSong, Song oldSong)
    {
        mtm.updateSong(updatedSong);
        songs.remove(oldSong);
        songs.add(updatedSong);
                
    }

    /**
     * Deletes a song from the database with the given parameter's songID
     * and removes it from the observable list
     * @param ds the song that you want to delete
     */
    public void deleteSong(Song ds)
    {
        songs.remove(ds);
        mtm.DeleteSong(ds);
        try
        {
            removeSong(new Playlist(41, "hiddenPL"), ds);
        } catch (SQLException ex)
        {
            Logger.getLogger(MyTunesModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Searches the song database for a song/songs with a title or artist that
     * contains the given parameter.
     *
     * @param searchWord
     * @return an ObservableList of the matching songs
     * @throws SQLException
     */
    public ObservableList<Song> getFoundedSong(String searchWord) throws SQLException
    {
        ObservableList<Song> searchSong;
        searchSong = FXCollections.observableArrayList();
        searchSong.addAll(mtm.searchSong(searchWord));
        return searchSong;
    }

    /**
     * Creates a playlist with the given parameters as its name
     *
     * @param name
     * @return the created playlist
     * @throws SQLException
     */
    public Playlist createPlaylist(String name) throws SQLException
    {
        Playlist playlist = mtm.createPlaylist(name);
        playlists.add(playlist);
        return playlist;
    }

    /**
     * Adds the given song to the given playlist
     *
     * @param song
     * @param playlist
     * @throws SQLException
     */
    public void addSongToPlaylist(Song song, Playlist playlist) throws SQLException
    {
        mtm.addSongToPlaylist(song, playlist);
        System.out.println("" + song + "," + playlist);
    }

    /**
     *
     * @param playlist
     * @return a list with all the songs on the given playlist
     * @throws SQLException
     */
    public List<Song> getAllSongsOnPlaylist(Playlist playlist) throws SQLException
    {
        return mtm.getAllSongsOnPlaylist(playlist);
    }

    /**
     * removes the given song from the given playlist
     *
     * @param playlist
     * @param song
     */
    public void removeSong(Playlist playlist, Song song) throws SQLException
    {
        mtm.removeSong(playlist, song);
        
    }

    /**
     *
     * @return an ObservableList of all playlists
     */
    public ObservableList<Playlist> getAllPlaylists() throws SQLException
    {
        playlists.removeAll(playlists);
        playlists.addAll(mtm.getAllPlaylists());
        return playlists;
    }

    /**
     * Returns the playlist with the given playlistId
     *
     * @param playlistId
     * @return a playis with the id given
     * @throws SQLException
     */
    public Playlist getPlaylist(int playlistId) throws SQLException
    {
        return mtm.getPlaylist(playlistId);
    }
    
    
    {
    }

    /**
     * Gets the songs on the given playlist
     *
     * @param playlist
     * @return an ObservableList of the songs on the given playlist
     * @throws SQLException
     */
    public ObservableList<Song> getSongsOnPl(Playlist playlist) throws SQLException
    {
        ObservableList<Song> songsOnPl;
        songsOnPl = FXCollections.observableArrayList();
        songsOnPl.addAll(mtm.getAllSongsOnPlaylist(playlist));
        return songsOnPl;
    }

    /**
     * deletes the given playlist
     *
     * @param playlist
     * @throws SQLException
     */
    public void deleteplaylist(Playlist playlist) throws SQLException
    {
        playlists.remove(playlist);
        mtm.deletePlayList(selectedPlaylist.getPlaylistID());
    }

    /**
     * Updates the ListView Songs On Playlist
     *
     * @return a new observable list that can be set to the listview.
     * @throws SQLException
     */
    public ObservableList<Song> updateSopview() throws SQLException
    {
        ObservableList<Song> updatedsopview;
        updatedsopview = FXCollections.observableArrayList();
        updatedsopview.addAll(getAllSongsOnPlaylist(selectedPlaylist));
        
        return updatedsopview;
    }
    
    /**
     * Plays a song when the current song ends
     * @param curPlaylist the playlist that you are useing
     * @param curSong the song that is currently playing
     */
    public void playNextSong(Song curSong, Playlist curPlaylist)
    {
        mtm.playNextSong(curPlaylist, curSong);
        curPlaySongString.setValue("Currently playing: " + curSong.getTitle());
    }
    
    /**
     * gets the media player
     * @return the media player
     */
    public MediaPlayer getMediaPlayer()
    {
        return mtm.getMediaPlayer();
    }

    /**
     * Allows you to edit the name of a playlist
     * @param name the new name that you want for the playlist
     * @param playlist the playlist that you wish to edit
     * @throws SQLException
     */
    public void editPlaylist(String name, Playlist playlist) throws SQLException
    {
        mtm.editPlaylist(name, playlist);
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
        mtm.ChangePlaylistOrder(spotsOfMomvement, playlist, song);
    }
}
