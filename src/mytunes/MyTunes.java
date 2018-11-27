/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mytunes.be.Song;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;

/**
 *
 * @author Melchertsen
 */
public class MyTunes extends Application
{

    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("gui/FXMLDocument.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, TagException
    {
        //launch(args);
        File sourceFile = new File("Data/Blue_Dot_Sessions_-_04_-_Cupcake_Marshall.mp3");
        MP3File mp3file = new MP3File(sourceFile);
        if (mp3file.hasID3v2Tag())
        {
            AbstractID3v2 tag = mp3file.getID3v2Tag();
            int albumTitle = tag.getSize();
            System.out.println(albumTitle);
            return;
        }
        else 
        {
            System.out.println("fuck");
        }
    }

    public static void readID3() throws FileNotFoundException, IOException
    {
        
    }

}
