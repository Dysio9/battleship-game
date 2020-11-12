package pl.dysio9.battleship;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Label;


public class Controller {
    private Constants constants = Constants.getInstance();
    private int totalScorePlayer = 0;
    private int totalScoreOpponent = 0;
    private boolean playerTurn;
    private boolean gameStarted = false;
    private Map<Cell, Ship> playerShips = new HashMap<>();
    private Map<Cell, Ship> opponentShips = new HashMap<>();
    private static Controller instance = null;
    private Label menuLabel;
    private Label totalScorePlayerLabel;
    private Label totalScoreOpponentLabel;

//    Image shipImage = new Image("file:src/main/resources/ship1mast.png");
//    Image shipSunkImage = new Image("file:src/main/resources/ship1mast-sunk.png");
//    Image shotNegativeImage = new Image("file:src/main/resources/shoot-negative.png");
//    Image shotPositiveImage = new Image("file:src/main/resources/shoot-positive.png");


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

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public void getDefaultMenuLabel() {
        if (playerTurn) {
            menuLabel.setText(constants.getMenuLabelTextPlayerTurn());
        } else  {
            menuLabel.setText(constants.getMenuLabelTextOpponentTurn());
        }
    }

    public void cellClicked(Cell cell) {
        System.out.println("Clicked x="+cell.getValX()+ " y="+cell.getValY());
        int x = cell.getValX();
        int y = cell.getValY();

        if (gameStarted) {
            try {
                cell.getShip().hit();
            } catch (Exception e) {
//            System.out.println("Error: In this cell (" + x + "," + y + ") there is no ship! Don't worry!");
            }

            cell.shot();

            if (isPlayerTurn()) {
                menuLabel.setText(constants.getMenuLabelTextPlayerTurn());
                
                long unsunkCells = playerShips.entrySet().stream()
                        .filter(o -> !o.getValue().isSunk())
                        .count();
                System.out.println("Unsunk player cells No: " + unsunkCells);
                if (unsunkCells == 0) {
                    roundLost();
                }

            } else {
                menuLabel.setText(constants.getMenuLabelTextOpponentTurn());
                long unsunkCells = opponentShips.entrySet().stream()
                        .filter(o -> !o.getValue().isSunk())
                        .count();
                System.out.println("Unsunk opponent cells No: " + unsunkCells);

                if (unsunkCells == 0) {
                    roundWin();
                }
            }
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

    public void setMenuLabel(Label menuLabel) {
        this.menuLabel = menuLabel;
    }

    public void setPlayerTotalScoreBoard(Label totalScoreLabel) {
        this.totalScorePlayerLabel = totalScoreLabel;
    }

    public void setTotalScoreOpponentLabel(Label totalScoreOpponentLabel) {
        this.totalScoreOpponentLabel = totalScoreOpponentLabel;
    }

    public int getTotalScorePlayer() {
        return totalScorePlayer;
    }

    public int getTotalScoreOpponent() {
        return totalScoreOpponent;
    }

    public void roundWin() {
        totalScorePlayer++;
        System.out.println("You Win the round! Your score: " + totalScorePlayer);
        totalScorePlayerLabel.setText(String.valueOf(totalScorePlayer));
        gameStarted = false;
        menuLabel.setText("You Win!\n Place Your ships again");
    }

    public void roundLost() {
        totalScoreOpponent++;
        System.out.println("You've lost the round! Opponent score: " + totalScoreOpponent);
        totalScoreOpponentLabel.setText(String.valueOf(totalScoreOpponent));
        gameStarted = false;
        menuLabel.setText("Round Lost.\n Place your ships again");
    }

    public void clearTotalScores() {
        totalScorePlayer = 0;
        totalScoreOpponent = 0;
    }

}