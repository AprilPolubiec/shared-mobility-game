package com.rideshare;
/**
 * Description: TODO
 * Attributes:
    PositionX (int): Mailboxes’ position on the x-axis 
    PositionY (int): Mailboxes’ position on the y-axis 
    Status (Waiting | Complete): Indicates the current status of the mailbox
    IsVisible (boolean): Indicates whether the user can see the mailbox or not
    Duration (int): Number of seconds that the mailbox should stay on the screen
    StartTime (DateTime): When the mailbox started showing - this is the time of day within the game as defined by “Clock”
    
    Methods:
    show(): updates mailbox to make it visible
    hide(): updates mailbox to make it invisible
    markComplete(): marks the mailbox complete
    startTimer(): starts an infinite loop which constantly checks what “time” is on the Clock, and if the amount indicated by the Duration attribute is hit, it sets the mailbox to invisible

 */
public class Mailbox {

}
