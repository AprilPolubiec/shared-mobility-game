package com.rideshare.AudioManager;

import com.rideshare.App;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {
    // Media players are static so we can initialize as many AudioManagers as we
    // want and only ever load in the media once
    private static MediaPlayer mainMediaPlayer;
    private static MediaPlayer mailboxWaitingAudio;
    private static MediaPlayer mailboxCompleteAudio;

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

    public void playMailboxWaitingAudio() {
        try {
            if (mailboxWaitingAudio == null) {
                Media waitingMedia = new Media(App.class.getResource("/images/audio/question_003.mp3").toString());
                mailboxWaitingAudio = new MediaPlayer(waitingMedia);
            }
            mailboxWaitingAudio.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void playMailboxCompletedAudio() {
        try {
            if (mailboxCompleteAudio == null) {
                Media completedMedia = new Media(
                        App.class.getResource("/images/audio/confirmation_001.mp3").toString());
                mailboxCompleteAudio = new MediaPlayer(completedMedia);
            }
            mailboxCompleteAudio.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
