package pl.dysio9.battleship;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.*;

public class BattleshipGame extends Application {
    private int totalScorePlayer = 0;
    private int totalScoreOpponent = 0;
    private List<Ship> playerShips = new ArrayList<>();
    private List<Ship> opponentShips = new ArrayList<>();

    private Image imageback = new Image("file:src/main/resources/background2.png");
    private Image ship4mast = new Image("file:src/main/resources/ship4mast.png");
    private Image ship3mast = new Image("file:src/main/resources/ship3mast.png");
    private Image ship2mast = new Image("file:src/main/resources/ship2mast.png");
    private Image ship1mast = new Image("file:src/main/resources/ship1mast.png");
    private Image ship4mastSinked = new Image("file:src/main/resources/ship4mast-sunk.png");
    private Image ship3mastSinked = new Image("file:src/main/resources/ship3mast-sunk.png");
    private Image ship2mastSinked = new Image("file:src/main/resources/ship2mast-sunk.png");
    private Image ship1mastSinked = new Image("file:src/main/resources/ship1mast-sunk.png");
    GridPane playgroundGridPlayer;
    GridPane playgroundGridOpponent;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BackgroundSize backgroundSize = new BackgroundSize(950, 950, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.SPACE, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        BorderPane grid = new BorderPane();
        grid.setPadding(new Insets(38.0, 38.0, 38.0, 38.0));
        grid.setBackground(background);

// ------------------------------------------- Top Section --------------------------------------------------
        FlowPane topPanel = new FlowPane(Orientation.HORIZONTAL);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPrefHeight(76.0);
        topPanel.setHgap(65.0);
        topPanel.getChildren().addAll(playersNamesLabel("Player", Pos.BOTTOM_LEFT), totalScoreLabel(totalScorePlayer), timerLabel(), totalScoreLabel(totalScoreOpponent), playersNamesLabel("Opponent", Pos.BOTTOM_RIGHT));

// ------------------------------------------- Middle Section -------------------------------------------------
        HBox ships4masts = new HBox(new ImageView(ship4mast));
        HBox ships3masts = new HBox(new ImageView(ship3mast), new ImageView(ship3mast));
        ships3masts.setSpacing(38.0);
        HBox ships2masts = new HBox(new ImageView(ship2mast), new ImageView(ship2mast), new ImageView(ship2mast));
        ships2masts.setSpacing(38.0);
        HBox ships1masts = new HBox(new ImageView(ship1mast), new ImageView(ship1mast), new ImageView(ship1mast), new ImageView(ship1mast));
        ships1masts.setSpacing(38.0);

        HBox ships4mastsOpponent = new HBox(new ImageView(ship4mastSinked));
        ships4mastsOpponent.setAlignment(Pos.TOP_RIGHT);
        HBox ships3mastsOpponent = new HBox(new ImageView(ship3mastSinked), new ImageView(ship3mastSinked));
        ships3mastsOpponent.setSpacing(38.0);
        ships3mastsOpponent.setAlignment(Pos.TOP_RIGHT);
        HBox ships2mastsOpponent = new HBox(new ImageView(ship2mastSinked), new ImageView(ship2mastSinked), new ImageView(ship2mastSinked));
        ships2mastsOpponent.setSpacing(38.0);
        ships2mastsOpponent.setAlignment(Pos.TOP_RIGHT);
        HBox ships1mastsOpponent = new HBox(new ImageView(ship1mastSinked), new ImageView(ship1mastSinked), new ImageView(ship1mastSinked), new ImageView(ship1mastSinked));
        ships1mastsOpponent.setSpacing(38.0);
        ships1mastsOpponent.setAlignment(Pos.TOP_RIGHT);

        // Player fleet
        FlowPane fleetPlayer = new FlowPane(Orientation.VERTICAL);
        fleetPlayer.setAlignment(Pos.TOP_LEFT);
        fleetPlayer.setVgap(22.0);
        fleetPlayer.setPrefWidth(342.0);
//        fleetPlayer.setPrefHeight(266.0);
        fleetPlayer.getChildren().addAll(ships4masts, ships3masts, ships2masts, ships1masts);

        // MenuBar section
        Button random = new Button();
        random.setText("Random");
        random.setOnAction(e -> {
                placePlayerShips();
                updatePlaygroundGrid(playerShips, playgroundGridPlayer);
                placeOpponentShips();
                updatePlaygroundGrid(opponentShips,playgroundGridOpponent);
        });

        FlowPane menubar = new FlowPane(Orientation.VERTICAL);
        menubar.setAlignment(Pos.BASELINE_CENTER);
        menubar.setPrefWidth(190.0);
//        menubar.setPrefHeight(266.0);
        menubar.getChildren().addAll(new Label("Mode"),
                new Button("Select mode"),
                new Label("Place your ships, or press"),
                random,
                new Button("START"),
                new Button("How to play"));

        // Opponent fleet
        FlowPane fleetOpponent = new FlowPane(Orientation.VERTICAL);
        fleetOpponent.setAlignment(Pos.TOP_RIGHT);
        fleetOpponent.setVgap(22.0);
        fleetOpponent.setPrefWidth(342.0);
//        fleetOpponent.setPrefHeight(266.0);
        fleetOpponent.getChildren().addAll(ships4mastsOpponent, ships3mastsOpponent, ships2mastsOpponent, ships1mastsOpponent);

        BorderPane middlePanel = new BorderPane();
        middlePanel.setLeft(fleetPlayer);
        middlePanel.setCenter(menubar);
        middlePanel.setRight(fleetOpponent);

//// --------------------------------------- Bottom Section -----------------------------------------
        playgroundGridPlayer = new GridPane();
        playgroundGridPlayer.setGridLinesVisible(true);
        playgroundGridPlayer.setPrefSize(380.0, 380.0);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Cell cell = new Cell(x, y);
                playgroundGridPlayer.add(cell, x, y);
            }
        }

        playgroundGridOpponent = new GridPane();
        playgroundGridOpponent.setGridLinesVisible(true);
        playgroundGridOpponent.setPrefSize(380.0, 380.0);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Cell cell = new Cell(x, y);
                playgroundGridOpponent.add(cell, x, y);
            }
        }

        BorderPane bottomPanel = new BorderPane();
        bottomPanel.setLeft(playgroundGridPlayer);
        bottomPanel.setRight(playgroundGridOpponent);
        bottomPanel.setPrefHeight(380.0);
