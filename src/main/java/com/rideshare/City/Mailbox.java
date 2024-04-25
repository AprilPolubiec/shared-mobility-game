package com.rideshare.City;

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
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Mailbox {
   //Instance Field Declarations
   private final GridPanePosition position;
   private final ObjectProperty<MailboxStatus> status = new SimpleObjectProperty<>();
   private final TileManager tileManager;
   private ImageView mailboxTile;
   private final int houseTileId;
   private boolean hasExpiration = false;
    private Integer timeLeft = 0; // In seconds
   private Timeline timeline;
   private final AudioManager audio = new AudioManager();

   //Class Constructor
   public Mailbox(GridPanePosition housePosition, int houseTileId, TileManager tileManager) {
      position = housePosition.toTheRight(); // To the right of the house
      this.tileManager = tileManager;
      this.houseTileId = houseTileId;
      status.set(MailboxStatus.UNINITIALIZED);
   }

   //Class Getter Methods
   public GridPanePosition getGridPanePosition() {
      return position;
   }

   public int getTimeLeft() {
      return this.timeLeft;
   }

   //Class Setter Methods
   public void setTimer() {
      timeline = new Timeline(new KeyFrame(Duration.seconds(1.0), event -> {
         Utils.print(String.format("%s seconds left", timeLeft));
         timeLeft -= 1;
         if (status.get() == MailboxStatus.COMPLETED) { // Mailbox completed - stop the timer
            timeline.stop();
         }
         if (timeLeft == 0 && status.get() != MailboxStatus.COMPLETED) {
            markFailed();
            timeline.stop();
         }
      }));
      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.play();
   }

   //Class Static Methods
   /**
    * Given the row and column of a mailbox, returns the position of the related
    * house (it is always the tile to the left)
    * 
    * @param row row position of mailbox
    * @param col column position of mailbox
    * @return GridPanePosition
    */
   public static GridPanePosition getHousePosition(int row, int col) {
      return new GridPanePosition(row, col).toTheLeft();
   }

   public static boolean mailboxesClickable = true;


    public static void disableMailboxes() {
        mailboxesClickable = false;
    }

    public static void enableMailboxes() {
        mailboxesClickable = true;
    }
   
   //Class Render Methods

   public void render() {
      this.mailboxTile = this.tileManager.drawMailbox(this.houseTileId, position);
      this.mailboxTile.setOnMouseClicked(event -> {
        if (mailboxesClickable && status.get() == MailboxStatus.WAITING) {
            markSelected();
         }
      });
      UIComponentUtils.addHoverCursor(this.mailboxTile, false);
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

      this.mailboxTile.setOpacity(1);
   }

   //Status Check Methods
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

   //Status Setter Methods
   public void markComplete() {
      this.tileManager.drawTile(202, position.above());
      this.tileManager.replaceTileImage(this.mailboxTile, TileUtils.COMPLETED_FLAG_IDS[0]);
      this.status.set(MailboxStatus.COMPLETED);
      audio.playMailboxCompletedAudio();
   }

   public void markSelected() {
      if (mailboxesClickable ==true){
      this.status.set(MailboxStatus.SELECTED);
      }
   }

   public void markInProgress() {
      this.status.set(MailboxStatus.IN_PROGRESS);
   }

   public void markFailed() {
      this.tileManager.drawTile(201, position.above());
      this.status.set(MailboxStatus.FAILED);
   }

   public void markWaiting() {
      this.status.set(MailboxStatus.WAITING);
   }

   //Additional Class Methods
   public void addStatusListener(ChangeListener<? super MailboxStatus> listener) {
      this.status.addListener(listener);
   }

   //TODO: Methods declared but never used. Should they be utilised or deleted?
   public void pauseTimer() {
      timeline.pause();
   }

   public void hide() {
      this.mailboxTile.setOpacity(0);
   }

   public boolean isInProgress() {
      return this.status.get() == MailboxStatus.IN_PROGRESS;
   }

   public void setDuration(int duration) {
      if (duration <= 0) {
         throw new IllegalArgumentException("Duration must be greater than 0");
      }
      hasExpiration = true;
      // In seconds
       timeLeft = duration;
   }
}