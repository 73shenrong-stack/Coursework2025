# Tetris JFX – COMP2042 Coursework 2025

## GitHub

| Item | Link |
|------|------|
| Main repository | https://github.com/73shenrong-stack/Coursework2025 |

---

## Compilation Instructions

### 1. Prerequisites

| Requirement | Details |
|------------|---------|
| JDK | Java Development Kit (JDK) 23 |
| Build Tool | Apache Maven 3.x |
| Internet | Required for Maven to download dependencies (including JavaFX and `javafx-media`) |
| OS | Tested on Windows with JavaFX modules pulled via Maven |

### 2. Clone the Repository

From a terminal / command prompt:

    git clone https://github.com/73shenrong-stack/Coursework2025.git
    cd Coursework2025

### 3. Build the Project

Use Maven to clean and package the project:

    mvn clean package

This will:

- Download all required dependencies.
- Compile the source code.
- Run any configured tests.
- Produce a runnable JAR in the `target` directory.

If the build succeeds, you should see a file similar to:

- `target/tetris-jfx-1.0-SNAPSHOT.jar`

### 4. Run the Application

From the project root (or any directory where you can see the `target` folder):

    java -jar target/tetris-jfx-1.0-SNAPSHOT.jar

The application will start with the **Main Menu**, where you can select:

- **Blitz** (2-minute time attack)
- **40 Lines** (race to clear 40 lines)
- **Zen** (relaxed, endless mode)

---

## Implemented and Working Properly

| Feature | Description                                                                                                    | Location |
|---------|----------------------------------------------------------------------------------------------------------------|----------|
| **Multiple Game Modes** | Three distinct game modes: Blitz (2-minute timed), 40 Lines (speed challenge), and Zen (relaxed play).         | `com.comp2042.model.game.GameMode` |
| **Main Menu System** | Professional main menu with game mode selection and theme-aware styling.                                       | `com.comp2042.controller.MainMenuController`<br>`src/main/resources/MainMenu.fxml` |
| **Piece Hold Functionality** | Ability to hold the current piece and swap with the held piece (at most once per piece).                       | `com.comp2042.model.board.TetrisBoard.holdBrick()`<br>`com.comp2042.view.actions.GameActionHandler.handleHoldPiece()` |
| **Hard Drop** | Instant piece drop to the ghost/shadow position using the Space key.                                           | `com.comp2042.view.actions.GameActionHandler.handleHardDrop()` |
| **Ghost/Shadow Piece** | Visual shadow showing exactly where the current piece will land.                                               | `com.comp2042.model.board.TetrisBoard.getShadowYPosition()`<br>`com.comp2042.view.renderer.GameRenderer.updateShadow()` |
| **SRS Rotation System** | Super Rotation System with wall kicks for all piece types, including special handling of the I-piece.          | `com.comp2042.model.board.SRSKickData`<br>`com.comp2042.model.board.TetrisBoard.rotateLeftBrick()` |
| **Bag Randomizer** | Fair piece generation using a standard 7-bag system, preventing long droughts of any piece.                    | `com.comp2042.model.brick.RandomBrickGenerator` |
| **Audio System** | Background music and sound effects (drop, clear, rotate, etc.) integrated into gameplay.                       | `com.comp2042.audio.AudioManager` |
| **Scoring System** | Quadratic line clear scoring: 1 line = 50, 2 lines = 200, 3 lines = 450, 4 lines = 800. （50 x no. of lines**2） | `com.comp2042.util.MatrixUtils.clearCompletedLines()`<br>`com.comp2042.model.game.Score` |
| **Mode-Specific Timers** | Countdown timer for Blitz; count-up timers for 40 Lines and Zen, integrated with completion logic.             | `com.comp2042.view.timer.GameModeTimerManager` |
| **Records Persistence** | High scores and best times per mode are saved and loaded from disk.                                            | `com.comp2042.model.game.RecordsPersistence`<br>`com.comp2042.model.game.GameRecords` |
| **Dual Color Themes** | Two themes: Arcade (neon) and Zen (pastel), automatically selected based on game mode.                         | `com.comp2042.view.theme.ColorTheme`<br>`src/main/resources/arcade_style.css`<br>`src/main/resources/zen_style.css` |
| **Pause/Resume System** | Full pause/resume support including overlay UI and audio control.                                              | `com.comp2042.view.lifecycle.GameLifecycleManager.pauseGame()` |
| **Game Over Handling** | Mode-specific game over overlays with record updates and options to retry or return to menu.                   | `com.comp2042.view.ui.UIStateManager.showGameOverOverlay()` |
| **Preview Panels** | Centered next-piece and held-piece preview panels using a dynamic centering algorithm.                         | `com.comp2042.view.renderer.PreviewPanelRenderer` |
| **Responsive UI** | UI states managed via dedicated manager for overlays, gameplay area, and menu visibility.                      | `com.comp2042.view.ui.UIStateManager` |
| **Keyboard Controls** | Arrow keys / WASD for movement, Space for hard drop, C for hold, P for pause, N for new game, M for main menu. | `com.comp2042.controller.input.GameInputHandler` |
| **Soft Drop Scoring** | Soft drop gives incremental points (1 point per cell moved while soft-dropping).                               | `com.comp2042.controller.GameController.onDownEvent()` |
| **Completion Detection** | Automatic detection of Blitz timeout and 40 Lines completion, triggering appropriate completion handling.      | `com.comp2042.view.lifecycle.GameLifecycleManager` |

