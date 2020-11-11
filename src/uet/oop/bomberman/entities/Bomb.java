package uet.oop.bomberman.entities;


import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.PlayMusic;
import uet.oop.bomberman.graphics.Sprite;

import java.io.File;
import java.util.List;

public class Bomb extends Entity{

    protected Image[] imgBomb;
    protected Image[] imgExplosion;
    protected Image[] imgHorizontalLeft;
    protected Image[] imgHorizontalRight;
    protected Image[] imgVerticalUp;
    protected Image[] imgVerticalDown;
    protected Image[] imgHorizontal;
    protected Image[] imgVertical;

    protected double timeBomb, timeExplosion;
    public boolean hasFlame;

    protected Entity[] bomb_horizontal;
    protected Entity[] bomb_vertical;
    public MediaPlayer mediaPlayer;

    public  int xMax,xMin,yMax,yMin;   // lưu tọa độ bom nổ đầu và cuối

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
        hasFlame = false;

        xMax = yMax = xMin = yMin = 100;

        bomb_horizontal = new Entity[4];
        bomb_vertical   = new Entity[4];

        for(int i=0; i<4 ; i++) {
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

        imgHorizontal = new Image[3];
        imgHorizontal[0] = Sprite.explosion_horizontal.getFxImage();
        imgHorizontal[1] = Sprite.explosion_horizontal1.getFxImage();
        imgHorizontal[2] = Sprite.explosion_horizontal2.getFxImage();

        imgVertical = new Image[3];
        imgVertical[0] = Sprite.explosion_vertical.getFxImage();
        imgVertical[1] = Sprite.explosion_vertical1.getFxImage();
        imgVertical[2] = Sprite.explosion_vertical2.getFxImage();

    }

    @Override
    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;

        int X = (int)x;
        int Y = (int)y;

