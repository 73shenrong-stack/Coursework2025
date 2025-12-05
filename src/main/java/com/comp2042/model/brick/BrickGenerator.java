package com.comp2042.model.brick;

/**
 * Interface for generating sequences of Tetris pieces.
 *
 * <p>This interface defines the contract for piece generation systems in Tetris, abstracting the algorithm used to determine which pieces appear and in what order.
 * Modern Tetris implementations use sophisticated generation systems to ensure fair gameplay and prevent frustrating scenarios.
 *
 * // Game initialization
 * Brick firstPiece = generator.getBrick();       // I-piece
 * Brick preview = generator.getNextBrick();      // T-piece (not consumed)
 * displayNextPiece(preview);
 *
 * // Player locks first piece
 * Brick secondPiece = generator.getBrick();      // T-piece (was previewed)
 * Brick newPreview = generator.getNextBrick();   // L-piece (new preview)
 * displayNextPiece(newPreview);
 *
 * @see RandomBrickGenerator
 * @see Brick
 */
public interface BrickGenerator {

    Brick getBrick();

    Brick getNextBrick();
}
