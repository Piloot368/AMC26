package com.example.amc26;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class HelloApplication extends Application {
    Pane pane = new Pane();
    Scene scene = new Scene(pane, 800, 600);

    @Override
    public void start(Stage stage) throws IOException {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

            }
        }
        timer.start();

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
