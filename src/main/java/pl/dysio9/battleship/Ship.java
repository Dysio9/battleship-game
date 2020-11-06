package pl.dysio9.battleship;


import javafx.scene.Parent;
import javafx.scene.image.Image;

public class Ship extends Parent {
    private int x, x1, x2 ,x3;
    private int y ,y1, y2, y3;
    private int masts;
    private int mastsHit = 0;
    private boolean horizontalPosition;

    private Image ship4mast = new Image("file:src/main/resources/ship4mast.png");
    private Image ship3mast = new Image("file:src/main/resources/ship3mast.png");
    private Image ship2mast = new Image("file:src/main/resources/ship2mast.png");
    private Image ship1mast = new Image("file:src/main/resources/ship1mast.png");
    private Image ship4mastSinked = new Image("file:src/main/resources/ship4mast-sunk.png");
    private Image ship3mastSinked = new Image("file:src/main/resources/ship3mast-sunk.png");
    private Image ship2mastSinked = new Image("file:src/main/resources/ship2mast-sunk.png");
    private Image ship1mastSinked = new Image("file:src/main/resources/ship1mast-sunk.png");

    public Ship(int x, int y, int masts, boolean horizontalPosition) {
        this.x = x;
        this.y = y;
        this.masts = masts;
        this.horizontalPosition = horizontalPosition;
        if (horizontalPosition) {
            if (masts > 1) {
                this.x1 = x + 1;
                this.y1 = y;
                if (masts > 2) {
                    this.x2 = x + 2;
                    this.y2 = y;
                    if(masts > 3) {
                        this.x3 = x + 3;
                        this.y3 = y;
                    }
                }
            }
        } else {
            if (masts > 1) {
                this.x1 = x;
                this.y1 = y + 1;
                if (masts > 2) {
                    this.x2 = x;
                    this.y2 = y + 2;

                    if (masts > 3) {
                        this.x3 = x;
                        this.y3 = y + 3;
                    }
                }
            }
        }
    }

    public void hit() {
        mastsHit++;
    }

    public boolean isSunk() {
        return mastsHit >= masts;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isHorizontalPosition() {
        return horizontalPosition;
    }

    public void setHorizontalPosition(boolean horizontalPosition) {
        this.horizontalPosition = horizontalPosition;
    }

    public int getMasts() {
        return masts;
    }

    public void setMasts(int masts) {
        this.masts = masts;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getX3() {
        return x3;
    }

    public void setX3(int x3) {
        this.x3 = x3;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getY3() {
        return y3;
    }

    public void setY3(int y3) {
        this.y3 = y3;
    }

    public Image getShip4mast() {
        return ship4mast;
    }

    public void setShip4mast(Image ship4mast) {
        this.ship4mast = ship4mast;
    }

    public Image getShip3mast() {
        return ship3mast;
    }

    public void setShip3mast(Image ship3mast) {
        this.ship3mast = ship3mast;
    }

    public Image getShip2mast() {
        return ship2mast;
    }

    public void setShip2mast(Image ship2mast) {
        this.ship2mast = ship2mast;
    }

    public Image getShip1mast() {
        return ship1mast;
    }

    public void setShip1mast(Image ship1mast) {
        this.ship1mast = ship1mast;
    }

    public Image getShip4mastSinked() {
        return ship4mastSinked;
    }

    public void setShip4mastSinked(Image ship4mastSinked) {
        this.ship4mastSinked = ship4mastSinked;
    }

    public Image getShip3mastSinked() {
        return ship3mastSinked;
    }

    public void setShip3mastSinked(Image ship3mastSinked) {
        this.ship3mastSinked = ship3mastSinked;
    }

    public Image getShip2mastSinked() {
        return ship2mastSinked;
    }

    public void setShip2mastSinked(Image ship2mastSinked) {
        this.ship2mastSinked = ship2mastSinked;
    }

    public Image getShip1mastSinked() {
        return ship1mastSinked;
    }

    public void setShip1mastSinked(Image ship1mastSinked) {
        this.ship1mastSinked = ship1mastSinked;
    }
}
