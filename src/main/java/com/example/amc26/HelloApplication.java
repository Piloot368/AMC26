package com.example.amc26;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

        Image streakImage = new Image(getClass().getResource("/images/streakIcon.png").toExternalForm());
        ImageView imageView = new ImageView(streakImage);
        imageView.setFitHeight(64);
        imageView.setFitWidth(64);

        Image menuBackground = new Image(getClass().getResource("/images/menuBackground.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(menuBackground,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, false, true)
        );

        VBox vBox = new VBox();
        vBox.setBackground(new Background(backgroundImage));

        vBox.setPrefSize(WIDTH, HEIGHT);
        vBox.setAlignment(Pos.CENTER);

        Button button = new Button("Lancer le quiz !");
        button.setOnAction(event -> {
            loadNewPlan(Plan.GAMEPLAY);
        });

        vBox.getChildren().addAll(imageView, button);
        hBox.getChildren().addAll(vBox);
        pane.getChildren().add(hBox);
    }

    public void QuestionMenu() {
        pane.getChildren().remove(qstPane);
        qstPane.getChildren().clear();

        Button button = new Button("<-");
        button.setOnAction(event -> {
            loadNewPlan(Plan.ACCUEIL);
        });
        pane.getChildren().add(button);

        HBox hBox = new HBox();
        hBox.setLayoutY(100);
        hBox.setPrefWidth(WIDTH);
        hBox.setAlignment(Pos.CENTER);
            Label label = new Label(controller.getTheme());
            label.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        hBox.getChildren().add(label);
        qstPane.getChildren().add(hBox);

        pane.getChildren().add(qstPane);
    }
}
