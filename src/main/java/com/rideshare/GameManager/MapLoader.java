package com.rideshare.GameManager;

import com.rideshare.App;
import com.rideshare.City;
import com.rideshare.GridPanePosition;
import com.rideshare.Mailbox;
import com.rideshare.Route;
import com.rideshare.RouteNodeMatrix;
import com.rideshare.TransportationNode;
import com.rideshare.TransportationType;
import com.rideshare.Utils;
import com.rideshare.TileManager.MapJson;
import com.rideshare.TileManager.TileManager;
import com.rideshare.TileManager.TileUtils;
import com.rideshare.TileManager.TiledMapLayer;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class MapLoader {
    private AnchorPane root;
    private TileManager _tileManager;
    private City _city;

    public MapLoader(Scene scene) {
        // IDK
        this.root = (AnchorPane)scene.getRoot();
    }

    public void load(String mapName) {
        // TODO: validate map
        try {
            MapJson map = getMapDataFromFile(mapName);
            _tileManager = new TileManager(this.root, map.layers, map.height, map.width);
            _city = createCityFromMapData(map);
            _tileManager.draw();
            // _city.showAllMailboxes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public City getCity() {
        return _city;
    }

    public City createCityFromMapData(MapJson map) throws Exception {
        ArrayList<Route> routes = new ArrayList<Route>();
        ArrayList<Route> walkingRoute = getRoutes(map, "Walking", TransportationType.WALKING, "");
        ArrayList<Route> drivingRoute = getRoutes(map, "Roads", TransportationType.CAR, "Toyota Prius");
        ArrayList<Route> busRoute = getRoutes(map, "Bus", TransportationType.BUS, "39A");
        ArrayList<Route> trainRoute = getRoutes(map, "Train", TransportationType.TRAIN, "39A");
        routes.addAll(walkingRoute);
        routes.addAll(drivingRoute);
        routes.addAll(busRoute);
        routes.addAll(trainRoute);

        ArrayList<Mailbox> mailboxes = getMailboxes(map);
        City city = new City(map.height, routes, mailboxes);
        return city;
    }

    private ArrayList<Mailbox> getMailboxes(MapJson map) throws IOException {
        ArrayList<Mailbox> mailboxes = new ArrayList<Mailbox>();
        int[][] mailboxMatrix = null;
        for (TiledMapLayer layer : map.layers) {
            if (layer.name.equals("Houses")) {
                mailboxMatrix = arrayToMatrix(layer.data, map.height, map.width);   
            }
        }
        if (mailboxMatrix == null) {
            throw new IOException("No mailboxes found in map");
        }
        for (int i = 0; i < mailboxMatrix.length; i++) {
            int rowIdx = i;
            for (int j = 0; j < mailboxMatrix[rowIdx].length; j++) {
                int colIdx = j;
                int value = mailboxMatrix[rowIdx][colIdx];
                if (Arrays.asList(TileUtils.HOUSE_TILE_IDS).contains(value)) {
                    Mailbox mailbox = new Mailbox(rowIdx, colIdx, value, _tileManager);
                    // mailbox.setDuration(5); // TODO!
                    mailboxes.add(mailbox);
                }
            }
        }
        return mailboxes;
    }

    static public ArrayList<Route> getRoutes(MapJson map, String layerName, TransportationType transportationType, String name) {
        ArrayList<Route> routes = new ArrayList<Route>();
        TiledMapLayer layer = new TiledMapLayer();
        for (TiledMapLayer l : map.layers) {
            if(l.name.equals(layerName)) {
                layer = l;
            }
        }
        int height = map.height;
        int width = map.width;
        int[][] matrix = arrayToMatrix(layer.data, height, width);
        if (transportationType == TransportationType.BUS || transportationType == TransportationType.TRAIN) {
            Route route = new Route(matrix, transportationType, name);
            return getTransitRoutes(route);
        }
        Route route = new Route(matrix, transportationType, name);
        routes.add(route);
        return routes;
    }

    // We need to split up our bus matrix for each bus
    static public ArrayList<Route> getTransitRoutes(Route route) {
        ArrayList<Route> routes = new ArrayList<Route>();

        Graph<TransportationNode, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        
        RouteNodeMatrix matrix = route.getRouteNodeMatrix();
        for (int i = 0; i < matrix.get().length; i++) {
            for (int j = 0; j < matrix.get()[i].length; j++) {
                if (matrix.getNode(i, j) != null && !matrix.getNode(i, j).isSolid()) {
                    graph.addVertex(matrix.getNode(i, j));
                }
            }
        }

        // Add edges (connecting adjacent 1s)
        for (int i = 0; i < matrix.get().length; i++) {
            int cols = matrix.get()[i].length;
            for (int j = 0; j < cols; j++) {
                TransportationNode currentNode = matrix.getNode(i, j);

                TransportationNode aboveNode = matrix.getNode(i - 1, j);
                TransportationNode leftNode = matrix.getNode(i, j - 1);
                if (currentNode != null && !currentNode.isSolid()) {
                    if (aboveNode != null && !aboveNode.isSolid()) {
                        graph.addEdge(currentNode, aboveNode);
                    }
                    if (leftNode != null && !leftNode.isSolid()) {
                        graph.addEdge(currentNode, leftNode);
                    }
                }
            }
        }

        // Find connected components
        ConnectivityInspector<TransportationNode, DefaultEdge> inspector = new ConnectivityInspector<>(graph);
        List<Set<TransportationNode>> connectedSets = inspector.connectedSets();

        // Map bus routes
        for (Set<TransportationNode> connectedSet : connectedSets) {
            ArrayList<TransportationNode> newRoute = new ArrayList<>(connectedSet);
            routes.add(createTransitRouteFromConnectedNodes(route, newRoute));
        }

        return routes;
    }

    public static Route createTransitRouteFromConnectedNodes(Route route, ArrayList<TransportationNode> connectedNodes) {
        TransportationType transportationType = route.getTransportationType();
        int[][] originalMatrix = route.getRouteNodeMatrix().getOriginalMatrix();
        ArrayList<GridPanePosition> nodePositions = new ArrayList<GridPanePosition>();
        for (TransportationNode node : connectedNodes) {
            nodePositions.add(new GridPanePosition(node.row, node.col));
        }

        int[][] newMatrix = originalMatrix.clone();
        for (int i = 0; i < originalMatrix.length; i++) {
            for (int j = 0; j < originalMatrix[i].length; j++) {
                if (originalMatrix[i][j] != 0) {
                    // Check if it is in these connected nodes 
                    final int row = i;
                    final int col = j;
                    Optional<GridPanePosition> connectedNode = nodePositions.stream().filter((GridPanePosition p) -> p.row == row && p.col == col).findAny();
                    if (!connectedNode.isPresent()) {
                        newMatrix[i][j] = 0;
                    }
                }
            }
        }

        return new Route(newMatrix, transportationType, "39B");
    }

    static public int[][] arrayToMatrix(int[] arr, int numRows, int numColumns) {
        int[][] matrix = new int[numRows][numColumns];
        int currRow = -1;
        for (int i = 0; i < arr.length; i++) {
            int col = i % numColumns;
            if (col == 0) {
                currRow += 1;
            }
            matrix[currRow][col] = arr[i];
        }
        return matrix;
    }

    static public MapJson getMapDataFromFile(String mapName) throws Exception {
        MapJson data = new MapJson();
        URL url = App.class.getResource("/images/maps/" + mapName + ".json");
        if (url == null) {
            throw new Exception(String.format("%s not found", url.toString()));
        } 
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            // Read from the reader here
            // Parse JSON file into Java object
            Gson gson = new Gson();
            data = gson.fromJson(reader, MapJson.class);

            // Now you can use the data object as needed
            System.out.println(data.height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}