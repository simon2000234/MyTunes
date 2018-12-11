/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.bll.MTManager;
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
    private boolean isLastClickedSongview;

    private boolean isPaused;
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
    @FXML
    private Slider volumeSlider;

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
        try
        {
            plview.setItems(model.getAllPlaylists());
        } catch (SQLException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        songsview.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                model.setSelectedSong(songsview.getSelectionModel().getSelectedItem());
                model.setListviewtest("songsview");
                isLastClickedSongview = true;
                System.out.println("" + model.getListviewtest());
            }
        });
        sopview.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                model.setSelectedSong(sopview.getSelectionModel().getSelectedItem());
                model.setListviewtest("sopview");
                isLastClickedSongview = false;
                System.out.println("" + model.getListviewtest());
            }
        });

        headlinelbl.textProperty().bind(model.getCurPlaySongString());

    }

    @FXML

    private void filtersearch(ActionEvent event)
    {
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
        Parent root;
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mytunes/gui/EditPlayList.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Edit Playlist");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();

            EditPlayListController editPlayListController = loader.getController();
            editPlayListController.setModel(model);
        } catch (IOException ex)
        {
            //nothing
        }
    }

    @FXML
    private void handleplaylistnew(ActionEvent event)
    {
        Parent root;
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mytunes/gui/createPlayList.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Create Playlist");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();

            CreatePlayListController CreatePlayListController = loader.getController();
            CreatePlayListController.setModel(model);
        } catch (IOException ex)
        {
            //nothing
        }
    }

    @FXML
    private void handleaddtoplaylist(ActionEvent event)
    {
        try
        {
            model.addSongToPlaylist(model.getSelectedSong(), model.getSelectedPlaylist());
            sopview.setItems(model.updateSopview());
        } catch (SQLException ex)
        {
            //nothing
        }
    }

    public void WhenSongDone(Song curSong, Playlist curPlaylist)
    {
        model.getMediaPlayer().setOnEndOfMedia(new Runnable()
        {
            @Override
            public void run()
            {
                model.playNextSong(curSong, curPlaylist);
                WhenSongDone(model.getCurPlaySong(), model.getSelectedPlaylist());
                model.getCurPlaySongString().setValue("Currently playing: " + model.getCurPlaySong().getTitle());

            }
        });
    }

    @FXML
    private void handleplaylistdelete(ActionEvent event)
    {
        Alert confirmPLDelete = new Alert(AlertType.CONFIRMATION, "Delete: "
                + model.getSelectedPlaylist().getPlaylistName() + "?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        confirmPLDelete.setTitle("Delete playlist");
        confirmPLDelete.setHeaderText("Are you sure?");
        confirmPLDelete.showAndWait();

        if (confirmPLDelete.getResult() == ButtonType.YES)
        {
            try
            {
                model.deleteplaylist(model.getSelectedPlaylist());
            } catch (SQLException ex)
            {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void handlesopmoveup(ActionEvent event)
    {
        try
        {
            model.ChangePlaylistOrder(-1, plview.getSelectionModel().getSelectedItem(), model.getSelectedSong());
            sopview.setItems(model.updateSopview());
        } catch (SQLException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handlesopmovedown(ActionEvent event)
    {
        try
        {
            model.ChangePlaylistOrder(+1, plview.getSelectionModel().getSelectedItem(), model.getSelectedSong());
            sopview.setItems(model.updateSopview());
        } catch (SQLException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handlesopdelete(ActionEvent event) throws SQLException
    {
        Alert confirmSOPDelete = new Alert(AlertType.CONFIRMATION, "Delete: "
                + model.getSelectedSong().getTitle() + "\n"
                + "From: " + model.getSelectedPlaylist().getPlaylistName() + "?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        confirmSOPDelete.setHeaderText("Are you sure?");
        confirmSOPDelete.setTitle("Delete song from playlist");
        confirmSOPDelete.showAndWait();
        if (confirmSOPDelete.getResult() == ButtonType.YES)
        {

            model.removeSong(model.getSelectedPlaylist(), model.getSelectedSong());
            try
            {
                sopview.setItems(model.updateSopview());
            } catch (SQLException ex)
            {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void handleclose(ActionEvent event)
    {
        System.exit(0);
    }

    @FXML
    private void handlesongsdelete(ActionEvent event)
    {
        Alert confirmDelete = new Alert(AlertType.CONFIRMATION, "Delete: " + model.getSelectedSong().getTitle() + "?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        confirmDelete.setTitle("Delete Song");
        confirmDelete.setHeaderText("Are you sure?");
        confirmDelete.showAndWait();

        if (confirmDelete.getResult() == ButtonType.YES)
        {
            Song ds = songsview.getSelectionModel().getSelectedItem();
            model.deleteSong(ds);

        }
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
            stage.setScene(new Scene(root, 400, 300));
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
    private void handlePlaySong(ActionEvent event) throws SQLException
    {
        if (model.getCurPlaySong() == null || (model.getSelectedSong().getSongID() != -1 && model.getCurPlaySong() != model.getSelectedSong()))
        {
            model.setVolumeSlider(volumeSlider);
            model.StopSong();
            model.PlaySong();
            isPaused = true;
            //headlinelbl.setText("Currently playing: " + model.getSelectedSong().getTitle());
            //model.playNextSong(model.getCurPlaySong(), plview.getSelectionModel().getSelectedItem());
            if (isLastClickedSongview == false)
            {
                WhenSongDone(model.getCurPlaySong(), plview.getSelectionModel().getSelectedItem());
            } else if (isLastClickedSongview == true)
            {
                WhenSongDone(model.getCurPlaySong(), new Playlist(41, "hiddenPL"));
            }
            model.setSelectedSong(new Song(-1, "empty", null, 0, null, null));

        } else
        {
            model.PausePlaySong();
            if (isPaused)
            {
                model.getCurPlaySongString().setValue("Currently playing: " + model.getCurPlaySong().getTitle() + "(Paused)");
                isPaused = false;
            } else
            {
                model.getCurPlaySongString().setValue("Currently playing: " + model.getCurPlaySong().getTitle());
                isPaused = true;
            }
        }
    }

    @FXML
    private void handleFiltertxtSearch(ActionEvent event)
    {
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
    private void handleClickOnPlaylist(MouseEvent event)
    {
        try
        {
            Playlist playlist = plview.getSelectionModel().getSelectedItem();
            sopview.setItems(model.getSongsOnPl(playlist));
            model.setSelectedPlaylist(playlist);
            isLastClickedSongview = false;
        } catch (SQLException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
