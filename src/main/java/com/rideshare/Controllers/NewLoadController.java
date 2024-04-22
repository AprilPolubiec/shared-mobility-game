
package com.rideshare.Controllers;

import java.io.IOException;

import com.rideshare.App;
import com.rideshare.Player;
import com.rideshare.SaveManager.SaveLoad;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class NewLoadController {

    @FXML
    public javafx.scene.control.Button loadGameButton;
    public javafx.scene.control.Button newGameButton;

    private AnchorPane _root;
    private Stage _stage;

    private void setScene(boolean isFullScreen) throws IOException {
        Scene scene = this._stage.getScene();
        scene.setRoot(this._root);
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
        SaveLoad gameLoader = new SaveLoad();
        gameLoader.renderExistingGames(_root);
        gameLoader.onPlayerSelected(new ChangeListener<Player>() {
            @Override
            public void changed(ObservableValue<? extends Player> observable, Player oldValue, Player newValue) {
                // Player has been selected!
                try {
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("game.fxml"));
                    AnchorPane root = loader.load();
                    GameController controller = loader.getController();
                    controller.load(root, _stage);
                    controller.setPlayer(newValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
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