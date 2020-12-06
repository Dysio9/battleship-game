package pl.dysio9.battleship;

import static pl.dysio9.battleship.Constants.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        Label label = new Label(message);
        label.setFont(Font.font ("Verdana", FontWeight.BOLD, 26));
        label.setTextFill(Color.NAVY);

        Button closeButton = new Button("OK");
        closeButton.setAlignment(Pos.CENTER);
        closeButton.setPrefSize(200, 20);
        closeButton.setOnAction(e -> window.close());


        BackgroundSize backgroundSize = new BackgroundSize(950, 950, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(BACKGROUND_IMAGE, BackgroundRepeat.SPACE, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        BorderPane grid = new BorderPane();
        grid.setPadding(new Insets(0, 0, 20.0, 0));
        grid.setBackground(background);
        grid.setCenter(label);
        grid.setBottom(closeButton);
        BorderPane.setAlignment(closeButton,Pos.CENTER);

        Scene scene = new Scene(grid, 500, 150, Color.BLACK);
        window.setScene(scene);
        window.setResizable(false);
        window.show();
    }
}
