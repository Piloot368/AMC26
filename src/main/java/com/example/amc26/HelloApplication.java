package com.example.amc26;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HelloApplication extends Application {
    public final double WIDTH = 300, HEIGHT = 533;
    public Pane pane = new Pane();
    public Scene scene = new Scene(pane, WIDTH, HEIGHT);
    public Pane qstPane = new Pane();
    public GameController controller = new GameController(this);
    public StreakManager streakManager = new StreakManager(Path.of("src/main/resources/infos.json"));

    public Plan currentPlan = Plan.ACCUEIL;

    @Override
    public void start(Stage stage) throws IOException {
        streakManager.checkStreakOnLaunch();
        accueil();
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
        pane.setBackground(null);
        currentPlan = plan;
        switch (plan) {
            case ACCUEIL -> accueil();
            case GAMEPLAY -> questionMenu();
            case RESULTS -> results();
            case OPTIONS -> options();
        }
    }

    public void accueil() {
        HBox hBox = new HBox();
        hBox.setPrefSize(WIDTH, HEIGHT);
        hBox.setAlignment(Pos.CENTER);

        HBox streakBox = new HBox(10);
        streakBox.setPrefSize(Integer.MIN_VALUE, Integer.MIN_VALUE);
        streakBox.setAlignment(Pos.CENTER);
        Label streak = new Label("" + streakManager.getStreakCount());
        streak.setTextFill(Color.CHOCOLATE);
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

        VBox vBox = new VBox(5);
        vBox.setBackground(new Background(backgroundImage));

        vBox.setPrefSize(WIDTH, HEIGHT);
        vBox.setAlignment(Pos.CENTER);

        Button btnGameplay = new Button("Lancer le quiz !");
        btnGameplay.setOnAction(event -> {
            loadNewPlan(Plan.GAMEPLAY);
        });

        Button btnOptions = new Button("Options");
        btnOptions.setOnAction(event -> {
            loadNewPlan(Plan.OPTIONS);
        });

        streakBox.getChildren().addAll(streak, imageView);
        vBox.getChildren().addAll(streakBox, btnGameplay, btnOptions);
        hBox.getChildren().addAll(vBox);
        pane.getChildren().add(hBox);
    }

    public void questionMenu() {
        controller.nextQuestion();
        if(currentPlan == Plan.GAMEPLAY){
            pane.getChildren().clear();
            qstPane.getChildren().clear();

            String theme = controller.getTheme();
            Image background = getRightBackground(theme);

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
            Label lTheme = new Label(theme);
            lTheme.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            hBoxTheme.getChildren().add(lTheme);
            qstPane.getChildren().add(hBoxTheme);

            HBox hBoxStatement = new HBox();
            hBoxStatement.setLayoutY(100);
            hBoxStatement.setPrefWidth(WIDTH);
            hBoxStatement.setAlignment(Pos.CENTER);
            Label lStatement = new Label(controller.getStatement());
            lStatement.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 12));
            lStatement.setMaxWidth(WIDTH-40);
            lStatement.setWrapText(true);
            lStatement.setTextFill(Color.CHOCOLATE);
            hBoxStatement.getChildren().add(lStatement);
            qstPane.getChildren().add(hBoxStatement);

            HBox hBoxBtns = new HBox(50);
            hBoxBtns.setLayoutY(250);
            hBoxBtns.setPrefWidth(WIDTH);
            hBoxBtns.setAlignment(Pos.CENTER);
            Button btnVrai = new Button("Vrai");
            btnVrai.setPrefSize(100, 75);
            Button btnMythe = new Button("Mythe");
            btnMythe.setPrefSize(100, 75);
            btnVrai.setOnAction(event -> {
                clickButton(hBoxBtns, btnVrai, btnMythe, lStatement, true);
            });
            btnMythe.setOnAction(event -> {
                clickButton(hBoxBtns, btnVrai, btnMythe, lStatement, false);
            });
            hBoxBtns.getChildren().addAll(btnVrai, btnMythe);
            qstPane.getChildren().add(hBoxBtns);

            pane.getChildren().add(qstPane);
        }
    }

    private void clickButton(HBox hBoxBtns, Button btnVrai, Button btnMythe, Label lStatement, boolean buttonValue) {
        controller.verifyAnswer(buttonValue);
        hBoxBtns.getChildren().removeAll(btnVrai, btnMythe);
        VBox vBox = new VBox(10);
            Label lTruth = new Label(controller.getTruth());
            lTruth.setFont(Font.font(20));
            lTruth.setTextFill(controller.getTruthValue()? Color.GREEN:Color.RED);

            Label lFact = new Label(controller.getFact());
            lFact.setFont(Font.font(12));
            lFact.setMaxWidth(WIDTH-40);
            lFact.setWrapText(true);

            Button btnContinue = new Button("Continue");
            btnContinue.setOnAction(event -> {
                questionMenu();
            });
        vBox.getChildren().addAll(lTruth, lStatement, btnContinue);
        hBoxBtns.getChildren().add(vBox);
    }

    public void results() {
        streakManager.recordAnsweredToday();
        HBox hBox = new HBox();
        hBox.setLayoutY(100);
        hBox.setPrefSize(WIDTH, HEIGHT);
        hBox.setAlignment(Pos.CENTER);

        Label label = new Label(controller.currentIndexQuestion+"/"+controller.NB_QUESTIONS);

        hBox.getChildren().addAll(label);
        pane.getChildren().add(hBox);
    }

    public void options() {
        HBox hBox = new HBox();
        hBox.setPrefSize(WIDTH, HEIGHT);
        hBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(5);
        //vBox.setBackground(new Background(backgroundImage));

        vBox.setPrefSize(WIDTH, HEIGHT);
        vBox.setAlignment(Pos.CENTER);

        Button btnImage = new Button("Put an image as the background");
        btnImage.setOnAction(event -> {
            //Image => be chosen by the user
        });

        vBox.getChildren().addAll(btnImage);
        hBox.getChildren().addAll(vBox);
        pane.getChildren().add(hBox);
    }

    public Image getRightBackground(String theme){
        Image background = null;

        System.out.println(theme);

        switch (theme){
            case "Hiver":
                background = new Image(getClass().getResource("/images/hiverBackground.jpg").toExternalForm());
                break;
            case "Mobilité":
                background = new Image(getClass().getResource("/images/mobiliteBackground.jpg").toExternalForm());
                break;
            case "Quartier & Écologie":
                background = new Image(getClass().getResource("/images/quartierBackground.jpg").toExternalForm());
                break;
            case "Culture & Quotidien":
                background = new Image(getClass().getResource("/images/cultureBackground.jpg").toExternalForm());
                break;
        }

        return background;
    }
}
