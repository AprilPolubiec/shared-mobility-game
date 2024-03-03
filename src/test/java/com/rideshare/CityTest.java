package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
// Name (string): name to identify the city by (eg: “Dublin”)
// Routes (Route[]): all the possible routes that can be traveled on in the city
// DifficultyLevel (Integer): indicates the “difficulty” of the city, integer 0 - 10, where 0 is the easiest and 10 is the hardest.
// Mailboxes (Mailbox[]): a list of Mailboxes which exist within this city


public class CityTest {
    @Test
    void Test_CityHasName() {
        City c = new City("Dublin");
        assertEquals("Dublin", c.getName());
    }

    @Test
    void Test_CanChangeName() {
        City c = new City("Dublin");
        c.setName("Cork");
        assertEquals("Cork", c.getName());
    }

    @Test
    void Test_CanSetRoutes() {
        City c = new City("Dublin");
        Route[] routes = {new Route("Green Line"), new Route("Red Line"), new Route("Blue Line")};
        c.setRoutes(routes);
        assertEquals(c.getRoutes().length, 3);
    }

    @Test
    void Test_CanSetDifficultyLevel() {
        City c = new City("Dublin");
        c.setLevel(5);
        assertEquals(c.getLevel().length, 5);
    }

    @Test
    void Test_CanAddMailboxes() {
        City c = new City("Dublin");
        Mailbox[] mailboxes = {new Mailbox(), new Mailbox(), new Mailbox()};
        c.setMailboxes(mailboxes);
        assertEquals(c.getMailboxes().length, 3);
    }

    @Test
    void Test_CanCreateCity() {
        City c = new City("Dublin");
        Route[] routes = {new Route("Green Line"), new Route("Red Line"), new Route("Blue Line")};
        Mailbox[] mailboxes = {new Mailbox(), new Mailbox(), new Mailbox()};
        c.createCity(routes, mailboxes);
        assertEquals(c.getMailboxes().length, 3);
        assertEquals(c.getRoutes().length, 3);
    }

    @Test
    void Test_CanCreateMailboxes() {
        City c = new City("Dublin");
        c.addMailboxes(5);
        assertEquals(c.getMailboxes().length, 5);
    }
}
