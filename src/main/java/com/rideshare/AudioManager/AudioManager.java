package com.rideshare.AudioManager;

import com.rideshare.App;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {
    MediaPlayer mainMediaPlayer;
    private boolean muted = false;

    public AudioManager() {
    }

    public void playBackgroundMusic(String mediaName) {
        if (mainMediaPlayer == null) {
            Media media = new Media(App.class.getResource(String.format("/images/audio/%s.mp3",
            mediaName)).toString());
            mainMediaPlayer = new MediaPlayer(media);
            mainMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        mainMediaPlayer.play();
    }

    public void pauseBackgroundMusic() {
        if (mainMediaPlayer == null) {
            return;
        }
        mainMediaPlayer.pause();
    }

    public void changeBackgroundMusic(String mediaName) {
        Media media = new Media(App.class.getResource(String.format("/images/audio/%s.mp3",
                mediaName)).toString());
        mainMediaPlayer = new MediaPlayer(media);
    }

    public void muteBackgroundMusic(Boolean shouldMute) {
        if (mainMediaPlayer == null) {
            return;
        }
        mainMediaPlayer.setMute(shouldMute);
    }


    public boolean isMuted() {
        return muted;
    }

    public void toggleMute(Boolean shouldMute) {
        muted = shouldMute;
        if (mainMediaPlayer != null) {
            mainMediaPlayer.setMute(muted);
        }
    }


}