        if (!hasFlame) {
            bomb_horizontal[0].setXY(x - 1, y);
            bomb_horizontal[1].setXY(x + 1, y);
            bomb_vertical[0].setXY(x, y - 1);
            bomb_vertical[1].setXY(x, y + 1);
            xMin = X-1;
            xMax = X+1;
            yMin = Y-1;
            yMax = Y+1;

        } else  {               // Cài đặt vị trí bom nổ khi có item Flame

            // Trái
            bomb_horizontal[2].setXY(100,100);
            if(BombermanGame.hasWallPlayer[Y][X-1] ) {
                bomb_horizontal[0].setXY(100,100);
                xMin = X-1;
            } else if (BombermanGame.hasWallPlayer[Y][X-2]) {
                bomb_horizontal[0].setXY(x-1,y);
                xMin = X-2;
            } else if(X > 1) {
                bomb_horizontal[0].setXY(x-2,y);
                bomb_horizontal[2].setXY(x-1,y);
                xMin = X;
            }

            // Phải
            bomb_horizontal[3].setXY(100,100);
            if(BombermanGame.hasWallPlayer[Y][X+1]) {
                bomb_horizontal[1].setXY(100,100);
                xMax = X +1;
            } else if (BombermanGame.hasWallPlayer[Y][X+2]) {
                bomb_horizontal[1].setXY(x+1,y);
                xMax = X +2;
            } else  {
                xMax = X;
                bomb_horizontal[1].setXY(x+2,y);
                bomb_horizontal[3].setXY(x+1,y);
            }

            // Trên
            bomb_vertical[2].setXY(100,100);
            if(BombermanGame.hasWallPlayer[Y-1][X]) {
                bomb_vertical[0].setXY(100,100);
                yMin = Y-1;

            } else if (BombermanGame.hasWallPlayer[Y-2][X]) {
                bomb_vertical[0].setXY(x,y-1);
                yMin = Y-2;

            } else if(Y > 1){
                bomb_vertical[0].setXY(x,y-2);
                bomb_vertical[2].setXY(x,y-1);
                yMin = 100;
            }

            // Dưới
            bomb_vertical[3].setXY(100,100);
            if(BombermanGame.hasWallPlayer[Y+1][X]) {
                bomb_vertical[1].setXY(100,100);
                yMax = Y+1;

            } else if (BombermanGame.hasWallPlayer[Y+2][X]) {
                bomb_vertical[1].setXY(x,y+1);
                yMax = Y+2;

            } else {
                bomb_vertical[1].setXY(x,y+2);
                bomb_vertical[3].setXY(x,y+1);
                yMax =100;
            }


        }
    }


    public void  Animation(double deltaTime,List<Entity> stillObject) {

        if( draw ) {
            timeBomb += deltaTime;


            int i = (int) (timeBomb * 2);

            if (i < 6) {
                setImg(imgBomb[i % 3]);

                for(int k=0; k<4; k++) {
                    bomb_horizontal[k].setDraw(false);
                    bomb_vertical[k].setDraw(false);
                }

                // Thêm vị trí bom vào hasWall

                BombermanGame.hasWallMonster[(int)y][(int)x] = true;

                double X = BombermanGame.bomberman.getX();
                double Y = BombermanGame.bomberman.getY();

                // Nếu vị trí player trong khoảng của bom thì có thể đi qua bom ngược lại thì không

                if( ! (Math.abs(Y-y)<1 && ( ( x>=X && x-X < 0.75 ) || ( x<=X  && X-x <1 ) ) ) )
                    BombermanGame.hasWallPlayer[(int)y][(int)x] = true;



            } else {
                int j = (int) (timeBomb * 4.5);

                if(j < 15) {

                    setImg(imgExplosion[j % 3]);
                    bomb_horizontal[0].setImg(imgHorizontalLeft[j%3]);
                    bomb_horizontal[1].setImg(imgHorizontalRight[j%3]);
                    bomb_vertical[0].setImg(imgVerticalUp[j%3]);
                    bomb_vertical[1].setImg(imgVerticalDown[j%3]);
                    bomb_vertical[2].setImg(imgVertical[j%3]);
                    bomb_vertical[3].setImg(imgVertical[j%3]);
                    bomb_horizontal[2].setImg(imgHorizontal[j%3]);
                    bomb_horizontal[3].setImg(imgHorizontal[j%3]);

                    // Animation cho brick khi trúng bom
                    Brick.Animation(stillObject,x,y,j%3,xMin,yMin,xMax,yMax);

                 // nếu gặp wall , brick hoặc tọa độ sai thì không vẽ
                 for(int k=0; k<4; k++) {

                     if (bomb_horizontal[k].getX() >= BombermanGame.WIDTH || bomb_horizontal[k].getY() >= BombermanGame.HEIGHT) {
                         bomb_horizontal[k].setDraw(false);
                     } else {

                         bomb_horizontal[k].setDraw(!BombermanGame.hasWallPlayer[(int) bomb_horizontal[k].getY()]
                                 [(int) bomb_horizontal[k].getX()]);
                     }

                     if (bomb_vertical[k].getX() >= BombermanGame.WIDTH || bomb_vertical[k].getY() >= BombermanGame.HEIGHT) {
                         bomb_vertical[k].setDraw(false);
                     } else {

                         bomb_vertical[k].setDraw(!BombermanGame.hasWallPlayer[(int) bomb_vertical[k].getY()]
                                 [(int) bomb_vertical[k].getX()]);
                     }


                 }

                 //

                    if(CheckPosBomb(BombermanGame.bomberman))
                         BombermanGame.bomberman.setDead(true);
                    if(CheckPosBomb(BombermanGame.balloom)) {
                         BombermanGame.balloom.setDead(true);
                    }

                }
                else  {
                    playMusic(PlayMusic.explosion_sound);
                    Brick.remove(stillObject,(int)x,(int)y,xMin,yMin,xMax,yMax);

                    BombermanGame.hasWallPlayer[(int)y][(int)x] = false;
                    BombermanGame.hasWallMonster[(int)y][(int)x] = false;
                    draw = false;


                }
            }

        } else timeBomb = 0.0;
    }

    // Check vị trị có thể bị chết so với bom
    boolean CheckPosBomb(Entity entity) {

        if(draw) {
            if ((int) entity.x == (int)x &&  Math.abs((int) entity.y - (int)y) < 1 )
                return true;

                if (bomb_horizontal[0].isDraw() && Math.abs(entity.x - bomb_horizontal[0].getX()) < 0.75
                        &&  Math.abs(entity.y - bomb_horizontal[0].getY()) <1 )
                    return true;
                if (bomb_horizontal[1].isDraw() && Math.abs(entity.x - bomb_horizontal[1].getX()) < 1
                    &&  Math.abs(entity.y - bomb_horizontal[1].getY()) <1 )
                return true;

                if (bomb_vertical[1].isDraw() && ((entity.x >= bomb_vertical[1].getX() && (entity.x - bomb_vertical[1].getX()) < 1 )
                      || (entity.x <= bomb_vertical[1].getX() && bomb_vertical[1].getX()-entity.x <0.75)  )
                        &&  Math.abs(entity.y - bomb_vertical[1].getY()) <1 )

                    return true;

                if (bomb_vertical[0].isDraw() && ((entity.x >= bomb_vertical[0].getX() && (entity.x - bomb_vertical[0].getX()) < 1 )
                    || (entity.x <= bomb_vertical[1].getX() && bomb_vertical[0].getX()-entity.x <0.75)   )
                    &&  Math.abs(entity.y - bomb_vertical[0].getY()) <1 )
                   return true;


        }

        return false;
    }



    public void Render(GraphicsContext gc) {

        render(gc);
        int index ;
        if(hasFlame)
            index = 4;
        else
            index = 2;

        for(int i=0; i<index; i++) {

            bomb_horizontal[i].render(gc);
            bomb_vertical[i].render(gc);
        }
    }


    public void update( double time,List<Entity> stillObject) {
        Animation(time,stillObject);

    }

    public  void playMusic(String path) {

            Media media = new Media(new File(path).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();

    }
}
