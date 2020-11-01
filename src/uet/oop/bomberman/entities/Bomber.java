package uet.oop.bomberman.entities;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import uet.*;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

public class Bomber extends Entity {

    public enum State{Left,Up,Right,Down}
    public  State state ;
    public static  double totalTime ;

    public boolean move;
    public Image[] player_right, player_left,player_down, player_up;
    public int[] indexImg;

    public Bomber(int x, int y, Image img) {
        super( x, y, img);
        Init();
    }



    public void Init(){

        totalTime =0.0;
        move =false;

        player_right = new Image[3];
        player_left = new Image[3];
        player_down = new Image[3];
        player_up = new Image[3];

        player_right[0] = Sprite.player_right.getFxImage();
        player_right[1] = Sprite.player_right_1.getFxImage();
        player_right[2] = Sprite.player_right_2.getFxImage();

        player_left[0]  = Sprite.player_left.getFxImage();
        player_left[1]  = Sprite.player_left_1.getFxImage();
        player_left[2]  = Sprite.player_left_2.getFxImage();

        player_up[0] = Sprite.player_up.getFxImage();
        player_up[1] = Sprite.player_up_1.getFxImage();
        player_up[2] = Sprite.player_up_2.getFxImage();

        player_down[0] = Sprite.player_down.getFxImage();
        player_down[1] = Sprite.player_down_1.getFxImage();
        player_down[2] = Sprite.player_down_2.getFxImage();

        indexImg = new int[4];
        indexImg[0] = indexImg[1] = indexImg[2] = indexImg[3] = 0;
    }


    @Override
    public void update(Scene scene,double deltaTime) {


        scene.setOnKeyPressed(event ->  {
            
                switch (event.getCode()) {
                    case A:
                        indexImg[0]++;

                        if (x > 1) {
                            x -= 1 / 3.0;
                            setImg(player_left[indexImg[0] % 3]);

                        }
                        break;

                    case D:
                        indexImg[1]++;

                        if (x < BombermanGame.WIDTH - 2) {
                            x += 1 / 3.0;
                            setImg(player_right[indexImg[1] % 3]);

                        }
                        break;

                    case W:
                        indexImg[2]++;

                        if (y > 1) {
                            y -= 1 / 3.0;
                            setImg(player_up[indexImg[2] % 3]);
                        }
                        break;

                    case S:
                        indexImg[3]++;
                        if (y < BombermanGame.HEIGHT - 2) {

                            y += 1 / 3.0;
                            setImg(player_down[indexImg[3] % 3]);

                        }
                        break;
                }

        });


    }



}
