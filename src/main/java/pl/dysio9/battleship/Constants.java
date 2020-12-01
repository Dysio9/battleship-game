package pl.dysio9.battleship;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;

public class Constants {
    public static final boolean SHOW_OPPONENT_FLEET = true;
    public static final boolean SHOW_NEIGHBORS = false;
    public static final boolean SHOW_SUNK_SHIPS_NEIGHBORS = false;
    public static final int OPPONENT_DELAY = 500;

    public static final Image BACKGROUND_IMAGE = new Image("file:src/main/resources/background2.png");
    public static final ImageView SHIP_4_MAST = new ImageView(new Image("file:src/main/resources/ship4mast.png"));
    public static final Image SHIP_3_MAST = new Image("file:src/main/resources/ship3mast.png");
    public static final Image SHIP_2_MAST = new Image("file:src/main/resources/ship2mast.png");
    public static final Image SHIP_1_MAST = new Image("file:src/main/resources/ship1mast.png");
    public static final Image SHIP_4_MAST_SUNK = new Image("file:src/main/resources/ship4mast-sunk.png");
    public static final Image SHIP_3_MAST_SUNK = new Image("file:src/main/resources/ship3mast-sunk.png");
    public static final Image SHIP_2_MAST_SUNK = new Image("file:src/main/resources/ship2mast-sunk.png");
    public static final Image SHIP_1_MAST_SUNK = new Image("file:src/main/resources/ship1mast-sunk.png");

    public static final Image shipImage = new Image("file:src/main/resources/ship1mast.png");
    public static final Image shipSunkImage = new Image("file:src/main/resources/ship1mast-sunk.png");
    public static final ImagePattern SHOT_NEGATIVE_IMAGE = new ImagePattern(new Image("file:src/main/resources/shoot-negative.png"));
    public static final ImagePattern SHOT_POSITIVE_IMAGE = new ImagePattern(new Image("file:src/main/resources/shoot-positive.png"));
    public static final ImagePattern CELL_TRANSPARENT_IMAGE = new ImagePattern(new Image("file:src/main/resources/cellTransparent.png"));

    public static final String MENU_LABEL_TEXT_DEFAULT = "Place your ships\n pressing random.";
    public static final String MENU_LABEL_TEXT_PLAYER_TURN = "Player turn.\n Choose a target on\n the opponent's grid.";
    public static final String MENU_LABEL_TEXT_OPPONENT_TURN = "Opponent Turn.\n\nPlease wait...";
    public static final String MENU_LABEL_TEXT_SURRENDER = "You have surrender!\nTry the next time.";

    private Constants() {}
}
