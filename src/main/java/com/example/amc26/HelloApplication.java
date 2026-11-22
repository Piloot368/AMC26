package com.example.amc26;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.io.IOException;

public class HelloApplication extends Application {
    public final double WIDTH = 300, HEIGHT = 533;
    public Pane pane = new Pane();
    public Scene scene = new Scene(pane, WIDTH, HEIGHT);
    public Pane qstPane = new Pane();
    public GameController controller = new GameController();

    public Plan currentPlan = Plan.ACCUEIL;

    @Override
    public void start(Stage stage) throws IOException {
        Accueil();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

            }
        };
        timer.start();

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public void loadNewPlan(Plan plan) {
        pane.getChildren().clear();
        currentPlan = plan;
        switch (plan) {
            case ACCUEIL -> Accueil();
            case GAMEPLAY -> QuestionMenu();
        }
    }

    public void Accueil() {
        HBox hBox = new HBox();
        hBox.setPrefSize(WIDTH, HEIGHT);
        hBox.setAlignment(Pos.CENTER);
            VBox vBox = new VBox();
            vBox.setPrefSize(WIDTH, HEIGHT);
            vBox.setAlignment(Pos.CENTER);
                Button button = new Button("Lancer le quiz !");
                button.setOnAction(event -> {
                    loadNewPlan(Plan.GAMEPLAY);
                });
            vBox.getChildren().add(button);
        hBox.getChildren().add(vBox);
        pane.getChildren().add(hBox);
    }

    public void QuestionMenu() {
        pane.getChildren().remove(qstPane);
        qstPane.getChildren().clear();

        Label label = new Label(controller.getTheme());
        qstPane.getChildren().add(label);

        pane.getChildren().add(qstPane);
    }
}
