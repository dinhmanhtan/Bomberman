package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.monster.Balloom;
import uet.oop.bomberman.monster.Kondoria;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.monster.Monster;

import java.io.*;
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
    protected List<Entity> entities ;
    public static List<Entity> stillObjects ;
    public static Bomber bomberman ;
    public static Balloom balloom[] = new Balloom[4];
    public static Kondoria kondoria[] = new Kondoria[4];
    protected List<Grass> grassList ;
    public static List<Monster> monsters ;
    public  List<Bomb> bombs ;
    public  Item item;
    public static boolean[][] hasWallPlayer;  // dành cho player
    public static boolean[][] hasWallMonster;  // dành cho quái
    public static boolean[][] isOk;

    public PlayMusic music;
    public static  MediaPlayer mediaPlayer;
    public  Media media;
    public static boolean GameOver;
//
//    public static boolean music_stage;
    public double timeResetStage;
    public double timePressEnter;
    public AnimationTimer animationTimer;
    public Label labelTimer,labelScore;
    public double timer;

    public static void main(String[] args) { Application.launch(BombermanGame.class); }

    public void Init() {

        GameOver = false;
//        timeResetStage = 0;

        try {
               s = insert();
        } catch (FileNotFoundException e) {
               e.printStackTrace();
        }
//
//        stillObjects = new ArrayList<>();
//        grassList = new ArrayList<>();
        monsters = new ArrayList<>();
//        bombs = new ArrayList<>();
//
//
//        Bomb bomb = new Bomb();
//        bombs.add(bomb);

//        music = new PlayMusic();
//        music_stage = true;

 //       item = new Item();
        hasWallPlayer = new boolean[HEIGHT][WIDTH];   // lưu vị trí các wall,brick
        hasWallMonster = new boolean[HEIGHT][WIDTH];
        isOk = new boolean[HEIGHT][WIDTH];


        bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
   }

   public void InitStage1() {
        GameOver = false;

         stillObjects = new ArrayList<>();
         grassList = new ArrayList<>();
         monsters = new ArrayList<>();
         bombs = new ArrayList<>();

         balloom[0] = new Balloom(4, 5, Sprite.balloom_right1.getFxImage());
         balloom[1] = new Balloom(12, 1, Sprite.balloom_right1.getFxImage());
         balloom[2] = new Balloom(25, 6, Sprite.balloom_right1.getFxImage());
         kondoria[0] = new Kondoria(8,9,Sprite.kondoria_right1.getFxImage());

         item = new Item();
         Bomb bomb = new Bomb();
         bombs.add(bomb);

         monsters.add(balloom[0]);
         monsters.add(balloom[1]);
         monsters.add(balloom[2]);
         monsters.add(kondoria[0]);
         bomberman.Reset();

         createMap();
   }

    @Override
    public void start(Stage stage) throws IOException {

      //  stage.initStyle(StageStyle.UNDECORATED);   // cài đặt window bỏ thanh ở trên đầu
      Init();
      TitleScreen(stage);
    }

    public void TitleScreen(Stage stage) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getStyleClass().add("title-screen");

        Button buttonPlay = new Button();
        buttonPlay.setText("Play Game");
        Button buttonExit = new Button();
        buttonExit.setText("Exit");


        if(Sprite.SCALED_SIZE == 48) {
            buttonExit.getStyleClass().add("button-title1");
            buttonPlay.getStyleClass().add("button-title1");
            buttonPlay.setLayoutX(624);
            buttonPlay.setLayoutY(480);
            buttonPlay.setPrefSize(200, 50);
            buttonExit.setLayoutX(624);
            buttonExit.setLayoutY(540);
            buttonExit.setPrefSize(200, 50);
        } else {
            buttonExit.getStyleClass().add("button-title2");
            buttonPlay.getStyleClass().add("button-title2");
            buttonPlay.setLayoutX(400);
            buttonPlay.setLayoutY(300);
            buttonPlay.setPrefSize(150, 50);
            buttonExit.setLayoutX(400);
            buttonExit.setLayoutY(340);
            buttonExit.setPrefSize(150, 50);

        }


        anchorPane.getChildren().addAll(buttonExit,buttonPlay);

        scene = new Scene(anchorPane, Sprite.SCALED_SIZE*WIDTH, Sprite.SCALED_SIZE*(HEIGHT+1));
        scene.getStylesheets().add(getClass().getResource("/fxml/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        PlayMusicLoop(PlayMusic.title_screen_music);

        buttonExit.setOnMouseClicked(event -> stage.close());

        buttonPlay.setOnMouseClicked(event -> {
            mediaPlayer.stop();
            Game(stage);


        });

    }

    public void Game(Stage stage) {
//        mediaPlayer.stop();
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE *HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        AnchorPane root = new AnchorPane();
        root.getStyleClass().add("screen-playing");
        labelTimer = new Label();
        labelTimer.setLayoutX(3*Sprite.SCALED_SIZE);
        labelTimer.setLayoutY(4);

        labelScore = new Label();
        labelScore.setLayoutX(10*Sprite.SCALED_SIZE);
        labelScore.setLayoutY(4);


        root.getChildren().addAll(canvas,labelTimer,labelScore);
        canvas.setLayoutY(Sprite.SCALED_SIZE);
        // Tao scene
        scene = new Scene(root,Sprite.SCALED_SIZE*WIDTH,Sprite.SCALED_SIZE*(HEIGHT+1));
        scene.getStylesheets().add(getClass().getResource("/fxml/style.css").toExternalForm());

        // Them scene vao stage
        stage.setScene(scene);
        //    stage.show();

        InitStage1();

        item.setPosition(stillObjects);
        timeResetStage =0;

        AnimationTimer animationTimer =   new AnimationTimer()  {

            @Override
            public void handle(long now) {

                if(prevTime == 0) {
                    prevTime = now;
                }
                deltaTime = (now - prevTime)/1000000000;
                prevTime = now;

                if (!GameOver) {
                    timer -= deltaTime;
                    update();
                    render();
                } else {
                    timeResetStage += deltaTime;
                    mediaPlayer.stop();
                    if(timeResetStage > 3) {

                        this.stop();

                        GameOver(stage);

                    }

                }
            }

        };
        animationTimer.start();

        PlayMusicLoop(PlayMusic.stage_theme_music);

    }

    public void GameOver(Stage stage) {

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getStyleClass().add("screen-game-over");

        Label label = new Label("Press Enter");
        label.getStyleClass().add("label-game-over");
        label.setLayoutX(WIDTH*Sprite.SCALED_SIZE/2.3);
        label.setLayoutY(HEIGHT*Sprite.SCALED_SIZE*0.8);

        anchorPane.getChildren().add(label);

        scene = new Scene(anchorPane,WIDTH*Sprite.SCALED_SIZE,(HEIGHT+1)*Sprite.SCALED_SIZE);
        scene.getStylesheets().add(getClass().getResource("/fxml/style.css").toExternalForm());
        stage.setScene(scene);

        stage.show();


        PlayMusicLoop(PlayMusic.game_over_music);

        prevTime = 0;
        timePressEnter =0;

      AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(prevTime == 0) {
                    prevTime = now;
                }
                deltaTime = (now - prevTime)/1000000000;
                prevTime = now;



                timePressEnter += deltaTime;

                if(timePressEnter <= 1) {
                    label.setTextFill(Color.WHITE);


                } else if(timePressEnter <=2){
                    label.setTextFill(Color.BLACK);

                } else
                    timePressEnter =0;
            }
        };
        animationTimer.start();

        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                animationTimer.stop();
                mediaPlayer.stop();
                TitleScreen(stage);
            }
        });

    }



   public void PlayMusicLoop(String path) {


        media = new Media(new File(path).toURI().toString());
       mediaPlayer = new MediaPlayer(media);
       mediaPlayer.setOnEndOfMedia(new Runnable() {
           public void run() {
               mediaPlayer.seek(Duration.ZERO);
           }
       });

       mediaPlayer.play();
   }

    public void createMap() {
        for (int i = 0; i < s.size(); i++) {
            for (int j = 0; j < s.get(i).length(); j++) {

                hasWallPlayer[i][j] = false;
                hasWallMonster[i][j] = false;
                isOk[i][j] = false;
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


        item.update(bombs);

        for (Bomb bom : bombs)
          bom.update(deltaTime,stillObjects);


        for(int i=0; i<monsters.size(); i++) {
            if(monsters.get(i).isDraw())
                monsters.get(i).update(deltaTime);
            else {
                monsters.remove(i);
                i--;
            }
        }

        labelTimer.setText("Time : " + (int)bomberman.timer);
        labelScore.setText("Score : " + bomberman.score);
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
