package uet.oop.bomberman.entities;


import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public class Item extends Entity{

    public Entity SpeedItem;
    public Entity FlameItem;
    public Entity BombItem;

    public Item() {

    }

    private void Init() {

        SpeedItem = new Entity(100,100, Sprite.powerup_speed.getFxImage());
        FlameItem = new Entity(100,100,Sprite.powerup_flames.getFxImage());
        BombItem  = new Entity(100,10,Sprite.powerup_bombs.getFxImage());

    }


}
