package uet.oop.bomberman.entities;


import javafx.event.EventType;
import javafx.scene.Camera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.PlayMusic;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.monster.Monster;

import java.io.File;
import java.util.List;

public class Bomber extends Entity {
    //private boolean dead;

    public static  double totalTime ;
    public static double timeDead;

    public boolean move;
    public Image[] player_right, player_left,player_down, player_up;
    public Image[] player_dead;
    public int[] indexImg;
    public  int indexSoundStep ;
    public boolean musicDead ;

    public int number_bomb ;

    private double speed;
    public int num_plays = 3;
    public double timer ;
    public int score;
    public int level ;
    public boolean hasFlame;

    public Bomber(int x, int y, Image img) {
        super( x, y, img);
        Init();
    }

    public void setDead(boolean dead) {this.dead = dead;}
    public void setSpeed(double speed) {this.speed = speed;}
    public double getSpeed() {return speed;}
    public  boolean getDeath() {return this.dead;}

    /**
     *   Khởi tạo các thuộc tính
     */
    public void Init(){

        x = y = 1;
        level = 1;
        hasFlame = false;

        dead = false;
        totalTime =0.0;
        timeDead = 0.0;
        move =false;
        draw = true;
        number_bomb = 1;
        indexSoundStep = 0;
        musicDead  = true;

        speed = 0.1;
        timer = 220;
        score =0;

        player_right = new Image[3];
        player_left = new Image[3];
        player_down = new Image[3];
        player_up = new Image[3];
        player_dead = new Image[3];

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

        player_dead[0] = Sprite.player_dead1.getFxImage();
        player_dead[1] = Sprite.player_dead2.getFxImage();
        player_dead[2] = Sprite.player_dead3.getFxImage();

        indexImg = new int[4];
        indexImg[0] = indexImg[1] = indexImg[2] = indexImg[3] = 0;
    }

    public void Reset() {
        x = y = 1;

        dead = false;
        totalTime =0.0;
        timeDead = 0.0;
        move =false;
        draw = true;
        number_bomb = 1;
        indexSoundStep = 0;
        musicDead  = true;

        speed = 0.04;
        timer = 220;
        score = 0;
        img = player_right[0];
    }

    /**
     *  Check vị trị chạm tường
     */
    public boolean CheckPos(List<Entity> stillObjects) {


            if (state == State.Right && (BombermanGame.hasWallPlayer[(int) (y)][(int) (x + 1 - 0.25)] ||
                    BombermanGame.hasWallPlayer[(int) (y + 0.5)][(int) (x + 1 - 0.25)]))
                return true;

            if (state == State.Left && (BombermanGame.hasWallPlayer[(int) (y)][(int) (x - 0.25)] ||
                    BombermanGame.hasWallPlayer[(int) (y + 0.5)][(int) (x - 0.25)]))
                return true;

            if (state == State.Up && BombermanGame.hasWallPlayer[(int) (y - 0.25)][(int) (x + 0.25)])
                return true;

            if (state == State.Down && BombermanGame.hasWallPlayer[(int) (y + 1)][(int) (x + 0.25)])
                return true;



        return false; // ko bi cham tuong, bomb
    }



    public void update(double deltaTime,List<Entity> stillObjects,List<Bomb> bombs,List <Monster> monster) {

        if(timer >0)
          timer -= deltaTime;

        DeadByMonster(monster);

        if(dead) {

           playMusic(PlayMusic.death_sound,musicDead);
           musicDead = false;
            AnimationPlayerDead( deltaTime);

        }

        else {
            timeDead = 0.0;
            totalTime += deltaTime;

            HandlePressKey(bombs,deltaTime);
        }

        Moving(stillObjects);

       if(hasFlame)
           for(Bomb bomb : bombs)
               bomb.hasFlame = true;
    }

    public void HandlePressKey(List<Bomb> bombs, double deltaTime) {
        // sự kiện nhả phím ra
        BombermanGame.scene.setOnKeyReleased(event -> {


            if(move ) {
                if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.D
                        || event.getCode() == KeyCode.W || event.getCode() == KeyCode.S) {

                    move = false;

                }
            }
        });

