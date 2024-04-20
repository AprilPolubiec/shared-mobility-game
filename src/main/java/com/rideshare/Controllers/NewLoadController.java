
package com.rideshare.Controllers;

import java.io.File;
import java.io.IOException;

import com.rideshare.App;
import com.rideshare.SaveManager.SaveLoad;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class NewLoadController {

    @FXML
    public javafx.scene.control.Button loadGameButton;
    public javafx.scene.control.Button newGameButton;
    
    private AnchorPane _root;
    private Stage _stage;
    private SaveLoad saveLoad; // Assuming you intended to use SaveLoad here

    private void setScene(boolean isFullScreen) throws IOException {
        Scene scene = new Scene(_root);
        _stage.setScene(scene);
        _stage.setFullScreen(isFullScreen);
    }


    void load(AnchorPane root, Stage stage) {
        try {
            _stage = stage;
            _root = root;
   
            setScene(true);
      
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLoadButtonPressed(ActionEvent event) {
        //
    }

    @FXML
    public void handleNewGameButtonPressed(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("chooseCharacter.fxml"));
            AnchorPane root = loader.load();
            ChooseCharacterController controller = loader.getController();
            controller.load(root, _stage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}