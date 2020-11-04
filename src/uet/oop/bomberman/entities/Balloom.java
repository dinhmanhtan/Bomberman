package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Balloom extends Entity {


    private Image[] Balloom;
    public static float totaltime;
    public static float totaltime1;

    public Balloom(int x, int y, Image img) {
        super(x, y, img);
        init();
    }


    private void init() {
        state = State.Right;

        totaltime = 0.0f;
        totaltime1 = 0.0f;
        Balloom = new Image[6];

        Balloom[0] = Sprite.balloom_right1.getFxImage();
        Balloom[1] = Sprite.balloom_right2.getFxImage();
        Balloom[2] = Sprite.balloom_right3.getFxImage();
        Balloom[3] = Sprite.balloom_left1.getFxImage();
        Balloom[4] = Sprite.balloom_left2.getFxImage();
        Balloom[5] = Sprite.balloom_left3.getFxImage();

    }

    @Override
    public void update( double time) {

    }

    public void update(double deltaTime, List<Entity> stillObjects) {
        insertImg(0.5f, deltaTime);
        move(deltaTime, stillObjects);
    }


    private void insertImg(float time, double deltaTime) {
        totaltime += deltaTime;

        for (int i = 0; i < 6; i++) {
            if (totaltime >= i * time) {
                setImg(Balloom[i]);
            }
        }

        if (totaltime >= 6 * time) {
            totaltime = 0.0f;
        }
    }

    private void move(double deltaTime,List<Entity> stillObjects ) {
        totaltime1 += deltaTime;
        if (totaltime1 >= 0.15) {
            moving(stillObjects);
            totaltime1 = 0.0f;
        }
    }

    private void moving(List<Entity> stillObjects) {
        if(state == State.Up) {
            if(!check(stillObjects)) {
                state = State.Down;
            }
            y-= 0.25;
        }

        if(state == State.Down) {
            if(!check(stillObjects)) {
                state = State.Up;
            }
            y += 0.25;
        }

        if(state == State.Right) {
            if(!check(stillObjects)) {
                state = State.Left;
            }
            x += 0.25;
        }

        if(state == State.Left) {
            if(!check(stillObjects)) {
                state = State.Right;
            }
            x -= 0.25;
        }

    }

    boolean check(List<Entity> stillObjects) {
        for (Entity entity : stillObjects) {


            if(state == State.Right && (int)(x+1) == (int)entity.x && (int)y == (int)entity.y)
                return false;
            if(state == State.Left && (int)(x-0.5) == (int)entity.x && (int)y == (int)entity.y)
                return false;

            if (state == State.Up && (int) (x ) == (int) entity.x && (int) (y - 0.25) == (int) entity.y)
                return false;
            if (state == State.Down && (int) (x ) == (int) entity.x && (int) (y + 1.25) == (int) entity.y)
                return false;
            }
        return true;
    }


}

