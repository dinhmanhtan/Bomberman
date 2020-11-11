package uet.oop.bomberman;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class PlayMusic {

    public static final String step_horizontal_sound = "res/music/step_horizontal.wav";
    public static final String step_vertical_sound = "res/music/step_vertical2.wav";
    public static final String stage_theme_music = "res/music/03_Stage Theme.mp3";
    public static final String explosion_sound = "res/music/explosion.wav";
    public static final String put_bomb_sound = "res/music/put_bomb.wav";
    public static final String title_screen_music = "res/music/01_Title Screen.mp3";
    public static final String death_sound = "res/music/death.wav";
    public static final String life_lost_music = "res/music/08_Life Lost.mp3";
    public static final String game_over_music = "res/music/09_Game Over.mp3";

    public MediaPlayer mediaPlayer;


    public static void playMusic(String path, boolean isPlay, MediaPlayer mediaPlayer) {

       if(isPlay) {
           Media media = new Media(new File(path).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
           mediaPlayer.play();
       }
    }

    public void PlayLoopMusic(String path) {
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaPlayer.play();

    }

}
