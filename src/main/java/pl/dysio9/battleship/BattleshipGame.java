package pl.dysio9.battleship;

import static pl.dysio9.battleship.Constants.*;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class BattleshipGame extends Application {
    private final Controller controller = Controller.getInstance();
    private final Map<Cell, Ship> playerShips = controller.getPlayerShips();
    private final Map<Cell, Ship> opponentShips = controller.getOpponentShips();
    private final GridPane playgroundGridPlayer = controller.getPlaygroundGridPlayer();
    private final GridPane playgroundGridOpponent = controller.getPlaygroundGridOpponent();
    private final Timeline timeline = new Timeline();
    private boolean colorChange = true;
    private Label menuLabel;
    private FlowPane menuMiddleSection;
    private Button randomButton;
    private Button startButton;
    private Button nextRoundButton;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BackgroundSize backgroundSize = new BackgroundSize(950, 950, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(BACKGROUND_IMAGE, BackgroundRepeat.SPACE, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        BorderPane grid = new BorderPane();
        grid.setPadding(new Insets(20.0, 28.0, 28.0, 28.0));
        grid.setBackground(background);
        grid.setTop(createTopPanel());
        grid.setCenter(createTotalScoresPanel());
        grid.setBottom(createBottomPanel());

        Scene scene = new Scene(grid, 900, 620, Color.BLACK);

        primaryStage.setMinHeight(630.0);
        primaryStage.setMinWidth(910.0);
        primaryStage.setTitle("Battleship Game");
        primaryStage.setScene(scene);
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

    private BorderPane createBottomPanel() {
        controller.createPlaygroundGridPane(playerShips, playgroundGridPlayer,true);
        controller.createPlaygroundGridPane(opponentShips, playgroundGridOpponent,false);

        BorderPane bottomPanel = new BorderPane();
        bottomPanel.setLeft(playgroundGridPlayer);
        bottomPanel.setRight(playgroundGridOpponent);
        bottomPanel.setPrefHeight(380.0);
        return bottomPanel;
    }

    private FlowPane createTotalScoresPanel() {
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
        return topPanel;
    }

    private BorderPane createTopPanel() {
        menuLabel = new Label(MENU_LABEL_TEXT_DEFAULT);
        menuLabel.setFont(Font.font ("Verdana", FontWeight.BOLD, 18));
        menuLabel.setTextFill(Color.NAVY);
        controller.setMenuLabel(menuLabel);

        BorderPane topMenu = new BorderPane();
        topMenu.setLeft(createFileMenu());
        topMenu.setCenter(menuLabel);
        topMenu.setRight(createButtonsMenu());
        return topMenu;
    }

    private MenuBar createFileMenu() {
        // ---- game menu
        Menu gameMenu = new Menu("_Game");
        MenuItem newGame = new MenuItem("_New game");
        newGame.setOnAction(event -> {
            controller.startNewGame();
            controller.setGameStarted(false);
            menuLabel.setText(MENU_LABEL_TEXT_DEFAULT);
            menuMiddleSection.getChildren().clear();
            menuMiddleSection.getChildren().addAll(randomButton, startButton);
        });
        MenuItem loadGame = new MenuItem("_Load game");
        loadGame.setOnAction(event -> controller.loadGame());
        MenuItem saveGame = new MenuItem("_Save game");
        saveGame.setOnAction(event -> controller.saveGame());
        MenuItem exitGame = new MenuItem("E_xit");
        exitGame.setOnAction(event -> Platform.exit());
        gameMenu.getItems().addAll(newGame, loadGame, saveGame, new SeparatorMenuItem(), exitGame);

        // ---- difficulty menu
        Menu difficultyMenu = new Menu("_Difficulty");
        ToggleGroup difficultiesToggle = new ToggleGroup();
        RadioMenuItem easy = new RadioMenuItem("_Easy");
        easy.setOnAction(event -> controller.setDifficultyLevel(1));
        RadioMenuItem medium = new RadioMenuItem("_Medium");
        medium.setOnAction(event -> controller.setDifficultyLevel(2));
        medium.setSelected(true);
        easy.setToggleGroup(difficultiesToggle);
        medium.setToggleGroup(difficultiesToggle);
        difficultyMenu.getItems().addAll(easy, medium);

        // ---- how to play
        Menu howToPlayMenu = new Menu("Ho_w to play");
        MenuItem howToPlay = new MenuItem("Ho_w to play");
        howToPlay.setOnAction(event -> HowToPlayStage.display());
        howToPlayMenu.getItems().add(howToPlay);

        MenuBar menuBar = new MenuBar();
        menuBar.setBackground(Background.EMPTY);
        menuBar.getMenus().addAll(gameMenu, difficultyMenu, howToPlayMenu);

        return menuBar;
    }

    private FlowPane createButtonsMenu() {
        randomButton = new Button("Random");
        controller.setRandomButton(randomButton);
        randomButton.setPrefSize(100, 22);
        startButton = new Button("Start");
        controller.setStartButton(startButton);
        startButton.setPrefSize(100, 22);
        Button surrenderButton = new Button("Surrender");
        controller.setSurrenderButton(surrenderButton);
        surrenderButton.setPrefSize(200, 22);
        nextRoundButton = new Button("Play another round");
        controller.setNextRoundButton(nextRoundButton);
        nextRoundButton.setPrefSize(200, 22);

        makeButtonBlink(randomButton);

        menuMiddleSection = new FlowPane(Orientation.HORIZONTAL);
        menuMiddleSection.setAlignment(Pos.TOP_RIGHT);
        menuMiddleSection.setPrefWidth(200.0);
        menuMiddleSection.setVgap(10.0);
        controller.setMenuMiddleSection(menuMiddleSection);
        menuMiddleSection.getChildren().addAll(randomButton, startButton);

        randomButton.setOnAction(e -> {
            controller.placeShipsRandomly(true);
            controller.createPlaygroundGridPane(playerShips, playgroundGridPlayer, true);
            controller.placeShipsRandomly(false);
            controller.createPlaygroundGridPane(opponentShips, playgroundGridOpponent, false);
            menuLabel.setText(MENU_LABEL_TEXT_PLACED_SHIPS);
            makeButtonsStopBlinking();
        });
        startButton.setOnAction(e -> {
            if (controller.shipsArePlaced(true) && controller.shipsArePlaced(false)) {
                controller.setPlayerTurn(true);
                controller.setGameStarted(true);
                menuLabel.setText(MENU_LABEL_TEXT_PLAYER_TURN);
                menuMiddleSection.getChildren().remove(randomButton);
                menuMiddleSection.getChildren().remove(startButton);
                menuMiddleSection.getChildren().add(surrenderButton);
            } else {
                menuLabel.setText(MENU_LABEL_TEXT_DEFAULT);
            }
            makeButtonBlink(randomButton);
        });
        surrenderButton.setOnAction(e -> {
            controller.setGameStarted(false);
            controller.roundLost();
            menuLabel.setText(MENU_LABEL_TEXT_SURRENDER);
            menuMiddleSection.getChildren().clear();
            menuMiddleSection.getChildren().add(nextRoundButton);
        });
        nextRoundButton.setOnAction(e -> {
            playerShips.clear();
            opponentShips.clear();
            menuLabel.setText(MENU_LABEL_TEXT_DEFAULT);
            menuMiddleSection.getChildren().clear();
            menuMiddleSection.getChildren().addAll(randomButton, startButton);
        });

        return menuMiddleSection;
    }

    private void makeButtonBlink(Button button) {
        EventHandler<ActionEvent> on_finished = (ActionEvent event) -> {
            if (colorChange) {
                button.setStyle ("-fx-base: #f2f2f2");
                colorChange = false;
            } else {
                button.setStyle ("-fx-base: #b9bbb6");
                colorChange = true;
            }
        };
        KeyFrame keyframe = new KeyFrame(Duration.millis(400), on_finished);

        timeline.getKeyFrames().add(keyframe);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void makeButtonsStopBlinking() {
        timeline.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
