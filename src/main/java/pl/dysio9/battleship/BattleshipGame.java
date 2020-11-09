package pl.dysio9.battleship;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.*;

public class BattleshipGame extends Application {
    private int totalScorePlayer = 0;
    private int totalScoreOpponent = 0;
    private Controller controller = Controller.getInstance();
    private Map<Cell, Ship> playerShips = controller.getPlayerShips();
    private Map<Cell, Ship> opponentShips = controller.getOpponentShips();

    private Image imageback = new Image("file:src/main/resources/background2.png");
    private Image ship4mast = new Image("file:src/main/resources/ship4mast.png");
    private Image ship3mast = new Image("file:src/main/resources/ship3mast.png");
    private Image ship2mast = new Image("file:src/main/resources/ship2mast.png");
    private Image ship1mast = new Image("file:src/main/resources/ship1mast.png");
    private Image ship4mastSunk = new Image("file:src/main/resources/ship4mast-sunk.png");
    private Image ship3mastSunk = new Image("file:src/main/resources/ship3mast-sunk.png");
    private Image ship2mastSunk = new Image("file:src/main/resources/ship2mast-sunk.png");
    private Image ship1mastSunk = new Image("file:src/main/resources/ship1mast-sunk.png");
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

        HBox ships4mastsOpponent = new HBox(new ImageView(ship4mastSunk));
        ships4mastsOpponent.setAlignment(Pos.TOP_RIGHT);
        HBox ships3mastsOpponent = new HBox(new ImageView(ship3mastSunk), new ImageView(ship3mastSunk));
        ships3mastsOpponent.setSpacing(38.0);
        ships3mastsOpponent.setAlignment(Pos.TOP_RIGHT);
        HBox ships2mastsOpponent = new HBox(new ImageView(ship2mastSunk), new ImageView(ship2mastSunk), new ImageView(ship2mastSunk));
        ships2mastsOpponent.setSpacing(38.0);
        ships2mastsOpponent.setAlignment(Pos.TOP_RIGHT);
        HBox ships1mastsOpponent = new HBox(new ImageView(ship1mastSunk), new ImageView(ship1mastSunk), new ImageView(ship1mastSunk), new ImageView(ship1mastSunk));
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
            updatePlaygroundGrid(opponentShips, playgroundGridOpponent);

            System.out.println("Statki playera:");
            playerShips.entrySet().stream()
                    .map(a -> a.getKey().getValX() + "," + a.getKey().getValY() + " - masztów:" + a.getValue().getMasts() + ", czy jest zatopiony:" + a.getValue().isSunk() + ", czy strzelano w to pole:" + a.getKey().wasEverShot() + a.getKey().getShip())
                    .forEach(System.out::println);
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
        updatePlaygroundGrid(playerShips, playgroundGridPlayer);


        playgroundGridOpponent = new GridPane();
        updatePlaygroundGrid(opponentShips, playgroundGridOpponent);
//        playgroundGridOpponent.setGridLinesVisible(true);
//        playgroundGridOpponent.setPrefSize(380.0, 380.0);
//        for (int x = 0; x < 10; x++) {
//            for (int y = 0; y < 10; y++) {
//                Cell cell = new Cell(x, y);
//                cell.setFill(Color.TRANSPARENT);
//                cell.setStroke(Color.TRANSPARENT);
//                playgroundGridOpponent.add(cell, x, y);
//            }
//        }

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

    private Map<Cell, Ship> addShip (Map<Cell, Ship> shipsMap, Ship ship) {
        Cell cell;
        for (int i = 1; i <= ship.getMasts(); i++) {
            switch (i) {
                case 4:
                    cell = new Cell(ship.getX3(), ship.getY3(), ship);
                    break;
                case 3:
                    cell = new Cell(ship.getX2(), ship.getY2(), ship);
                    break;
                case 2:
                    cell = new Cell(ship.getX1(), ship.getY1(), ship);
                    break;
                default:
                    cell = new Cell(ship.getX(), ship.getY(), ship);
                    break;
            }
            shipsMap.put(cell, ship);
        }
        return shipsMap;
    }

//    private Map<Cell, Ship> placeShipsRandom (Map<Cell, Ship> shipsMap) {
//        addShip(shipsMap, new Ship(0,0, 1, true));
//        addShip(shipsMap, new Ship(2,1, 1, true));
//        addShip(shipsMap, new Ship(4,2, 1, true));
//        addShip(shipsMap, new Ship(6,3, 1, true));
//        addShip(shipsMap, new Ship(8,4, 2, true));
//        addShip(shipsMap, new Ship(0,3, 3, false));
//        return shipsMap;
//    }

    private void placePlayerShips() {
        addShip(playerShips, new Ship(0,0, 1, true));
        addShip(playerShips, new Ship(2,1, 1, true));
        addShip(playerShips, new Ship(4,2, 1, false));
        addShip(playerShips, new Ship(6,3, 1, true));
        addShip(playerShips, new Ship(8,0, 2, true));
        addShip(playerShips, new Ship(8,2, 2, true));
        addShip(playerShips, new Ship(8,4, 2, true));
        addShip(playerShips, new Ship(0,3, 3, false));
        addShip(playerShips, new Ship(0,7, 3, false));
        addShip(playerShips, new Ship(6,9, 4, true));
    }

    private void  placeOpponentShips() {
        addShip(opponentShips, new Ship(0,3, 1, true));
        addShip(opponentShips, new Ship(2,2, 1, true));
        addShip(opponentShips, new Ship(4,1, 1, false));
        addShip(opponentShips, new Ship(6,0, 1, true));
        addShip(opponentShips, new Ship(8,2, 2, true));
        addShip(opponentShips, new Ship(8,0, 2, true));
        addShip(opponentShips, new Ship(8,4, 2, false));
        addShip(opponentShips, new Ship(3,5, 3, false));
        addShip(opponentShips, new Ship(9,0, 3, true));
        addShip(opponentShips, new Ship(5,7, 4, true));
    }

    public GridPane updatePlaygroundGrid(Map<Cell, Ship> shipsMap, GridPane playgroundGridOfPerson) {
//        List<Cell> cellsTaken = shipsMap.keySet().stream().map(e -> new Cell(e.getValX(), e.getValY())).collect(Collectors.toList());
//        System.out.println(cellsTaken);

        playgroundGridOfPerson.getChildren().clear();

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Cell cell = new Cell(x,y);
                if (shipsMap.containsKey(cell)) {
                    cell = new Cell(x, y, shipsMap.get(cell));
                    if (shipsMap.get(cell).isSunk()) {
                        cell.setFill(Color.RED);
                        cell.setStroke(Color.BLACK);
                    } else {
                        cell.setFill(Color.LIGHTBLUE);
                        cell.setStroke(Color.BLACK);
                    }
                } else {
                    cell.setFill(Color.TRANSPARENT);
                    cell.setStroke(Color.BLACK);
                }
                playgroundGridOfPerson.add(cell, x, y);
            }
        }
        playgroundGridOfPerson.setGridLinesVisible(false);
        playgroundGridOfPerson.setPrefSize(380.0, 380.0);

        return playgroundGridOfPerson;
    }

    public boolean isValidCell(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }



    public static void main(String[] args) {
        launch(args);
    }
}