        BombermanGame.scene.setOnKeyPressed(event -> {
            if(event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.A) {
                state = State.Left;
                move = true;



            }
            else if(event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.D) {
                state = State.Right;
                move = true;

            }
            else if(event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.W) {
                state = State.Up;
                move = true;

            }
            else if(event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.S) {
                state = State.Down;
                move = true;

            }
             if(event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.SPACE) {

                 int index = 0;
                for(int i=0; i<number_bomb; i++) {

                    if( !bombs.get(i).isDraw() && index < 1) {
                        index ++;

                        if (state == State.Right)
                            bombs.get(i).setXY((int) (x + 0.25), (int) (y + 0.25));
                        else
                            bombs.get(i).setXY((int) x, (int) (y + 0.5));

                        bombs.get(i).setDraw(true);
                        playMusic(PlayMusic.put_bomb_sound,true);
                    }
                }
            }

        });


    }

    /**
     *  Hàm di chuyển
     */
    public void Moving(List<Entity> stillObjects) {
        if(move  && totalTime >= speed)   {
          indexSoundStep ++;
            // sang trái
            if(state == State.Left) {
                if (x > 1 && !CheckPos(stillObjects)) {

                    if (y - (int) y == 0.25)
                        y = (int) y;
                    else if (y - (int) y == 0.75)      // Check vị trí sát mép tường cho phép di chuyển
                        y = (int) y + 1;

                    x -= 0.25;

                }
                indexImg[0]++;
                setImg(player_left[indexImg[0] % 3]);

                if (indexSoundStep % 3 == 0)
                playMusic(PlayMusic.step_horizontal_sound,true);

                // sang phải
            } else  if(state == State.Right) {
                if(x < BombermanGame.WIDTH - 2 && !CheckPos(stillObjects) ) {
                    if(y - (int)y == 0.25)
                        y = (int) y;
                    else if(y - (int)y == 0.75)
                        y = (int) y + 1;

                    x += 0.25;


                }
                indexImg[1]++;
                setImg(player_right[indexImg[1] % 3]);

                if(indexSoundStep % 3 ==0)
                playMusic(PlayMusic.step_horizontal_sound,true);

                // xuống dưới
            } else if(state == State.Down ) {
                if (y < BombermanGame.HEIGHT - 2 && !CheckPos(stillObjects)) {

                    if(x - (int)x == 0.5)
                        x -= 0.25;
                    else if(x - (int)x == 0.75)
                        x = (int) x + 1;

                    y += 0.25;

                }
                indexImg[3]++;
                setImg(player_down[indexImg[3] % 3]);

                if(indexSoundStep % 3 ==0)
                    playMusic(PlayMusic.step_vertical_sound,true);

                // lên trên
            } else if(state == State.Up ) {
                if(y >= 1  && !CheckPos(stillObjects)) {

                    if(x - (int)x == 0.5)
                        x -= 0.25;
                    else if(x - (int)x == 0.75)
                        x = (int) x + 1;


                    y -= 0.25;

                }
                indexImg[2]++;
                setImg(player_up[indexImg[2] % 3]);

                if(indexSoundStep % 3 ==0)
                  playMusic(PlayMusic.step_vertical_sound,true);
            }
            System.out.println(  x + "   " + y);
            totalTime = 0.0f;
            //move = false;
        }

    }

    public void DeadByMonster(List <Monster> monster) {
        if(CheckDeadByMonster(monster)) {

            dead = true;
        }

    }

    public boolean CheckDeadByMonster(List <Monster> monster) {

        for (Monster entity : monster) {

            if(entity.isDraw()) {
                if ((entity.getState() == State.Down || entity.getState() == State.Up)) {

                    if ((x >= entity.x) && (x - entity.x) < 1 && Math.abs(y - entity.y) < 1)
                        return true;

                    if (entity.x > x && entity.x - x < 0.75 && Math.abs(y - entity.y) < 1)
                        return true;

                } else if (Math.abs(entity.y - y) < 1 && Math.abs(x - entity.x) < 0.75)
                    return true;
            }
        }

        return false;
    }

    public void AnimationPlayerDead(double deltaTime) {

        timeDead += deltaTime;
        int index = (int)(timeDead*2);



        if(index < 3) {

            if(index == 1)

            this.setImg(player_dead[index % 3]);

        }else {

            draw = false;
            BombermanGame.GameOver = true;
            BombermanGame.mediaPlayer.stop();
            playMusic(PlayMusic.life_lost_music,true);
        }

    }


}