/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.dal.SongDAO;

/**
 * FXML Controller class
 *
 * @author Richart hansen
 */
public class FXMLDocumentController implements Initializable
{

    private Song song;
    private SongDAO SongDAO = new SongDAO();
    private MyTunesModel model;
    @FXML
    private Label headlinelbl;
    @FXML
    private TextField filtertxt;
    @FXML
    private ListView<Playlist> plview;
    @FXML
    private ListView<Song> sopview;
    @FXML
    private ListView<Song> songsview;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            this.model = new MyTunesModel();
        } catch (SQLException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            songsview.setItems(model.getSongs());
        } catch (SQLException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML

    private void filtersearch(ActionEvent event) {
       String searchWord = filtertxt.getText();
       
        try
        {
            songsview.setItems(model.getFoundedSong(searchWord));
            
        } catch (SQLException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    

    @FXML
    private void handleplaylistedit(ActionEvent event)
    {

    }

    @FXML
    private void handleplaylistnew(ActionEvent event)
    {
    }

    @FXML
    private void handleaddtoplaylist(ActionEvent event)
    {
    }

    @FXML
    private void handleplaylistdelete(ActionEvent event)
    {
    }

    @FXML
    private void handlesopmoveup(ActionEvent event)
    {
    }

    @FXML
    private void handlesopmovedown(ActionEvent event)
    {
    }

    @FXML
    private void handlesopdelete(ActionEvent event)
    {
    }

    @FXML
    private void handleclose(ActionEvent event)
    {
        System.exit(0);
    }

    @FXML
    private void handlesongsdelete(ActionEvent event)
    {
     Song ds = songsview.getSelectionModel().getSelectedItem();
     model.deleteSong(ds);

    }

    @FXML
    private void handlesongsedit(ActionEvent event)
    {
        Parent root;
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mytunes/gui/EditSongWindow.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Edit song");
            stage.setScene(new Scene(root,400,300));
            stage.show();
            
            EditSongWindowController editSongWindowController = loader.getController();
            model.setSelectedSong(songsview.getSelectionModel().getSelectedItem());
            editSongWindowController.setModel(model);
        } catch (IOException ex)
        {
            //nothing
        }
    }


    @FXML
    private void handlesongsnew(ActionEvent event)
    {
        Parent root;
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mytunes/gui/CreateSongWindow.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add a new song");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();

            CreateSongWindowController createSongWindowController = loader.getController();
            createSongWindowController.setModel(model);

        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }


        @FXML
        private void handlePlaySong
        (ActionEvent event) throws SQLException
        {
            if (model.getCurPlaySong().isEmpty()
                    || model.getCurPlaySong() != songsview.getSelectionModel().getSelectedItem().getFilePath())
            {
                model.StopSong();
                model.setSelectedSong(songsview.getSelectionModel().getSelectedItem());
                model.PlaySong();
            } else
            {
                model.PausePlaySong();
            }
        }

    }
