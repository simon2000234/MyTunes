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

     @FXML
    private void btncn(ActionEvent event) throws SQLException
    {
     model.editPlaylist(txtchangeplaylistname.getText(), model.getSelectedPlaylist());

    }
    void setModel(MyTunesModel model)
    {
        this.model = model;
    }

    
}
