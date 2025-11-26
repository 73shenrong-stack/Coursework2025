package com.comp2042.model.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class MoveEventTest {

    @Test
    @DisplayName("Should store event type and source")
    void testEventCreation() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);

        assertEquals(EventType.DOWN, event.getEventType());
        assertEquals(EventSource.USER, event.getEventSource());
    }

    @Test
    @DisplayName("Should distinguish between user and thread events")
    void testEventSource() {
        MoveEvent userEvent = new MoveEvent(EventType.DOWN, EventSource.USER);
        MoveEvent threadEvent = new MoveEvent(EventType.DOWN, EventSource.THREAD);

        assertEquals(EventSource.USER, userEvent.getEventSource());
        assertEquals(EventSource.THREAD, threadEvent.getEventSource());
    }

    @Test
    @DisplayName("Should support all event types")
    void testAllEventTypes() {
        MoveEvent down = new MoveEvent(EventType.DOWN, EventSource.USER);
        MoveEvent left = new MoveEvent(EventType.LEFT, EventSource.USER);
        MoveEvent right = new MoveEvent(EventType.RIGHT, EventSource.USER);
        MoveEvent rotate = new MoveEvent(EventType.ROTATE, EventSource.USER);

        assertEquals(EventType.DOWN, down.getEventType());
        assertEquals(EventType.LEFT, left.getEventType());
        assertEquals(EventType.RIGHT, right.getEventType());
        assertEquals(EventType.ROTATE, rotate.getEventType());
    }
}