/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Richart hansen
 */
public class EditSongWindowController implements Initializable
{

    @FXML
    private TextField lblestitle;
    @FXML
    private TextField lbledcatagory;
    @FXML
    private TextField lbledartist;
    @FXML
    private Button handleedcancel;
    @FXML
    private Button handleedsave;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    
    
}
