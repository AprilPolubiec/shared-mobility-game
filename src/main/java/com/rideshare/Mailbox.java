package com.rideshare;

import java.util.TimerTask;

import com.rideshare.TileManager.TileManager;
import com.rideshare.TileManager.TileUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
   Integer _duration; // In seconds
   Integer _timeLeft = 10; // In seconds; this is for scoring purposes
   Timeline _timeline;
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

   public static GridPanePosition getHousePosition(int row, int col) {
      return new GridPanePosition(row, col - 1);
   }

   public void render() {
      Media waitingMedia = new Media(App.class.getResource("/images/audio/question_003.mp3").toString());
      mailboxWaitingAudio = new MediaPlayer(waitingMedia);

      Media completedMedia = new Media(App.class.getResource("/images/audio/confirmation_001.mp3").toString());
      mailboxCompletedAudio = new MediaPlayer(completedMedia);

      _mailboxTileImageIdx = 0;
      this._mailboxTile = _tileManager.drawTile(_houseTileId + TileUtils.FLAG_HOUSE_OFFSET + _mailboxTileImageIdx, _row,
            _col);

      _mailboxTile.setOnMouseClicked(event -> {
         if (status.get() == MailboxStatus.WAITING) {
            markSelected();
         }
      });

      _mailboxTile.setOnMouseEntered(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event) {
            _mailboxTile.setCursor(Cursor.HAND);
         }
      });
      _mailboxTile.setOnMouseExited(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event) {
            _mailboxTile.setCursor(Cursor.DEFAULT);
         }
      });
      status.set(MailboxStatus.READY);
   }

   public void setDuration(int duration) {
      _duration = duration;
      _timeLeft = _duration;
   }

   public MailboxStatus getStatus() {
      return this.status.get();
   }

   public int getTimeLeft() {
      return this._timeLeft;
   }

   public void addStatusListener(ChangeListener<? super MailboxStatus> listener) {
      this.status.addListener(listener);
   }

   public GridPanePosition getGridPanePosition() {
      return new GridPanePosition(_row, _col);
   }

   // TODO: show a countdown above the mailbox instead
   public void show() {
      if (this.status.get() != MailboxStatus.WAITING) {
         mailboxWaitingAudio.play();
         markWaiting();
         setTimer();
      }

      this._isVisible = true;
      this._mailboxTile.setOpacity(1);
   }

   public void setTimer() {
      _timeline = new Timeline(new KeyFrame(Duration.seconds(1.0), event -> {
         Utils.print(String.format("%s seconds left", _timeLeft));
         if (_timeLeft >= 0) {
            _timeLeft -= 1;
         }
         if (_duration != null) {
            if (status.get() == MailboxStatus.COMPLETED) { // Mailbox completed - stop the timer
               _timeline.stop();
            }
            if (_timeLeft == 0 && status.get() != MailboxStatus.COMPLETED) {
               markFailed();
               _timeline.stop();
            }
         }

      }));
      _timeline.setCycleCount(Timeline.INDEFINITE);
      _timeline.play();
   }

   public void hide() {
      this._isVisible = false;
      this._mailboxTile.setOpacity(0);
   }

   public void markComplete() {
      _tileManager.drawTile(202, _row - 1, _col);
      _tileManager.replaceTileImage(_mailboxTile, TileUtils.COMPLETED_FLAG_IDS[0]);
      this.status.set(MailboxStatus.COMPLETED);
      mailboxCompletedAudio.play();
   }

   public void markSelected() {
      this.status.set(MailboxStatus.SELECTED);
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
   }

}