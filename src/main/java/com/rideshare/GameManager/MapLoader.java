package com.rideshare.GameManager;

import com.rideshare.App;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MapLoader {
    private AnchorPane root;
    MapLoader(AnchorPane root) {
        // IDK
        this.root = root;
    }

    public void load(String mapName) {
        Image image = new Image(App.class.getResource(String.format("/images/maps/%s.png", mapName)).toString()); 
        ImageView imageView = new ImageView(image);
        root.getChildren().add(imageView);
    }

    public void getCity(String mapName) {
        // ok
        // City city = new City();
        Gson gson = new Gson();

        URL url = App.class.getResource("/images/maps/" + mapName + ".json");
        if (url != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                // Read from the reader here
                // Parse JSON file into Java object
                MapJson data = gson.fromJson(reader, MapJson.class);

                // Now you can use the data object as needed
                System.out.println(data.height);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Resource not found.");
        }
    }

    private void readFile(String mapName){
        String inputFile = String.format("%s.json", mapName);
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
    public int tileHeight;
    public int width;
    public int tileWidth;
    public Layer[] layers;
}
