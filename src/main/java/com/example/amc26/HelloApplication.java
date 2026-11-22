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

        String theme = controller.getTheme();
        Image background = null;

        switch (theme){
            case "Hiver":
                background = new Image(getClass().getResource("/images/hiverBackground.jpg").toExternalForm());
                break;
            case "Mobilité":
                background = new Image(getClass().getResource("/images/hiverBackground.jpg").toExternalForm());
                break;
            case "Quartier & Écologie":
                background = new Image(getClass().getResource("/images/hiverBackground.jpg").toExternalForm());
                break;
            case "Culture & Quotidien":
                background = new Image(getClass().getResource("/images/hiverBackground.jpg").toExternalForm());
                break;
        }

        BackgroundImage backgroundImage = new BackgroundImage(background,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, false, true)
        );

        pane.setBackground(new Background(backgroundImage));

        Button button = new Button("<-");
        button.setOnAction(event -> {
            loadNewPlan(Plan.ACCUEIL);
        });
        pane.getChildren().add(button);

        HBox hBoxTheme = new HBox();
        hBoxTheme.setLayoutY(50);
        hBoxTheme.setPrefWidth(WIDTH);
        hBoxTheme.setAlignment(Pos.CENTER);
            Label lTheme = new Label(controller.getTheme());
            lTheme.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        hBoxTheme.getChildren().add(lTheme);
        qstPane.getChildren().add(hBoxTheme);

        HBox hBoxStatement = new HBox();
        hBoxStatement.setLayoutY(100);
        hBoxStatement.setPrefWidth(WIDTH);
        hBoxStatement.setAlignment(Pos.CENTER);
            Label lStatement = new Label(controller.getQuestion());
            lStatement.setFont(Font.font(12));
            lStatement.setMaxWidth(WIDTH-20);
            lStatement.setWrapText(true);
        hBoxStatement.getChildren().add(lStatement);
        qstPane.getChildren().add(hBoxStatement);

        HBox hBoxBtns = new HBox(50);
        hBoxBtns.setLayoutY(250);
        hBoxBtns.setPrefWidth(WIDTH);
        hBoxBtns.setAlignment(Pos.CENTER);
            Button btnVrai = new Button("Vrai");
            btnVrai.setPrefSize(100, 75);
            btnVrai.setOnAction(event -> {
                ///TODO
            });
            Button btnMythe = new Button("Mythe");
            btnMythe.setPrefSize(100, 75);
            btnMythe.setOnAction(event -> {
                ///TODO
            });
        hBoxBtns.getChildren().addAll(btnVrai, btnMythe);
        qstPane.getChildren().add(hBoxBtns);

        pane.getChildren().add(qstPane);
    }
}
