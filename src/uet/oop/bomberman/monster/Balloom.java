package uet.oop.bomberman.monster;

import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class Balloom extends Monster {


    private Image[] imgBalloom;
    private int rd;
    private final Random random = new Random();

    private float totalDead;

    public Balloom(int x, int y, Image img) {
        super(x, y, img);
        init();

    }

    @Override
    public void init() {
        draw = true;
        state = State.Left;
        dead = false;
        totaltime = 0.0f;
        totaltime1 = 0.0f;

        imgBalloom = new Image[6];

        imgBalloom[0] = Sprite.balloom_right1.getFxImage();
        imgBalloom[1] = Sprite.balloom_right2.getFxImage();
        imgBalloom[2] = Sprite.balloom_right3.getFxImage();
        imgBalloom[3] = Sprite.balloom_left1.getFxImage();
        imgBalloom[4] = Sprite.balloom_left2.getFxImage();
        imgBalloom[5] = Sprite.balloom_left3.getFxImage();

        mob_dead = new Image[3];

        mob_dead[0] = Sprite.mob_dead1.getFxImage();
        mob_dead[1] = Sprite.mob_dead2.getFxImage();
        mob_dead[2] = Sprite.mob_dead3.getFxImage();
        imgMonsterDead = Sprite.balloom_dead.getFxImage();

    }

    @Override
    public void update(double deltaTime) {
        if(dead){
            AnimationDead(deltaTime);
        }
        else {
            insertImg(0.3f, deltaTime);
            Animation();
            moving(deltaTime,0.15);
        }
    }


    public void insertImg(float time, double deltaTime) {
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

    @Override
    public void moving(double deltaTime,double timeSpeed) {
        totaltime1 += deltaTime;
        if (totaltime1 >= timeSpeed) {

            CheckMove();

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

            totaltime1 = 0.0f;
        }
    }


    public void Random () {
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

    @Override
    public void Animation() {
        if (Check()) {

            if ((img == imgBalloom[0])) {

                rd = random.nextInt(2);

            } else if ((img == imgBalloom[3])) {

                rd = random.nextInt(2) + 2;

            }
            Random();
        }

    }

    public void AnimationDead(double deltaTime) {
        totalDead += deltaTime;
        if(totalDead <= 0.4) {
            setImg(imgMonsterDead);
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
            BombermanGame.bomberman.score += 200;
          //  BombermanGame.monsters.remove(this);

        }
    }
}