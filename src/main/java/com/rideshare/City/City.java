package com.rideshare.City;

import com.rideshare.TileManager.GridPanePosition;
import com.rideshare.TileManager.TileManager;
import com.rideshare.TransportationMode.TransportationType;
import com.rideshare.Trip.TransportationNode;

import java.util.ArrayList;

/**
 * Description: A city represents a location which can be visualized as a map.
 * It contains various modes of transportation which can navigate on various
 * routes to take people from one place to another.
 * Attributes:
 * Name (string): name to identify the city by (eg: “Dublin”)
 * Routes (Route[]): all the possible routes that can be traveled on in the city
 * DifficultyLevel (Integer): indicates the “difficulty” of the city, integer 0
 * - 10, where 0 is the easiest and 10 is the hardest.
 * Mailboxes (Mailbox[]): a list of Mailboxes which exist within this city
 * Dimensions - what is the height/width of the city?
 *
 * Methods:
 * constructor - at your discretion
 * getters/setters - at your discretion
 * loadCity(): Prepares a city for gameplay and renders the map to be visible to
 * the player (is this necessary?)
 * addMailboxes(int count): adds n mailboxes to the city
 */

public class City {
  //Instance Field Declarations
  private ArrayList<Route> routes;
  private ArrayList<Mailbox> mailboxes;
  private final int size;
  private TileManager tileManager;

  //Class Constructor
  public City(int size) {
    if (size < 0) {
      throw new IllegalArgumentException("Size cannot be a negative value.");
    }
    this.size = size;
  }

  //Class Getter Methods
  // TODO: do we really need to store size in city?
  public int getSize() { return this.size; }

  public ArrayList<Route> getRoutes() {
    return routes;
  }

  /**
   * Get all mailboxes in the city
   *
   * @return ArrayList<Mailbox> mailboxes
   */
  public ArrayList<Mailbox> getMailboxes() {
    return this.mailboxes;
  }

  /**
   * Get all mailboxes that are neither failed nor completed
   *
   * @return ArrayList<Mailbox>
   */
  public ArrayList<Mailbox> getMailboxesLeft() {
    ArrayList<Mailbox> mailboxesLeft = new ArrayList<>();
    for (Mailbox mailbox : this.mailboxes) {
      if (!mailbox.isExpired() && !mailbox.isCompleted()) {
        mailboxesLeft.add(mailbox);
      }
    }
    return mailboxesLeft;
  }

  /**
   * Get all mailboxes that are waiting to be selected
   *
   * @return ArrayList<Mailbox>
   */
  public ArrayList<Mailbox> getWaitingMailboxes() {
    ArrayList<Mailbox> waitingMailboxes = new ArrayList<>();
    for (Mailbox mailbox : this.mailboxes) {
      if (mailbox.isWaiting()) {
        waitingMailboxes.add(mailbox);
      }
    }
    return waitingMailboxes;
  }

  /**
   * Get mailboxes that have not yet been rendered
   *
   * @return ArrayList<Mailbox>
   */
  public ArrayList<Mailbox> getUninitializedMailboxes() {
    ArrayList<Mailbox> uninitializedMailboxes = new ArrayList<>();
    for (Mailbox mailbox : this.mailboxes) {
      if (!mailbox.isInitialized()) {
        uninitializedMailboxes.add(mailbox);
      }
    }
    return uninitializedMailboxes;
  }

  /**
   * @param rowIdx Row/y to get node at
   * @param colIdx Column/x to get node at
   * @return ArrayList<TransportationNode> list of transportation nodes which
   *         exist at these coordinates in the city
   */
  public ArrayList<TransportationNode> getRouteNodes(GridPanePosition position) {
    ArrayList<TransportationNode> nodes = new ArrayList<TransportationNode>();
    for (Route route : routes) {
      RouteNodeMatrix routeNodeMatrix = route.getRouteNodeMatrix();
      TransportationNode node = routeNodeMatrix.getNode(position);
      nodes.add(node);
    }
    return nodes;
  }

  /**
   * @param rowIdx             Row/y to get node at
   * @param colIdx             Column/x to get node at
   * @param transportationType
   * @return ArrayList<TransportationNode> list of transportation nodes which
   *         exist at these coordinates in the city
   */
  public TransportationNode getRouteNode(GridPanePosition position, TransportationType transportationType) {
    for (Route route : routes) {
      if (route.getTransportationType() != transportationType) {
        continue;
      }

      RouteNodeMatrix routeNodeMatrix = route.getRouteNodeMatrix();
      return routeNodeMatrix.getNode(position);
    }
    return null;
  }

  //Class Setter Methods
  public void setRoutes(ArrayList<Route> routes) {
    if (routes == null) {
      throw new IllegalArgumentException("Routes cannot be null.");
    }
    this.routes = routes;
  }

  public void setMailboxes(ArrayList<Mailbox> mailboxes) {
    if (mailboxes == null) {
      throw new IllegalArgumentException("Mailboxes cannot be null");
    }
    this.mailboxes = mailboxes;
  }

  public void setTileManager(TileManager tm) {
    if (tm == null) {
      throw new IllegalArgumentException("TileManager cannot be null.");
    }
    this.tileManager = tm;
  }

  //Class Rendering Methods
  /**
   * Clears the city from the UI
   */
  public void clear() {
    this.tileManager.clear();
  }

  /**
   * Render and show all mailboxes in the city
   */
  public void showAllMailboxes() {
    for (Mailbox mailbox : mailboxes) {
      if (!mailbox.isInitialized()) {
        mailbox.render();
      }
      mailbox.show();
    }
  }
}
