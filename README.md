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

| Feature | Description | Location |
|---------|-------------|----------|
| **Multiple Game Modes** | Three distinct game modes: Blitz (2-minute timed), 40 Lines (speed challenge), and Zen (relaxed play). | `com.comp2042.model.game.GameMode` |
| **Main Menu System** | Professional main menu with game mode selection and theme-aware styling. | `com.comp2042.controller.MainMenuController`<br>`src/main/resources/MainMenu.fxml` |
| **Piece Hold Functionality** | Ability to hold the current piece and swap with the held piece (at most once per piece). | `com.comp2042.model.board.TetrisBoard.holdBrick()`<br>`com.comp2042.view.actions.GameActionHandler.handleHoldPiece()` |
| **Hard Drop** | Instant piece drop to the ghost/shadow position using the Space key. | `com.comp2042.view.actions.GameActionHandler.handleHardDrop()` |
| **Ghost/Shadow Piece** | Visual shadow showing exactly where the current piece will land. | `com.comp2042.model.board.TetrisBoard.getShadowYPosition()`<br>`com.comp2042.view.renderer.GameRenderer.updateShadow()` |
| **SRS Rotation System** | Super Rotation System with wall kicks for all piece types, including special handling of the I-piece. | `com.comp2042.model.board.SRSKickData`<br>`com.comp2042.model.board.TetrisBoard.rotateLeftBrick()` |
| **Bag Randomizer** | Fair piece generation using a standard 7-bag system, preventing long droughts of any piece. | `com.comp2042.model.brick.RandomBrickGenerator` |
| **Audio System** | Background music and sound effects (drop, clear, rotate, etc.) integrated into gameplay. | `com.comp2042.audio.AudioManager` |
| **Scoring System** | Quadratic line clear scoring: 1 line = 50, 2 lines = 200, 3 lines = 450, 4 lines = 800. | `com.comp2042.util.MatrixUtils.clearCompletedLines()`<br>`com.comp2042.model.game.Score` |
| **Mode-Specific Timers** | Countdown timer for Blitz; count-up timers for 40 Lines and Zen, integrated with completion logic. | `com.comp2042.view.timer.GameModeTimerManager` |
| **Records Persistence** | High scores and best times per mode are saved and loaded from disk. | `com.comp2042.model.game.RecordsPersistence`<br>`com.comp2042.model.game.GameRecords` |
| **Dual Color Themes** | Two themes: Arcade (neon) and Zen (pastel), automatically selected based on game mode. | `com.comp2042.view.theme.ColorTheme`<br>`src/main/resources/arcade_style.css`<br>`src/main/resources/zen_style.css` |
| **Pause/Resume System** | Full pause/resume support including overlay UI and audio control. | `com.comp2042.view.lifecycle.GameLifecycleManager.pauseGame()` |
| **Game Over Handling** | Mode-specific game over overlays with record updates and options to retry or return to menu. | `com.comp2042.view.ui.UIStateManager.showGameOverOverlay()` |
| **Preview Panels** | Centered next-piece and held-piece preview panels using a dynamic centering algorithm. | `com.comp2042.view.renderer.PreviewPanelRenderer` |
| **Responsive UI** | UI states managed via dedicated manager for overlays, gameplay area, and menu visibility. | `com.comp2042.view.ui.UIStateManager` |
| **Keyboard Controls** | Arrow keys / WASD for movement, Space for hard drop, C for hold, P for pause, N for new game, M for mute. | `com.comp2042.controller.input.GameInputHandler` |
| **Soft Drop Scoring** | Soft drop gives incremental points (1 point per cell moved while soft-dropping). | `com.comp2042.controller.GameController.onDownEvent()` |
| **Completion Detection** | Automatic detection of Blitz timeout and 40 Lines completion, triggering appropriate completion handling. | `com.comp2042.view.lifecycle.GameLifecycleManager` |

---
