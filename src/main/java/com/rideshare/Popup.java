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

public class Popup {
    // Attributes
    private String title;
    private String text;
    private List<String> buttons;
    private String openButtonText;
    private String closeButtonTitle;
    private String closeButtonText;

    // Constructor
    public Popup(String title, String text, List<String> buttons, String openButtonText, String closeButtonTitle,
            String closeButtonText) {
        this.title = title;
        this.text = text;
        this.buttons = buttons;
        this.openButtonText = openButtonText;
        this.closeButtonTitle = closeButtonTitle;
        this.closeButtonText = closeButtonText;
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

    public String getOpenButtonText() {
        return openButtonText;
    }

    public String getCloseButtonTitle() {
        return closeButtonTitle;
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

    // Example usage
    public static void main(String[] args) {
        // Creating a Popup instance
        Popup popup = new Popup("Confirmation", "Are you sure you want to delete this item?", List.of("Yes", "No"),
                "Open", "Close", "Cancel");

        // Accessing attributes
        System.out.println("Title: " + popup.getTitle());
        System.out.println("Text: " + popup.getText());
        System.out.println("Buttons: " + popup.getButtons());
        System.out.println("Open Button Text: " + popup.getOpenButtonText());
        System.out.println("Close Button Title: " + popup.getCloseButtonTitle());
        System.out.println("Close Button Text: " + popup.getCloseButtonText());

        // Opening the popup
        popup.open();

        // Handling submission
        popup.onSubmit("Yes");

        // Closing the popup
        popup.close();
    }
}
