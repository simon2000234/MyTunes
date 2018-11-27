/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

/**
 *
 * @author Melchertsen
 */
public class Song
{

    private int songID;
    private String title;
    private String artist;
    private int time;
    private String category;
    private String fileLoc;

    public Song(int songID, String title, String artist, int time, String category)
    {
        this.songID = songID;
        this.title = title;
        this.artist = artist;
        this.time = time;
        this.category = category;
        this.fileLoc = "data/"+title+".mp3";

    }

    public int getSongID()
    {
        return songID;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public int getTime()
    {
        return time;
    }

    public void setTime(int time)
    {
        this.time = time;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getFileLoc()
    {
        return fileLoc;
    }

    public void setFileLoc(String fileLoc)
    {
        this.fileLoc = fileLoc;
    }

    @Override
    public String toString()
    {
        return "Name: " + title + "\t" + "Artist: " + artist + " [TEMPORARYtoString]";
    }

}
