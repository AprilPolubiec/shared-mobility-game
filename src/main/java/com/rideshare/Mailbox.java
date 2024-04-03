package com.rideshare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import com.rideshare.TileManager.TileManager;
import com.rideshare.TileManager.TileUtils;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Description: TODO
 * Attributes:
 * PositionX (int): Mailboxes’ position on the x-axis
 * PositionY (int): Mailboxes’ position on the y-axis
 * Status (Waiting | Complete): Indicates the current status of the mailbox
 * IsVisible (boolean): Indicates whether the user can see the mailbox or not
 * Duration (int): Number of seconds that the mailbox should stay on the screen
 * StartTime (DateTime): When the mailbox started showing - this is the time of
 * day within the game as defined by “Clock”
 * 
 * Methods:
 * show(): updates mailbox to make it visible
 * hide(): updates mailbox to make it invisible
 * markComplete(): marks the mailbox complete
 * startTimer(): starts an infinite loop which constantly checks what “time” is
 * on the Clock, and if the amount indicated by the Duration attribute is hit,
 * it sets the mailbox to invisible
 * 
 */
public class Mailbox {
   private int _row;
   private int _col;
   MailboxStatus status = MailboxStatus.UNINITIALIZED;
   boolean _isVisible = false;
   TileManager _tileManager;
   ImageView _mailboxTile;
   int _mailboxTileImageIdx;
   int _houseTileId;
   int _duration; // In seconds
   // DateTime startTime; // Maybe

   public Mailbox(int row, int col, int houseTileId, TileManager tileManager) {
      _row = row;
      _col = col + 1; // Render a mailbox to the right of the house
      _tileManager = tileManager;
      _houseTileId = houseTileId;
   }

   public void render() {
      _mailboxTileImageIdx = 0;
      this._mailboxTile = _tileManager.drawTile(_houseTileId + TileUtils.FLAG_HOUSE_OFFSET + _mailboxTileImageIdx, _row,
            _col);
      this.status = MailboxStatus.READY;

      java.util.Timer timer = new java.util.Timer();
      timer.schedule(new TimerTask() {
         @Override
         public void run() {
            hide();
            timer.cancel();
         }
      }, _duration * 1000); // Convert seconds to milliseconds
   }

   // TODO: actually just deduce from the house id
   public void setDuration(int duration) {
      _duration = duration;
   }

   public void show() {
      this._isVisible = true;
      this._mailboxTile.setOpacity(1);
   }

   public void hide() {
      this._isVisible = false;
      this._mailboxTile.setOpacity(0);
   }

   public void markComplete() {
      _tileManager.replaceTileImage(_mailboxTile, TileUtils.COMPLETED_FLAG_IDS[0]);
      this.status = MailboxStatus.COMPLETED;
   }

   public void markWaiting() {
      this.status = MailboxStatus.WAITING;
   }

}