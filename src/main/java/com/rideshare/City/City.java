package com.rideshare.City;

import java.util.ArrayList;

import com.rideshare.City.City;
import com.rideshare.TileManager.GridPanePosition;
import com.rideshare.TileManager.TileManager;
import com.rideshare.TransportationMode.TransportationType;
import com.rideshare.Trip.TransportationNode;

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
  private ArrayList<Route> routes;
  private ArrayList<Mailbox> mailboxes;
  private int size;
  private TileManager _tileManager;

  public City(int size) {
    this.size = size;
  }

  // TODO: do we really need to store size in city?
  public int getSize() {
    return this.size;
  }

  public void setRoutes(ArrayList<Route> routes) {
    this.routes = routes;
  }

  public void setMailboxes(ArrayList<Mailbox> mailboxes) {
    this.mailboxes = mailboxes;
  }

  public void setTileManager(TileManager tm) {
    this._tileManager = tm;
  }

  public ArrayList<Route> getRoutes() {
    return routes;
  }

  /**
   * Clears the city from the UI
   */
  public void clear() {
    this._tileManager.clear();
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
      TransportationNode node = routeNodeMatrix.getNode(position);
      return node;
    }
    return null;
  }

}
