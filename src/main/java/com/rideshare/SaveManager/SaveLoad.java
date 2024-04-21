package com.rideshare.SaveManager;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import javafx.stage.FileChooser;

import com.rideshare.ScoreKeeper;
import com.rideshare.Player;
import com.rideshare.GameManager.Sprite;

import javafx.scene.chart.PieChart.Data;

public class SaveLoad {

    ScoreKeeper scoreKeeper;
    Player player;
    Sprite sprite;

    public SaveLoad(ScoreKeeper scoreKeeper, Player player, Sprite sprite) {
        this.scoreKeeper = scoreKeeper;
        this.player = player;
        this.sprite = sprite;
    }

    public void save(String fileName) {
        try {
            // create a file outputstream wrapped in bufferedoutputstream, allows us to
            // append the data instead of overwriting it
            DataStorage ds = new DataStorage();
            ds.name = player.getPlayerName();
            ds.spriteName = sprite.getSpriteName();
            ds.score = scoreKeeper.calculateScore();
            ds.mailboxesCompleted = scoreKeeper.getMailboxesCompleted();
            ds.level = scoreKeeper.getLevel();
            ds.CO2Saved = scoreKeeper.getCO2Saved();
            ds.CO2Used = scoreKeeper.getCO2Used();
            ds.totalMailboxes = scoreKeeper.getTotalMailboxes();
            OutputStream outputStream = new FileOutputStream(fileName, true);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            ObjectOutputStream oos = new ObjectOutputStream(bufferedOutputStream);
            oos.writeObject(ds);
            System.out.println("Game state appended to: " + fileName);
            oos.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failure to append game state to: " + fileName);
            e.printStackTrace();
        }
    }

    public void load() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");

        File initialDirectory = new File("/game_data");
        fileChooser.setInitialDirectory(initialDirectory);

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Data Files (*.dat)", "*.dat");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String fileName = selectedFile.getAbsolutePath();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile))) {
                DataStorage ds = (DataStorage) ois.readObject();

                player.setPlayerName(ds.name);
                sprite.setSpriteName(ds.spriteName);
                scoreKeeper.setScore(ds.score);
                scoreKeeper.setMailboxesCompleted(ds.mailboxesCompleted);
                scoreKeeper.setLevel(ds.level);
                scoreKeeper.setCO2Saved(ds.CO2Saved);
                scoreKeeper.setCO2Used(ds.CO2Used);
                scoreKeeper.setTotalMailboxes(ds.totalMailboxes);
            } catch (Exception e) {
                System.out.println("Something went wrong while loading the game state from file: " + fileName);
                e.printStackTrace();
            }
        }
    }

}
