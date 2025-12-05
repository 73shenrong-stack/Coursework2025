package com.comp2042.model.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages the player's score using JavaFX property binding.
 * Provides an observable IntegerProperty that can be bound to UI elements for automatic score display updates.
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * Gets the score property for binding to UI elements.
     * This property automatically notifies listeners when the score changes.
     *
     * @return the observable IntegerProperty for the score
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Adds points to the current score.
     * Used for soft drop points and line clear bonuses.
     *
     * @param i the number of points to add
     */
    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    public void reset() {
        score.setValue(0);
    }
}
