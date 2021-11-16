package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    final int width = 625;
    final int height = width;
    final int line = 25;
    final int col = line;
    final int square = width / line;
    final String[] food_sprite = new String[]{"/sprite/food.png", "/sprite/food1.png",
            "/sprite/food2.png", "/sprite/food3.png"};

    final int RIGHT = 0;
    final int LEFT = 1;
    final int UP = 2;
    final int DOWN = 3;

    GraphicsContext getcan;
    List<Point> body = new ArrayList();
    Point head;
    Image foodImage;
    int foodX;
    int foodY;
    boolean gameover;
    int direction;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("snake");
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        getcan = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.D) {
                if (direction != LEFT) {
                    direction = RIGHT;
                }
            } else if (code == KeyCode.A) {
                if (direction != RIGHT) {
                    direction = LEFT;
                }
            } else if (code == KeyCode.W) {
                if (direction != DOWN) {
                    direction = UP;
                }
            } else if (code == KeyCode.S) {
                if (direction != UP) {
                    direction = DOWN;
                }
            }
        });

        for (int i = 0; i < 3; i++) {
            body.add(new Point(5, line / 2));
        }
        head = body.get(0);
        food();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> run(getcan)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void run(GraphicsContext getcan) {
        if (gameover) {
            getcan.setFill(Color.web("8c03fc"));
            getcan.setFont(new Font(100));
            getcan.fillText("Gameover", width / 7.5, height / 2.5);
            return;
        }
        drawBack(getcan);
        drawFood(getcan);
        drawSnake(getcan);

        for (int i = body.size() - 1; i >= 1; i--) {
            body.get(i).x = body.get(i - 1).x;
            body.get(i).y = body.get(i - 1).y;
        }

        switch (direction) {
            case RIGHT:
                moveRight();
                break;
            case LEFT:
                moveLeft();
                break;
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
        }
        gameOver();
        eat();

    }

    private void drawBack(GraphicsContext getcan) {
        for (int i = 0; i < line; i++) {
            for (int j = 0; j < col; j++) {
                getcan.setFill(Color.web("a8b37d"));
                getcan.fillRect(i * square, j * square, square, square);
            }
        }
    }

    private void food() {
        start:
        while (true) {
            foodX = (int) (Math.random() * line);
            foodY = (int) (Math.random() * col);

            for (Point snake : body) {
                if (snake.getX() == foodX && snake.getY() == foodY) {
                    continue start;
                }
            }
            foodImage = new Image(food_sprite[(int) (Math.random() * food_sprite.length)]);
            break;
        }
    }

    private void drawFood(GraphicsContext getcan) {
        getcan.drawImage(foodImage, foodX * square, foodY * square, square, square);
    }

    private void drawSnake(GraphicsContext getcan) {
        getcan.setFill(Color.web("088f06"));

        for (int i = 0; i < body.size(); i++) {
            getcan.fillRoundRect(body.get(i).getX() * square, body.get(i).getY() * square, square,
                    square, 0, 0);
        }
    }

    private void moveRight() {
        head.x++;
    }

    private void moveLeft() {
        head.x--;
    }

    private void moveUp() {
        head.y--;
    }

    private void moveDown() {
        head.y++;
    }

    public void gameOver() {
        if (head.x < 0 || head.y < 0 || head.x * square >= width || head.y * square >= height) {
            gameover = true;
        }
        for (int i = 1; i < body.size(); i++) {
            if (head.x == body.get(i).getX() && head.getY() == body.get(i).getY()) {
                gameover = true;
                break;
            }
        }
    }

    private void eat(){
        if (head.getX() == foodX && head.getY() == foodY){
            body.add(new Point(-1,-1));
            food();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
