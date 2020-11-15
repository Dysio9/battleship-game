package pl.dysio9.battleship;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


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
    Button randomButton;
    Button startButton;
    BorderPane menuMiddleSection;

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
        System.out.println("Clicked x=" + cell.getValX()+ " y=" + cell.getValY());

        if (gameStarted) {
            if (cell.isPlayerCell() != isPlayerTurn()) {
                if (!cell.wasEverShot()) {
                    System.out.println("Player Turn: " + isPlayerTurn());

                    if (cell.isThereAShip()) {
                        cell.setFill(constants.getShotPositiveImage());
                        cell.getShip().hit();
                        System.out.println("Unsunk player(" + isPlayerTurn() + ") cells No: " + getUnsunkCellsCount(isPlayerTurn()));
                        if (getUnsunkCellsCount(isPlayerTurn()) == 0) {
                            if (isPlayerTurn()) {
                                roundWin();
                            } else {
                                roundLost();
                            }
                        }
                    } else {
                        cell.setFill(constants.getShotNegativeImage());
                        setPlayerTurn(!isPlayerTurn());
                        if (isPlayerTurn()) {
                            menuLabel.setText(constants.getMenuLabelTextPlayerTurn());
                        } else {
                            menuLabel.setText(constants.getMenuLabelTextOpponentTurn());
                        }
                    }
                    cell.setEverShot(true);
                }
            }
        }

//        Cell firstRight = opponentShips.entrySet().stream()
//                .map(cellShipEntry -> cellShipEntry.getKey())
//                .filter(c -> c.getValX() == x+1 && c.getValY() == y)
//                .findFirst().get();
//        firstRight.setFill(Color.RED);

    }

    public int getUnsunkCellsCount(boolean player) {
        if (!player) {
            return (int)playerShips.entrySet().stream()
                        .filter(o -> !o.getValue().isSunk())
                        .count();
        } else {
            return (int)opponentShips.entrySet().stream()
                        .filter(o -> !o.getValue().isSunk())
                        .count();
        }
    }

    public void setRandomButton(Button randomButton) {
        this.randomButton = randomButton;
    }

    public void setStartButton(Button startButton) {
        this.startButton = startButton;
    }

    public void setMenuMiddleSection(BorderPane menuMiddleSection) {
        this.menuMiddleSection = menuMiddleSection;
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
        menuMiddleSection.setCenter(randomButton);
        menuMiddleSection.setBottom(startButton);
    }

    public void roundLost() {
        totalScoreOpponent++;
        System.out.println("You've lost the round! Opponent score: " + totalScoreOpponent);
        totalScoreOpponentLabel.setText(String.valueOf(totalScoreOpponent));
        gameStarted = false;
        menuLabel.setText("Round Lost.\n Place your ships again");
        menuMiddleSection.setCenter(randomButton);
        menuMiddleSection.setBottom(startButton);
    }

    public void clearTotalScores() {
        totalScorePlayer = 0;
        totalScoreOpponent = 0;
        totalScorePlayerLabel.setText(String.valueOf(totalScorePlayer));
        totalScoreOpponentLabel.setText(String.valueOf(totalScoreOpponent));
    }

}