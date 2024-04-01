package com.rideshare.GameManager;

import com.rideshare.App;
import com.rideshare.City;
import com.rideshare.Route;
import com.rideshare.RouteNodeMatrix;
import com.rideshare.TransportationType;

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
    MapLoader(AnchorPane root) {
        // IDK
        this.root = root;
    }

    public void load(String mapName) {
        // TODO: validate map
        Image image = new Image(App.class.getResource(String.format("/images/maps/%s.png", mapName)).toString()); 
        ImageView imageView = new ImageView(image);
        root.getChildren().add(imageView);
    }

    public City getCity(String mapName) throws Exception {
        MapJson map = getMapDataFromFile(mapName);

        ArrayList<Route> routes = new ArrayList<Route>();
        Route walkingRoute = getRoute(map, "Walking", TransportationType.WALKING, "");
        Route drivingRoute = getRoute(map, "Roads", TransportationType.CAR, "Toyota Prius");
        Route busRoute = getRoute(map, "Bus", TransportationType.BUS, "39A");
        Route trainRoute = getRoute(map, "Train", TransportationType.TRAIN, "39A");
        routes.add(walkingRoute);
        routes.add(drivingRoute);
        routes.add(busRoute);
        routes.add(trainRoute);
        City city = new City(map.height, routes);
        return city;
    }

    static public Route getRoute(MapJson map, String layerName, TransportationType transportationType, String name) {
        Layer layer = new Layer();
        for (Layer l : map.layers) {
            if(l.name.equals(layerName)) {
                layer = l;
            }
        }
        int height = layer.height;
        int width = layer.width;
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

class Layer {
    public int[] data;
    public int height;
    public int id;
    public String name;
    public float opacity;
    public String type;
    public boolean visible;
    public int width;
    public int x;
    public int y;
}


class MapJson {
    public int height;
    public int tileheight;
    public int width;
    public int tilewidth;
    public Layer[] layers;
}
