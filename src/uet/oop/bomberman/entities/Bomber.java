package uet.oop.bomberman.entities;


import javafx.scene.Scene;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Bomber extends Entity {
    private boolean dead;

    public static  double totalTime ;
    public static double timeDead;

    public boolean move;
    public Image[] player_right, player_left,player_down, player_up;
    public Image[] player_dead;
    public int[] indexImg;

    public Bomber(int x, int y, Image img) {
        super( x, y, img);
        Init();
    }

    @Override
    public void update( double time) {

    }

    /**
     *   Khởi tạo các thuộc tính
     */
    public void Init(){
        dead = false;
        totalTime =0.0;
        timeDead = 0.0;
        move =false;
        draw = true;

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

    /**
     *  Check vị trị chạm tường
     */
    public boolean CheckPos(List<Entity> stillObjects) {

        for(Entity entity : stillObjects) {

            if(state == State.Right && (int)(x+1-0.25) == (int)entity.x && Math.abs(y-entity.y) < 0.75)
               return true;
            if(state == State.Left && (int)(x-0.25) == (int)entity.x && Math.abs(y-entity.y) < 0.75)
                return true;
            if(state == State.Up && (int)(x+0.25) == (int)entity.x && (int)(y-0.25) == (int)entity.y)
                return true;
            if(state == State.Down && (int)(x+0.25) == (int)entity.x && (int)(y+1) == (int)entity.y)
                return true;

        }
       return false;
    }



    public void update(double deltaTime,List<Entity> stillObjects,Bomb bomb,List <Entity> monster) {
        deadbymonster(monster);
        if(dead) {
            timeDead += deltaTime;
            AnimationDead( 1);
        }
        else {
            totalTime += deltaTime;
            BombermanGame.scene.setOnKeyPressed(event -> {

                switch (event.getCode()) {
                    case A:
                        indexImg[0]++;
                        state = State.Left;
                        move = true;
                        break;

                    case D:
                        indexImg[1]++;
                        state = State.Right;
                        move = true;
                        break;

                    case W:
                        indexImg[2]++;
                        state = State.Up;
                        move = true;
                        break;

                    case S:
                        indexImg[3]++;
                        state = State.Down;
                        move = true;
                        break;
                    case SPACE:
                        bomb.setXY((int) x, (int) (y + 0.25));
                        bomb.setDraw(true);
                        break;
                }

            });
        }

        Moving(stillObjects);



    }

    /**
     *  Hàm di chuyển
     */
    public void Moving(List<Entity> stillObjects) {
        if(move  && totalTime >= 0.055)   {

            if(state == State.Left) {
                if(x > 1  && !CheckPos(stillObjects)) {

                    if(y - (int)y == 0.25)
                        y = (int) y;
                    else if(y - (int)y == 0.75)      // Check vị trí sát mép tường cho phép di chuyển
                        y = (int) y + 1;

                    x -= 0.25;
                }
                setImg(player_left[indexImg[0] % 3]);

            } else  if(state == State.Right) {
                if(x < BombermanGame.WIDTH - 2 && !CheckPos(stillObjects) ) {
                    if(y - (int)y == 0.25)
                        y = (int) y;
                    else if(y - (int)y == 0.75)
                        y = (int) y + 1;

                    x += 0.25;
                }
                setImg(player_right[indexImg[1] % 3]);
            } else if(state == State.Down ) {
                if (y < BombermanGame.HEIGHT - 2 && !CheckPos(stillObjects)) {

                    if(x - (int)x == 0.5)
                        x -= 0.25;
                    else if(x - (int)x == 0.75)
                        x = (int) x + 1;

                    y += 0.25;
                }
                setImg(player_down[indexImg[3] % 3]);

            } else if(state == State.Up ) {
                if(y >= 1  && !CheckPos(stillObjects)) {

                    if(x - (int)x == 0.5)
                        x -= 0.25;
                    else if(x - (int)x == 0.75)
                        x = (int) x + 1;


                    y -= 0.25;
                }
                setImg(player_up[indexImg[2] % 3]);
            }
            System.out.println(  x + "   " + y);
            totalTime = 0.0f;
            move = false;
        }

    }

    public void deadbymonster(List <Entity> monster) {
        if(CheckDead(monster)) {
            dead = true;
        }

    }

    public boolean CheckDead(List <Entity> monster) {

        for (Entity entity : monster)
            if ((entity.getState() == State.Down || entity.getState() == State.Up)) {

                if ((x >= entity.x) && (x - entity.x) < 1 && Math.abs(y - entity.y) < 1)
                    return true;

                if (entity.x > x && entity.x - x < 0.75 && Math.abs(y - entity.y) < 1)
                    return true;
            } else if( Math.abs(entity.y - y )< 1 &&  Math.abs(x - entity.x) < 0.75)
                   return  true;



        return false;
    }

    public void AnimationDead(double time) {
        if(timeDead < 3 * time) {
            for (int i = 0 ; i < 3 ; i++) {
                if(timeDead >= i * time) {
                    this.setImg(player_dead[i]);
                }
            }
        }
        else draw = false;

        }


}
