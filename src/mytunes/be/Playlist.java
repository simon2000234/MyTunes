/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

import java.util.ArrayList;

/**
 *
 * @author Caspe
 */
public class Playlist
{

    private int playlistID;
    private String playlistName;

    public Playlist(int playlistID, String playlistName)
    {
        this.playlistID = playlistID;
        this.playlistName = playlistName;
    }

    public int getPlaylistID()
    {
        return playlistID;
    }

    public void setPlaylistID(int playlistID)
    {
        this.playlistID = playlistID;
    }

    public String getPlaylistName()
    {
        return playlistName;
    }

    public void setPlaylistName(String playlistName)
    {
        this.playlistName = playlistName;
    }

    public int songCount()
    {
        return -1;
    }

    @Override
    public String toString()
    {
        return "Playlist: " + playlistName;
    }

}
