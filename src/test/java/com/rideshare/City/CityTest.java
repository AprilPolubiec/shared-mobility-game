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

    @Test
    void Test_CanAddRoutes() {
        City c = new City(30);

        ArrayList<Route> routes = new ArrayList<>();
        Route r = new Route(new GridPanePosition(0, 0), new GridPanePosition(1, 1));
        routes.add(r);
        c.setRoutes(routes);
        assertEquals(1, c.getRoutes().size());
    }

    @Test
    void Test_CanGetWaitingMailboxes() {
        City c = new City(30);

        ArrayList<Mailbox> mailboxes = new ArrayList<>();
        Mailbox m1 = new Mailbox(new GridPanePosition(0, 0), 0, null);
        Mailbox m2 = new Mailbox(new GridPanePosition(1, 1), 0, null);
        mailboxes.add(m1);
        mailboxes.add(m2);
        c.setMailboxes(mailboxes);

        ArrayList<Mailbox> waitingMailboxes = c.getWaitingMailboxes();
        assertEquals(2, waitingMailboxes.size());
        assertEquals(m1, waitingMailboxes.get(0));
        assertEquals(m2, waitingMailboxes.get(1));
    }

    @Test
    void Test_CanGetRouteNodes() {
        City c = new City(30);

        ArrayList<Route> routes = new ArrayList<>();
        Route r = new Route(new GridPanePosition(0, 0), new GridPanePosition(1, 1));
        routes.add(r);
        c.setRoutes(routes);

        ArrayList<GridPanePosition> routeNodes = c.getRouteNodes();
        assertEquals(2, routeNodes.size());
        assertEquals(new GridPanePosition(0, 0), routeNodes.get(0));
        assertEquals(new GridPanePosition(1, 1), routeNodes.get(1));
    }
}
