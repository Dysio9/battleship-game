package pl.dysio9.battleship;

public class Ship {
    private final int[] xArray;
    private final int[] yArray;
    private final int masts;
    private final boolean horizontalPosition;
    private int mastsHit = 0;

    public Ship(int x, int y, int masts, boolean horizontalPosition) {
        this.masts = masts;
        this.horizontalPosition = horizontalPosition;
        this.xArray = new int[masts];
        this.yArray = new int[masts];
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

    public int getXCoordinates(int i) {
        return xArray[i];
    }

    public int getYCoordinates(int i) {
        return yArray[i];
    }

    public int getMasts() {
        return masts;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s isHorizontal(%s)", xArray[0],yArray[0],masts, isHorizontalPosition());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Ship ship = (Ship) o;
        return xArray[0] == ship.xArray[0] &&
                yArray[0] == ship.yArray[0];
    }
}
