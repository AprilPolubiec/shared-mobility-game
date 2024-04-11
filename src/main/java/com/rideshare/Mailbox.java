package com.rideshare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import com.rideshare.GameManager.Sprite;
import com.rideshare.TileManager.TileManager;
import com.rideshare.TileManager.TileUtils;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Mailbox {
   private int _row;
   private int _col;
   ObjectProperty<MailboxStatus> status = new SimpleObjectProperty<>();
   boolean _isVisible = false;
   TileManager _tileManager;
   ImageView _mailboxTile;
   int _mailboxTileImageIdx;
   int _houseTileId;
   int _duration; // In seconds
   // DateTime startTime; // Maybe
   MediaPlayer mailboxWaitingAudio;
   MediaPlayer mailboxCompletedAudio;

   public Mailbox(int row, int col, int houseTileId, TileManager tileManager) {
      _row = row;
      _col = col + 1; // Render a mailbox to the right of the house
      _tileManager = tileManager;
      _houseTileId = houseTileId;
      status.set(MailboxStatus.UNINITIALIZED);
   }

   // TODO: pass sprite in constructor, not here
   public void render() {
      Media waitingMedia = new Media(App.class.getResource("/images/audio/question_003.mp3").toString());
      mailboxWaitingAudio = new MediaPlayer(waitingMedia);

      Media completedMedia = new Media(App.class.getResource("/images/audio/confirmation_001.mp3").toString());
      mailboxCompletedAudio = new MediaPlayer(completedMedia);

      _mailboxTileImageIdx = 0;
      this._mailboxTile = _tileManager.drawTile(_houseTileId + TileUtils.FLAG_HOUSE_OFFSET + _mailboxTileImageIdx, _row,
            _col);
      
      _mailboxTile.setOnMouseClicked(event -> {
         markInProgress();
      });
      status.set(MailboxStatus.READY);
   }

   // TODO: actually just deduce from the house id
   public void setDuration(int duration) {
      _duration = duration;
   }

   public MailboxStatus getStatus() {
      return this.status.get();
   }
   public void addStatusListener(ChangeListener<? super MailboxStatus> listener) {
      this.status.addListener(listener);
   }

   public GridPanePosition getGridPanePosition() {
      return new GridPanePosition(_row, _col);
   }

   public void show() {
      if (this.status.get() != MailboxStatus.WAITING) {
         markWaiting();
         java.util.Timer timer = new java.util.Timer();
         timer.schedule(new TimerTask() {
            @Override
            public void run() {
               if (status.get() != MailboxStatus.COMPLETED) {
                  Platform.runLater(() -> {
                     // Code to update the UI goes here
                     markFailed();
                  });
               }
               timer.cancel();
            }
         }, _duration * 1000); // Convert seconds to milliseconds
      }

      this._isVisible = true;
      this._mailboxTile.setOpacity(1);
   }

   public void hide() {
      this._isVisible = false;
      this._mailboxTile.setOpacity(0);
   }

   public void markComplete() {
      _tileManager.drawTile(202, _row - 1, _col);
      _tileManager.replaceTileImage(_mailboxTile, TileUtils.COMPLETED_FLAG_IDS[0]);
      this.status.set(MailboxStatus.COMPLETED);
      // mailboxCompletedAudio.play();
   }

   public void markInProgress() {
      this.status.set(MailboxStatus.IN_PROGRESS);
   }

   public void markFailed() {
      _tileManager.drawTile(201, _row - 1, _col);
      this.status.set(MailboxStatus.FAILED);
   }

   public void markWaiting() {
      this.status.set(MailboxStatus.WAITING);
      // mailboxWaitingAudio.play();
   }

}