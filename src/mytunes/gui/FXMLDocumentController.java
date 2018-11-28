/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import mytunes.be.Playlist;
import mytunes.be.Song;

/**
 * FXML Controller class
 *
 * @author Richart hansen
 */
public class FXMLDocumentController implements Initializable
{

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
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        songsview.setItems(model.getSongs());
    }

    @FXML
    private void filtersearch(ActionEvent event)
    {
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
    }

    @FXML
    private void handlesongsdelete(ActionEvent event)
    {
    }

    @FXML
    private void handlesongsedit(ActionEvent event)
    {
    }

    @FXML
    private void handlesongsnew(ActionEvent event)
    {
    }

    @FXML
    private void handlePlaySong(ActionEvent event) throws SQLException
    {
        model.StopSong();
        model.setSelectedSong(songsview.getSelectionModel().getSelectedItem());
        model.PlaySong();

    }

}
