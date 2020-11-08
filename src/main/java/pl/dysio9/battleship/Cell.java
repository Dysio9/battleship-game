package pl.dysio9.battleship;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class Cell extends Rectangle {
    private int x;
    private int y;
    private Ship ship = null;
//    private PlayGrid playGrid;
    private boolean everShot = false;
    Color color = Color.WHITE;

    public Cell() {}

    public Cell(int x, int y, Ship ship) {
        super(x,y,38, 38);
        this.x = x;
        this.y = y;
        this.ship = ship;
//        setFill(Color.BLUE);
//        setStroke(Color.TRANSPARENT);
    }

    public Cell(int x, int y) {
        super(x,y,38, 38);
        this.x = x;
        this.y = y;
//        setFill(Color.TRANSPARENT);
//        setStroke(Color.TRANSPARENT);
    }
//    public Cell(int x, int y, PlayGrid playGrid) {
//        super(x, y, 38, 38);
//        //super(38, 38);
//        this.x = x;
//        this.y = y;
//        this.ship = ship;
//        this.playGrid = playGrid;
//        setFill(Color.LIGHTBLUE);
//        setStroke(Color.TRANSPARENT);
//    }

//    public boolean shoot() {
//        everShot = true;
//
//        if (ship != null) {
//            ship.hit();
//            setFill(Color.RED);
//            if (ship.isSunk()) {
//                playGrid.setUnsunkShips(playGrid.getUnsunkShips()-1);
//            }
//            return true;
//        } else {
//            setFill(Color.BLACK);
//        }
//        return false;
//    }

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


    public void setShip(Ship ship) {
        this.ship = ship;
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
