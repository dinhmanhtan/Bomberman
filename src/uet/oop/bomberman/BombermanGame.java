package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;
import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

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
    protected List<Entity> stillObjects ;
    public static Bomber bomberman;
    public static Balloom balloom;
    protected List<Grass> grassList ;
    public static List<Entity> monsters ;
    public  List<Bomb> bombs ;
    public  Item item;
    public static boolean[][] hasWallPlayer;  // dành cho player
    public static boolean[][] hasWallMonster;  // dành cho quái

    public PlayMusic music;
    public static  MediaPlayer mediaPlayer;
    public  Media media;
    public static boolean GameOver;

    public static boolean music_stage;
    double timeResetStage;
    double timePressEnter;
    AnimationTimer animationTimer;

    public static void main(String[] args) { Application.launch(BombermanGame.class); }

    public void Init() {

        GameOver = false;
        timeResetStage = 0;

        try {
               s = insert();
        } catch (FileNotFoundException e) {
               e.printStackTrace();
        }
        entities = new ArrayList<>();
        stillObjects = new ArrayList<>();
        grassList = new ArrayList<>();
        monsters = new ArrayList<>();
        bombs = new ArrayList<>();


        Bomb bomb = new Bomb();
        bombs.add(bomb);

        music = new PlayMusic();
        music_stage = true;

        item = new Item();
        hasWallPlayer = new boolean[HEIGHT][WIDTH];   // lưu vị trí các wall,brick
        hasWallMonster = new boolean[HEIGHT][WIDTH];

        bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        entities.add(bomberman);
        balloom = new Balloom(4, 5, Sprite.balloom_right1.getFxImage());


        monsters.add(balloom);
   }


    @Override
    public void start(Stage stage) throws IOException {

        stage.initStyle(StageStyle.UNDECORATED);   // cài đặt window bỏ thanh ở trên đầu

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

            Game(stage);


        });

    }

    public void Game(Stage stage) {
        mediaPlayer.stop();
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE *HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();

        root.getChildren().add(canvas);
        canvas.setLayoutY(Sprite.SCALED_SIZE);
        // Tao scene
        scene = new Scene(root,Sprite.SCALED_SIZE*WIDTH,Sprite.SCALED_SIZE*(HEIGHT+1));

        // Them scene vao stage
        stage.setScene(scene);
        //    stage.show();

        Init();
        createMap();
        item.setPosition(stillObjects);
        timeResetStage =0;


        animationTimer =   new AnimationTimer()  {
            @Override
            public void handle(long now) {

                if(prevTime == 0) {
                    prevTime = now;
                }
                deltaTime = (now - prevTime)/1000000000;
                prevTime = now;

                if (!GameOver) {
                    update();
                    render();
                } else {
                    timeResetStage += deltaTime;

                    if(timeResetStage > 3) {

                        animationTimer.stop();
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

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(prevTime == 0) {
                    prevTime = now;
                }
                deltaTime = (now - prevTime)/1000000000;
                prevTime = now;

                scene.setOnKeyPressed(event -> {
                    if(event.getCode() == KeyCode.ENTER) {
                        animationTimer.stop();
                        mediaPlayer.stop();
                        TitleScreen(stage);
                    }
                });

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
