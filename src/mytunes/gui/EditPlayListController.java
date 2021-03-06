/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Richart hansen
 */
public class EditPlayListController implements Initializable
{

    private MyTunesModel model;
    @FXML
    private TextField txtchangeplaylistname;
    @FXML
    private Button BTNcn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }
/**
 * this is where we can change the name of a playlist
 * @param event
 * @throws SQLException 
 */
    @FXML
    private void btncn(ActionEvent event) throws SQLException
    {
        model.editPlaylist(txtchangeplaylistname.getText(), model.getSelectedPlaylist());
        Stage stage = (Stage) BTNcn.getScene().getWindow();
        model.getAllPlaylists();
        stage.close();

    }

    void setModel(MyTunesModel model)
    {
        this.model = model;
    }

}
