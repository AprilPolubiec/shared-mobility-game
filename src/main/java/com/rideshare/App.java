package com.rideshare;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        // Launch will call stage
        launch(args); // Static method belonging to Application
    }

    private void demoStage(Stage stage) {
        Group root = new Group(); // Group = collection of nodes
        Scene scene = new Scene(root, Color.BLACK);
        
        // Don't need path if its in src file
        Image icon = new Image(App.class.getResource("/images/girl-1.png").toString());
        stage.getIcons().add(icon);
        stage.setTitle("Stage Demo Program :)");
        stage.setWidth((420));
        stage.setHeight((420));
        stage.setResizable(false);
        // stage.setX(50);
        // stage.setY(50);

        stage.setFullScreen(true);
        // TODO: this message isnt showing but whatevs
        stage.setFullScreenExitHint("YOU CANT ESCAPE unless you press Q");
        stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("q"));

        stage.setScene(scene);
        stage.show();
    }

    private void demoScenes(Stage stage) {
        Group root = new Group(); // Group = collection of nodes
        Scene scene = new Scene(root, 600, 600, Color.LIGHTCORAL);

        Text text = new Text();
        text.setText("Hi :)");
        text.setX(50);
        text.setY(50);
        text.setFont(Font.font("Verdana", 50));
        text.setFill(Color.GREEN);

        Line line = new Line();
        line.setStartX(200);
        line.setStartY(200);
        line.setEndX(500);
        line.setEndY(200);
        line.setStrokeWidth(10);
        line.setStroke(Color.ORANGE);
        line.setOpacity(0.5);
        line.setRotate(45);

        Rectangle rectangle = new Rectangle();
        rectangle.setX(100);
        rectangle.setY(100);
        rectangle.setWidth(100);
        rectangle.setHeight(100);
        rectangle.setFill(Color.ALICEBLUE);
        rectangle.setStrokeWidth(3);
        rectangle.setStroke(Color.BLACK);

        Polygon triangle = new Polygon();
        triangle.getPoints().setAll(
            200.0, 200.0, 
            300.0, 300.0, 
            200.0, 300.0);
        triangle.setFill(Color.YELLOW);

        Circle circle = new Circle();
        circle.setCenterX(350);
        circle.setCenterY(350);
        circle.setRadius(50);
        circle.setFill(Color.BISQUE);

        Image image = new Image(App.class.getResource("/images/girl-1.png").toString());
        ImageView imageView = new ImageView(image);
        imageView.setX(400);
        imageView.setY(400);

        root.getChildren().add(text);
        root.getChildren().add(line);
        root.getChildren().add(rectangle);
        root.getChildren().add(triangle);
        root.getChildren().add(circle);
        root.getChildren().add(imageView);

        stage.setScene(scene);
        stage.show();
    }
    
    // @Override
    // public void start(Stage stage) throws Exception { 
    //     // TODO: learn about nodes
    //     stage.setScene(scene);
    //     stage.show();
    // }

}