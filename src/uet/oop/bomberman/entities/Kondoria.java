package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;


public class Kondoria extends Entity{
    private Image[] mob_dead;
    private Image[] imgKondoriaLeft;
    private Image[] imgKondoriaRight;
    private Image imgKondoriaDead;

    public boolean kt ;
    private float totaltime;
    private float totaltime1;
    private float totalDead;
    public List <String> duongdi;

    public Kondoria (int x, int y, Image img) {
        super(x, y , img);
        init();
    }

    public void init() {
        draw = true;
        state = State.Left;
        dead = false;
        totaltime = 0.0f;
        totaltime1 = 0.0f;
        duongdi = new ArrayList<>();
        imgKondoriaRight = new Image[3];
        imgKondoriaLeft = new Image[3];
        mob_dead = new Image[3];

        imgKondoriaRight[0] = Sprite.kondoria_right1.getFxImage();
        imgKondoriaRight[1] = Sprite.kondoria_right2.getFxImage();
        imgKondoriaRight[2] = Sprite.kondoria_right3.getFxImage();
        imgKondoriaLeft[0] = Sprite.kondoria_left1.getFxImage();
        imgKondoriaLeft[1] = Sprite.kondoria_left2.getFxImage();
        imgKondoriaLeft[2] = Sprite.kondoria_left3.getFxImage();

        mob_dead[0] = Sprite.mob_dead1.getFxImage();
        mob_dead[1] = Sprite.mob_dead2.getFxImage();
        mob_dead[2] = Sprite.mob_dead3.getFxImage();

        imgKondoriaDead = Sprite.kondoria_dead.getFxImage();

    }

    public void  update (double deltaTime) {
        totaltime += deltaTime;

        if(dead) {
            AnimationDead(deltaTime);
        }
        else {
            insertImg();
            moving(deltaTime);
        }
    }


    public void moving (double deltaTime) {
        totaltime1 += deltaTime;

        if(totaltime1 >= 0.2) {
            move();
            totaltime1 = 0.0f;
        }
    }

    public void insertImg() {
        if(state == State.Down || state == State.Right) {
            insertImgRight(0.3);
        }

        if(state == State.Up || state == State.Left) {
            insertImgLeft(0.3);
        }
    }
    public void  insertImgLeft(double time) {
        if (totaltime <= 3 * time) {
            for (int i = 0; i < 3; i++) {
                if (totaltime >= i * time) {
                    setImg(imgKondoriaLeft[i]);
                }
            }
        }
        else {
            totaltime = 0.0f;
        }
    }

    public void insertImgRight(double time) {
        if (totaltime <= 3 * time) {
            for (int i = 0; i < 3; i++) {
                if (totaltime >= i * time) {
                    setImg(imgKondoriaRight[i]);
                }
            }
        }
        else {
            totaltime = 0.0f;
        }
    }

    public void move(){

        Animation();

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
    }


    public void CheckMove() {
        do {
            if (state == State.Up) {
                if (!checkPosWall()) {
                    state = State.Left;
                }
            }

            if (state == State.Down) {
                if (!checkPosWall()) {
                    state = State.Right;
                }
            }

            if (state == State.Right) {
                if (!checkPosWall()) {
                    state = State.Up;
                }
            }

            if (state == State.Left) {
                if (!checkPosWall()) {
                    state = State.Down;
                }
            }

        } while (!checkPosWall());
    }

    public boolean checkPosWall() {

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

    public void Animation() {
        int x1 = (int) BombermanGame.bomberman.getX();
        int y1 = (int) BombermanGame.bomberman.getY();

        if (Math.sqrt((x - x1)*(x - x1) + (y - y1)*(y - y1)) < 10) {
            if(x - (int)x == 0 && y - (int) y == 0)
                State();
        }

    }

    public void State () {
        int dx = (int) x;
        int dy = (int) y;

        timduong(dx , dy , "");
        if( !duongdi.isEmpty()) {

            String s = duongdingannhat();

            if(s.charAt(0) == 'D') {
                state = State.Down;
            }
            if(s.charAt(0) == 'U') {
                state = State.Up;
            }
            if(s.charAt(0) == 'R') {
                state = State.Right;
            }
            if(s.charAt(0) == 'L') {
                state = State.Left;
            }

            duongdi.removeAll(duongdi);
        }
    }

    public String duongdingannhat () {

        String s = duongdi.get(0);

        for(String value : duongdi) {
            if (s.length() > value.length()) {
                s = value;
            }
        }

        return  s;
    }

    public void timduong(int x1, int y1, String ds) {

        if (x1 == (int) BombermanGame.bomberman.x && y1 == (int) BombermanGame.bomberman.y) {
            kt = true;
            duongdi.add(ds);
        }
        else {
            if (x1 < BombermanGame.WIDTH - 1 && !BombermanGame.hasWallMonster[y1][x1 + 1] && !BombermanGame.isOk[y1][x1 + 1]) {
                BombermanGame.isOk[y1][x1] = true;
                timduong(x1 + 1, y1, ds + "R");
                BombermanGame.isOk[y1][x1] = false;
            }

            if (y1 < BombermanGame.HEIGHT - 1 && !BombermanGame.hasWallMonster[y1 + 1][x1] && !BombermanGame.isOk[y1 + 1][x1]) {
                BombermanGame.isOk[y1][x1] = true;
                timduong(x1, y1 + 1, ds + "D");
                BombermanGame.isOk[y1][x1] = false;
            }


            if (x1 > 0 && !BombermanGame.hasWallMonster[y1][x1 - 1] && !BombermanGame.isOk[y1][x1 - 1]) {
                BombermanGame.isOk[y1][x1] = true;
                timduong(x1 -1 ,y1, ds + "L");
                BombermanGame.isOk[y1][x1] = false;
            }

            if (y1 > 0 && !BombermanGame.hasWallMonster[y1 - 1][x1] && !BombermanGame.isOk[y1 - 1][x1]) {
                BombermanGame.isOk[y1][x1] = true;
                timduong(x1, y1 - 1, ds + "U");
                BombermanGame.isOk[y1][x1] = false;
            }
        }


    }

    public void AnimationDead(double deltaTime) {
        totalDead += deltaTime;
        if(totalDead <= 0.4) {
            setImg(imgKondoriaDead);
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

