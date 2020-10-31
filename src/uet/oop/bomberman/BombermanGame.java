package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BombermanGame extends Application {
    
    public static final int WIDTH = 13;
    public static final int HEIGHT = 31;
    public static List<String> s;

    static {
        try {
            s = insert();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private GraphicsContext gc;
    private Canvas canvas;
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();


    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case D: entities.get(0).setX(entities.get(0).getX() + 1);
                    break;
                case A: entities.get(0).setX(entities.get(0).getX() - 1);
                    break;
                case W: entities.get(0).setY(entities.get(0).getY() - 1);
                    break;
                case S: entities.get(0).setY(entities.get(0).getY() + 1);
                    break;
            }
        });
        // Them scene vao stage
        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                render();
                update();
            }
        };
        timer.start();

        createMap();

        Entity bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        entities.add(bomberman);
    }

    public void createMap() {
        for (int i = 0; i < s.size(); i++) {
            for (int j = 0; j < s.get(i).length(); j++) {
                Entity object;
//                if (j == 0 || j == HEIGHT - 1 || i == 0 || i == WIDTH - 1) {
//                    object = new Wall(i, j, Sprite.wall.getFxImage());
//                }
//                else {
//                    object = new Grass(i, j, Sprite.grass.getFxImage());
//                }
                  if (s.get(i).charAt(j) == '#') {
                      object = new Wall(i, j, Sprite.wall.getFxImage());
                  }
                  else if (s.get(i).charAt(j) == '*') {
                      object = new Brick(i, j, Sprite.brick.getFxImage());
                  }
                  else {
                      object = new Grass(i, j, Sprite.grass.getFxImage());
                  }
                stillObjects.add(object);
            }
        }
    }

    public void update() {
        entities.forEach(Entity::update);
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
    }

    public static List<String> insert() throws FileNotFoundException {
        List<String> s = new ArrayList<>();
        Scanner scanner = new Scanner(new File("E:\\New folder (4)\\Bomberman\\res\\levels\\level.txt"));
        while (scanner.hasNext()) {
            String a = scanner.nextLine();
            s.add(a);
        }
        return s;
    }
}
