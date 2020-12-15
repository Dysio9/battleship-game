package pl.dysio9.battleship;

import static pl.dysio9.battleship.Constants.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Controller {
    private static Controller instance = null;
    private final Random random = new Random();
    private final Map<Cell, Ship> playerShips = new HashMap<>();
    private final Map<Cell, Ship> opponentShips = new HashMap<>();
    private final List<Cell> playgroundPlayerList = new ArrayList<>();
    private final List<Cell> playgroundOpponentList = new ArrayList<>();
    private final GridPane playgroundGridPlayer = new GridPane();
    private final GridPane playgroundGridOpponent = new GridPane();
    private final Deque<Cell> shotsHistory = new ArrayDeque<>();
    private int totalScorePlayer = 0;
    private int totalScoreOpponent = 0;
    private int difficultyLevel = 2;    // 1 - easy, 2 - medium
    private boolean gameStarted = false;

    private boolean playerTurn;
    private Label menuLabel;
    private Label totalScorePlayerLabel;
    private Label totalScoreOpponentLabel;
    private Button randomButton;
    private Button startButton;
    private Button nextRoundButton;
    private Button surrenderButton;
    private FlowPane menuMiddleSection;


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
        fillPlaygroundWithEmptyCells(player);

        placeSpecificShipRandomly(4,1, player);
        placeSpecificShipRandomly(3,2, player);
        placeSpecificShipRandomly(2,3, player);
        placeSpecificShipRandomly(1,4, player);

        fillPlaygroundWithTheShips(player);
    }

    private void placeSpecificShipRandomly(int masts, int howMany, boolean player) {
        Ship sh;
        for (int i = 0; i < howMany; ) {
            sh = new Ship(random.nextInt(10), random.nextInt(10), masts, random.nextBoolean());
            if (canPlaceShip(sh, player)) {
                addShip(sh, player);
                i++;
            }
        }
    }

    public void addShip (Ship ship, boolean isPlayer) {
        for (int i = 0; i < ship.getMasts(); i++) {
            Cell cell = new Cell(ship.getXCoordinates(i), ship.getYCoordinates(i), ship, isPlayer);
            if (isPlayer) {
                playerShips.put(cell, ship);
            } else {
                opponentShips.put(cell, ship);
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

    public void fillPlaygroundWithTheShips(boolean player) {
        Map<Cell, Ship> shipsMap = player ? playerShips : opponentShips;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Cell cell = new Cell(i, j, player);
                if (shipsMap.containsKey(cell)) {
                    cell = new Cell(i, j, shipsMap.get(cell), player);
                }
                if (getAllNeighbors(shipsMap, player).contains(cell)) {
                    cell.setNeighbor(true);
                }
                replaceCellInPlayground(cell,player);
            }
        }
    }

    public void fillPlaygroundWithEmptyCells(boolean player) {
        List<Cell> cellList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Cell cell = new Cell(i,j,player, false);
                cellList.add(cell);
            }
        }

        if (player) {
            playgroundPlayerList.clear();
            playgroundPlayerList.addAll(cellList);
        } else {
            playgroundOpponentList.clear();
            playgroundOpponentList.addAll(cellList);
        }
    }

    public boolean canPlaceShip (Ship ship, boolean player) {
        Map<Cell, Ship> ships = player ? playerShips : opponentShips;

        if (ships.size() == 0) {
            return isValidCell(ship.getXCoordinates(ship.getMasts() - 1), ship.getYCoordinates(ship.getMasts() - 1));
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
        } else {
            placedShipsCounter = (int) opponentShips.entrySet().stream()
                    .filter(e -> e.getValue() != null).
                    count();
        }
        return placedShipsCounter == 20;
    }

    public void createPlaygroundGridPane(Map<Cell,Ship> shipsMap, GridPane playgroundGrid, boolean isPlayers) {
        playgroundGrid.getChildren().clear();
        List<Cell> cells = isPlayers ? playgroundPlayerList : playgroundOpponentList;

        if (shipsMap.isEmpty()) {
            fillPlaygroundWithEmptyCells(isPlayers);
        }

        for (Cell cell : cells) {
                cell.setFill(Color.TRANSPARENT);
                cell.setStroke(Color.BLACK);
                if (cell.isThereAShip()) {
                    if (cell.wasEverShot()) {
                        cell.setFill(SHOT_POSITIVE_IMAGE);
                    } else if (cell.isPlayerCell()) {
                        cell.setFill(Color.BLUE);
                    } else if (SHOW_OPPONENT_FLEET) {
                        cell.setFill(Color.BLUE);
                    }
                } else {
                    if (cell.wasEverShot()) {
                        cell.setFill(SHOT_NEGATIVE_IMAGE);
                    } else if (cell.isNeighbor() && SHOW_NEIGHBORS) {
                            cell.setFill(Color.LIGHTBLUE);
                    }
                }
                playgroundGrid.getChildren().remove(cell);
                playgroundGrid.add(cell, cell.getValX(), cell.getValY());
        }
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

    public void opponentShoot(double milliseconds) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(milliseconds), event -> {
            if (gameStarted) {
                List<Cell> playersHiddenFields;
                Cell computerChoiceCell;
                playersHiddenFields = getCellsToShot();

                int computerChoiceIndex = random.nextInt(playersHiddenFields.size());
                computerChoiceCell = playersHiddenFields.get(computerChoiceIndex);
                computerChoiceCell.setClicked();

                if (computerChoiceCell.isThereAShip()) {
                    computerChoiceCell.setFill(SHOT_POSITIVE_IMAGE);
                    computerChoiceCell.getShip().hit();
                    shotsHistory.add(computerChoiceCell);
                    System.out.println("Unsunk player(" + isPlayerTurn() + ") cells No: " + getUnsunkCellsCount(isPlayerTurn()));
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

                    menuLabel.setText(isPlayerTurn() ? MENU_LABEL_TEXT_PLAYER_TURN : MENU_LABEL_TEXT_OPPONENT_TURN);
                }
            }
        }));
        timeline.setCycleCount(isPlayerTurn() ? Timeline.INDEFINITE : 0);
        timeline.play();
    }

    public List<Cell> findNonDiagonalNeighbors(Cell cell) {
        return playgroundPlayerList.stream()
                .filter(c -> (c.getValX() == cell.getValX()
                        && (c.getValY() == cell.getValY()+1 || c.getValY() == cell.getValY()-1))
                        || (c.getValY() == cell.getValY()
                        && (c.getValX() == cell.getValX()+1 || c.getValX() == cell.getValX()-1)))
                .filter(c -> isValidCell(cell.getValX(), cell.getValY()))
                .filter(c -> !c.wasEverShot())
                .collect(Collectors.toList());
    }

    public void roundWin() {
        totalScorePlayer++;
        totalScorePlayerLabel.setText(String.valueOf(totalScorePlayer));
        gameStarted = false;
        menuLabel.setText("You Win!");
        menuMiddleSection.getChildren().remove(surrenderButton);
        menuMiddleSection.getChildren().add(nextRoundButton);
        AlertBox.display("Player win!", "You win! Congratulations!");
    }

    public void roundLost() {
        totalScoreOpponent++;
        totalScoreOpponentLabel.setText(String.valueOf(totalScoreOpponent));
        gameStarted = false;
        menuLabel.setText("Round Lost");
        menuMiddleSection.getChildren().remove(surrenderButton);
        menuMiddleSection.getChildren().add(nextRoundButton);
        AlertBox.display("Player lost", "You lost this round! Try again!");
    }

    public void startNewGame() {
        totalScorePlayer = 0;
        totalScoreOpponent = 0;
        totalScorePlayerLabel.setText(String.valueOf(totalScorePlayer));
        totalScoreOpponentLabel.setText(String.valueOf(totalScoreOpponent));
        gameStarted = false;
        playerShips.clear();
        opponentShips.clear();
        fillPlaygroundWithEmptyCells(true);
        fillPlaygroundWithEmptyCells(false);
        createPlaygroundGridPane(playerShips,playgroundGridPlayer,true);
        createPlaygroundGridPane(opponentShips,playgroundGridOpponent,false);
    }

    public void loadGame() {
        String lineString;
        playgroundPlayerList.clear();
        playgroundOpponentList.clear();
        playerShips.clear();
        opponentShips.clear();

        fillPlaygroundWithEmptyCells(true);
        fillPlaygroundWithEmptyCells(false);
        try {
            File file = new File("gameplay.list");
            Scanner scanner = new Scanner(file);

            lineString = scanner.nextLine();
            String[] splited = lineString.split("\\s+");
            // 0 - totalScorePlayer, 1 - totalScoreOpponent, 2 - difficultyLevel
            totalScorePlayer = Integer.parseInt(splited[0]);
            totalScoreOpponent = Integer.parseInt(splited[1]);
            difficultyLevel = Integer.parseInt(splited[2]);

            for (int i = 0; i < 20; i++) {
                lineString = scanner.nextLine();
                splited = lineString.split("\\s+");
                // 0 - ValX, 1 - valY, 2 - mastsNo, 3 - isHorizontal

                Ship ship = new Ship(Integer.parseInt(splited[0]),
                        Integer.parseInt(splited[1]),
                        Integer.parseInt(splited[2]),
                        splited[3].equals("isHorizontal(true)"));

                addShip(ship, i < 10);
            }
            fillPlaygroundWithTheShips(true);
            fillPlaygroundWithTheShips(false);

            while (scanner.hasNext()) {
                lineString = scanner.nextLine();
                splited = lineString.split("\\s+");
                // 0 - ValX, 1 - valY, 2 - isPlayer, 3 - wasEverClicked, 4 - isNeighbor, 5 - shipX, 6 - shipY, 7 - mastsNp, 8 - isHorizontal

                ListIterator<Cell> iterator = splited[2].equals("player(true)") ? playgroundPlayerList.listIterator() : playgroundOpponentList.listIterator();
                while (iterator.hasNext()) {
                    Cell next = iterator.next();
                    if (next.getValX() == Integer.parseInt(splited[0]) && next.getValY() == Integer.parseInt(splited[1])) {
                        next.setClicked();
                        if (next.isThereAShip()) {
                            next.getShip().hit();
                        }
                    }
                }
            }
            scanner.close();

            totalScorePlayerLabel.setText(String.valueOf(totalScorePlayer));
            totalScoreOpponentLabel.setText(String.valueOf(totalScoreOpponent));
            createPlaygroundGridPane(playerShips, playgroundGridPlayer, true);
            createPlaygroundGridPane(opponentShips, playgroundGridOpponent, false);
            menuMiddleSection.getChildren().clear();
            menuMiddleSection.getChildren().add(randomButton);
            menuMiddleSection.getChildren().add(startButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGame() {
        List<String> playgrounds = new ArrayList<>();
        Set<Ship> playerShipsSet = new HashSet<>(playerShips.values());
        Set<Ship> opponentShipsSet = new HashSet<>(opponentShips.values());
        playgrounds.add("" + totalScorePlayer + " " + totalScoreOpponent + " " + difficultyLevel);
        playgrounds.addAll(playerShipsSet.stream()
                .map(Ship::toString)
                .collect(Collectors.toList()));
        playgrounds.addAll(opponentShipsSet.stream()
                .map(Ship::toString)
                .collect(Collectors.toList()));
        playgrounds.addAll(playgroundPlayerList.stream()
                .filter(Cell::wasEverShot)
                .map(e -> e.getValX() + " " + e.getValY() + " player(" + e.isPlayerCell() + ") wasClicked("
                        + e.wasEverShot() + ") isNeighbor(" + e.isNeighbor() + ") " + e.getShip())
                .collect(Collectors.toList()));
        playgrounds.addAll(playgroundOpponentList.stream()
                .filter(Cell::wasEverShot)
                .map(e -> e.getValX() + " " + e.getValY() + " player(" + e.isPlayerCell() + ") wasClicked("
                        + e.wasEverShot() + ") isNeighbor(" + e.isNeighbor() + ") " + e.getShip())
                .collect(Collectors.toList()));

        try {
            Files.write(Paths.get("gameplay.list"), playgrounds);
            System.out.println("game has been saved");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Cell> getNeighbors(Ship ship, boolean player) {
        List<Cell> neighbors = new ArrayList<>();
        if (ship != null) {
            for (int i = 1; i <= ship.getMasts(); i++) {
                if (ship.isHorizontalPosition()) {
                    switch (i) {
                        case 4:
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 2, ship.getYCoordinates(0) - 1, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 2, ship.getYCoordinates(0) + 1, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 3, ship.getYCoordinates(0) - 1, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 3, ship.getYCoordinates(0) + 1, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 4, ship.getYCoordinates(0) - 1, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 4, ship.getYCoordinates(0), player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 4, ship.getYCoordinates(0) + 1, player, true));
                            break;
                        case 3:
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 2, ship.getYCoordinates(0) - 1, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 2, ship.getYCoordinates(0) + 1, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 3, ship.getYCoordinates(0) - 1, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 3, ship.getYCoordinates(0), player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 3, ship.getYCoordinates(0) + 1, player, true));
                            break;
                        case 2:
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 2, ship.getYCoordinates(0) - 1, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 2, ship.getYCoordinates(0), player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 2, ship.getYCoordinates(0) + 1, player, true));
                            break;
                        case 1:
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 1, ship.getYCoordinates(0), player, true));
                            break;
                    }
                    neighbors.add(new Cell(ship.getXCoordinates(0), ship.getYCoordinates(0) + 1, player, true));
                } else {
                    switch (i) {
                        case 4:
                            neighbors.add(new Cell(ship.getXCoordinates(0) - 1, ship.getYCoordinates(0) + 2, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 1, ship.getYCoordinates(0) + 2, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) - 1, ship.getYCoordinates(0) + 3, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 1, ship.getYCoordinates(0) + 3, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) - 1, ship.getYCoordinates(0) + 4, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0), ship.getYCoordinates(0) + 4, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 1, ship.getYCoordinates(0) + 4, player, true));
                            break;
                        case 3:
                            neighbors.add(new Cell(ship.getXCoordinates(0) - 1, ship.getYCoordinates(0) + 2, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 1, ship.getYCoordinates(0) + 2, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) - 1, ship.getYCoordinates(0) + 3, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0), ship.getYCoordinates(0) + 3, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 1, ship.getYCoordinates(0) + 3, player, true));
                            break;
                        case 2:
                            neighbors.add(new Cell(ship.getXCoordinates(0) - 1, ship.getYCoordinates(0) + 2, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0), ship.getYCoordinates(0) + 2, player, true));
                            neighbors.add(new Cell(ship.getXCoordinates(0) + 1, ship.getYCoordinates(0) + 2, player, true));
                            break;
                        case 1:
                            neighbors.add(new Cell(ship.getXCoordinates(0), ship.getYCoordinates(0) + 1, player, true));
                    }
                }
                neighbors.add(new Cell(ship.getXCoordinates(0) + 1, ship.getYCoordinates(0), player, true));
            }
            neighbors.add(new Cell(ship.getXCoordinates(0) - 1, ship.getYCoordinates(0) - 1, player, true));
            neighbors.add(new Cell(ship.getXCoordinates(0) - 1, ship.getYCoordinates(0), player, true));
            neighbors.add(new Cell(ship.getXCoordinates(0) - 1, ship.getYCoordinates(0) + 1, player, true));
            neighbors.add(new Cell(ship.getXCoordinates(0), ship.getYCoordinates(0) - 1, player, true));
            neighbors.add(new Cell(ship.getXCoordinates(0) + 1, ship.getYCoordinates(0) - 1, player, true));
            neighbors.add(new Cell(ship.getXCoordinates(0) + 1, ship.getYCoordinates(0) + 1, player, true));
        }

        return neighbors.stream().filter(e -> isValidCell(e.getValX(), e.getValY())).collect(Collectors.toList());
    }

    public List<Cell> getAllNeighbors(Map<Cell, Ship> shipsMap, boolean player) {
        List<Ship> shipList = new ArrayList<>(shipsMap.values());
        List<Cell> neighborsAll = new ArrayList<>();

        for (Ship ship : shipList) {
            neighborsAll.addAll(getNeighbors(ship,player));
        }
        return neighborsAll.stream().filter(e -> isValidCell(e.getValX(), e.getValY())).collect(Collectors.toList());
    }

    public List<Cell> getCellsToShot() {
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
            List<Cell> cellsToShotList = new ArrayList<>(findNonDiagonalNeighbors(shotsHistory.peekLast()));
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

    public void setMenuMiddleSection(FlowPane menuMiddleSection) {
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

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}