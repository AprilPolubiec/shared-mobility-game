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
  Route[] routes = {};
  ArrayList<ArrayList<ArrayList<TransportationNode>>> grid;
  public City(int size) {
    // grid = new TransportationNode[size][size][];
  }

  /**
   * Iterates over each value in the route map
   * @param routeMap
   */
  private void addRoute(Route route) {
    for (int i = 0; i < route.nodeMatrix.get().length; i++) {
      int rowIdx = i;
      TransportationNode[] row = route.nodeMatrix.get()[rowIdx];
      for (int j = 0; j < row.length; j++) {
        
      }
    }
  }

  public Route[] getRoutes() {
    return routes;
  }
}