//        bottomPanel.setPrefWidth(950.0);

// --------------------------------------- Stage Section ---------------------------------
        grid.setTop(topPanel);
        grid.setCenter(middlePanel);
        grid.setBottom(bottomPanel);

        Scene scene = new Scene(grid, 900, 760, Color.BLACK);

        primaryStage.setMinHeight(880.0);
        primaryStage.setMinWidth(900.0);
        primaryStage.setTitle("Battleship Game");
        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private VBox totalScoreLabel(int totalScore) {
        Label totalScoreTextLabel = new Label("Total Score");
        totalScoreTextLabel.setFont(new Font("Arial", 12));
        totalScoreTextLabel.setTextFill(Color.web("#0d1ea7"));

        Label totalScoreLabel = new Label(String.valueOf(totalScore));
        totalScoreLabel.setFont(new Font("Arial", 36));
        totalScoreLabel.setTextFill(Color.web("#0d1ea7"));
        VBox totalScoreVBox = new VBox(totalScoreTextLabel, totalScoreLabel);
        totalScoreVBox.setAlignment(Pos.CENTER);
        return totalScoreVBox;
    }

    private VBox timerLabel() {
        Label timerTextLabel = new Label("Time");
        timerTextLabel.setFont(new Font("Arial", 12));
        timerTextLabel.setTextFill(Color.web("#0d1ea7"));

        Label timer = new Label("Timer");
        timer.setFont(new Font("Arial", 28));
        timer.setTextFill(Color.web("#0d1ea7"));
        VBox timerVBox = new VBox(timerTextLabel, timer);
        timerVBox.setAlignment(Pos.CENTER);
        return timerVBox;
    }

    private VBox playersNamesLabel(String name, Pos position) {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        Label playerNameLabel = new Label(name);
        playerNameLabel.setEffect(ds);
        playerNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        playerNameLabel.setTextFill(Color.web("#0d1ea7"));


        Label fleetTextLabel = new Label("Fleet");
        fleetTextLabel.setEffect(ds);
        fleetTextLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        fleetTextLabel.setTextFill(Color.web("#0d1ea7"));
        VBox playersNameVBox = new VBox(playerNameLabel, fleetTextLabel);
        playersNameVBox.setAlignment(position);
        playersNameVBox.setPrefWidth(180.0);
        return playersNameVBox;
    }

    private void placePlayerShips() {

        playerShips = new ArrayList<>();

        playerShips.add(new Ship(0,0, 1, true));
        playerShips.add(new Ship(2,1, 1, true));
        playerShips.add(new Ship(4,2, 1, true));
        playerShips.add(new Ship(6,3, 1, true));
        playerShips.add(new Ship(8,4, 2, true));
        playerShips.add(new Ship(0,3, 3, false));

    }

    private void placeOpponentShips() {

        opponentShips = new ArrayList<>();

        opponentShips.add(new Ship(0,3, 1, true));
        opponentShips.add(new Ship(2,2, 1, true));
        opponentShips.add(new Ship(4,1, 1, true));
        opponentShips.add(new Ship(6,0, 1, true));
        opponentShips.add(new Ship(8,4, 2, true));
        opponentShips.add(new Ship(3,5, 3, false));
        opponentShips.add(new Ship(0,0, 1, true));


    }

    public GridPane updatePlaygroundGrid(List<Ship> personShips, GridPane playgroundGridOfPerson) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                for (Ship s : personShips) {
                    if (x == s.getX() && y == s.getY()) {
                        switch (s.getMasts()) {
                            case 4:
                                playgroundGridOfPerson.add(new Cell(s.getX3(), s.getY3(), s), s.getX3(), s.getY3());
                            case 3:
                                playgroundGridOfPerson.add(new Cell(s.getX2(), s.getY2(), s), s.getX2(), s.getY2());
                            case 2:
                                playgroundGridOfPerson.add(new Cell(s.getX1(), s.getY1(), s), s.getX1(), s.getY1());
                            case 1:
                                playgroundGridOfPerson.add(new Cell(s.getX(), s.getY(), s), s.getX(), s.getY());
                        }
                    } else {
                        Cell cell = new Cell(x, y);
                        playgroundGridOfPerson.add(cell, x, y);
                    }
                }
            }
        }
        return playgroundGridOfPerson;
    }

    public boolean isValidCell(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }



    public static void main(String[] args) {
        launch(args);
    }
}
