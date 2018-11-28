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
public class MyTunesModel
{
    private MTManager mtm;
    private ObservableList<Song> songs;

    public MyTunesModel() throws SQLException
    {
        this.mtm = new MTManager();
        this.songs = FXCollections.observableArrayList();
        songs.addAll(mtm.getAllSong());
    }
    
    public ObservableList<Song> getSongs()
    {
        return songs;
    }
    
}
