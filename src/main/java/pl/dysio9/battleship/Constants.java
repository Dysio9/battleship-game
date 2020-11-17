package pl.dysio9.battleship;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Constants {
    private static Constants instanceConstants = null;
    private boolean showOpponentFleet = true;
    private boolean showNeighbors = true;

    private Image backgroundImage = new Image("file:src/main/resources/background2.png");
    private Image ship4mast = new Image("file:src/main/resources/ship4mast.png");
    private Image ship3mast = new Image("file:src/main/resources/ship3mast.png");
    private Image ship2mast = new Image("file:src/main/resources/ship2mast.png");
    private Image ship1mast = new Image("file:src/main/resources/ship1mast.png");
    private Image ship4mastSunk = new Image("file:src/main/resources/ship4mast-sunk.png");
    private Image ship3mastSunk = new Image("file:src/main/resources/ship3mast-sunk.png");
    private Image ship2mastSunk = new Image("file:src/main/resources/ship2mast-sunk.png");
    private Image ship1mastSunk = new Image("file:src/main/resources/ship1mast-sunk.png");

    Image shipImage = new Image("file:src/main/resources/ship1mast.png");
    Image shipSunkImage = new Image("file:src/main/resources/ship1mast-sunk.png");
    private Image shotNegativeImage = new Image("file:src/main/resources/shoot-negative.png");
    private Image shotPositiveImage = new Image("file:src/main/resources/shoot-positive.png");

    private String menuLabelTextDefault = "Place your ships\nor press random.";
    private String menuLabelTextPlayerTurn = "Player turn.\n Choose a target on\n the opponent's grid.";
    private String menuLabelTextOpponentTurn = "Opponent Turn.\nPlease wait...";
    private String menuLabelTestSurrender = "You have surrender!\nTry the next time.";

    private Constants() {}

    public static Constants getInstance() {
        if(instanceConstants == null) {
            instanceConstants = new Constants();
        }
        return instanceConstants;
    }

    public boolean showOpponentFleet() {
        return showOpponentFleet;
    }

    public boolean ShowNeighbors() {
        return showNeighbors;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public ImagePattern getShotNegativeImage() {
        return new ImagePattern(shotNegativeImage);
    }

    public ImagePattern getShotPositiveImage() {
        return new ImagePattern(shotPositiveImage);
    }

    public String getMenuLabelTextDefault() {
        return menuLabelTextDefault;
    }

    public String getMenuLabelTextPlayerTurn() {
        return menuLabelTextPlayerTurn;
    }

    public String getMenuLabelTextOpponentTurn() {
        return menuLabelTextOpponentTurn;
    }

    public String getMenuLabelTextSurrender() {
        return menuLabelTestSurrender;
    }
}
