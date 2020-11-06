package pl.dysio9.battleship;

import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import pl.dysio9.battleship.Cell;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.awt.event.MouseEvent;
import java.beans.EventHandler;

public class PlayGrid extends Parent {
    private GridPane playgroundGrid;
    private boolean playerPlayGrid = true;
//    GridPane playGridPlayer = new GridPane();

    public PlayGrid(boolean isPlayerPlayGrid, EventHandler<? super MouseEvent> mouseHandler) {
        super();

        playgroundGrid = new GridPane();
        playgroundGrid.setGridLinesVisible(true);
        playgroundGrid.setPrefSize(380.0, 380.0);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Cell cell = new Cell(x, y);
                playgroundGrid.add(cell, x, y);
                cell.setOnMouseClicked(mouseHandler);

            }
        }
    }
}
