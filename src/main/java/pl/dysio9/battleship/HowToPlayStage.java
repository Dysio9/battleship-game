package pl.dysio9.battleship;

import static pl.dysio9.battleship.Constants.*;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HowToPlayStage {
    public static void display() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("How to play");

        BackgroundSize backgroundSize = new BackgroundSize(631, 850, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(HOW_TO_PLAY_IMAGE, BackgroundRepeat.SPACE, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        BorderPane grid = new BorderPane();
        grid.setBackground(background);

        Scene scene = new Scene(grid, 631, 850, Color.TRANSPARENT);
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();
    }
}
