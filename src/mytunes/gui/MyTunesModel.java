/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui;

import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.be.Song;
import mytunes.bll.MTManager;

/**
 *
 * @author Melchertsen
 */
public class MyTunesModel {

    private MTManager mtm;
    private ObservableList<Song> songs;
    private Song selectedSong;

    public Song getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }

    public MyTunesModel() throws SQLException {
        this.mtm = new MTManager();
        this.songs = FXCollections.observableArrayList();
        songs.addAll(mtm.getAllSong());
    }

    public ObservableList<Song> getSongs() {
        return songs;
    }

    public void PlaySong() {
        mtm.PlaySong(selectedSong);
    }

    public void StopSong() {
        mtm.StopSong();
    }

    public void PausePlaySong() {
        mtm.PausePlaySong();
    }

    public String getCurPlaySong() {
        return mtm.getCurPlaySong();
    }

    public void createSong(String title, String artist, int time, String category, String filePath) {
        mtm.CreateSong(title, artist, time, category, filePath);
    }
}
