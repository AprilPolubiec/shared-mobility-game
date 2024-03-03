package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class CityTest {
    @Test
    void Test_CanCreateCity() {
        Route[] routes = {new Route("Green Line"), new Route("Red Line"), new Route("Blue Line")};
        Mailbox[] mailboxes = {new Mailbox(), new Mailbox(), new Mailbox()};
        int difficultyLevel = 5
        City c = new City("Dublin", routes, mailboxes, 5);
    
        assertEquals("Dublin", c.getName());
        assertEquals(c.getRoutes().length, 3);
        assertEquals(c.getMailboxes().length, 5);
    }

    @Test
    void Test_CanAddMailboxes() {
        City c = new City("Dublin");
        c.addMailboxes(5);
        assertEquals(c.getMailboxes().length, 5);
    }
}
