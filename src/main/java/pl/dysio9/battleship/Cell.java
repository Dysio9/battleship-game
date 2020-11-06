package pl.dysio9.battleship;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle {
    private int x;
    private int y;
    private Ship ship = null;
    private boolean everShot = false;

    public Cell(int x, int y) {
        super(x, y ,38, 38);
        this.x = x;
        this.y = y;
        setFill(Color.TRANSPARENT);
        setStroke(Color.TRANSPARENT);
    }

    public Cell(int x, int y, Ship ship) {
        super(x, y, 38, 38);
        //super(38, 38);
        this.x = x;
        this.y = y;
        this.ship = ship;
        setFill(Color.LIGHTBLUE);
        setStroke(Color.TRANSPARENT);
    }

    public boolean shoot() {
        everShot = true;

        if (ship != null) {
            ship.hit();
            setFill(Color.RED);
            if (ship.isSunk()) {
                // add some changes in board variable (check if round is end);
            }
            return true;
        } else {
            setFill(Color.BLACK);
        }
        return false;
    }

    public int getValX() {
        return x;
    }

    public int getValY() {
        return y;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean isEverShot() {
        return everShot;
    }

    public void setEverShot(boolean everShot) {
        this.everShot = everShot;
    }
}
