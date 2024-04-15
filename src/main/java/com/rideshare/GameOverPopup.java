package com.rideshare;

import java.util.List;

public class GameOverPopup extends Popup {

    public GameOverPopup(String title, String text, List<String> buttons, String openButtonText,
            String closeButtonTitle,
            String closeButtonText) {
        super(title, text, buttons, openButtonText, closeButtonTitle, closeButtonText);
    }

    @Override
    public void onSubmit(String buttonClicked) {
        super.onSubmit(buttonClicked); //
        if (buttonClicked.equals("Restart")) {

            System.out.println("Restarting the game...");
        } else if (buttonClicked.equals("Quit")) {

            System.out.println("Quitting the game...");
        }
    }

    // Example usage
    public static void main(String[] args) {
        // Creating a GameOverPopup instance
        GameOverPopup gameOverPopup = new GameOverPopup("Game Over", "You lost. What would you like to do?",
                List.of("Restart", "Quit"),
                "Open", "Close", "Cancel");

        System.out.println("Title: " + gameOverPopup.getTitle());
        System.out.println("Text: " + gameOverPopup.getText());
        System.out.println("Buttons: " + gameOverPopup.getButtons());
        System.out.println("Open Button Text: " + gameOverPopup.getOpenButtonText());
        System.out.println("Close Button Title: " + gameOverPopup.getCloseButtonTitle());
        System.out.println("Close Button Text: " + gameOverPopup.getCloseButtonText());

        gameOverPopup.open();

        gameOverPopup.onSubmit("Restart");

        gameOverPopup.close();
    }
}
