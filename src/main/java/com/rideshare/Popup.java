// package com.rideshare;
// public class Popup {
//     // Attributes:
//     // title
//     // text
//     // buttons

//     // Methods:
//     // OnSubmit

package com.rideshare;

import java.util.List;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;

public class Popup {
    // Attributes
    private String title;
    private String text;
    private String bodyText;
    private List<String> buttons;
    private String confirmButtonText;
    private String cancelButtonText;
    private String closeButtonText;
    Dialog<String> dialog;
    DialogPane dialogPane;

    // Constructor
    public Popup(AnchorPane root, String title, String text, List<String> buttons, String confirmButtonText,
            String cancelButtonText,
            String closeButtonText) {
        
        // Create dialog
        this.dialog = new Dialog<String>();
        this.dialog.setTitle(title);
        
        // Add buttons
        ButtonType confirmButtonType = new ButtonType(confirmButtonText, ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(cancelButtonText, ButtonData.CANCEL_CLOSE);
        this.dialog.getDialogPane().getButtonTypes().add(confirmButtonType);
        this.dialog.getDialogPane().getButtonTypes().add(cancelButtonType);

        // Set content
        this.dialog.setContentText(text);
        this.dialog.setHeaderText("Header");

        // Show
        this.dialog.showAndWait();
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public List<String> getButtons() {
        return buttons;
    }

    public String getConfirmButtonText() {
        return confirmButtonText;
    }

    public String getCancelButtonText() {
        return cancelButtonText;
    }

    public String getCloseButtonText() {
        return closeButtonText;
    }

    // Method for opening the popup
    public void open() {
        System.out.println("Popup opened!");
    }

    // Method for closing the popup
    public void close() {
        System.out.println("Popup closed!");
    }

    // Method for handling submission
    public void onSubmit(String buttonClicked) {
        // Do something based on the button clicked
        System.out.println("Button clicked: " + buttonClicked);
    }

}