---
## Implemented but Not Working Properly

| Feature | Issue Description | Attempted Solutions | Status |
|---------|-------------------|---------------------|--------|
| **None** | All implemented features are currently working as intended. | N/A | ✅ Fully operational |

---

## Features Not Implemented

| Feature | Reason for Omission |
|---------|---------------------|
| **Clockwise Rotation** | Focus was on implementing SRS with counter-clockwise rotation. Full SRS requires separate clockwise kick tables for all rotation transitions, which was not feasible within the time constraints. |
| **Multiplayer Mode** | Would require networked communication, synchronization, and additional UI, which is beyond the scope of this coursework. |
| **Custom Key Bindings** | Implementing a configuration UI and persistence for key bindings would significantly expand scope; standard Tetris controls were prioritized instead. |
| **Online Leaderboard System** | Requires backend server infrastructure and APIs. Local per-mode records are implemented as a simpler alternative. |
| **T-Spin Detection** | Needs complex pattern recognition and rotation context tracking. Time was instead invested into solid SRS implementation and polishing core gameplay. |

---

## New Java Classes

| Class Name | Package | Purpose | Key Methods / Notes |
|------------|--------|-------------|-------------------|
| **GameMode** | `com.comp2042.model.game` | Enum defining Blitz, 40 Lines, and Zen game modes, with display names and descriptions. | `getDisplayName()`, `getDescription()` |
| **GameRecords** | `com.comp2042.model.game` | Manages high scores and best times per game mode; serializable for persistence. | `updateBlitzRecord()`, `updateFortyLinesRecord()`, `updateZenRecord()` |
| **RecordsPersistence** | `com.comp2042.model.game` | Saves and loads `GameRecords` to/from disk using Java serialization. | `saveRecords()`, `loadRecords()` |
| **GameConstants** | `com.comp2042.constants` | Stores shared game constants: board dimensions, tick rates, scoring factors, etc. | Exposes `public static final` constants. |
| **ColorTheme** | `com.comp2042.view.theme` | Represents a color theme and provides factory methods for Arcade and Zen themes. | `arcade()`, `zen()`, `forMode(GameMode)`, `getColor()` |
| **AudioManager** | `com.comp2042.audio` | Singleton responsible for playing sound effects and background music for the game. | `playSound()`, `playBackground()`, `stopBackground()` |
| **MainMenuController** | `com.comp2042.controller` | JavaFX controller for the main menu scene, handling mode selection and scene transitions. | `onBlitzMode()`, `onZenMode()`, `onFortyLinesMode()`, `returnToMainMenu()` |
| **GameInputHandler** | `com.comp2042.controller.input` | Central keyboard input handler; maps keys to high-level actions via a callback interface. | `handleKeyPressed()`, `handleKeyReleased()`, inner `InputCallback` interface. |
| **GameActionHandler** | `com.comp2042.view.actions` | High-level action handler for moves, rotations, hard drop, and hold operations. | `handleMoveDown()`, `handleHardDrop()`, `handleHoldPiece()` |
| **GameInitializationManager** | `com.comp2042.view.init` | Coordinates initialization of renderers, themes, timers, and game loop when starting a new game. | `initializeRenderers()`, `applyTheme()`, `createGameLoop()` |
| **GameLifecycleManager** | `com.comp2042.view.lifecycle` | Orchestrates game lifecycle states: start, pause, resume, game over, and mode completion. | `startNewGame()`, `pauseGame()`, `handleGameOver()`, `handleBlitzComplete()`, `handleFortyLinesComplete()` |
| **GameModeTimerManager** | `com.comp2042.view.timer` | Controls mode-specific timers (countdown vs count-up) and counts cleared lines. | `startTimer()`, `addLinesCleared()`, `pauseTimer()` |
| **UIStateManager** | `com.comp2042.view.ui` | Controls UI overlays and general UI state: pause overlay, game over overlay, and main game UI visibility. | `showGameOverOverlay()`, `showPauseOverlay()`, `setGameUIVisibility()` |
| **GameRenderer** | `com.comp2042.view.renderer` | Renders the main board, active brick, and ghost/shadow piece based on `ViewData`. | `refreshBrick()`, `updateShadow()`, `refreshGameBackground()` |
| **PreviewPanelRenderer** | `com.comp2042.view.renderer` | Renders next and held pieces in 4×4 preview areas and ensures they are visually centered. | `updateNextBrickDisplay()`, `updateHoldBrickDisplay()` |
| **SRSKickData** | `com.comp2042.model.board` | Encapsulates SRS wall-kick data for JLSTZ pieces and the I-piece. | `getKicks(pieceType, fromRotation, toRotation)` |

