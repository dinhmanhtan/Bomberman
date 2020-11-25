package uet.oop.bomberman.monster;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;
import java.util.Random;

public abstract class  Monster extends Entity {

        protected Image[] mob_dead = new Image[3];
        protected Image imgMonsterDead ;

        protected float totaltime;
        protected float totaltime1;
        protected float totalDead;

        public Monster(int x, int y, Image img) {
            super(x, y, img);
            init();
        }

        public void setDead (boolean dead) {
            this.dead = dead;
        }

        abstract public void update(double deltaTime);
        abstract public void init();
        abstract public void moving(double deltaTime,double timeSpeed) ;
        abstract public void Animation() ;


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

        public boolean Check () {

            if( x  - (int) x == 0 && y - (int) y== 0 ) {
                if(  state == State.Right) {
                    if (!BombermanGame.hasWallMonster[(int) y + 1][(int) x]) {
                        return true;
                    }
                }

                if( state == State.Left) {
                    if (!BombermanGame.hasWallMonster[(int) y - 1][(int) x]) {
                        return true;
                    }
                }

                if( state == State.Up) {
                    if (!BombermanGame.hasWallMonster[(int) (y)][(int) x - 1]) {
                        return true;
                    }
                }

                if ( state == State.Down) {
                    if (!BombermanGame.hasWallMonster[(int) y][(int) x + 1]) {
                        return true;
                    }
                }
            }
            // Check trai
            return false;
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



    public void AnimationDead(double deltaTime) {
        totalDead += deltaTime;
        if(totalDead <= 0.4) {
            setImg(imgMonsterDead);
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
            BombermanGame.bomberman.score += 200;
            //  BombermanGame.monsters.remove(this);

        }
    }

}
