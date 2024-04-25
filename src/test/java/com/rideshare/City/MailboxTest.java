package com.rideshare.City;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import com.rideshare.TileManager.GridPanePosition;

public class MailboxTest {
    @Test
    void Test_CanCreateMailbox() {
        Mailbox mailbox = new Mailbox(new GridPanePosition(1, 0), 0, null);

        assertEquals(1, mailbox.getGridPanePosition().col);
        assertEquals(1, mailbox.getGridPanePosition().row);
        assertTrue(mailbox.isReady());
    }

}