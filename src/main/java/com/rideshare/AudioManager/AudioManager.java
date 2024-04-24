package com.rideshare.AudioManager;

import com.rideshare.App;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AudioManager {
    // Media players are static so we can initialize as many AudioManagers as we
    // want and only ever load in the media once
    private static final Logger LOGGER = Logger.getLogger(AudioManager.class.getName());
    private static MediaPlayer mainMediaPlayer;

    // Not static because it had weird behavior when being shared across mailboxes
    private MediaPlayer mailboxWaitingAudio;
    private MediaPlayer mailboxCompleteAudio;

    private boolean muted = false;

    public AudioManager() {
    }

    public void playBackgroundMusic(String mediaName) {
        if (mainMediaPlayer == null) {
            Media media = new Media(Objects.requireNonNull(App.class.getResource(String.format("/images/audio/%s.mp3",
                    mediaName))).toString());
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
        Media media = new Media(Objects.requireNonNull(App.class.getResource(String.format("/images/audio/%s.mp3",
                mediaName))).toString());
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
                Media waitingMedia = new Media(
                        Objects.requireNonNull(App.class.getResource("/images/audio/question_003.mp3")).toString());
                mailboxWaitingAudio = new MediaPlayer(waitingMedia);
            }
            mailboxWaitingAudio.play();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception occurred while playing audio", e);
        }

    }

    public void playMailboxCompletedAudio() {
        try {
            if (mailboxCompleteAudio == null) {
                Media completedMedia = new Media(
                        Objects.requireNonNull(App.class.getResource("/images/audio/confirmation_001.mp3")).toString());
                mailboxCompleteAudio = new MediaPlayer(completedMedia);
            }
            mailboxCompleteAudio.play();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception occurred while playing audio", e);
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
