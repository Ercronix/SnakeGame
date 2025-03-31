package net.ercronix.snakegame;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputHandler {

    private Direction currentDirection = Direction.EAST; // Default direction
    public boolean allowDirectionChange = true;
    Direction lastDirection;

    GameBoard gameBoard;

    public void setLastDirection(Direction lastDirection) {
        this.lastDirection = lastDirection;
    }

    public InputHandler(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void setup(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPressed);
    }

    private void handleKeyPressed(KeyEvent event) {
        if (!allowDirectionChange) return;

        if (event.getCode() == KeyCode.ESCAPE) {
            if (gameBoard.gameState == GameState.PAUSE) {
                gameBoard.resume();
            } else {
                gameBoard.pause();
            }
        }

        KeyCode code = event.getCode();

        switch (code) {
            case W, UP:
                if (currentDirection != Direction.SOUTH && lastDirection != Direction.SOUTH) {
                    if(lastDirection == Direction.NORTH) break;
                    currentDirection = Direction.NORTH;
                }
                break;
            case S, DOWN:
                if (currentDirection != Direction.NORTH && lastDirection != Direction.NORTH) {
                    if(lastDirection == Direction.SOUTH) break;
                    currentDirection = Direction.SOUTH;
                }
                break;
            case A, LEFT:
                if (currentDirection != Direction.EAST && lastDirection != Direction.EAST) {
                    if (lastDirection == Direction.WEST) break;
                    currentDirection = Direction.WEST;
                }
                break;
            case D, RIGHT:
                if (currentDirection != Direction.WEST && lastDirection != Direction.WEST) {
                    if (lastDirection == Direction.EAST) break;
                    currentDirection = Direction.EAST;
                }
                break;
            default:
                break;
        }
    }

    public Direction getDirection() {
        return currentDirection;
    }
}
