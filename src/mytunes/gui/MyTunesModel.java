/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui;

import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ObservableList<Song> songs;
    private Song selectedSong;
    private Playlist selectedPlaylist;
    
    public Playlist getSelectedPlaylist()
    {
        return selectedPlaylist;
    }
    
    public void setSelectedPlaylist(Playlist selectedPlaylist)
    {
        this.selectedPlaylist = selectedPlaylist;
    }
    private ObservableList<Playlist> playlists;
    
    public Song getSelectedSong()
    {
        return selectedSong;
    }
    
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
    }
    
    public ObservableList<Song> getSongs() throws SQLException
    {
        return songs;
    }
    
    public void PlaySong()
    {
        mtm.PlaySong(selectedSong);
    }
    
    public void StopSong()
    {
        mtm.StopSong();
    }
    
    public void PausePlaySong()
    {
        mtm.PausePlaySong();
    }
    
    public String getCurPlaySong()
    {
        return mtm.getCurPlaySong();
    }
    
    public void createSong(String title, String artist, int time, String category, String filePath)
    {
        Song newsong = mtm.CreateSong(title, artist, time, category, filePath);
        songs.add(newsong);
    }
    
    public void updateSong(Song updatedSong, Song oldSong)
    {
        mtm.updateSong(updatedSong);
        songs.remove(oldSong);
        songs.add(updatedSong);
        
    }

    public void deleteSong(Song ds)
    {
        songs.remove(ds);
        mtm.DeleteSong(ds);
    }
    
    public ObservableList<Song> getFoundedSong(String searchWord) throws SQLException
    {
        ObservableList<Song> searchSong;
        searchSong = FXCollections.observableArrayList();
        searchSong.addAll(mtm.searchSong(searchWord));
        return searchSong;
    }
    
    public Playlist createPlaylist(String name) throws SQLException
    {
        return mtm.createPlaylist(name);
    }
    
    public void addSongToPlaylist(Song song, Playlist playlist) throws SQLException
    {
        mtm.addSongToPlaylist(song, playlist);
    }
    
    public List<Song> getAllSongsOnPlaylist(Playlist playlist) throws SQLException
    {
        return mtm.getAllSongsOnPlaylist(playlist);
    }
    
    public void removeSong(Playlist playlist, Song song)
    {
        mtm.removeSong(playlist, song);
    }
    
    public ObservableList<Playlist> getAllPlaylists()
    {
        return playlists;
    }
    
    public void deleteplaylist (Playlist playlist) throws SQLException{
        mtm.deletePlayList(selectedPlaylist.getPlaylistID());
    }
    
}
