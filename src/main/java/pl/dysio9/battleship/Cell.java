package pl.dysio9.battleship;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class Cell extends Rectangle {
    private Controller controller = Controller.getInstance();
    private Constants constants = Constants.getInstance();
    private int x;
    private int y;
    private Ship ship;
    private boolean everShot = false;
    private boolean isPlayerCell;

    public Cell(int x, int y, boolean isPlayerCell) {
        this(x,y,null, isPlayerCell);
    }

    public Cell(int x, int y, Ship ship, boolean isPlayerCell) {
        super(x,y,38, 38);
        this.x = x;
        this.y = y;
        this.ship = ship;
        this.isPlayerCell = isPlayerCell;
        setOnMouseClicked(e -> Controller.getInstance().cellClicked(this));
    }

    public void shot() {
        if (isPlayerCell() != controller.isPlayerTurn()) {
            if (!wasEverShot()) {
                System.out.println("Player Turn: " + controller.isPlayerTurn());

                ImagePattern positiveShotImage = new ImagePattern(constants.getShotPositiveImage());
                ImagePattern negativeShotImage = new ImagePattern(constants.getShotNegativeImage());

                if (isThereAShip()) {
                    this.setFill(positiveShotImage);
                    getShip().hit();
                    System.out.println("Unsunk player(" + controller.isPlayerTurn() + ") cells No: " + controller.getUnsunkCellsCount(controller.isPlayerTurn()));
                    if (controller.getUnsunkCellsCount(controller.isPlayerTurn()) == 0) {
                        if (controller.isPlayerTurn()) {
                            controller.roundWin();
                        } else {
                            controller.roundLost();
                        }
                    }
                } else {
                    this.setFill(negativeShotImage);
                    controller.setPlayerTurn(!controller.isPlayerTurn());
                }
                this.setEverShot(true);
            }
        }
    }

    public int getValX() {
        return x;
    }

    public int getValY() {
        return y;
    }

    public boolean isThereAShip() {
        return ship != null;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean wasEverShot() {
        return everShot;
    }

    public void setEverShot(boolean everShot) {
        this.everShot = everShot;
    }

    public boolean isPlayerCell() {
        return isPlayerCell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x &&
                y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
