package com.comp2042.model.data;

/**
 * Immutable data class representing a game move event.
 * Encapsulates both the type of move (direction/rotation) and the source of the event (user input or automatic game loop).
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a new move event.
     *
     * @param eventType the type of movement or action
     * @param eventSource the source of the event (user or automatic)
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Gets the type of this event.
     *
     * @return the event type (DOWN, LEFT, RIGHT, or ROTATE)
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the source of this event.
     *
     * @return the event source (USER or THREAD)
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}