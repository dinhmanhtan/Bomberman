package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class Balloom extends Entity {

    private Image[] mob_dead;
    private Image[] imgBalloom;
    private Image imgBalloomDead;
    private int rd;
    private final Random random = new Random();

    private float totaltime;
    private float totaltime1;
    private float totalDead;
    private boolean dead;

    public Balloom(int x, int y, Image img) {
        super(x, y, img);
        init();
    }

    public void setDead (boolean dead) {
        this.dead = dead;
    }

    private void init() {
        draw = true;
        state = State.Left;
        dead = false;
        totaltime = 0.0f;
        totaltime1 = 0.0f;
        imgBalloom = new Image[6];
        mob_dead = new Image[3];

        imgBalloom[0] = Sprite.balloom_right1.getFxImage();
        imgBalloom[1] = Sprite.balloom_right2.getFxImage();
        imgBalloom[2] = Sprite.balloom_right3.getFxImage();
        imgBalloom[3] = Sprite.balloom_left1.getFxImage();
        imgBalloom[4] = Sprite.balloom_left2.getFxImage();
        imgBalloom[5] = Sprite.balloom_left3.getFxImage();

        mob_dead[0] = Sprite.mob_dead1.getFxImage();
        mob_dead[1] = Sprite.mob_dead2.getFxImage();
        mob_dead[2] = Sprite.mob_dead3.getFxImage();

        imgBalloomDead = Sprite.balloom_dead.getFxImage();


    }

    @Override
    public void update( double time) {

    }

    public void update(double deltaTime, List<Entity> stillObjects) {
        if(dead){
            AnimationDead(deltaTime);
        }
        else {
            insertImg(0.3f, deltaTime);
            Animation();
            move(deltaTime, stillObjects);
        }
    }


    private void insertImg(float time, double deltaTime) {
        totaltime += deltaTime;
        for (int i = 0; i < 6; i++) {
            if (totaltime >= i * time) {
                setImg(imgBalloom[i]);
            }
        }
        if (totaltime >= 6 * time) {
            totaltime = 0.0f;
        }
    }

    private void move(double deltaTime,List<Entity> stillObjects ) {
        totaltime1 += deltaTime;
        if (totaltime1 >= 0.1) {
            moving(stillObjects);
            totaltime1 = 0.0f;
        }
    }

    private void moving(List<Entity> stillObjects) {

        CheckMove(stillObjects);

        if(state == State.Up) {
                y -= 0.25;
        }
        if(state == State.Down) {

                y += 0.25;
        }
        if(state == State.Right) {
                x += 0.25;
        }
        if(state == State.Left) {
                x -= 0.25;
        }

      //  System.out.println(state);
       // System.out.println(x +" " + y);
    }
    // 0, 1, 2 phai
    // 3, 4, 5 trai

    public void CheckMove(List<Entity> stillObjects) {
        while (true) {
            if(state == State.Up) {
                if(!checkPosWall(stillObjects)) {
                    state = State.Left;
                }
            }

            if(state == State.Down) {
                if(!checkPosWall(stillObjects)) {
                    state = State.Right;
                }
            }

            if(state == State.Right) {
                if(!checkPosWall(stillObjects)) {
                    state = State.Up;
                }
            }

            if(state == State.Left) {
                if(!checkPosWall(stillObjects)) {
                    state = State.Down;
                }
            }

            if(checkPosWall(stillObjects)) break;
        }
    }

    public void Radom () {
        if (rd == 0) {
            state = State.Down;
        }
        if (rd == 1) {
            state = State.Right;
        }
        if(rd == 2) {
            state = State.Up;
        }
        if(rd == 3) {
            state = State.Left;
        }
    }

    public void Animation() {
        if (Check()) {

            if ((img == imgBalloom[0])) {

                rd = random.nextInt(2);

            } else if ((img == imgBalloom[3])) {

                rd = random.nextInt(2) + 2;

            }
            Radom();
        }

    }

    public boolean Check () {

        if( x  - (int) x == 0 && y - (int) y== 0 ) {
            if(state == State.Right) {
                if (!BombermanGame.hasWallMonster[(int) y + 1][(int) x]) {
                    return true;
                }
            }

            if(state == State.Left) {
                if (!BombermanGame.hasWallMonster[(int) y - 1][(int) x]) {
                    return true;
                }
            }

            if(state == State.Up) {
                if (!BombermanGame.hasWallMonster[(int) (y)][(int) x - 1]) {
                    return true;
                }
            }

            if (state == State.Down) {
                if (!BombermanGame.hasWallMonster[(int) y][(int) x + 1]) {
                    return true;
                }
            }
        }
        // Check trai
        return false;
    }

    public boolean checkPosWall(List<Entity> stillObjects) {

            if (state == State.Right && BombermanGame.hasWallMonster[(int) (y)][(int) (x + 1)])
                return false;

            if (state == State.Left && BombermanGame.hasWallMonster[(int) (y)][(int) (x - 0.25)])
                return false;

            if (state == State.Up && BombermanGame.hasWallMonster[(int) (y - 0.25)][(int) x])
                return false;

            if (state == State.Down && BombermanGame.hasWallMonster[(int) (y + 1)][(int) (x)])
                return false;

        return true;
    }

    public void AnimationDead(double deltaTime) {
        totalDead += deltaTime;
        if(totalDead <= 0.4) {
            setImg(imgBalloomDead);
        }
        if (totalDead <= 0.7) {
            for (int i = 0 ; i < 3 ; i++) {
                if(totalDead >= 0.4 + i*0.1) {
                    setImg(mob_dead[i]);
                }
            }
        } else if (totalDead > 0.7) {
            draw = false;
            totalDead = 0;
            BombermanGame.monsters.remove(this);

        }
    }

}