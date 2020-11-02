package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Balloom extends Entity {

    enum State {up, down, right, left};


    private State state;
    private Image[] Balloom;
    public static float totaltime;
    public static float totaltime1;

    public Balloom(int x, int y, Image img) {
        super(x, y, img);
        init();
    }


    private void init() {
        state = State.down;

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
    public void update(Scene scene, double time) {

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

    private void move(double deltatime,List<Entity> stillObjects ) {
        totaltime1 += deltatime;
        if (totaltime1 >= 0.3) {
            moving(stillObjects);
            totaltime1 = 0.0f;
        }
    }

    private void moving(List<Entity> stillObjects) {
        if(state == State.up) {
            if(!check(stillObjects)) {
                state = State.down;
            }
            y-= 0.25;
        }

        if(state == State.down) {
            if(!check(stillObjects)) {
                state = State.up;
            }
            y += 0.25;
        }

    }

    boolean check(List<Entity> stillObjects) {
        for (Entity entity : stillObjects) {
            if (state == State.up && (int) (x ) == (int) entity.x && (int) (y - 0.25) == (int) entity.y)
                return false;
            if (state == State.down && (int) (x ) == (int) entity.x && (int) (y + 1.25) == (int) entity.y)
                return false;
            }
        return true;
    }


}

