package com.comp2042.logic.bricks;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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

    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            nextBricks.add(getNextBrickFromBag());
        }
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}
