package com.rideshare;

public enum MailboxStatus {
    // Mailbox object has been constructed
    UNINITIALIZED,
    // Mailbox object has been rendered
    READY,
    // Mailbox has been rendered and it's timer has started
    WAITING,
    // Player has selected a trip and is in transit to the mailbox
    IN_PROGRESS,
    // Player has reached the mailbox before its timer ended
    COMPLETED,
    // Mailbox timer has run out before the player has reached it
    FAILED
}
