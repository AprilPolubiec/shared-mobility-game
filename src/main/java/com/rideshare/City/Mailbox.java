package com.rideshare.City;

import com.rideshare.App;
import com.rideshare.Utils;
import com.rideshare.AudioManager.AudioManager;
import com.rideshare.TileManager.GridPanePosition;
import com.rideshare.TileManager.TileManager;
import com.rideshare.TileManager.TileUtils;
import com.rideshare.UI.UIComponentUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
   private ObjectProperty<MailboxStatus> status = new SimpleObjectProperty<>();
   private TileManager _tileManager;
   private ImageView _mailboxTile;
   private int _houseTileId;
   private boolean hasExpiration = false;
   private Integer _duration; // In seconds
   private Integer _timeLeft = 0; // In seconds
   private Timeline _timeline;
   private AudioManager audio = new AudioManager();

   // TODO: remove housetileid maybe?
   public Mailbox(int row, int col, int houseTileId, TileManager tileManager) {
      _row = row;
      _col = col + 1; // Render a mailbox to the right of the house
      _tileManager = tileManager;
      _houseTileId = houseTileId;
      status.set(MailboxStatus.UNINITIALIZED);
   }

   // #region STATIC FUNCTIONS
   /**
    * Given the row and column of a mailbox, returns the position of the related
    * house (it is always the tile to the left)
    * 
    * @param row row position of mailbox
    * @param col column position of mailbox
    * @return GridPanePosition
    */
   public static GridPanePosition getHousePosition(int row, int col) {
      return new GridPanePosition(row, col - 1);
   }

   // #endregion

   public void render() {
      this._mailboxTile = _tileManager.drawMailbox(_houseTileId, new GridPanePosition(_row, _col));
      _mailboxTile.setOnMouseClicked(event -> {
         if (status.get() == MailboxStatus.WAITING) {
            markSelected();
         }
      });
      UIComponentUtils.addHoverCursor(_mailboxTile);
      status.set(MailboxStatus.READY);
   }

   // TODO: show a countdown above the mailbox if it has a duration time
   public void show() {
      if (this.status.get() != MailboxStatus.WAITING) {
         // This is the first time we are showing the mailbox
         audio.playMailboxWaitingAudio();
         markWaiting();
         if (hasExpiration) {
            setTimer();
         }
      }

      this._mailboxTile.setOpacity(1);
   }

   public void hide() {
      this._mailboxTile.setOpacity(0);
   }

   // #region getters and setters

   public void setDuration(int duration) {
      if (duration <= 0) {
         throw new IllegalArgumentException("Duration must be greater than 0");
      }
      hasExpiration = true;
      _duration = duration;
      _timeLeft = _duration;
   }

   public int getTimeLeft() {
      return this._timeLeft;
   }

   public boolean isInitialized() {
      return this.status.get() != MailboxStatus.UNINITIALIZED;
   }

   public boolean isReady() {
      return this.status.get() != MailboxStatus.READY;
   }

   public boolean isCompleted() {
      return this.status.get() == MailboxStatus.COMPLETED;
   }

   public boolean isSelected() {
      return this.status.get() == MailboxStatus.SELECTED;
   }

   public boolean isExpired() {
      return this.status.get() == MailboxStatus.FAILED;
   }

   public boolean isWaiting() {
      return this.status.get() == MailboxStatus.WAITING;
   }

   public boolean isInProgress() {
      return this.status.get() == MailboxStatus.IN_PROGRESS;
   }

   public void markComplete() {
      _tileManager.drawTile(202, _row - 1, _col);
      _tileManager.replaceTileImage(_mailboxTile, TileUtils.COMPLETED_FLAG_IDS[0]);
      this.status.set(MailboxStatus.COMPLETED);
      audio.playMailboxCompletedAudio();
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

   public GridPanePosition getGridPanePosition() {
      return new GridPanePosition(_row, _col);
   }
   // #endregion

   public void addStatusListener(ChangeListener<? super MailboxStatus> listener) {
      this.status.addListener(listener);
   }

   public void setTimer() {
      _timeline = new Timeline(new KeyFrame(Duration.seconds(1.0), event -> {
         Utils.print(String.format("%s seconds left", _timeLeft));
         _timeLeft -= 1;
         if (status.get() == MailboxStatus.COMPLETED) { // Mailbox completed - stop the timer
            _timeline.stop();
         }
         if (_timeLeft == 0 && status.get() != MailboxStatus.COMPLETED) {
            markFailed();
            _timeline.stop();
         }
      }));
      _timeline.setCycleCount(Timeline.INDEFINITE);
      _timeline.play();
   }

   public void pauseTimer() {
      _timeline.pause();
   }

}