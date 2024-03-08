package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class MailboxTest {
    @Test
    void Test_CanCreateMailbox() {
        int positionX = 0;
        int positionY = 0;
        int duration = 5;
        Mailbox mailbox = new Mailbox(positionX, positionY, duration);
    
        assertEquals(this.positionX, mailbox.getPosition().x);
        assertEquals(this.positiony, mailbox.getPosition().y);
        assertEquals(MailboxStatus.READY, mailbox.getStatus());
        assertNotNull(mailbox.getTimer());
    }

    @Test
    void Test_CanHideMailbox() {
        int positionX = 0;
        int positionY = 0;
        int duration = 5;
        Mailbox mailbox = new Mailbox(positionX, positionY, duration);
        // We are defauling to hiding but can be showing - up to you
        assertEquals(false, mailbox.IsVisible);
        mailbox.show();
        assertEquals(true, mailbox.IsVisible);
        assertEquals(MailboxStatus.WAITING, mailbox.getStatus());
        mailbox.hide();
        assertEquals(false, mailbox.IsVisible);
        assertEquals(MailboxStatus.READY, mailbox.getStatus());
    }

    @Test
    void Test_CanMarkMailboxComplete() {
        int positionX = 0;
        int positionY = 0;
        int duration = 5;
        Mailbox mailbox = new Mailbox(positionX, positionY, duration);
        mailbox.show();
        mailbox.markComplete();
        assertEquals(MailboxStatus.COMPLETED, mailbox.getStatus());
    }

    @Test
    void Test_CannotMarkHiddenMailboxComplete() {
        int positionX = 0;
        int positionY = 0;
        int duration = 5;
        Mailbox mailbox = new Mailbox(positionX, positionY, duration);
        mailbox.hide();
        assertThrows(Exception.class, () -> mailbox.markComplete());
    }

    @Test
    void Test_MailboxTimerStartsOnShow() {
        int positionX = 0;
        int positionY = 0;
        int duration = 5;
        Mailbox mailbox = new Mailbox(positionX, positionY, duration);
        mailbox.show();
        assertEquals(TimerState.RUNNING, mailbox.getTimer());
    }

    @Test
    void Test_MailboxTimerPausesOnHide() {
        int positionX = 0;
        int positionY = 0;
        int duration = 5;
        Mailbox mailbox = new Mailbox(positionX, positionY, duration);
        mailbox.show();
        mailbox.hide();
        assertEquals(TimerState.PAUSED, mailbox.getTimer());

    }
    @Test
    void Test_MailboxTimerStopsOnComplete() {
        int positionX = 0;
        int positionY = 0;
        int duration = 5;
        Mailbox mailbox = new Mailbox(positionX, positionY, duration);
        mailbox.show();
        mailbox.markComplete();
        assertEquals(TimerState.STOPPED, mailbox.getTimer());
    }

    @Test
    void Test_CanGetMailboxStartTime() {
        int positionX = 0;
        int positionY = 0;
        int duration = 5;
        Mailbox mailbox = new Mailbox(positionX, positionY, duration);
        mailbox.show();
        assertNotNull(mailbox.getStartTime());
    }
}