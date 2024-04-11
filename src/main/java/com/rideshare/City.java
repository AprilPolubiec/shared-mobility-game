package com.rideshare;

import java.util.ArrayList;

import javafx.geometry.Point2D;

/**
 * Description: A city represents a location which can be visualized as a map. It contains various modes of transportation which can navigate on various routes to take people from one place to another.
    Attributes:
      Name (string): name to identify the city by (eg: “Dublin”)
      Routes (Route[]): all the possible routes that can be traveled on in the city
      DifficultyLevel (Integer): indicates the “difficulty” of the city, integer 0 - 10, where 0 is the easiest and 10 is the hardest.
      Mailboxes (Mailbox[]): a list of Mailboxes which exist within this city
      Dimensions - what is the height/width of the city?
    
    Methods:
      constructor - at your discretion
      getters/setters - at your discretion
      loadCity(): Prepares a city for gameplay and renders the map to be visible to the player (is this necessary?)
      addMailboxes(int count): adds n mailboxes to the city
 */

public class City {
  ArrayList<Route> routes;
  ArrayList<Mailbox> mailboxes;
  int size;
  
  public City(int size, ArrayList<Route> routes, ArrayList<Mailbox> mailboxes) {
    this.routes = routes;
    this.size = size;
    this.mailboxes = mailboxes;

    for (Mailbox mailbox : mailboxes) {
      mailbox.setTripCalculator(this);
    }
  }

  public ArrayList<Mailbox> getMailboxes() {
    return this.mailboxes;
  }

  // public void markAllMailboxesReady() {
  //   for (Mailbox mailbox : this.mailboxes) {
  //     mailbox.
  //   }
  // }

  public ArrayList<Mailbox> getWaitingMailboxes() {
    ArrayList<Mailbox> waitingMailboxes = new ArrayList<>();
    for (Mailbox mailbox : this.mailboxes) {
      if (mailbox.getStatus() == MailboxStatus.WAITING) {
        waitingMailboxes.add(mailbox);
      }
    }
    return waitingMailboxes;
  }

  public ArrayList<Mailbox> getReadyMailboxes() {
    ArrayList<Mailbox> readyMailboxes = new ArrayList<>();
    for (Mailbox mailbox : this.mailboxes) {
      if (mailbox.getStatus() == MailboxStatus.READY) {
        readyMailboxes.add(mailbox);
      }
    }
    return readyMailboxes;
  }
  public ArrayList<Mailbox> getUninitializedMailboxes() {
    ArrayList<Mailbox> uninitializedMailboxes = new ArrayList<>();
    for (Mailbox mailbox : this.mailboxes) {
      if (mailbox.getStatus() == MailboxStatus.UNINITIALIZED) {
        uninitializedMailboxes.add(mailbox);
      }
    }
    return uninitializedMailboxes;
  }

  public void showAllMailboxes() {
    for (Mailbox mailbox : mailboxes) {
      if (mailbox.status == MailboxStatus.UNINITIALIZED) {
        mailbox.render();
      }
      mailbox.show();
    }
  }

  public ArrayList<Route> getRoutes() {
    return routes;
  }

  
  /** 
   * @param rowIdx Row/y to get node at
   * @param colIdx Column/x to get node at
   * @return ArrayList<TransportationNode> list of transportation nodes which exist at these coordinates in the city
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

}
