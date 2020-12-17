package uet.oop.bomberman.monster;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Oneal extends Monster{
    private Image[] imgOnealLeft;
    private Image[] imgOnealRight;
    private double timespeed;

    public boolean kt ;
    public List<String> duongdi;


    public Oneal(int x, int y, Image img) {
        super(x, y, img);

        init();
    }



    @Override
    public void update(double deltaTime) {
        totaltime += deltaTime;

        if(dead) {
            AnimationDead(deltaTime);
        }
        else {
            insertImg();
            moving(deltaTime,timespeed);
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
                    setImg(imgOnealLeft[i]);
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
                    setImg(imgOnealRight[i]);
                }
            }
        }
        else {
            totaltime = 0.0f;
        }
    }

    @Override
    public void init() {
        draw = true;
        state = State.Left;
        dead = false;
        totaltime = 0.0f;
        totaltime1 = 0.0f;
        duongdi = new ArrayList<>();
        kt = false;
        timespeed = 0.3;

        mob_dead = new Image[3];

        mob_dead[0] = Sprite.mob_dead1.getFxImage();
        mob_dead[1] = Sprite.mob_dead2.getFxImage();
        mob_dead[2] = Sprite.mob_dead3.getFxImage();


        imgOnealLeft = new Image[3];
        imgOnealRight = new Image[3];

        imgOnealRight[0] = Sprite.oneal_right1.getFxImage();
        imgOnealRight[1] = Sprite.oneal_right2.getFxImage();
        imgOnealRight[2] = Sprite.oneal_right3.getFxImage();
        imgOnealLeft[0] = Sprite.oneal_left1.getFxImage();
        imgOnealLeft[1] = Sprite.oneal_left2.getFxImage();
        imgOnealLeft[2] = Sprite.oneal_left3.getFxImage();

        imgMonsterDead = Sprite.oneal_dead.getFxImage();
    }

    @Override
    public void moving(double deltaTime, double timeSpeed) {
        totaltime1 += deltaTime;

        if(totaltime1 >= timeSpeed) {


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

            totaltime1 = 0.0f;
        }
    }

    @Override
    public void Animation() {
        int x1 = (int) BombermanGame.bomberman.getX();
        int y1 = (int) BombermanGame.bomberman.getY();

        if (Math.sqrt((x - x1)*(x - x1) + (y - y1)*(y - y1)) < 5) {
            if(x - (int)x == 0 && y - (int) y == 0){
                State();
            }
        }

    }

    public void State () {
        int dx = (int) x;
        int dy = (int) y;
        timespeed = 0.2;
        FindShortestPath(dx , dy , "");
        if(!ShortestPath().isEmpty()  ) {
            String s = ShortestPath();

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
            timespeed = 0.15;
            duongdi.removeAll(duongdi);
            kt = false;
        }
    }

    public String ShortestPath () {
        String s = "";
        if(!duongdi.isEmpty()) {
            s = duongdi.get(0);

            for (String value : duongdi) {
                if (s.length() > value.length()) {
                    s = value;
                }
            }

        }
        return s;
    }

    public void FindShortestPath(int x1, int y1, String ds) {

        double X = BombermanGame.bomberman.getX();
        double Y = BombermanGame.bomberman.getY();
        if (x1 == (int) BombermanGame.bomberman.getX() && y1 == (int) BombermanGame.bomberman.getY()) {
            kt = true;
            duongdi.add(ds);
        }
        else  if((Math.sqrt((X - x1) * (X- x1) + (Y - y1) * (Y - y1)) <= Math.sqrt((X - x) * (X- x) + (Y - y1) * (Y - y)))) {
            if (x1 < BombermanGame.WIDTH - 1 && !BombermanGame.hasWallMonster[y1][x1 + 1] && !BombermanGame.isOk[y1][x1 + 1]) {
                BombermanGame.isOk[y1][x1] = true;
                FindShortestPath(x1 + 1, y1, ds + "R");
                BombermanGame.isOk[y1][x1] = false;
            }

            if (y1 < BombermanGame.HEIGHT - 1 && !BombermanGame.hasWallMonster[y1 + 1][x1] && !BombermanGame.isOk[y1 + 1][x1]) {
                BombermanGame.isOk[y1][x1] = true;
                FindShortestPath(x1, y1 + 1, ds + "D");
                BombermanGame.isOk[y1][x1] = false;
            }


            if (x1 > 0 && !BombermanGame.hasWallMonster[y1][x1 - 1] && !BombermanGame.isOk[y1][x1 - 1]) {
                BombermanGame.isOk[y1][x1] = true;
                FindShortestPath(x1 -1 ,y1, ds + "L");
                BombermanGame.isOk[y1][x1] = false;
            }

            if (y1 > 0 && !BombermanGame.hasWallMonster[y1 - 1][x1] && !BombermanGame.isOk[y1 - 1][x1]) {
                BombermanGame.isOk[y1][x1] = true;
                FindShortestPath(x1, y1 - 1, ds + "U");
                BombermanGame.isOk[y1][x1] = false;
            }
        }


    }
}
