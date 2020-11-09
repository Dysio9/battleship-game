package pl.dysio9.battleship;

import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class Cell extends Rectangle {
    private int x;
    private int y;
    private Ship ship;
    private boolean everShot = false;

    public Cell(int x, int y) {
        this(x,y,null);
    }

    public Cell(int x, int y, Ship ship) {
        super(x,y,38, 38);
        this.x = x;
        this.y = y;
        this.ship = ship;
        setOnMouseClicked(e -> Controller.getInstance().cellClicked(this));
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