---
## Modified Java Classes


| Class Name | Package | Modifications | Rationale |
|------------|---------|---------------|-----------|
| **Main** | `com.comp2042` | Updated application entry point to load `MainMenu.fxml` instead of a direct game layout; added F11 fullscreen toggle and integration with the main menu controller. | Provide a professional entry experience with explicit mode selection, while improving usability with fullscreen toggling. |
| **Board** (interface) | `com.comp2042.model.board` | Added new methods: `holdBrick()`, `getShadowYPosition()`, and `clearBoard()`. | Support new gameplay mechanics: hold piece, ghost/shadow piece rendering, and clearing the board in Zen mode. |
| **TetrisBoard** | `com.comp2042.model.board` | Implemented hold piece logic using a `hasHeld` flag; added `getShadowYPosition()`; rewrote `rotateLeftBrick()` to use `SRSKickData`; added spawn position constants; implemented `clearBoard()` for Zen mode; moved rotation index management to `BrickRotator`. | Implement new mechanics (hold, ghost piece, Zen board clearing), and implement proper SRS, separating rotation concerns for maintainability. |
| **GameController** | `com.comp2042.controller` | Enhanced to accept `GameMode` in constructor; integrated `AudioManager`; added `onHoldEvent()`; modified collision handling to clear the board rather than ending the game in Zen mode; added `getScore()` to expose Score to the view. | Wire new modes and behaviors into the core game loop, integrate sound feedback, and adjust behavior for the non-ending Zen mode. |
| **GameViewController** | `com.comp2042.view` | Refactored into a composition of managers (`GameInitializationManager`, `GameActionHandler`, `GameLifecycleManager`, `UIStateManager`, `GameModeTimerManager`, `GameRenderer`, `PreviewPanelRenderer`); added `initGameView(GameMode)`; implemented pause/resume logic; added methods to update next/held pieces and UI. | Improve separation of concerns, make the view layer more modular and testable, and support multiple game modes and UI states. |
| **InputEventListener** (interface) | `com.comp2042.controller` | Changed `onDownEvent()` return type from `DownData` to `MoveDownResult`. | Align with the new data model based on `MoveDownResult` and `ViewData`. |
| **Score** | `com.comp2042.model.game` | Moved from the root package into `com.comp2042.model.game`; otherwise unchanged. | Group scoring with mode and record-related classes for clearer package organization. |
| **MatrixUtils** | `com.comp2042.util` | Renamed from `MatrixOperations`; renamed methods `intersect()` → `hasCollision()` and `checkRemoving()` → `clearCompletedLines()`; `clearCompletedLines()` now returns `LineClearResult`. | Improve API readability and integrate with new immutable result types. |
| **RandomBrickGenerator** | `com.comp2042.model.brick` | Changed from naive random selection to 7-bag randomizer, tracking a shuffled set of the 7 tetrominoes. | Match modern Tetris behavior, avoiding piece droughts and improving fairness. |
| **IBrick, JBrick, LBrick, OBrick, SBrick, TBrick, ZBrick** | `com.comp2042.model.brick` | Changed classes from package-private to `public` and marked them `final`. | Expose piece definitions to other packages (e.g., view/rendering) and ensure they are not subclassed. |
| **BrickRotator** | `com.comp2042.model.board` | Manages brick rotation index and provides rotated shapes, decoupling rotation from TetrisBoard. | `getCurrentShape()`, `setBrick()`, `setRotationIndex()` |
| **LineClearResult** | `com.comp2042.model.data` | Immutable result object for line clearing containing cleared line count, new matrix, and score bonus. | `getLinesRemoved()`, `getNewMatrix()`, `getScoreBonus()` |
| **MoveDownResult** | `com.comp2042.model.data` | Wraps the results of a downward move, combining clear result and view data. | `getClearRow()`, `getViewData()` |
| **MoveEvent** | `com.comp2042.model.data` | Represents a movement event describing type and source (USER or THREAD). | `getEventType()`, `getEventSource()` |
| **ViewData** | `com.comp2042.model.data` | Immutable view model containing brick positions, next piece, held piece, and shadow position. | `getBrickData()`, `getNextBrickData()`, `getHeldBrickData()`, `getShadowYPosition()` |
| **RotationInfo** | `com.comp2042.model.data` | Describes rotation shape and position, used for rendering and collision checks. | `getShape()`, `getPosition()` |
| **EventType** | `com.comp2042.model.data` | Enum describing movement types such as DOWN, LEFT, RIGHT, ROTATE, etc. | Enum only. |
| **EventSource** | `com.comp2042.model.data` | Enum describing event source: USER or THREAD. | Enum only. |
| **NotificationPanel** | `com.comp2042.view.components` | Displays animated score notifications and other small HUD messages. | `showScore(int score, int x, int y)` |

---
