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

    static public Scene scene;



    private GraphicsContext gc;
    private Canvas canvas;
    protected List<Entity> entities = new ArrayList<>();
    protected List<Entity> stillObjects = new ArrayList<>();

    public static Bomber bomberman;

    public static Balloom balloom;
    protected List<Grass> grassList = new ArrayList<>();
    public static List<Entity> monsters = new ArrayList<>();


    public  List<Bomb> bombs = new ArrayList<>();
    public  Item item;

    public static boolean[][] hasWallPlayer;  // dành cho player
    public static boolean[][] hasWallMonster;  // dành cho quái

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    public void Init() {


        try {
               s = insert();
        } catch (FileNotFoundException e) {
               e.printStackTrace();
        }

        Bomb bomb = new Bomb();
        bombs.add(bomb);

//        Bomb bomb1 = new Bomb();
//        bombs.add(bomb1);
//
//        Bomb bomb2 = new Bomb();
//        bombs.add(bomb2);

        item = new Item();
        hasWallPlayer = new boolean[HEIGHT][WIDTH];   // lưu vị trí các wall,brick
        hasWallMonster = new boolean[HEIGHT][WIDTH];



        bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        entities.add(bomberman);
        balloom = new Balloom(4, 5, Sprite.balloom_right1.getFxImage());
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
        createMap();
        item.setPosition(stillObjects);


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





    }

    public void createMap() {
        for (int i = 0; i < s.size(); i++) {
            for (int j = 0; j < s.get(i).length(); j++) {

                hasWallPlayer[i][j] = false;
                hasWallMonster[i][j] = false;

                  if (s.get(i).charAt(j) == '#') {

                    Wall  object = new Wall(j, i, Sprite.wall.getFxImage());
                        stillObjects.add(object);
                        hasWallPlayer[i][j] = true;
                        hasWallMonster[i][j] = true;
                  }
                  else if (s.get(i).charAt(j) == '*') {
                     Brick object = new Brick(j, i, Sprite.brick.getFxImage());
                      hasWallPlayer[i][j] = true;
                      hasWallMonster[i][j] = true;
                      stillObjects.add(object);
                  }

                     Grass grass = new Grass(j, i, Sprite.grass.getFxImage());
                     grassList.add(grass);

            }
        }
    }

    public void update() {
       // entities.forEach(Entity::update(scene,0));
        bomberman.update(deltaTime,stillObjects,bombs,monsters);

        if(balloom.isDraw())
        balloom.update(deltaTime,stillObjects);

        item.update(bombs);

        for (Bomb bom : bombs)
          bom.update(deltaTime,stillObjects);

//       for (Entity entity : entities) {
//           entity.update(scene,deltaTime);
//       }
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        grassList.forEach(g -> g.render(gc));
        item.render(gc);
        stillObjects.forEach(g -> g.render(gc));

        if(!monsters.isEmpty())
        monsters.forEach(g -> g.render(gc));

        for (Bomb bom : bombs)
          if(bom.isDraw())
             bom.Render(gc);

        bomberman.render(gc);

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
