package com.comp2042.model.data;

/**
 * Enumeration representing the source of a game event.
 * Distinguishes between user-initiated actions and automatic game events.
 *
 * This is used primarily to determine scoring behavior:
 * soft drop points are only awarded for USER-initiated downward movements, not for automatic piece falling triggered by the game loop (THREAD).
 */
public enum EventSource {
    USER, THREAD
}