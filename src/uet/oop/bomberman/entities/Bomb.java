package uet.oop.bomberman.entities;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Bomb extends Entity{

    protected Image[] imgBomb;
    protected Image[] imgExplosion;
    protected Image[] imgHorizontalLeft;
    protected Image[] imgHorizontalRight;
    protected Image[] imgVerticalUp;
    protected Image[] imgVerticalDown;

    protected double timeBomb, timeExplosion;


    protected Entity[] bomb_horizontal;
    protected Entity[] bomb_vertical;

    public Bomb() {
        Init();
    }

    public Bomb(double x, double y) {

        super(x,y);
        Init();
    }

    public Bomb(int x, int y, Image img) {
        super(x, y, img);
        Init();
    }


    public void Init() {

        setImg(Sprite.bomb.getFxImage());
        x = y = 1;
        timeBomb = timeExplosion = 0.0;

        bomb_horizontal = new Entity[3];
        bomb_vertical   = new Entity[3];

        for(int i=0; i<3 ; i++) {
            bomb_horizontal[i] = new Entity();
            bomb_vertical[i] = new Entity();
        }

        draw = false;
        imgBomb = new Image[3];
        imgBomb[2] = Sprite.bomb.getFxImage();
        imgBomb[1] = Sprite.bomb_1.getFxImage();
        imgBomb[0] = Sprite.bomb_2.getFxImage();

        imgExplosion = new Image[3];
        imgExplosion[0] = Sprite.bomb_exploded.getFxImage();
        imgExplosion[1] = Sprite.bomb_exploded1.getFxImage();
        imgExplosion[2] = Sprite.bomb_exploded2.getFxImage();

        imgHorizontalLeft = new Image[3];
        imgHorizontalLeft[0] = Sprite.explosion_horizontal_left_last.getFxImage();
        imgHorizontalLeft[1] = Sprite.explosion_horizontal_left_last1.getFxImage();
        imgHorizontalLeft[2] = Sprite.explosion_horizontal_left_last2.getFxImage();

        imgHorizontalRight = new Image[3];
        imgHorizontalRight[0] = Sprite.explosion_horizontal_right_last.getFxImage();
        imgHorizontalRight[1] = Sprite.explosion_horizontal_right_last1.getFxImage();
        imgHorizontalRight[2] = Sprite.explosion_horizontal_right_last2.getFxImage();

        imgVerticalUp = new Image[3];
        imgVerticalUp[0] = Sprite.explosion_vertical_top_last.getFxImage();
        imgVerticalUp[1] = Sprite.explosion_vertical_top_last1.getFxImage();
        imgVerticalUp[2] = Sprite.explosion_vertical_top_last2.getFxImage();

        imgVerticalDown = new Image[3];
        imgVerticalDown[0] = Sprite.explosion_vertical_down_last.getFxImage();
        imgVerticalDown[1] = Sprite.explosion_vertical_down_last1.getFxImage();
        imgVerticalDown[2] = Sprite.explosion_vertical_down_last2.getFxImage();
    }

    @Override
    public void setXY(double x, double y) {
      this.x = x;
      this.y = y;


      bomb_horizontal[0].setXY(x-1,y);
      bomb_horizontal[1].setXY(x+1,y);
      bomb_vertical[0].setXY(x,y-1);
      bomb_vertical[1].setXY(x,y+1);
    }



    public void  Animation(double deltaTime,List<Entity> stillObject) {

        if( draw ) {
            timeBomb += deltaTime;


            int i = (int) (timeBomb * 2);

            if (i < 6) {
                setImg(imgBomb[i % 3]);

                for(int k=0; k<3; k++) {
                    bomb_horizontal[k].setDraw(false);
                    bomb_vertical[k].setDraw(false);
                }

            } else {
                int j = (int) (timeBomb * 4.5);

                if(j < 15) {

                    setImg(imgExplosion[j % 3]);
                    bomb_horizontal[0].setImg(imgHorizontalLeft[j%3]);
                    bomb_horizontal[1].setImg(imgHorizontalRight[j%3]);
                    bomb_vertical[0].setImg(imgVerticalUp[j%3]);
                    bomb_vertical[1].setImg(imgVerticalDown[j%3]);

                    Brick.Animation(stillObject,(int)x,(int)y,j%3);

                 for(int k=0; k<3; k++) {
                    bomb_horizontal[k].setDraw(!BombermanGame.hasWall[(int)bomb_horizontal[k].getY()][(int) bomb_horizontal[k].getX()]);

                    bomb_vertical[k].setDraw(!BombermanGame.hasWall[(int)bomb_vertical[k].getY()][(int) bomb_vertical[k].getX()]);

                    }
                }
                else  {
                    draw = false;
                    Brick.remove(stillObject,(int)x,(int)y);
                }
            }

        } else timeBomb = 0.0;
    }


    public void Render(GraphicsContext gc) {

        render(gc);

        for(int i=0; i<2; i++) {

            if(bomb_horizontal[i].isDraw())
                bomb_horizontal[i].render(gc);

            if(bomb_vertical[i].isDraw())
                bomb_vertical[i].render(gc);
        }
    }


    public void update( double time,List<Entity> stillObject) {
        Animation(time,stillObject);

    }
}
