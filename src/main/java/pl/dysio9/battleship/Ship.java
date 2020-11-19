package pl.dysio9.battleship;


public class Ship {
    private int x, x1 = -1, x2 = -1 ,x3 = -1;
    private int y ,y1 = -1, y2 = -1, y3 = -1;
    private int xArray[];
    private int yArray[];
    private int masts;
    private int mastsHit = 0;
    private boolean horizontalPosition;

    public Ship(int x, int y, int masts, boolean horizontalPosition) {
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

        xArray = new int[masts];
        yArray = new int[masts];
        if (horizontalPosition) {
            for (int i = 0; i < masts; i++) {
                this.xArray[i] = x + i;
                this.yArray[i] = y;
            }
        } else {
            for (int i = 0; i < masts; i++) {
                this.xArray[i] = x;
                this.yArray[i] = y + i;
            }
        }
    }

    public void hit() {
        mastsHit++;
    }

    public boolean isSunk() {
        return mastsHit >= masts;
    }

    public boolean isHorizontalPosition() {
        return horizontalPosition;
    }

    public void setHorizontalPosition(boolean horizontalPosition) {
        this.horizontalPosition = horizontalPosition;
    }

    public int getXCoordinates(int i) {
        return xArray[i];
    }

    public int getYCoordinates(int i) {
        return yArray[i];
    }

    public int getMasts() {
        return masts;
    }

    public int getX() {
        return x;
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

    public int getY() {
        return y;
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
        System.out.print("Nowe koordynaty statku " + masts + "masztowego:");
        System.out.print("[" + yArray[0] + "," + xArray[0] + "]");
        if (masts >= 2) System.out.print("[" + yArray[1] + "," + xArray[1] + "]");
        if (masts >= 3) System.out.print("[" + yArray[2] + "," + xArray[2] + "]");
        if (masts >= 4) System.out.print("[" + yArray[3] + "," + xArray[3] + "]");
        System.out.println();
    }

    @Override
    public String toString() {
        return "masts=" + masts + '}';
    }
}
