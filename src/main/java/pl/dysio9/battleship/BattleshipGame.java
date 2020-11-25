package pl.dysio9.battleship;

import static pl.dysio9.battleship.Constants.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.*;

public class BattleshipGame extends Application {
    private Controller controller = Controller.getInstance();
    private Map<Cell, Ship> playerShips = controller.getPlayerShips();
    private Map<Cell, Ship> opponentShips = controller.getOpponentShips();
    private GridPane playgroundGridPlayer = controller.getPlaygroundGridPlayer();
    private GridPane playgroundGridOpponent = controller.getPlaygroundGridOpponent();


    @Override
    public void start(Stage primaryStage) throws Exception {
        BackgroundSize backgroundSize = new BackgroundSize(950, 950, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(BACKGROUND_IMAGE, BackgroundRepeat.SPACE, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        BorderPane grid = new BorderPane();
        grid.setPadding(new Insets(38.0, 38.0, 38.0, 38.0));
        grid.setBackground(background);

// ------------------------------------------- Top Section --------------------------------------------------
        FlowPane topPanel = new FlowPane(Orientation.HORIZONTAL);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPrefHeight(76.0);
        topPanel.setHgap(65.0);
        topPanel.getChildren().addAll(
                playersNamesLabel("Player", Pos.BOTTOM_LEFT),
                totalScorePlayerLabel(),
                timerLabel(),
                totalScoreOpponentLabel(),
                playersNamesLabel("Opponent", Pos.BOTTOM_RIGHT)
        );

// ------------------------------------------- Middle Section -------------------------------------------------
        HBox ships4masts = new HBox(SHIP_4_MAST);
        HBox ships3masts = new HBox(new ImageView(SHIP_3_MAST), new ImageView(SHIP_3_MAST));
        ships3masts.setSpacing(38.0);
        HBox ships2masts = new HBox(new ImageView(SHIP_2_MAST), new ImageView(SHIP_2_MAST), new ImageView(SHIP_2_MAST));
        ships2masts.setSpacing(38.0);
        HBox ships1masts = new HBox(new ImageView(SHIP_1_MAST), new ImageView(SHIP_1_MAST), new ImageView(SHIP_1_MAST), new ImageView(SHIP_1_MAST));
        ships1masts.setSpacing(38.0);

        HBox ships4mastsOpponent = new HBox(new ImageView(SHIP_4_MAST_SUNK));
        ships4mastsOpponent.setAlignment(Pos.TOP_RIGHT);
        HBox ships3mastsOpponent = new HBox(new ImageView(SHIP_3_MAST_SUNK), new ImageView(SHIP_3_MAST_SUNK));
        ships3mastsOpponent.setSpacing(38.0);
        ships3mastsOpponent.setAlignment(Pos.TOP_RIGHT);
        HBox ships2mastsOpponent = new HBox(new ImageView(SHIP_2_MAST_SUNK), new ImageView(SHIP_2_MAST_SUNK), new ImageView(SHIP_2_MAST_SUNK));
        ships2mastsOpponent.setSpacing(38.0);
        ships2mastsOpponent.setAlignment(Pos.TOP_RIGHT);
        HBox ships1mastsOpponent = new HBox(new ImageView(SHIP_1_MAST_SUNK), new ImageView(SHIP_1_MAST_SUNK), new ImageView(SHIP_1_MAST_SUNK), new ImageView(SHIP_1_MAST_SUNK));
        ships1mastsOpponent.setSpacing(38.0);
        ships1mastsOpponent.setAlignment(Pos.TOP_RIGHT);

        // Player fleet
        FlowPane fleetPlayer = new FlowPane(Orientation.VERTICAL);
        fleetPlayer.setAlignment(Pos.TOP_LEFT);
        fleetPlayer.setVgap(22.0);
        fleetPlayer.setPrefWidth(312.0);
        fleetPlayer.getChildren().addAll(ships4masts, ships3masts, ships2masts, ships1masts);

        // MenuBar section
        Label menuTopLabel = new Label("New Game");
        Button randomButton = new Button("Random");
        controller.setRandomButton(randomButton);
        randomButton.setPrefWidth(190);
        Button newGameButton = new Button("Clear Total Scores");
        newGameButton.setPrefWidth(190);
        Button loadButton = new Button("Load");
        loadButton.setPrefWidth(190);
        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(190);
        VBox menuTopVBox = new VBox(menuTopLabel, newGameButton, loadButton, saveButton);
        menuTopVBox.setAlignment(Pos.CENTER);
        menuTopVBox.setPadding(new Insets(2, 0, 2, 0));
        menuTopVBox.setSpacing(2);

        Label menuLabel = new Label(MENU_LABEL_TEXT_DEFAULT);
        controller.setMenuLabel(menuLabel);
        Button startButton = new Button("Start");
        controller.setStartButton(startButton);
        startButton.setPrefWidth(190);
        Button surrenderButton = new Button("Surrender");
        controller.setSurrenderButton(surrenderButton);
        surrenderButton.setPrefWidth(190);
        Button howToPlayButton = new Button("Player Win this round");
        howToPlayButton.setPrefWidth(190);
        Button nextRoundButton = new Button("Play another round");
        controller.setNextRoundButton(nextRoundButton);
        howToPlayButton.setPrefWidth(190);

        BorderPane menuMiddleSection = new BorderPane();
        menuMiddleSection.setPadding(new Insets(0, 0, 0, 0));
        menuMiddleSection.setPrefWidth(190.0);
        menuMiddleSection.setBorder(new Border(new BorderStroke(Color.YELLOW,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        menuMiddleSection.setTop(menuLabel);
        menuMiddleSection.setCenter(randomButton);
        menuMiddleSection.setBottom(startButton);
        controller.setMenuMiddleSection(menuMiddleSection);
        BorderPane.setAlignment(menuLabel, Pos.CENTER);
        BorderPane.setAlignment(startButton, Pos.CENTER);
        BorderPane.setAlignment(surrenderButton, Pos.CENTER);
        BorderPane.setAlignment(nextRoundButton, Pos.CENTER);
        BorderPane.setAlignment(howToPlayButton, Pos.CENTER);

        randomButton.setOnAction(e -> {
            controller.placeShipsRandomly(true);
            controller.createPlaygroundGridPane(playerShips, playgroundGridPlayer,true);
            controller.placeShipsRandomly(false);
            controller.createPlaygroundGridPane(opponentShips, playgroundGridOpponent, false);
            menuLabel.setText("Ships have been placed \n start the game");

//            System.out.println("Statki playera:");
//            playerShips.entrySet().stream()
//                    .map(a -> a.getKey().getValX() + "," + a.getKey().getValY() + " - masztów:" + a.getValue().getMasts() + ", czy jest zatopiony:" + a.getValue().isSunk() + ", czy strzelano w to pole:" + a.getKey().wasEverShot())
//                    .forEach(System.out::println);
        });
        newGameButton.setOnAction(e -> controller.clearTotalScores());
        loadButton.setOnAction(e -> controller.loadTotalScores());
        saveButton.setOnAction(e -> controller.saveTotalScores());
        startButton.setOnAction(e -> {
            if (controller.shipsArePlaced(true) && controller.shipsArePlaced(false)) {
                controller.setPlayerTurn(true);
                controller.setGameStarted(true);
                menuLabel.setText(MENU_LABEL_TEXT_PLAYER_TURN);
                menuMiddleSection.getChildren().remove(randomButton);
                menuMiddleSection.setBottom(surrenderButton);
            } else {
                menuLabel.setText(MENU_LABEL_TEXT_DEFAULT);
            }
        });
        surrenderButton.setOnAction(e -> {
            controller.setGameStarted(false);
            controller.roundLost();
            menuLabel.setText(MENU_LABEL_TEXT_SURRENDER);
            menuMiddleSection.setCenter(nextRoundButton);
            menuMiddleSection.getChildren().remove(surrenderButton);
        });
        nextRoundButton.setOnAction(e -> {
            playerShips.clear();
            opponentShips.clear();
            menuLabel.setText(MENU_LABEL_TEXT_DEFAULT);
            menuMiddleSection.setCenter(randomButton);
            menuMiddleSection.setBottom(startButton);
        });
        howToPlayButton.setOnAction(e -> {
            controller.roundWin();
        });

        BorderPane menubar = new BorderPane();
        menubar.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        menubar.setPadding(new Insets(0,0,0,0));
        menubar.setTop(menuTopVBox);
        menubar.setCenter(menuMiddleSection);
        menubar.setBottom(howToPlayButton);

        // Opponent fleet
        FlowPane fleetOpponent = new FlowPane(Orientation.VERTICAL);
        fleetOpponent.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        fleetOpponent.setAlignment(Pos.TOP_RIGHT);
        fleetOpponent.setVgap(22.0);
        fleetOpponent.setPrefWidth(312.0);
        fleetOpponent.getChildren().addAll(ships4mastsOpponent, ships3mastsOpponent, ships2mastsOpponent, ships1mastsOpponent);

        BorderPane middlePanel = new BorderPane();
        middlePanel.setLeft(fleetPlayer);
        middlePanel.setCenter(menubar);
        middlePanel.setRight(fleetOpponent);

//// --------------------------------------- Bottom Section -----------------------------------------
        controller.createPlaygroundGridPane(playerShips, playgroundGridPlayer,true);
        controller.createPlaygroundGridPane(opponentShips, playgroundGridOpponent,false);

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

        primaryStage.setMinHeight(900.0);
        primaryStage.setMinWidth(910.0);
        primaryStage.setTitle("Battleship Game");
        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private VBox totalScorePlayerLabel() {
        Label totalScoreTextLabel = new Label("Total Score");
        totalScoreTextLabel.setFont(new Font("Arial", 12));
        totalScoreTextLabel.setTextFill(Color.NAVY);

        Label totalScorePlayerLabel = new Label(String.valueOf(controller.getTotalScorePlayer()));
        controller.setTotalScorePlayerLabel(totalScorePlayerLabel);
        totalScorePlayerLabel.setFont(new Font("Arial", 36));
        totalScorePlayerLabel.setTextFill(Color.NAVY);
        VBox totalScoreVBox;
        totalScoreVBox = new VBox(totalScoreTextLabel, totalScorePlayerLabel);
        totalScoreVBox.setAlignment(Pos.CENTER);
        return totalScoreVBox;
    }

    private VBox totalScoreOpponentLabel() {
        Label totalScoreTextLabel = new Label("Total Score");
        totalScoreTextLabel.setFont(new Font("Arial", 12));
        totalScoreTextLabel.setTextFill(Color.NAVY);

        Label totalScoreOpponentLabel = new Label(String.valueOf(controller.getTotalScoreOpponent()));
        controller.setTotalScoreOpponentLabel(totalScoreOpponentLabel);
        totalScoreOpponentLabel.setFont(new Font("Arial", 36));
        totalScoreOpponentLabel.setTextFill(Color.NAVY);
        VBox totalScoreVBox;
        totalScoreVBox = new VBox(totalScoreTextLabel, totalScoreOpponentLabel);
        totalScoreVBox.setAlignment(Pos.CENTER);
        return totalScoreVBox;
    }

    private VBox timerLabel() {
        Label timerTextLabel = new Label("Time");
        timerTextLabel.setFont(new Font("Arial", 12));
        timerTextLabel.setTextFill(Color.NAVY);

        Label timer = new Label("Timer");
        timer.setFont(new Font("Arial", 28));
        timer.setTextFill(Color.NAVY);
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
        playerNameLabel.setTextFill(Color.NAVY);


        Label fleetTextLabel = new Label("Fleet");
        fleetTextLabel.setEffect(ds);
        fleetTextLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        fleetTextLabel.setTextFill(Color.NAVY);
        VBox playersNameVBox = new VBox(playerNameLabel, fleetTextLabel);
        playersNameVBox.setAlignment(position);
        playersNameVBox.setPrefWidth(180.0);
        return playersNameVBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
