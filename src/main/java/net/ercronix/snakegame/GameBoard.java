package net.ercronix.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.util.Duration;

import java.awt.*;
import java.util.LinkedList;

public class GameBoard {
    private final static double TICK_RATE = 0.15;
    private Timeline gameLoop;
    private final InputHandler inputHandler;
    private final int boardSize = 20;
    private final Field[][] board;
    private final UiController uiController;
    private boolean wallActive = false;
    private final LinkedList<Point> snake;
    public GameState gameState;
    private Direction direction;

    public GameBoard(Scene scene, UiController uiController) {
        this.uiController = uiController;
        inputHandler = new InputHandler(this);
        inputHandler.setup(scene);
        board = new Field[boardSize][boardSize];
        setupBoard();
        setupGameLoop();
        snake = new LinkedList<>();
        createSnake();
        createApple();
        createApple();
    }

    private void createSnake() {
        snake.add(new Point((boardSize/2)-1, boardSize/2));
        snake.add(new Point(boardSize/2, boardSize/2));
    }

    private void setupGameLoop() {
        gameLoop = new Timeline(new KeyFrame(Duration.seconds(TICK_RATE), event -> tick()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
    }

    public void pause() {
        gameLoop.pause();
        inputHandler.allowDirectionChange = false;
        gameState = GameState.PAUSE;
        uiController.showPauseMenu();
    }
    public void resume() {
        gameLoop.play();
        gameState = GameState.PLAYING;
        uiController.hidePauseMenu();
        inputHandler.allowDirectionChange = true;
    }

    private void setupBoard() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                board[row][col] = Field.GRASS;
            }
        }
        if(wallActive){
            for (int row = 0; row < boardSize; row++) {
                board[row][0] = Field.WALL;
                board[row][boardSize - 1] = Field.WALL;
            }
            for (int col = 0; col < boardSize; col++) {
                board[0][col] = Field.WALL;
                board[boardSize - 1][col] = Field.WALL;
            }
        }
    }

    public void restart() throws InterruptedException {
        gameLoop.stop();
        inputHandler.allowDirectionChange = false;
        snake.clear();
        setupBoard();
        createSnake();
        createApple();
        createApple();
        uiController.drawBoard(board, boardSize);
        uiController.hidePauseMenu();
        uiController.hideGameOverMenu();
        uiController.setScore(1);
        inputHandler.allowDirectionChange = true;
        gameLoop.play();
        gameState = GameState.PLAYING;
    }

    public void start() {
        gameLoop.play();
        gameState = GameState.PLAYING;
    }

    public void stop() {
        gameLoop.stop();
        gameState = GameState.GAMEOVER;
        inputHandler.allowDirectionChange = false;
    }

    private Point nextHeadPosition(Point headPosition) {
        int x = headPosition.x;
        int y = headPosition.y;

        switch (inputHandler.getDirection()) {
            case NORTH:
                y = Math.floorMod(y - 1, boardSize);
                direction = Direction.NORTH;
                break;
            case SOUTH:
                y = Math.floorMod(y + 1, boardSize);
                direction = Direction.SOUTH;
                break;
            case EAST:
                x = Math.floorMod(x + 1, boardSize);
                direction = Direction.EAST;
                break;
            case WEST:
                x = Math.floorMod(x - 1, boardSize);
                direction = Direction.WEST;
                break;
        }
        return new Point(x, y);
    }

    private void tick() {
        Point LastSnakePoint = snake.getFirst();
        Point head = snake.getLast();
        Point newHead = nextHeadPosition(head);
        inputHandler.setLastDirection(direction);
        if (board[newHead.y][newHead.x].equals(Field.SNAKE) || board[newHead.y][newHead.x].equals(Field.WALL)) {
            System.out.println("Game over");
            stop();
            uiController.showGameOverMenu();
            //remove return if snake should die in wall or in itself
            return;
        }
        if (!board[newHead.y][newHead.x].equals(Field.APPLE)) {
            snake.removeFirst();
            uiController.setScore(snake.size());
        } else {
            createApple();
        }
        snake.add(newHead);
        updateBoard(LastSnakePoint);
        uiController.drawBoard(board, boardSize);
        //inputHandler.resetDirectionChange();

    }

    private void updateBoard(Point lastSnakePos) {
        board[lastSnakePos.y][lastSnakePos.x] = Field.GRASS;
        for (int i = 0; i < snake.size(); i++) {
            Point p = snake.get(i);
            board[p.y][p.x] = (i == snake.size() - 1) ? Field.SNAKEHEAD : Field.SNAKE;
        }
    }

    public void createApple() {
        while (true) {
            int randX = (int) (Math.random() * boardSize);
            int randY= (int) (Math.random() * boardSize);
            boolean isOccupied = false;
            for (Point p : snake) {
                if (p.equals(new Point(randX, randY))) {
                    isOccupied = true;
                    break;
                }
            }
            if (board[randY][randX].equals(Field.APPLE) || board[randX][randY].equals(Field.WALL)) {
                isOccupied = true;
            }
            if (!isOccupied) {
                board[randY][randX] = Field.APPLE;
                break;
            }
        }
    }

    public void toggleWall() {
        System.out.println("Toggle wall");
        if (!wallActive) {
            for (int row = 0; row < boardSize; row++) {
                board[row][0] = Field.WALL;
                board[row][boardSize - 1] = Field.WALL;
            }
            for (int col = 0; col < boardSize; col++) {
                board[0][col] = Field.WALL;
                board[boardSize - 1][col] = Field.WALL;
            }
            wallActive = true;
        } else {
            for (int row = 0; row < boardSize; row++) {
                board[row][0] = Field.GRASS;
                board[row][boardSize - 1] = Field.GRASS;
            }
            for (int col = 0; col < boardSize; col++) {
                board[0][col] = Field.GRASS;
                board[boardSize - 1][col] = Field.GRASS;
            }
            wallActive = false;
        }
        uiController.drawBoard(board, boardSize);
    }
}



