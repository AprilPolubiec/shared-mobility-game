package com.rideshare.City;

// TODO: it would be nice to set this as "default"
// however we currently need it for when we create
// mailbox status listeners (addStatusListener) -
// could think of a way to encapsulate this further.
public enum MailboxStatus {
    // Mailbox object has been constructed
    UNINITIALIZED,
    // Mailbox object has been rendered
    READY,
    // Mailbox has been rendered and it's timer has started
    WAITING,
    // Player has selected the mailbox
    SELECTED,
    // Player has selected a trip and is in transit to the mailbox
    IN_PROGRESS,
    // Player has reached the mailbox before its timer ended
    COMPLETED,
    // Mailbox timer has run out before the player has reached it
    FAILED
}
