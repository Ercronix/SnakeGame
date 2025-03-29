package net.ercronix.snakegame;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputHandler {

    private Direction currentDirection = Direction.EAST; // Default direction
    public boolean allowDirectionChange = true;
    GameBoard gameBoard;

    public InputHandler(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void setup(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPressed);
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            if (gameBoard.gameState == GameState.PAUSE) {
                gameBoard.resume();
            } else {
                gameBoard.pause();
            }
        }
        if (!allowDirectionChange) return;

        KeyCode code = event.getCode();

        switch (code) {
            case W, UP:
                if (currentDirection != Direction.SOUTH) {
                    currentDirection = Direction.NORTH;
                    allowDirectionChange = false;
                }
                break;
            case S, DOWN:
                if (currentDirection != Direction.NORTH) {
                    currentDirection = Direction.SOUTH;
                    allowDirectionChange = false;
                }
                break;
            case A, LEFT:
                if (currentDirection != Direction.EAST) {
                    currentDirection = Direction.WEST;
                    allowDirectionChange = false;
                }
                break;
            case D, RIGHT:
                if (currentDirection != Direction.WEST) {
                    currentDirection = Direction.EAST;
                    allowDirectionChange = false;
                }
                break;
            default:
                break;
        }
    }

    public Direction getDirection() {
        return currentDirection;
    }

    public void resetDirectionChange() {
        allowDirectionChange = true;
    }
}
