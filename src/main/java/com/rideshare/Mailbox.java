package com.rideshare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import com.rideshare.TileManager.TileManager;
import com.rideshare.TileManager.TileUtils;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Mailbox {
   private final int row;
   private final int col;
   MailboxStatus status = MailboxStatus.UNINITIALIZED;
   boolean isVisible = false;
   TileManager tileManager;
   ImageView mailboxTile;
   int mailboxTileImageIdx;
   int houseTileId;
   int duration; // In seconds
   // DateTime startTime; // Maybe
   MediaPlayer mailboxWaitingAudio;
   MediaPlayer mailboxCompletedAudio;
   TripCalculator tripCalculator;

   public Mailbox(int row, int col, int houseTileId, TileManager tileManager) {
      this.row = row;
      this.col = col + 1; // Render a mailbox to the right of the house
      this.tileManager = tileManager;
      this.houseTileId = houseTileId;
   }

   public void setTripCalculator(City city) {
      tripCalculator = new TripCalculator(city);
   }

   public void render() {
      Media waitingMedia = new Media(App.class.getResource("/images/audio/question_003.mp3").toString());
      mailboxWaitingAudio = new MediaPlayer(waitingMedia);

      Media completedMedia = new Media(App.class.getResource("/images/audio/confirmation_001.mp3").toString());
      mailboxCompletedAudio = new MediaPlayer(completedMedia);

      mailboxTileImageIdx = 0;
      this.mailboxTile = tileManager.drawTile(houseTileId + TileUtils.FLAG_HOUSE_OFFSET + mailboxTileImageIdx, row,
            col);
      mailboxTile.setOnMouseClicked(event -> {
         markComplete();
         // Path finder eek
         tripCalculator.calculateTrips(13, 13, row, col);
      });
      this.status = MailboxStatus.READY;

      java.util.Timer timer = new java.util.Timer();
      timer.schedule(new TimerTask() {
         @Override
         public void run() {
            if (status != MailboxStatus.COMPLETED) {
               Platform.runLater(() -> {
                  // Code to update the UI goes here
                  markFailed();
               });
            }
            timer.cancel();
         }
      }, duration * 1000L); // Convert seconds to milliseconds
   }

   // TODO: actually just deduce from the house id
   public void setDuration(int duration) {
      this.duration = duration;
   }

   public MailboxStatus getStatus() {
      return this.status;
   }

   public void show() {
      this.isVisible = true;
      this.mailboxTile.setOpacity(1);
   }

   public void hide() {
      this.isVisible = false;
      this.mailboxTile.setOpacity(0);
   }

   public void markComplete() {
      tileManager.drawTile(202, row - 1, col);
      tileManager.replaceTileImage(mailboxTile, TileUtils.COMPLETED_FLAG_IDS[0]);
      this.status = MailboxStatus.COMPLETED;
      mailboxCompletedAudio.play();
   }

   public void markFailed() {
      tileManager.drawTile(201, row - 1, col);
      this.status = MailboxStatus.FAILED;
   }

   public void markWaiting() {
      this.status = MailboxStatus.WAITING;
      mailboxWaitingAudio.play();
   }

}