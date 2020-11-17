package pl.dysio9.battleship;

import java.util.*;
import java.util.stream.Collectors;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;


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
    Button nextRoundButton;
    Button surrenderButton;
    BorderPane menuMiddleSection;
    GridPane playgroundGridPlayer;
    GridPane playgroundGridOpponent;

    private Controller() {
        // Exists only to defeat instantiation.
    }

    public static Controller getInstance() {
        if(instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void opponentShoot() {
        Random random = new Random();
        List <Cell> allNeighbors = getAllNeighbors(playerShips,true);
//        Cell cell;
//        Ship ship;

        int x = random.nextInt(10);
        int y = random.nextInt(10);

        List<Cell> cellsEverShot = playerShips.keySet().stream()
                .filter(e -> e.wasEverShot())
                .collect(Collectors.toList());



//        int c = (int)playerShips.keySet().stream()
//                .filter(e -> e.getValX() == x && e.getValY() == y)
//                .filter(e -> e.wasEverShot())
//                .count();
//        if (c == 0) {
//            Cell cell = new Cell(x,y,true);
//            cell.setEverShot(true);
//            playerShips.put(cell, null);
//        } else {
//            opponentShoot();
//        }
//        setPlayerTurn(true);
    }

    public void placeShipsRandomly(boolean player) {
        Map<Cell, Ship> ships;

        if (player) {
            ships = playerShips;
        } else {
            ships = opponentShips;
        }

        Random random = new Random();

        ships.clear();

        Ship sh;
        for (int i = 0; i < 1; ) {
            sh = new Ship(random.nextInt(10), random.nextInt(10), 4, random.nextBoolean());
            if (canPlaceShip(sh, player)) {
                addShip(sh, player);
                i++;
            }
        }

        for (int i = 0; i < 2; ) {
            sh = new Ship(random.nextInt(10), random.nextInt(10), 3, random.nextBoolean());
            if (canPlaceShip(sh, player)) {
                addShip(sh, player);
                i++;
            }
        }

        for (int i = 0; i < 3; ) {
            sh = new Ship(random.nextInt(10), random.nextInt(10), 2, random.nextBoolean());
            if (canPlaceShip(sh, player)) {
                addShip(sh, player);
                i++;
            }
        }

        for (int i = 0; i < 4; ) {
            sh = new Ship(random.nextInt(10), random.nextInt(10), 1, random.nextBoolean());
            if (canPlaceShip(sh, player)) {
                addShip(sh, player);
                i++;
            }
        }
    }

    public boolean canPlaceShip (Ship ship, boolean player) {
        Map<Cell, Ship> ships;

        if (player) {
            ships = playerShips;
        } else {
            ships = opponentShips;
        }

        if (ships.size() == 0) {
            if (!isValidCell(ship.getXCoordinates(ship.getMasts()-1), ship.getYCoordinates(ship.getMasts()-1))) {
                return false;
            } else {
                return true;
            }
        } else {
            if (!isValidCell(ship.getXCoordinates(ship.getMasts()-1), ship.getYCoordinates(ship.getMasts()-1))) {
                return false;
            } else {
                for (int j = 0; j < ship.getMasts(); j++) {
                    if (getAllNeighbors(ships, player).contains(new Cell(ship.getXCoordinates(j), ship.getYCoordinates(j), player))) {
                        return false;
                    }
                    if (ships.containsKey(new Cell(ship.getXCoordinates(j), ship.getYCoordinates(j), player))) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    public List<Cell> getNeighbors(Ship ship, boolean player) {
        List<Cell> neighbors = new ArrayList<>();
        if (ship != null) {
            for (int i = 1; i <= ship.getMasts(); i++) {
                if (ship.isHorizontalPosition()) {
                    switch (i) {
                        case 4:
                            neighbors.add(new Cell(ship.getX() + 2, ship.getY() - 1, player, true));
                            neighbors.add(new Cell(ship.getX() + 2, ship.getY() + 1, player, true));
                            neighbors.add(new Cell(ship.getX() + 3, ship.getY() - 1, player, true));
                            neighbors.add(new Cell(ship.getX() + 3, ship.getY() + 1, player, true));
                            neighbors.add(new Cell(ship.getX() + 4, ship.getY() - 1, player, true));
                            neighbors.add(new Cell(ship.getX() + 4, ship.getY(), player, true));
                            neighbors.add(new Cell(ship.getX() + 4, ship.getY() + 1, player, true));
                            break;
                        case 3:
                            neighbors.add(new Cell(ship.getX() + 2, ship.getY() - 1, player, true));
                            neighbors.add(new Cell(ship.getX() + 2, ship.getY() + 1, player, true));
                            neighbors.add(new Cell(ship.getX() + 3, ship.getY() - 1, player, true));
                            neighbors.add(new Cell(ship.getX() + 3, ship.getY(), player, true));
                            neighbors.add(new Cell(ship.getX() + 3, ship.getY() + 1, player, true));
                            break;
                        case 2:
                            neighbors.add(new Cell(ship.getX() + 2, ship.getY() - 1, player, true));
                            neighbors.add(new Cell(ship.getX() + 2, ship.getY(), player, true));
                            neighbors.add(new Cell(ship.getX() + 2, ship.getY() + 1, player, true));
                            break;
                        case 1:
                            neighbors.add(new Cell(ship.getX() + 1, ship.getY(), player, true));
                            break;
                    }
                    neighbors.add(new Cell(ship.getX(), ship.getY() + 1, player, true));
                } else {
                    switch (i) {
                        case 4:
                            neighbors.add(new Cell(ship.getX() - 1, ship.getY() + 2, player, true));
                            neighbors.add(new Cell(ship.getX() + 1, ship.getY() + 2, player, true));
                            neighbors.add(new Cell(ship.getX() - 1, ship.getY() + 3, player, true));
                            neighbors.add(new Cell(ship.getX() + 1, ship.getY() + 3, player, true));
                            neighbors.add(new Cell(ship.getX() - 1, ship.getY() + 4, player, true));
                            neighbors.add(new Cell(ship.getX(), ship.getY() + 4, player, true));
                            neighbors.add(new Cell(ship.getX() + 1, ship.getY() + 4, player, true));
                            break;
                        case 3:
                            neighbors.add(new Cell(ship.getX() - 1, ship.getY() + 2, player, true));
                            neighbors.add(new Cell(ship.getX() + 1, ship.getY() + 2, player, true));
                            neighbors.add(new Cell(ship.getX() - 1, ship.getY() + 3, player, true));
                            neighbors.add(new Cell(ship.getX(), ship.getY() + 3, player, true));
                            neighbors.add(new Cell(ship.getX() + 1, ship.getY() + 3, player, true));
                            break;
                        case 2:
                            neighbors.add(new Cell(ship.getX() - 1, ship.getY() + 2, player, true));
                            neighbors.add(new Cell(ship.getX(), ship.getY() + 2, player, true));
                            neighbors.add(new Cell(ship.getX() + 1, ship.getY() + 2, player, true));
                            break;
                        case 1:
                            neighbors.add(new Cell(ship.getX(), ship.getY() + 1, player, true));
                    }
                }
                neighbors.add(new Cell(ship.getX() + 1, ship.getY(), player, true));
            }
            neighbors.add(new Cell(ship.getX() - 1, ship.getY() - 1, player, true));
            neighbors.add(new Cell(ship.getX() - 1, ship.getY(), player, true));
            neighbors.add(new Cell(ship.getX() - 1, ship.getY() + 1, player, true));
            neighbors.add(new Cell(ship.getX(), ship.getY() - 1, player, true));
            neighbors.add(new Cell(ship.getX() + 1, ship.getY() - 1, player, true));
            neighbors.add(new Cell(ship.getX() + 1, ship.getY() + 1, player, true));
        }
        return neighbors.stream().filter(e -> isValidCell(e.getValX(), e.getValY())).collect(Collectors.toList());
    }

    public List<Cell> getAllNeighbors(Map<Cell, Ship> shipsMap, boolean player) {
        List<Ship> shipList = shipsMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
        List<Cell> neighborsAll = new ArrayList<>();

        for (Ship ship : shipList) {
            neighborsAll.addAll(getNeighbors(ship,player));
        }
        return neighborsAll.stream().filter(e -> isValidCell(e.getValX(), e.getValY())).collect(Collectors.toList());
    }

    public void addShip (Ship ship, boolean isPlayer) {
        ship.showAllCoordinates();
        for (int i = 0; i < ship.getMasts(); i++) {
            Cell cell = new Cell(ship.getXCoordinates(i), ship.getYCoordinates(i), ship, isPlayer);
            if (isPlayer) {
                playerShips.put(cell, ship);
            } else {
                opponentShips.put(cell, ship);
            }
        }
    }

    public boolean isValidCell(int x, int y) {
        if (x >= 0 && x < 10 && y >= 0 && y < 10) {
            return true;
        } else {
            return false;
        }
    }

    public boolean shipsArePlaced(boolean player) {
        int placedShipsCounter;
        if (player) {
            placedShipsCounter = (int) playerShips.entrySet().stream()
                    .filter(e -> e.getValue() != null)
                    .count();
        } else {
            placedShipsCounter = (int) opponentShips.entrySet().stream()
                    .filter(e -> e.getValue() != null).
                    count();
        }
        if (placedShipsCounter == 10) {
            return true;
        }

        return false;
    }

    public GridPane updatePlaygroundGrid(Map<Cell, Ship> shipsMap, GridPane playgroundGridOfPerson, boolean isPlayers) {
        List <Cell> allNeighbors = getAllNeighbors(shipsMap,isPlayers);

        playgroundGridOfPerson.getChildren().clear();
// przy zapisie do pliku spróbować impl interfejs serializable lub własne kodowanie np 11-RED-Shooted-
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Cell cell = new Cell(x,y, isPlayers);
                cell.setStroke(Color.BLACK);
                if (shipsMap.containsKey(cell)) {
                    cell = new Cell(x, y, shipsMap.get(cell), isPlayers);
                    cell.setStroke(Color.BLACK);
                    if (constants.showOpponentFleet() || isPlayers) {
                        if (shipsMap.get(cell) != null) {
//                            if (shipsMap.get(cell).isSunk()) {
//                                cell.setFill(Color.RED);
//                            } else {
                                cell.setFill(Color.LIGHTBLUE);
//                            }
                        } else {
                            cell.setFill(Color.YELLOW);
                        }
                    } else {
                        cell.setFill(Color.TRANSPARENT);
                    }
                } else {
                    if (cell.wasEverShot()) {
                        cell.setFill(constants.getShotNegativeImage());
                    } else {
                        if (allNeighbors.contains(cell)) {
                            if (constants.ShowNeighbors()) {
                                cell.setFill(Color.LIGHTGOLDENRODYELLOW);
                            } else {
                                cell.setFill(Color.TRANSPARENT);
                            }
                        } else {
                            cell.setFill(Color.TRANSPARENT);
                        }
                        cell.setStroke(Color.BLACK);
                    }
                }
                playgroundGridOfPerson.add(cell, x, y);
            }
        }

        for (Cell c : allNeighbors) {
            c.setNeighbour(true);
            shipsMap.put(c, null);
        }
        playgroundGridOfPerson.setGridLinesVisible(true);
        playgroundGridOfPerson.setPrefSize(380.0, 380.0);

        return playgroundGridOfPerson;
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
        opponentShoot();
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

    public int getUnsunkCellsCount(boolean player) {
        if (!player) {
            System.out.println("statki" +(int)playerShips.entrySet().stream()
                    .filter(o -> o.getValue() != null)
                    .filter(o -> !o.getValue().isSunk())
                    .count());

            return (int)playerShips.entrySet().stream()
                        .filter(o -> o.getValue() != null)
                        .filter(o -> !o.getValue().isSunk())
                    .count();
        } else {
            return (int)opponentShips.entrySet().stream()
                    .filter(o -> o.getValue() != null)
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

    public void setNextRoundButton(Button nextRoundButton) {
        this.nextRoundButton = nextRoundButton;
    }

    public void setSurrenderButton(Button surrenderButton) {
        this.surrenderButton = surrenderButton;
    }

    public void setMenuMiddleSection(BorderPane menuMiddleSection) {
        this.menuMiddleSection = menuMiddleSection;
    }

    public void setMenuLabel(Label menuLabel) {
        this.menuLabel = menuLabel;
    }

    public void setPlaygroundGridPlayer(GridPane playgroundGridPlayer) {
        this.playgroundGridPlayer = playgroundGridPlayer;
    }

    public void setPlaygroundGridOpponent(GridPane playgroundGridOpponent) {
        this.playgroundGridOpponent = playgroundGridOpponent;
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
        menuLabel.setText("You Win!");
        menuMiddleSection.getChildren().remove(surrenderButton);
        menuMiddleSection.setCenter(nextRoundButton);

    }

    public void roundLost() {
        totalScoreOpponent++;
        System.out.println("You've lost the round! Opponent score: " + totalScoreOpponent);
        totalScoreOpponentLabel.setText(String.valueOf(totalScoreOpponent));
        gameStarted = false;
        menuLabel.setText("Round Lost");
        menuMiddleSection.getChildren().remove(surrenderButton);
        menuMiddleSection.setCenter(nextRoundButton);
    }

    public void clearTotalScores() {
        totalScorePlayer = 0;
        totalScoreOpponent = 0;
        totalScorePlayerLabel.setText(String.valueOf(totalScorePlayer));
        totalScoreOpponentLabel.setText(String.valueOf(totalScoreOpponent));
    }

}