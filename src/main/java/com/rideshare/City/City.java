package com.rideshare.City;

import java.util.ArrayList;

import com.rideshare.City.City;
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

  public void clear() {
    this._tileManager.clear();
  }

  public ArrayList<Mailbox> getMailboxes() {
    return this.mailboxes;
  }

  public ArrayList<Mailbox> getWaitingMailboxes() {
    ArrayList<Mailbox> waitingMailboxes = new ArrayList<>();
    for (Mailbox mailbox : this.mailboxes) {
      if (mailbox.isWaiting()) {
        waitingMailboxes.add(mailbox);
      }
    }
    return waitingMailboxes;
  }

  public ArrayList<Mailbox> getFailedOrCompletedMailboxes() {
    ArrayList<Mailbox> failedOrCompleted = new ArrayList<>();
    for (Mailbox mailbox : this.mailboxes) {
      if (mailbox.isExpired() || mailbox.isCompleted()) {
        failedOrCompleted.add(mailbox);
      }
    }
    return failedOrCompleted;
  }

  public ArrayList<Mailbox> getReadyMailboxes() {
    ArrayList<Mailbox> readyMailboxes = new ArrayList<>();
    for (Mailbox mailbox : this.mailboxes) {
      if (mailbox.isReady()) {
        readyMailboxes.add(mailbox);
      }
    }
    return readyMailboxes;
  }

  public ArrayList<Mailbox> getUninitializedMailboxes() {
    ArrayList<Mailbox> uninitializedMailboxes = new ArrayList<>();
    for (Mailbox mailbox : this.mailboxes) {
      if (!mailbox.isInitialized()) {
        uninitializedMailboxes.add(mailbox);
      }
    }
    return uninitializedMailboxes;
  }

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
  public ArrayList<TransportationNode> getRouteNodes(int rowIdx, int colIdx) {
    ArrayList<TransportationNode> nodes = new ArrayList<TransportationNode>();
    for (Route route : routes) {
      RouteNodeMatrix routeNodeMatrix = route.getRouteNodeMatrix();
      TransportationNode node = routeNodeMatrix.getNode(rowIdx, colIdx);
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
  public TransportationNode getRouteNode(int rowIdx, int colIdx, TransportationType transportationType) {
    for (Route route : routes) {
      if (route.getTransportationType() != transportationType) {
        continue;
      }

      RouteNodeMatrix routeNodeMatrix = route.getRouteNodeMatrix();
      TransportationNode node = routeNodeMatrix.getNode(rowIdx, colIdx);
      return node;
    }
    return null;
  }

}
