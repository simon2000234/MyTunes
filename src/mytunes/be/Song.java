/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

import com.mpatric.mp3agic.Mp3File;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Melchertsen
 */
public class Song {

    private int songID;
    private String title;
    private String artist;
    private int time;
    private String category;
    private String filePath;

    public Song(int songID, String title, String artist, int time, String category, String filePath) {
        this.songID = songID;
        this.title = title;
        this.artist = artist;
        this.time = time;
        this.category = category;
        this.filePath = filePath;

    }

    public int getSongID() {
        return songID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String displayTime() {
        int tempTime = 0;
        int tempMinutes = 0;
        tempTime = time;
        while (tempTime >= 60) {
                tempTime = tempTime - 60;
                tempMinutes++;
 
        }
        String displayTime = tempMinutes+":"+tempTime;
        return displayTime;

    }

    @Override
    public String toString() {
        return "Song Title: " + title + "\n"
                + "Artist: " + artist + "\n"
                + "Song Duration: " + displayTime() + "\n"
                + "Genre: " + category;
    }

    public void playSong() {
        String bip = this.filePath;
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }

}
