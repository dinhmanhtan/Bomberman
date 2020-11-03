package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Grass extends Entity {

    public Grass(Image img) {this.img = img;}
    public Grass(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update(Scene scene,double time) {

    }
}
