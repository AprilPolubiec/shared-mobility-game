
package com.rideshare;

import java.util.List;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;

// to go over which I need or don't with April
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class GameOverPopup extends Popup {
    // attributes
    private Scene scene;
    private Stage stage;
    private Node root;
    private HighScores highscores;
    // is this high score one needed for our game? - I do believe it is
    // I do think I need a game over text or dialog also.. but willl wait for you to
    // cosign, or is the confirm one enough?
    private Popup popup;
    private Button confirm;
    // private String title;
    // private String text;
    // private String bodyText;
    // private List<String> buttons;
    // private String confirmButtonText;
    // private String cancelButtonText;
    // private String closeButtonText;

    // Constructor
    public GameOverPopup(Scene scene, Stage stage, Node root) {
        this.scene = scene;
        this.stage = stage;
        this.root = root;
        this.highscores = scene.getGameLogic().getHighScores(); // Assuming GameLogic has a method to get HighScores - again need to double check this one with you 
}

public static void gameOver() {
    int score = scene.getGameLogic().getScore(); // get score

    HighScores highscores = scene.getGameLogic().getHighScores();

    Logger.getInstance().writeLineToLogger("The game is over. The final score is " + score + ".");
    Logger.getInstance().disposeLogger();
    Popup popup = Popups.gameOverPopup(score);
    popup.show(stage);
    root.setDisable(true);
    scene.getGameLogic().setDisabled(true);
    Button confirm = (Button) popup.getContent().get(2);
    confirm.setOnAction(new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent e) {
            highscores.addHighScore(score);
            try {
                highscores.writeScoreFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            popup.hide();
            switchMenu();

            // This bottom one is chatgpt ...
// public GameOverPopup(String title, String text, List<String> buttons, String
// openButtonText,
// String closeButtonTitle,
// String closeButtonText) {
// super(title, text, buttons, openButtonText, closeButtonTitle,
// closeButtonText);
// }

// @Override
// public void onSubmit(String buttonClicked) {
// super.onSubmit(buttonClicked); //
// if (buttonClicked.equals("Restart")) {

// System.out.println("Restarting the game...");
// } else if (buttonClicked.equals("Quit")) {

// System.out.println("Quitting the game...");
// }
// }

// // Example usage
// public static void main(String[] args) {
// // Creating a GameOverPopup instance
// GameOverPopup gameOverPopup = new GameOverPopup("Game Over", "You lost. What
// would you like to do?",
// List.of("Restart", "Quit"),
// "Open", "Close", "Cancel");

// System.out.println("Title: " + gameOverPopup.getTitle());
// System.out.println("Text: " + gameOverPopup.getText());
// System.out.println("Buttons: " + gameOverPopup.getButtons());
// System.out.println("Open Button Text: " +
// gameOverPopup.getConfirmButtonText());
// System.out.println("Close Button Title: " +
// gameOverPopup.getCancelButtonText());
// System.out.println("Close Button Text: " +
// gameOverPopup.getCloseButtonText());

// gameOverPopup.open();

// gameOverPopup.onSubmit("Restart");

// gameOverPopup.close();
// }
// }
