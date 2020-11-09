package pl.dysio9.battleship;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

import javafx.scene.paint.ImagePattern;

public class Controller {
    private Map<Cell, Ship> playerShips = new HashMap<>();
    private Map<Cell, Ship> opponentShips = new HashMap<>();
    private static Controller instance = null;

    Image shipImage = new Image("file:src/main/resources/ship1mast.png");
    Image shipSunkImage = new Image("file:src/main/resources/ship1mast-sunk.png");
    Image shotNegativeImage = new Image("file:src/main/resources/shoot-negative.png");
    Image shotPositiveImage = new Image("file:src/main/resources/shoot-positive.png");


    private Controller() {
        // Exists only to defeat instantiation.
    }

    public static Controller getInstance() {
        if(instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public Map<Cell, Ship> getPlayerShips() {
        return playerShips;
    }

    public Map<Cell, Ship> getOpponentShips() {
        return opponentShips;
    }

    public void cellClicked(Cell cell) {
        System.out.println("Clicked x="+cell.getValX()+ " y="+cell.getValY());
        int x = cell.getValX();
        int y = cell.getValY();

//        cell.getShip().hit();
//        cell.setEverShot(true);

        ImagePattern positiveShotImage = new ImagePattern(shotPositiveImage);
        ImagePattern negativeShotImage = new ImagePattern(shotNegativeImage);

        if (cell.isThereAShip()) {
            cell.setFill(positiveShotImage);
        } else {
            cell.setFill(negativeShotImage);

        }

//        ImageView positiveShotImage = new ImageView(shotPositiveImage);
//        ImageView negativeShotImage = new ImageView(shotNegativeImage);
//        ImageView unSunkShipImage = new ImageView(shipImage);
//        ImageView sunkShipImage = new ImageView(shipSunkImage);
//        StackPane imageContainer = new StackPane();
//
//        if (cell.isThereAShip()) {
//            if (cell.getShip().isSunk()) {
//                imageContainer.getChildren().addAll(sunkShipImage, positiveShotImage);
//            } else {
//                imageContainer.getChildren().addAll(unSunkShipImage, positiveShotImage);
//
//            }
//        } else {
//            if (cell.getShip().isSunk()) {
//                imageContainer.getChildren().addAll(sunkShipImage, negativeShotImage);
//            } else {
//                imageContainer.getChildren().addAll(unSunkShipImage, negativeShotImage);
//
//            }
//        }
//        cell.setFill(imageContainer);


//        Cell firstRight = opponentShips.entrySet().stream()
//                .map(cellShipEntry -> cellShipEntry.getKey())
//                .filter(c -> c.getValX() == x+1 && c.getValY() == y)
//                .findFirst().get();
//        firstRight.setFill(Color.RED);

    }
}