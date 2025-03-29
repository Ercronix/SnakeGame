package net.ercronix.snakegame;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class UiController implements Initializable {

    public Label Score;
    public ToggleButton toggleWallButton;
    @FXML
    private GridPane gameGrid;
    @FXML
    private StackPane rootPane; // StackPane to overlay pause menu over game
    private boolean snakeColored = false;
    private GameBoard gameBoard;
    private VBox pauseMenu;

    private VBox gameOverMenu;

    private final Image appleImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("Apple.png")));

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void drawBoard(Field[][] board, int boardSize) {
        gameGrid.getChildren().clear();
        snakeColored = !snakeColored;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Rectangle tile = new Rectangle(boardSize, boardSize);
                switch (board[row][col]) {
                    case GRASS -> tile.setFill(Color.rgb(100, 160, 80, 1));
                    case WALL -> tile.setFill(Color.rgb(30,30,30,1));
                    case APPLE ->  {
                        tile.setFill(new ImagePattern(appleImage));
                    }
                    case SNAKE -> {
                        if ((row + col) % 2 == 0) {
                            tile.setFill(snakeColored ? Color.rgb(45, 45, 35, 1) : Color.rgb(30, 30, 25, 1));
                        } else {
                            tile.setFill(snakeColored ? Color.rgb(30, 30, 25, 1) : Color.rgb(45, 45, 35, 1));
                        }
                    }
                    case SNAKEHEAD -> tile.setFill(Color.BLACK);
                }
                gameGrid.add(tile, col, row);
            }
        }
    }

    public void toggleWall() {
        gameBoard.toggleWall();
    }

    public void setScore(int score) {
        Score.setText("Score: " + score);
    }

    public void showPauseMenu() {
        if (pauseMenu == null) {
            pauseMenu = new VBox(20);
            pauseMenu.setAlignment(Pos.CENTER);
            pauseMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-padding: 40; -fx-background-radius: 10;");
            pauseMenu.setMaxSize(300, 200);

            Label pausedLabel = new Label("Game Paused");
            pausedLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24;");

            Button toggleWallButton = new Button("Toggle Wall");
            toggleWallButton.getStyleClass().add("button-3"); // Apply the CSS style here
            toggleWallButton.setOnAction(e -> {
                gameBoard.toggleWall();
            });

            Button resumeButton = new Button("Resume");
            resumeButton.getStyleClass().add("button-3"); // Apply the CSS style here
            resumeButton.setOnAction(e -> {
                hidePauseMenu();
                gameBoard.resume();
            });

            Button exitButton = new Button("Exit");
            exitButton.getStyleClass().add("button-3"); // Apply the CSS style here
            exitButton.setOnAction(e -> {
                System.exit(0);
            });

            Button restartButton = new Button("Restart");
            restartButton.getStyleClass().add("button-3");// Apply the CSS style here
            restartButton.setOnAction(e -> {
                try {
                    gameBoard.restart();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });

            pauseMenu.getChildren().addAll(pausedLabel,resumeButton, toggleWallButton, restartButton, exitButton);
        }

        if (!rootPane.getChildren().contains(pauseMenu)) {
            rootPane.getChildren().add(pauseMenu);
        }
    }

    public void showGameOverMenu() {
        if (gameOverMenu == null) {
            gameOverMenu = new VBox(20);
            gameOverMenu.setAlignment(Pos.CENTER);
            gameOverMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-padding: 40; -fx-background-radius: 10;");
            gameOverMenu.setMaxSize(300, 300);

            Label gameOverLabel = new Label("Game Over");
            gameOverLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24;");

            Button restartButton = new Button("Restart");
            restartButton.getStyleClass().add("button-3"); // Apply the CSS style here
            restartButton.setOnAction(e -> {
                try {
                    gameBoard.restart();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });

            Button exitButton = new Button("Exit");
            exitButton.getStyleClass().add("button-3"); // Apply the CSS style here
            exitButton.setOnAction(e -> {
                System.exit(0);
            });

            gameOverMenu.getChildren().addAll(gameOverLabel,restartButton, exitButton);
        }

        if (!rootPane.getChildren().contains(gameOverMenu)) {
            rootPane.getChildren().add(gameOverMenu);
        }
    }


    public void hidePauseMenu() {
        rootPane.getChildren().remove(pauseMenu);
    }

    public void hideGameOverMenu() {
        rootPane.getChildren().remove(gameOverMenu);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}
