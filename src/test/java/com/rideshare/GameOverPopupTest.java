package com.rideshare;

import java.util.List;

public class GameOverPopupTest {
    public static void main(String[] args) {

        GameOverPopup gameOverPopup = new GameOverPopup("Game Over", "You lost. What would you like to do?",
                List.of("Restart", "Quit"),
                "Open", "Close", "Cancel");

        System.out.println("Title: " + gameOverPopup.getTitle());
        System.out.println("Text: " + gameOverPopup.getText());
        System.out.println("Buttons: " + gameOverPopup.getButtons());
        System.out.println("Open Button Text: " + gameOverPopup.getConfirmButtonText());
        System.out.println("Close Button Title: " + gameOverPopup.getCancelButtonText());
        System.out.println("Close Button Text: " + gameOverPopup.getCloseButtonText());

        gameOverPopup.open();

        gameOverPopup.onSubmit("Restart");

        gameOverPopup.close();
    }
}
