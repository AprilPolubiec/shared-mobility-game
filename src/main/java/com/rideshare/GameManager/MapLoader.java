package com.rideshare.GameManager;

import com.rideshare.App;
import com.rideshare.City;
import com.rideshare.Mailbox;
import com.rideshare.Route;
import com.rideshare.RouteNodeMatrix;
import com.rideshare.TransportationType;
import com.rideshare.TileManager.TileManager;
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
import java.net.URL;
import java.util.ArrayList;

public class MapLoader {
    private AnchorPane root;
    private TileManager _tileManager;
    private City _city;
    MapLoader(Scene scene) {
        // IDK
        this.root = (AnchorPane)scene.getRoot();
    }

    public void load(String mapName) {
        // TODO: validate map
        try {
            MapJson map = getMapDataFromFile(mapName);
            _city = createCityFromMapData(map);
            _tileManager = new TileManager(this.root, map.layers, map.height, map.width);
            _tileManager.draw();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public City getCity() {
        return _city;
    }

    public City createCityFromMapData(MapJson map) throws Exception {
        ArrayList<Route> routes = new ArrayList<Route>();
        Route walkingRoute = getRoute(map, "Walking", TransportationType.WALKING, "");
        Route drivingRoute = getRoute(map, "Roads", TransportationType.CAR, "Toyota Prius");
        Route busRoute = getRoute(map, "Bus", TransportationType.BUS, "39A");
        Route trainRoute = getRoute(map, "Train", TransportationType.TRAIN, "39A");
        routes.add(walkingRoute);
        routes.add(drivingRoute);
        routes.add(busRoute);
        routes.add(trainRoute);

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
                Mailbox mailbox = new Mailbox(rowIdx, colIdx);
                mailboxes.add(mailbox);
            }
        }
        return mailboxes;
    }

    static public Route getRoute(MapJson map, String layerName, TransportationType transportationType, String name) {
        TiledMapLayer layer = new TiledMapLayer();
        for (TiledMapLayer l : map.layers) {
            if(l.name.equals(layerName)) {
                layer = l;
            }
        }
        int height = map.height;
        int width = map.width;
        int[][] matrix = arrayToMatrix(layer.data, height, width);
    
        Route route = new Route(matrix, transportationType, name);
        return route;
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

class MapJson {
    public int height;
    public int tileheight;
    public int width;
    public int tilewidth;
    public TiledMapLayer[] layers;
}
