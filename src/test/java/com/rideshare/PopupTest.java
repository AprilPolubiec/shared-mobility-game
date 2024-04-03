package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PopupTest {
    @Test
    public void Test_CanCreatePopup() {
        var title = "Title";
        var text = "Text";
        Popup popup = new Popup(title, text);
        assertEquals(title, popup.title);
        assertEquals(text, popup.text);
    }
}
