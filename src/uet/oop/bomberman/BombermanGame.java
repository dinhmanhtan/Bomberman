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
    
    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;
    public static List<String> s;

    float prevTime =0.0f;
    float deltaTime = 0.0f;
    float totalTime = 0.0f;

    static {
        try {
            s = insert();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private GraphicsContext gc;
    private Canvas canvas;
    protected List<Entity> entities = new ArrayList<>();
    protected List<Entity> stillObjects = new ArrayList<>();
    protected Scene scene;
    protected Bomber bomberman;
    protected Balloom balloom;
    protected List<Grass> grassList = new ArrayList<>();
    protected List<Entity> monsters = new ArrayList<>();


    protected Bomb bomb;

    public static boolean[][] hasWall;

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

   public void Init() {
       bomb = new Bomb();

       hasWall = new boolean[HEIGHT][WIDTH];   // lưu vị trí các wall,brick

       bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
       entities.add(bomberman);
       balloom = new Balloom(15, 10, Sprite.balloom_right1.getFxImage());
       monsters.add(balloom);
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
        scene = new Scene(root);

        // Them scene vao stage
        stage.setScene(scene);
        stage.show();

        Init();
       new AnimationTimer()  {
            @Override
            public void handle(long now) {

              if(prevTime == 0) {
                  prevTime = now;
              }
                deltaTime = (now - prevTime)/1000000000;
                prevTime = now;

                update();
                render();

            }
        }.start();


        createMap();


    }

    public void createMap() {
        for (int i = 0; i < s.size(); i++) {
            for (int j = 0; j < s.get(i).length(); j++) {

                hasWall[i][j] = false;


                  if (s.get(i).charAt(j) == '#') {
                    Wall  object = new Wall(j, i, Sprite.wall.getFxImage());
                        stillObjects.add(object);
                        hasWall[i][j] = true;
                  }
                  else if (s.get(i).charAt(j) == '*') {
                     Brick object = new Brick(j, i, Sprite.brick.getFxImage());
                        hasWall[i][j] = true;
                        stillObjects.add(object);
                  }

                     Grass grass = new Grass(j, i, Sprite.grass.getFxImage());
                     grassList.add(grass);

            }
        }
    }

    public void update() {
       // entities.forEach(Entity::update(scene,0));
        bomberman.update(scene,deltaTime,stillObjects,bomb,monsters,entities);
        balloom.update(deltaTime,stillObjects);

        bomb.update(deltaTime,hasWall,stillObjects);

//       for (Entity entity : entities) {
//           entity.update(scene,deltaTime);
//       }
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        grassList.forEach(g -> g.render(gc));
        stillObjects.forEach(g -> g.render(gc));
        monsters.forEach(g -> g.render(gc));

        if(bomb.isDraw())
            bomb.Render(gc);

        entities.forEach(g -> g.render(gc));

    }

    public static List<String> insert() throws FileNotFoundException {
        List<String> s = new ArrayList<>();
        Scanner scanner = new Scanner(new File("res/levels/Level.txt"));
        while (scanner.hasNext()) {
            String a = scanner.nextLine();
            s.add(a);
        }
        return s;
    }
}
