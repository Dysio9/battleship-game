package pl.dysio9.battleship;


import javafx.scene.Parent;
import javafx.scene.image.Image;

public class Ship {
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
//        this.x = x;
//        this.y = y;
        this.masts = masts;
        this.horizontalPosition = horizontalPosition;
        if (horizontalPosition) {
            switch (masts) {
                case 4:
                    this.x3 = x + 3;
                    this.y3 = y;
                case 3:
                    this.x2 = x + 2;
                    this.y2 = y;
                case 2:
                    this.x1 = x + 1;
                    this.y1 = y;
                case 1:
                    this.x = x;
                    this.y = y;
            }
        } else {
            switch (masts) {
            case 4:
                this.x3 = x;
                this.y3 = y + 3;
            case 3:
                this.x2 = x;
                this.y2 = y + 2;
            case 2:
                this.x1 = x;
                this.y1 = y + 1;
            case 1:
                this.x = x;
                this.y = y;
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

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getX3() {
        return x3;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }

    public int getY3() {
        return y3;
    }

    public void showAllCoordinates() {
        System.out.print("Koordynaty statku: ");
        System.out.print("[" + y + "," + x + "]");
        System.out.print("[" + y1 + "," + x1 + "]");
        System.out.print("[" + y2 + "," + x2 + "]");
        System.out.print("[" + y3 + "," + x3 + "]");
    }
}
