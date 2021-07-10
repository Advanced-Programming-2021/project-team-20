package project.view;

import java.net.URL;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javafx.util.Duration;

public class SongPlayer {
    private MediaPlayer backgroundMusic;
    private MediaView mediaView;
    private MediaPlayer shortMusic;
    private MediaView shortMediaView;
    private static SongPlayer songPlayer;

    private SongPlayer() {

    }

    public static SongPlayer getInstance() {
        if (songPlayer == null) {
            songPlayer = new SongPlayer();
        }
        return songPlayer;
    }

    public void prepareBackgroundMusic(String url) {
        URL resource = getClass().getResource(url);
        backgroundMusic = new MediaPlayer(new Media(resource.toString()));
        mediaView = new MediaView();
        mediaView.setMediaPlayer(backgroundMusic);
        backgroundMusic.setAutoPlay(true);
        backgroundMusic.setVolume(0.4);
        backgroundMusic.setOnEndOfMedia(new Runnable() {
            public void run() {
                backgroundMusic.seek(Duration.ZERO);
            }
        });
        backgroundMusic.play();
    }

    public void playShortMusic(String url){
        URL resource = getClass().getResource(url);
        shortMusic = new MediaPlayer(new Media(resource.toString()));
        shortMediaView = new MediaView();
        shortMediaView.setMediaPlayer(shortMusic);
        shortMusic.setVolume(1);
        shortMusic.play();
    }

    public void pauseMusic() {
        if (backgroundMusic != null)
            backgroundMusic.pause();
    }
}
