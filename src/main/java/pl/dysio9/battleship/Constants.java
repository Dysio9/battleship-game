package pl.dysio9.battleship;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Constants {
    public static final boolean SHOW_OPPONENT_FLEET = false;
    public static final boolean SHOW_NEIGHBORS = false;
    public static final boolean SHOW_SUNK_SHIPS_NEIGHBORS = false;
    public static final int OPPONENT_DELAY = 500;

    public static final Image BACKGROUND_IMAGE = new Image("file:src/main/resources/background2.png");
    public static final Image HOW_TO_PLAY_IMAGE = new Image("file:src/main/resources/howToPlay.jpg");

    public static final ImagePattern SHOT_NEGATIVE_IMAGE = new ImagePattern(new Image("file:src/main/resources/shoot-negative.png"));
    public static final ImagePattern SHOT_POSITIVE_IMAGE = new ImagePattern(new Image("file:src/main/resources/shoot-positive.png"));

    public static final String MENU_LABEL_TEXT_DEFAULT = "Place your ships pressing random.";
    public static final String MENU_LABEL_TEXT_PLAYER_TURN = "Player turn. Choose a target.";
    public static final String MENU_LABEL_TEXT_OPPONENT_TURN = "Opponent Turn. Please wait...";
    public static final String MENU_LABEL_TEXT_SURRENDER = "You have surrender! Try the next time.";
    public static final String MENU_LABEL_TEXT_PLACED_SHIPS = "Ships have been placed. Start the game.";

    private Constants() {}
}
