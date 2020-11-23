package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Brick extends Entity {

    public Brick() { Init();}

    public Brick(int x, int y, Image img) {
        super(x, y, img);
        Init();
    }

    public Image[] brick_exploded ;

    public void Init() {

        brick_exploded = new Image[3];

        brick_exploded[0] = Sprite.brick_exploded.getFxImage();
        brick_exploded[1] = Sprite.brick_exploded1.getFxImage();
        brick_exploded[2] = Sprite.brick_exploded2.getFxImage();

    }

   public static void remove(List<Entity> entities , int x ,int y, double xMin,double yMin, double xMax, double yMax) {

       for(int i=0; i<entities.size(); i++) {
           Entity entity = entities.get(i);
            if(entity instanceof Brick) {

                if ((xMax == entity.x || xMin == entity.x) && y == entity.y) {
                    entities.remove(i);
                    i--;
                    BombermanGame.hasWallMonster[(int)entity.y][(int)entity.x] = false;
                    BombermanGame.hasWallPlayer[(int)entity.y][(int)entity.x] = false;
                } else if ((yMax == entity.y || yMin == entity.y) && x == entity.x) {
                    entities.remove(i);
                    i--;
                    BombermanGame.hasWallMonster[(int)entity.y][(int)entity.x] = false;
                    BombermanGame.hasWallPlayer[(int)entity.y][(int)entity.x] = false;
                }

            }
        }

   }

   public static void Animation(List<Entity> entities , double x ,double y,int index,
                                double xMin,double yMin, double xMax, double yMax) {

       for(Entity entity : entities) {

           Brick brick = new Brick();
           if (entity instanceof Brick) {

               if ((xMax == entity.x || xMin == entity.x) && y == entity.y) {
                   entity.setImg(brick.brick_exploded[index]);
               }

               if ((yMax== entity.y || yMin == entity.y) && x == entity.x)
                   entity.setImg(brick.brick_exploded[index]);
           }
       }
   }


    @Override
    public void update(double time) {

    }


}
