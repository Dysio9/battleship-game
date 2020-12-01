package pl.dysio9.battleship;

import static pl.dysio9.battleship.Constants.*;

import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Controller {
    private static Controller instance = null;
    private final Random random = new Random();
    private Map<Cell, Ship> playerShips = new HashMap<>();
    private Map<Cell, Ship> opponentShips = new HashMap<>();
    private List<Cell> playgroundPlayerList = new ArrayList<>();
    private List<Cell> playgroundOpponentList = new ArrayList<>();
    private GridPane playgroundGridPlayer = new GridPane();
    private GridPane playgroundGridOpponent = new GridPane();
    private int totalScorePlayer = 0;
    private int totalScoreOpponent = 0;
    private int difficultyLevel = 2;    // 1 - easy, 2 - medium, 3 - hard
    private boolean gameStarted = false;
    private Deque<Cell> shotsHistory = new ArrayDeque<>();

    private boolean playerTurn;
    private Label menuLabel;
    private Label totalScorePlayerLabel;
    private Label totalScoreOpponentLabel;
    private Label timerLabel;
    private Button randomButton;
    private Button startButton;
    private Button nextRoundButton;
    private Button surrenderButton;
    private BorderPane menuMiddleSection;


    public Controller() {
        // Exists only to defeat instantiation.
    }

    public static Controller getInstance() {
        if(instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void placeShipsRandomly(boolean player) {
        Map<Cell, Ship> ships = player ? playerShips : opponentShips;

        ships.clear();
        fillCellsListWithEmptyCells(player);

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

//        System.out.println("Statki playera (" + player + ") w Mapie:");
//        playerShips.entrySet().stream().forEach(System.out::println);
//        System.out.println("Plansza playera (" + player + "):");
//        playgroundGridPlayer.getChildren().stream().forEach(System.out::println);
    }

    public void addShip (Ship ship, boolean isPlayer) {
        for (int i = 0; i < ship.getMasts(); i++) {
            Cell cell = new Cell(ship.getXCoordinates(i), ship.getYCoordinates(i), ship, isPlayer);
            if (isPlayer) {
                playerShips.put(cell, ship);
                replaceCellInPlayground(cell, true);
            } else {
                opponentShips.put(cell, ship);
                replaceCellInPlayground(cell, false);
            }
        }
    }

    public void replaceCellInPlayground(Cell cell, boolean isPlayer) {
        if (isPlayer) {
            ListIterator<Cell> iterator = playgroundPlayerList.listIterator();
            while (iterator.hasNext()) {
                Cell next = iterator.next();
                if (next.equals(cell)) {
                    iterator.set(cell);
                }
            }
        } else {
            ListIterator<Cell> iterator = playgroundOpponentList.listIterator();
            while (iterator.hasNext()) {
                Cell next = iterator.next();
                if (next.equals(cell)) {
                    iterator.set(cell);
                }
            }
        }
    }

    public void fillCellsListWithEmptyCells(boolean player) {
        List<Cell> cellList = new ArrayList<>();
//        if (player) {
//            cellList = playerShips.keySet().stream().collect(Collectors.toList());
//        } else {
//            cellList = opponentShips.keySet().stream().collect(Collectors.toList());
//        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Cell cell = new Cell(i,j,player, false);
//                if (!cellList.contains(cell)) {
                    cellList.add(cell);
//                }
            }
        }

        if (player) {
            playgroundPlayerList.clear();
            playgroundPlayerList.addAll(cellList);
        } else {
            playgroundOpponentList.clear();
            playgroundOpponentList.addAll(cellList);
        }

//        System.out.println("Cells list for player(" + player +"):");
//        cellList.stream().forEach(System.out::println);
    }

    public void fillCellsListWithTheShips(boolean player) {
        Map<Cell, Ship> shipsMap = player ? playerShips : opponentShips;

        fillCellsListWithEmptyCells(player);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Cell cell = new Cell(i, j, player);
                if (shipsMap.containsKey(cell)) {
                    cell = new Cell(i, j, shipsMap.get(cell), player);
                }
                if (getAllNeighbors(shipsMap, player).contains(cell)) {
                    cell.setNeighbor(true);
                }

                if (player) {
                    playgroundPlayerList.remove(cell);
                    playgroundPlayerList.add(cell);
                } else {
                    playgroundOpponentList.remove(cell);
                    playgroundOpponentList.add(cell);
                }
            }
        }
    }

    public boolean canPlaceShip (Ship ship, boolean player) {
        Map<Cell, Ship> ships = player ? playerShips : opponentShips;

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

    public boolean isValidCell(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public boolean shipsArePlaced(boolean player) {
        int placedShipsCounter;
        if (player) {
            placedShipsCounter = (int) playerShips.entrySet().stream()
                    .filter(e -> e.getValue() != null)
                    .count();
//            placedShipsCounter = (int)playgroundPlayerList.stream()
//                    .filter(e -> e.isThereAShip())
//                    .count();
        } else {
            placedShipsCounter = (int) opponentShips.entrySet().stream()
                    .filter(e -> e.getValue() != null).
                    count();
//            placedShipsCounter = (int)playgroundOpponentList.stream()
//                    .filter(e -> e.isThereAShip())
//                    .count();
        }
        return placedShipsCounter == 20;
    }

    public GridPane createPlaygroundGridPane(Map<Cell,Ship> shipsMap, GridPane playgroundGrid, boolean isPlayers) {
        playgroundGrid.getChildren().clear();
        List<Cell> cells = isPlayers ? playgroundPlayerList : playgroundOpponentList;

        if (shipsMap.isEmpty()) {
            fillCellsListWithEmptyCells(isPlayers);
        }

        for (Cell cell : cells) {
                cell.setFill(Color.TRANSPARENT);
                cell.setStroke(Color.BLACK);
                if (cell.isThereAShip()) {
                    if (cell.wasEverShot()) {
                        cell.setFill(SHOT_POSITIVE_IMAGE);
                    } else {
                        if (cell.isPlayerCell()) {
                            cell.setFill(Color.BLUE);
                        } else if (SHOW_OPPONENT_FLEET) {
                            cell.setFill(Color.BLUE);
                        }
                    }
                } else {
                    if (cell.wasEverShot()) {
                        cell.setFill(SHOT_NEGATIVE_IMAGE);
                    } else {
                        if (cell.isNeighbor() && SHOW_NEIGHBORS) {
                            cell.setFill(Color.LIGHTBLUE);
                        }
                    }
                }
                playgroundGrid.getChildren().remove(cell);
                playgroundGrid.add(cell, cell.getValX(), cell.getValY());
        }
        playgroundGrid.setGridLinesVisible(true);
        playgroundGrid.setPrefSize(380.0, 380.0);

        System.out.println("Cells list for player(" + isPlayers +"):");
        if (isPlayers) {
            playgroundPlayerList.stream().forEach(System.out::println);
        } else {
            playgroundOpponentList.stream().forEach(System.out::println);
        }
        return playgroundGrid;
    }

     public void cellClicked(Cell cell) {
        System.out.println("Clicked x=" + cell.getValX()+ " y=" + cell.getValY());

        if (playerTurn) {
            if (gameStarted) {
                if (cell.isPlayerCell() != isPlayerTurn()) {
                    if (!cell.wasEverShot()) {
                        System.out.println("Player Turn: " + isPlayerTurn());
                        cell.setClicked();
                        if (cell.isThereAShip()) {
                            cell.setFill(SHOT_POSITIVE_IMAGE);
                            cell.getShip().hit();
                            System.out.println("Unsunk player(" + isPlayerTurn() + ") cells No: " + getUnsunkCellsCount(isPlayerTurn()));
                            if (getUnsunkCellsCount(isPlayerTurn()) == 0) {
                                roundWin();
                            }
                        } else {
                            cell.setFill(SHOT_NEGATIVE_IMAGE);
                            setPlayerTurn(!isPlayerTurn());
                            if (isPlayerTurn()) {
                                menuLabel.setText(MENU_LABEL_TEXT_PLAYER_TURN);
                            } else {
                                menuLabel.setText(MENU_LABEL_TEXT_OPPONENT_TURN);
                            }
                            opponentShoot(OPPONENT_DELAY);
                        }
                    }
                }
            }
        }
    }

    public void opponentShootEasy(double milliseconds) {
// ------- wybieranie komórki z mapy statków ------------
//        List<Map.Entry<Cell, Ship>> playersHiddenFields = playerShips.entrySet().stream()
//                .filter(cell -> !cell.getKey().wasEverShot())
//                .collect(Collectors.toList());
//
//        System.out.println("Lista playerdHiddenFields:");
//        playersHiddenFields.stream().map(e ->  e.getKey() + "=" + e.getValue()).forEach(System.out::println);
//
//        int computerChoiceIndex = random.nextInt(playersHiddenFields.size());
//        Map.Entry<Cell, Ship> computerChoiceEntry = playersHiddenFields.get(computerChoiceIndex);
//        Cell computerChoiceCell = computerChoiceEntry.getKey();
//        Ship computerChoiceShip = computerChoiceEntry.getValue();
//        //do sth with this cell - marked as clicked, change the state & graphics etc.

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(milliseconds), event -> {
            List<Cell> playersHiddenFields = playgroundPlayerList.stream()
                    .filter(cell -> !cell.wasEverShot())
                    .collect(Collectors.toList());
            int computerChoiceIndex = random.nextInt(playersHiddenFields.size());
            Cell computerChoiceCell = playersHiddenFields.get(computerChoiceIndex);
            computerChoiceCell.setClicked();

            System.out.println("Wybór opponenta padł na: " + computerChoiceCell);
            if (computerChoiceCell.getShip() != null) {
                System.out.println("Komórka do której strzela opponent zawiera statek.");
            } else {
                System.out.println("Komórka do której strzela opponent nie zawiera statku");
            }

            if (computerChoiceCell.isThereAShip()) {
                computerChoiceCell.setFill(SHOT_POSITIVE_IMAGE);
                computerChoiceCell.getShip().hit();
                System.out.println("Unsunk player(" + isPlayerTurn() + ") cells No: " + getUnsunkCellsCount(isPlayerTurn()));
                if (getUnsunkCellsCount(isPlayerTurn()) == 0) {
                    roundLost();
                }
                opponentShootEasy(milliseconds);
            } else {
                computerChoiceCell.setFill(SHOT_NEGATIVE_IMAGE);
                setPlayerTurn(!isPlayerTurn());

                if (isPlayerTurn()) {
                    menuLabel.setText(MENU_LABEL_TEXT_PLAYER_TURN);
                } else {
                    menuLabel.setText(MENU_LABEL_TEXT_OPPONENT_TURN);
                }
            }
            System.out.println("---------------------------------------------------");
        }));
        timeline.setCycleCount(isPlayerTurn() ? Timeline.INDEFINITE : 0);
        timeline.play();
    }

    public void opponentShoot(double milliseconds) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(milliseconds), event -> {

            if (gameStarted) {
                List<Cell> playersHiddenFields;
                Cell computerChoiceCell;
                playersHiddenFields = getCellsToShot();

                System.out.println("playerhiddenFields: " + playersHiddenFields);
                int computerChoiceIndex = random.nextInt(playersHiddenFields.size());
                computerChoiceCell = playersHiddenFields.get(computerChoiceIndex);
                computerChoiceCell.setClicked();

                System.out.println("Wybór opponenta padł na: " + computerChoiceCell);
                if (computerChoiceCell.getShip() != null) {
                    System.out.println("Komórka do której strzela opponent zawiera statek.");
                } else {
                    System.out.println("Komórka do której strzela opponent nie zawiera statku");
                }

                if (computerChoiceCell.isThereAShip()) {
                    computerChoiceCell.setFill(SHOT_POSITIVE_IMAGE);
                    computerChoiceCell.getShip().hit();
                    System.out.println("Unsunk player(" + isPlayerTurn() + ") cells No: " + getUnsunkCellsCount(isPlayerTurn()));
                    shotsHistory.add(computerChoiceCell);
                    System.out.println("shotsHistory in opponentShotMedium: " + shotsHistory);
                    if (getUnsunkCellsCount(isPlayerTurn()) == 0) {
                        roundLost();
                    }
                    opponentShoot(milliseconds);
                } else {
                    if (!shotsHistory.isEmpty() && shotsHistory.peekLast().getShip().isSunk()) {
                        setNeighborsAsClicked(shotsHistory.peekLast().getShip());
                        shotsHistory.clear();
                    }
                    computerChoiceCell.setFill(SHOT_NEGATIVE_IMAGE);
                    setPlayerTurn(!isPlayerTurn());

                    if (isPlayerTurn()) {
                        menuLabel.setText(MENU_LABEL_TEXT_PLAYER_TURN);
                    } else {
                        menuLabel.setText(MENU_LABEL_TEXT_OPPONENT_TURN);
                    }
                }
                System.out.println("------------------------------------------");
            }
        }));
        timeline.setCycleCount(isPlayerTurn() ? Timeline.INDEFINITE : 0);
        timeline.play();
    }

    public List<Cell> findNonDiagonalNeighbors(Cell cell) {
        List<Cell> resultList;

//        List<Cell> neighborsList = getNeighbors(cell.getShip(),cell.isPlayerCell());

        resultList = playgroundPlayerList.stream()
//                .filter(c -> (c.getValX() == cell.getValX()))
//                        .filter(c -> c.getValY() == cell.getValY()+1 || c.getValY() == cell.getValY()-1)
//                        .filter(c -> c.getValY() == cell.getValY())
//                        .filter(c -> c.getValX() == cell.getValX()+1 || c.getValX() == cell.getValX()-1)
                .filter(c -> (c.getValX() == cell.getValX()
                        && (c.getValY() == cell.getValY()+1 || c.getValY() == cell.getValY()-1))
                        || (c.getValY() == cell.getValY()
                        && (c.getValX() == cell.getValX()+1 || c.getValX() == cell.getValX()-1)))
//                .filter(ce -> getNeighbors(cell.getShip(),cell.isPlayerCell()).contains(ce))
//                .filter(c -> c.getValY() == cell.getValY()-1 || c.getValY() == cell.getValY()+1)
                .filter(c -> isValidCell(cell.getValX(), cell.getValY()))
                .filter(c -> !c.wasEverShot())
                .collect(Collectors.toList());

//        resultList.removeIf(neighborsList::contains);

//        resultList = neighborsList.stream()
//                .filter(c -> (c.getValX() == cell.getValX()
//                        && (c.getValY() == cell.getValY()+1 || c.getValY() == cell.getValY()-1))
//                        || (c.getValY() == cell.getValY()
//                        && (c.getValX() == cell.getValX()+1 || c.getValX() == cell.getValX()-1)))
//                .collect(Collectors.toList());

        resultList.stream()
                .map(c -> "FoundNonDiagonalCells: " + c.getValX() + "," + c.getValY())
                .forEach(System.out::println);

        return resultList;
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
        List<Ship> shipList = shipsMap.values().stream().collect(Collectors.toList());
        List<Cell> neighborsAll = new ArrayList<>();

        for (Ship ship : shipList) {
            neighborsAll.addAll(getNeighbors(ship,player));
        }

        return neighborsAll.stream().filter(e -> isValidCell(e.getValX(), e.getValY())).collect(Collectors.toList());
    }

    public List<Cell> getCellsToShot() {
        System.out.println("first element shotHistory: " + shotsHistory.peekFirst());
        System.out.println("last element shotHistory: " + shotsHistory.peekLast());

        if (shotsHistory.isEmpty() || difficultyLevel == 1) {
            return playgroundPlayerList.stream()
                    .filter(cell -> !cell.wasEverShot())
                    .collect(Collectors.toList());
        } else if (shotsHistory.size() == 1) {
            return !findNonDiagonalNeighbors(shotsHistory.peekLast()).isEmpty() ?
                    findNonDiagonalNeighbors(shotsHistory.peekLast()) :
                    playgroundPlayerList.stream()
                            .filter(cell -> !cell.wasEverShot())
                            .collect(Collectors.toList());
        } else {
            List<Cell> cellsToShotList = new ArrayList<>();
            cellsToShotList.addAll(findNonDiagonalNeighbors(shotsHistory.peekLast()));
            System.out.println("cells to shot " + cellsToShotList);

            if (shotsHistory.peekFirst().getValX() == shotsHistory.peekLast().getValX()) {
                cellsToShotList = cellsToShotList.stream()
                        .filter(cell -> isValidCell(cell.getValX(), cell.getValY()))
                        .filter(cell -> !cell.wasEverShot())
                        .filter(cell -> cell.getValX() == shotsHistory.peekLast().getValX())
                        .collect(Collectors.toList());
            } else if (shotsHistory.peekFirst().getValY() == shotsHistory.peekLast().getValY()) {
                cellsToShotList = cellsToShotList.stream()
                        .filter(cell -> isValidCell(cell.getValX(), cell.getValY()))
                        .filter(cell -> !cell.wasEverShot())
                        .filter(cell -> cell.getValY() == shotsHistory.peekLast().getValY())
                        .collect(Collectors.toList());
            }
            return cellsToShotList.size() > 0 ? cellsToShotList : playgroundPlayerList.stream()
                    .filter(cell -> !cell.wasEverShot())
                    .collect(Collectors.toList());
        }
    }

    public Map<Cell, Ship> getPlayerShips() {
        return playerShips;
    }

    public Map<Cell, Ship> getOpponentShips() {
        return opponentShips;
    }

    public GridPane getPlaygroundGridPlayer() {
        return playgroundGridPlayer;
    }

    public GridPane getPlaygroundGridOpponent() {
        return playgroundGridOpponent;
    }

    public int getUnsunkCellsCount(boolean player) {
        if (!player) {
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

    public int getTotalScorePlayer() {
        return totalScorePlayer;
    }

    public int getTotalScoreOpponent() {
        return totalScoreOpponent;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setNeighborsAsClicked(Ship ship) {
        List<Cell> cellList = playgroundPlayerList.stream()
                .filter(cell -> getNeighbors(ship,true).contains(cell))
                .collect(Collectors.toList());

        for (Cell c : cellList) {
            if (!c.wasEverShot()) {
                c.setClicked();
                if (SHOW_SUNK_SHIPS_NEIGHBORS) {
                    c.setFill(SHOT_NEGATIVE_IMAGE);
                }
            }
        }
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
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

    public void setTotalScorePlayerLabel(Label totalScoreLabel) {
        this.totalScorePlayerLabel = totalScoreLabel;
    }

    public void setTotalScoreOpponentLabel(Label totalScoreOpponentLabel) {
        this.totalScoreOpponentLabel = totalScoreOpponentLabel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        if (difficultyLevel.equals("Difficulty: Easy")) {
            this.difficultyLevel = 1;
        } else if (difficultyLevel.equals("Difficulty: Medium")) {
            this.difficultyLevel = 2;
        }
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

    public void startNewGame() {
        totalScorePlayer = 0;
        totalScoreOpponent = 0;
        totalScorePlayerLabel.setText(String.valueOf(totalScorePlayer));
        totalScoreOpponentLabel.setText(String.valueOf(totalScoreOpponent));
        gameStarted = false;
        playerShips.clear();
        opponentShips.clear();
        fillCellsListWithEmptyCells(true);
        fillCellsListWithEmptyCells(false);
        createPlaygroundGridPane(playerShips,playgroundGridPlayer,true);
        createPlaygroundGridPane(opponentShips,playgroundGridOpponent,false);
        menuMiddleSection.getChildren().remove(surrenderButton);
        menuMiddleSection.setCenter(nextRoundButton);
        menuLabel.setText("The Game has been reset");
    }

    public void saveTotalScores() {
        List<String> playgrounds = new ArrayList<>();
        playgrounds.add("" + totalScorePlayer + " " + totalScoreOpponent + " " + difficultyLevel);
        playgrounds.addAll(playgroundPlayerList.stream()
                .map(e -> e.getValX() + " " + e.getValY() + " player(" + e.isPlayerCell() + ") wasClicked(" + e.wasEverShot() + ") isNeighbor(" + e.isNeighbor() + ") " + e.getShip())
                .collect(Collectors.toList()));
        playgrounds.addAll(playgroundOpponentList.stream()
                .map(e -> e.getValX() + " " + e.getValY() + " player(" + e.isPlayerCell() + ") wasClicked(" + e.wasEverShot() + ") isNeighbor(" + e.isNeighbor() + ") " + e.getShip())
                .collect(Collectors.toList()));

        try {
            Files.write(Paths.get("gameplay.list"), playgrounds);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTotalScores() {
        String lineString;
        playgroundPlayerList.clear();
        playgroundOpponentList.clear();
        playerShips.clear();
        opponentShips.clear();

        fillCellsListWithEmptyCells(true);
        fillCellsListWithEmptyCells(false);
        try {
            File file = new File("gameplay.list");
            Scanner scanner = new Scanner(file);

            for (int i = 0; i < 201; i++) {
                boolean isPlayers;
                boolean isHorizontal;
                Cell cell;
                Ship ship;


                lineString = scanner.nextLine();
                String[] splited = lineString.split("\\s+");
                // 0 - ValX, 1 - valY, 2 - isPlayers, 3 - everShot, 4 - isNeighbor, 5 - shipX, 6 - shipY, 7 - mastsNp, 8 - isHorizontal
                if (i == 0) {
                    totalScorePlayer = Integer.parseInt(splited[0]);
                    totalScoreOpponent = Integer.parseInt(splited[1]);
                    difficultyLevel = Integer.parseInt(splited[2]);
                } else {
                    isPlayers = splited[2].equals("player(true)");
                    if (splited[5].equals("null")) {
                        cell = new Cell(Integer.parseInt(splited[0]), Integer.parseInt(splited[1]), isPlayers);
                        if (splited[3].equals("wasClicked(true)")) {
                            cell.setClicked();
                        }
                        if (splited[4].equals("isNeighbor(true)")) {
                            cell.setNeighbor(true);
                        }
                        if (i > 0 && i <= 100) {
                            playgroundPlayerList.remove(cell);
                            playgroundPlayerList.add(cell);
                        } else {
                            playgroundOpponentList.remove(cell);
                            playgroundOpponentList.add(cell);
                        }
                    } else {
                        isHorizontal = splited[8].equals("true");
                        ship = new Ship(Integer.parseInt(splited[5]), Integer.parseInt(splited[6]), Integer.parseInt(splited[7]), isHorizontal);
                        cell = new Cell(Integer.parseInt(splited[0]), Integer.parseInt(splited[1]), ship, isPlayers);
                        if (splited[3].equals("wasClicked(true)")) {
                            cell.setClicked();
                        }
                        if (i > 0 && i <= 100) {
                            playerShips.put(cell,ship);
                            playgroundPlayerList.remove(cell);
                            playgroundPlayerList.add(cell);
                        } else {
                            opponentShips.put(cell,ship);
                            playgroundOpponentList.remove(cell);
                            playgroundOpponentList.add(cell);
                        }
                    }
                }
            }
//            fillCellsListWithTheShips(true);
//            fillCellsListWithTheShips(false);
            scanner.close();

            totalScorePlayerLabel.setText(String.valueOf(totalScorePlayer));
            totalScoreOpponentLabel.setText(String.valueOf(totalScoreOpponent));
            createPlaygroundGridPane(playerShips, playgroundGridPlayer, true);
            createPlaygroundGridPane(opponentShips, playgroundGridOpponent, false);

            System.out.println("player:" + totalScorePlayer + " opponent: " + totalScoreOpponent +
                    " difficulty level: " + difficultyLevel);
            System.out.println("Mapa playerShips:");
            playerShips.entrySet().stream().forEach(System.out::println);
            System.out.println("PlaygroundPlayerList:");
            playgroundPlayerList.stream().forEach(System.out::println);
            System.out.println("Mapa opponentShips:");
            opponentShips.entrySet().stream().forEach(System.out::println);
            System.out.println("PlaygroundOpponentList:");
            playgroundOpponentList.stream().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}