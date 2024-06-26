package com.rideshare.City;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.rideshare.TileManager.GridPanePosition;

public class CityTest {
    @Test
    void Test_CanCreateCity() {
        City c = new City(30);
        assertEquals(30, c.getSize());
    }

    @Test
    void Test_CanAddMailboxes() {
        City c = new City(30);

        ArrayList<Mailbox> mailboxes = new ArrayList<Mailbox>();
        Mailbox m = new Mailbox(new GridPanePosition(0, 0), 0, null);
        mailboxes.add(m);
        c.setMailboxes(mailboxes);
        assertEquals(c.getMailboxes().size(), 1);
    }
}
