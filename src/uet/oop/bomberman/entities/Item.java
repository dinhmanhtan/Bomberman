package uet.oop.bomberman.entities;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.PlayMusic;
import uet.oop.bomberman.graphics.Sprite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Item extends Entity{

    public Entity SpeedItem;
    public Entity FlameItem;
    public Entity BombItem;
    public Entity Portal;

    public Item() {
      Init();
    }

    private void Init() {

        SpeedItem = new Entity(100,100, Sprite.powerup_speed.getFxImage());
        FlameItem = new Entity(100,100,Sprite.powerup_flames.getFxImage());
        BombItem  = new Entity(100,100,Sprite.powerup_bombs.getFxImage());
        Portal    = new Entity(100,100,Sprite.portal.getFxImage());

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

        int i ,j,k,m;
        while (true) {
            k = random.nextInt(num);
            i = random.nextInt(num);
            j = random.nextInt(num);
            m = random.nextInt(num);
            if(k != i && k !=j && i != j && k !=m && i !=m && j != m)
                break;
        }

        SpeedItem.setXY(map.get(i).getX(),map.get(i).getY());
        BombItem.setXY(map.get(k).getX(),map.get(k).getY());
        FlameItem.setXY(map.get(j).getX(),map.get(j).getY());
        Portal.setXY(map.get(m).getX(),map.get(m).getY());

        System.out.println("Vị trí :\nspeed : " + SpeedItem.getX() + " " + SpeedItem.getY() );
        System.out.println("flame : " + FlameItem.getX() + " " + FlameItem.getY());
        System.out.println("bomb : " + BombItem.getX() + " " + BombItem.getY());
        System.out.println("portal : " + Portal.getX() + " " + Portal.getY());

    }

    public void update(List<Bomb> bombs) {

        double X =  BombermanGame.bomberman.getX();
        double Y =  BombermanGame.bomberman.getY();

        if(BombItem.isDraw() && X == BombItem.getX() && Y == BombItem.getY()){

            Bomb bomb = new Bomb();
            bombs.add(bomb);
            BombermanGame.bomberman.number_bomb ++;
            BombItem.setDraw(false);

        } else if(SpeedItem.isDraw() && X == SpeedItem.getX() && Y == SpeedItem.getY()) {
            SpeedItem.setDraw(false);
            BombermanGame.bomberman.setSpeed(BombermanGame.bomberman.getSpeed()-0.013);

        } else if(FlameItem.isDraw() && X == FlameItem.getX() && Y == FlameItem.getY()) {
            FlameItem.setDraw(false);
            BombermanGame.bomberman.hasFlame = true;
        } else  if(Portal.isDraw() && X == Portal.getX() && Portal.getY() == Y && BombermanGame.monsters.isEmpty()) {
                BombermanGame.bomberman.level ++;

                BombermanGame.AnimationNextStage = true;

                if(BombermanGame.bomberman.level == 3)
                    BombermanGame.win = true;

                BombermanGame.mediaPlayer.stop();

                Entity entity = new Entity();
                entity.playMusic(PlayMusic.stage_complete_music,true);

        }
    }


    @Override
    public void render(GraphicsContext gc) {
        SpeedItem.render(gc);
        FlameItem.render(gc);
        BombItem.render(gc);
        Portal.render(gc);
    }

}
