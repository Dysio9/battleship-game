package pl.dysio9.battleship;

import static pl.dysio9.battleship.Constants.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Objects;


public class Cell extends Rectangle {
    private Controller controller = Controller.getInstance();
//    private Constants constants = Constants.getInstance();
    private int x;
    private int y;
    private Ship ship;
    private boolean isPlayerCell;
    private boolean everShot = false;
    private boolean isNeighbor = false;

    public Cell(int x, int y, boolean isPlayerCell, boolean isNeighbor) {
        this(x,y,null, isPlayerCell);
        this.isNeighbor = isNeighbor;
    }

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

    public ImagePattern getCellFill() {
        if (ship != null) {
            if (everShot) {
                return SHOT_POSITIVE_IMAGE;
            } else {
                return CELL_TRANSPARENT_IMAGE;
            }
        } else {
            if (everShot) {
                return SHOT_NEGATIVE_IMAGE;
            } else {
                return CELL_TRANSPARENT_IMAGE;
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

    public Cell getCell(int x, int y) {
        return this;
    }

    public boolean wasEverShot() {
        return everShot;
    }

    public void setClicked() {
        this.everShot = true;
    }

    public boolean isPlayerCell() {
        return isPlayerCell;
    }

    public boolean isNeighbor() {
        return isNeighbor;
    }

    public void setNeighbor(boolean neighbor) {
        isNeighbor = neighbor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell c = (Cell) o;
        return x == c.x &&
                y == c.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        if (ship != null) {
            return "(" + x + "," + y + ") clicked(" + everShot + "), isNeighbour(" + isNeighbor + "), Ship(" + ship + ")";
        } else {
            return "(" + x + "," + y + ") clicked(" + everShot + "), isNeighbour(" + isNeighbor + "), Ship(null)";
        }
    }
}