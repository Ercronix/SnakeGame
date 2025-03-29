package net.ercronix.snakegame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("Styles.css")).toExternalForm());
        UiController uiController = fxmlLoader.getController();
        scene.getRoot().requestFocus();
        // Ensure key events register
        GameBoard gameBoard = new GameBoard(scene, uiController);
        gameBoard.start();
        uiController.setGameBoard(gameBoard);
        stage.setTitle("Snake Game");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}