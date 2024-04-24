package com.rideshare.SaveManager;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import com.rideshare.GameManager.Player;
import com.rideshare.GameManager.ScoreKeeper;
import com.rideshare.GameManager.Sprite;
import com.rideshare.UI.UIComponentUtils;

public class SaveLoad {
    private final String GAME_DATA_DIR = "game_data/";
    ObjectProperty<Player> player = new SimpleObjectProperty<>();

    public SaveLoad() {
        try {
            if (!Files.exists(Paths.get(GAME_DATA_DIR))) {
                Files.createDirectories(Paths.get(GAME_DATA_DIR));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPlayerSelected(ChangeListener<? super Player> listener) {
        this.player.addListener(listener);
    }

    public Player getPlayer() {
        return this.player.get();
    }

    public void save(Player player) {
        try {
            int currentLevel = player.getScoreKeeper().getLevel() - 1; // TODO: this is very very bad! right now,
                                                                       // Game.java updates the score before calling
                                                                       // this which is why we decrement it here - its
                                                                       // too much of an assumption that the scorekeeper
                                                                       // will be +1 of the actual level - needs to be
                                                                       // fixed in the future
            String playerName = player.getPlayerName();
            Path playerDirectory = Paths.get(String.format("game_data/%s", playerName));
            if (!Files.exists(playerDirectory)) {
                Files.createDirectories(playerDirectory);
            }

            String fileName = String.format("game_data/%s/%s.dat", playerName, currentLevel);
            // For each level, we want to know:
            DataStorage ds = new DataStorage();
            ds.spriteName = player.getAvatarName(); // TODO: not ideal to save this here
            ds.score = player.getScoreKeeper().getFinalLevelScore();
            saveFile(ds, fileName);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void saveFile(DataStorage ds, String fileName) {
        try {
            OutputStream outputStream = new FileOutputStream(fileName, false);
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

    public List<String> getExistingGames() {
        List<String> players = new ArrayList<String>();
        Path directory = Paths.get(GAME_DATA_DIR);
        try {
            players = Files.list(directory)
                    .filter(Files::isDirectory)
                    .map(Path::getFileName)
                    .map(Path::toString).collect(Collectors.toList());
            return players;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    public void renderExistingGames(AnchorPane root) {
        ScrollPane gameList = new ScrollPane();
        gameList.setPrefHeight(300);
        gameList.setPrefWidth(302);
        AnchorPane.setTopAnchor(gameList, (UIComponentUtils.getStageHeight(root) / 2) - 150);
        AnchorPane.setLeftAnchor(gameList, (UIComponentUtils.getStageWidth(root) / 2) - 150);
        List<String> existingPlayers = getExistingGames();
        VBox playerStack = new VBox();
        for (String playerName : existingPlayers) {
            // Create a panel with the player name
            AnchorPane playerOption = new AnchorPane();
            ImageView panel = UIComponentUtils.getPanel("red");
            panel.setFitWidth(300);
            panel.setFitHeight(100);

            Text playerNameText = new Text(playerName);
            playerNameText.setFont(new Font("Futura Bold", 14));
            playerNameText.setFill(Color.WHITE);
            AnchorPane.setTopAnchor(playerNameText, 40.0);
            AnchorPane.setLeftAnchor(playerNameText, 150 - (playerNameText.getLayoutBounds().getWidth() / 2));

            playerOption.getChildren().addAll(panel, playerNameText);
            UIComponentUtils.addHoverCursor(playerOption, true);
            playerOption.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    // Load player
                    player.set(loadPlayer(playerName));
                }
            });

            playerStack.getChildren().add(playerOption);
        }
        gameList.setContent(playerStack);
        root.getChildren().add(gameList);
    }

    // Loads the most recent level of the player
    public Player loadPlayer(String playerName) {
        String directoryString = String.format("%s/%s", GAME_DATA_DIR, playerName);
        Path directory = Paths.get(directoryString);
        try {
            List<Integer> savedLevels = Files.list(directory)
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(fileName -> fileName.replace(".dat", ""))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            Integer lastLevelCompleted = savedLevels
                    .stream()
                    .mapToInt(v -> v)
                    .max().orElseThrow(NoSuchElementException::new);

            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(String.format("%s/%s.dat", directoryString, lastLevelCompleted)));
            DataStorage ds = (DataStorage) ois.readObject();

            Player loadedPlayer = new Player(playerName, ds.spriteName);
            loadedPlayer.getScoreKeeper().setLevel(lastLevelCompleted + 1); // Start on the next level
            loadedPlayer.getScoreKeeper().setPlayerName(playerName);
            return loadedPlayer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
