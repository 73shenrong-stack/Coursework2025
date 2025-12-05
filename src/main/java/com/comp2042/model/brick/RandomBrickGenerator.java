package com.comp2042.model.brick;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates Tetris pieces using the standard "bag" randomization system.
 * @see BrickGenerator
 * @see Brick
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    private final Deque<Integer> currentBag = new ArrayDeque<>();


    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());

        fillBag();
        nextBricks.add(getNextBrickFromBag());
        nextBricks.add(getNextBrickFromBag());
    }

    private void fillBag() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < brickList.size(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices, ThreadLocalRandom.current());
        currentBag.addAll(indices);
    }

    /**
     * Gets the next piece from the current bag, refilling if necessary.
     * Each call creates a NEW brick instance using the type identified by the index.
     * This prevents pieces from sharing state (rotation, position).
     *
     * @return a new brick instance of the next type in the bag
     */
    private Brick getNextBrickFromBag() {
        if (currentBag.isEmpty()) {
            fillBag();
        }
        int index = currentBag.poll();
        Brick template = brickList.get(index);

        // Create new instance based on template type
        // Prevent using same type
        if (template instanceof IBrick) return new IBrick();
        if (template instanceof JBrick) return new JBrick();
        if (template instanceof LBrick) return new LBrick();
        if (template instanceof OBrick) return new OBrick();
        if (template instanceof SBrick) return new SBrick();
        if (template instanceof TBrick) return new TBrick();
        if (template instanceof ZBrick) return new ZBrick();

        return new IBrick();
    }

    /**
     * Retrieves and consumes the next piece from the queue.
     *
     * @return the next brick to be used as the active piece
     */
    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            nextBricks.add(getNextBrickFromBag());
        }
        return nextBricks.poll();
    }

    /**
     * Returns the next piece in the queue without consuming it.
     *
     * @return the piece that will be returned by the next getBrick() call
     */
    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}
