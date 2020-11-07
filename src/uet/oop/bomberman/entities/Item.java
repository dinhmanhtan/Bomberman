package uet.oop.bomberman.entities;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Item extends Entity{

    public Entity SpeedItem;
    public Entity FlameItem;
    public Entity BombItem;

    public Item() {
      Init();
    }

    private void Init() {

        SpeedItem = new Entity(100,100, Sprite.powerup_speed.getFxImage());
        FlameItem = new Entity(100,100,Sprite.powerup_flames.getFxImage());
        BombItem  = new Entity(100,100,Sprite.powerup_bombs.getFxImage());


    }

    public void setPosition(List<Entity> bricks) {

        Map<Integer,Entity> map = new HashMap<>();

        int num = 0;

        for (Entity entity : bricks) {

            if (entity instanceof  Brick) {
                map.put(num, entity);
                num ++;
            }
        }

        Random random = new Random();

        int i ,j,k;
        while (true) {
            k = random.nextInt(num);
            i = random.nextInt(num);
            j = random.nextInt(num);
            if(k != i && k !=j && i != j)
                break;
        }

        SpeedItem.setXY(map.get(i).getX(),map.get(i).getY());
        BombItem.setXY(map.get(k).getX(),map.get(k).getY());
        FlameItem.setXY(map.get(j).getX(),map.get(j).getY());


        System.out.println("Vị trí :\nspeed : " + SpeedItem.getX() + " " + SpeedItem.getY() );
        System.out.println("flame : " + FlameItem.getX() + " " + FlameItem.getY());
        System.out.println("bomb : " + BombItem.getX() + " " + BombItem.getY());


    }

    public void update(List<Bomb> bombs) {

        int X = (int) BombermanGame.bomberman.getX();
        int Y = (int) BombermanGame.bomberman.getY();

        if(BombItem.isDraw() && X == BombItem.getX() && Y == BombItem.getY()){

            Bomb bomb = new Bomb();
            bombs.add(bomb);
            BombermanGame.bomberman.number_bomb ++;
            BombItem.setDraw(false);

        } else if(SpeedItem.isDraw() && X == SpeedItem.getX() && Y == SpeedItem.getY()) {
            SpeedItem.setDraw(false);
            BombermanGame.bomberman.setSpeed(0.07);

        } else if(FlameItem.isDraw() && X == FlameItem.getX() && Y == FlameItem.getY()) {
            FlameItem.setDraw(false);

            for(Bomb bomb : bombs) { bomb.hasFlame = true;}
        }
    }


    @Override
    public void render(GraphicsContext gc) {
        SpeedItem.render(gc);
        FlameItem.render(gc);
        BombItem.render(gc);

    }

}
