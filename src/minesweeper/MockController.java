package minesweeper;

/**
 * 用于集成测试的模拟控制器
 * 不依赖Swing组件，可以在无头模式下运行
 */
public class MockController {
    private int mineCounter = 0;
    private boolean timerStarted = false;
    private boolean gameEnded = false;
    private boolean won = false;

    public MockController() {
        // 空的构造函数，不初始化任何UI组件
    }

    public void updateMineCounter(int numRest) {
        this.mineCounter = numRest;
    }

    public void startTimer() {
        this.timerStarted = true;
    }

    public void GameOver(boolean won) {
        this.gameEnded = true;
        this.won = won;
    }

    // 测试验证方法
    public int getMineCounter() { return mineCounter; }
    public boolean isTimerStarted() { return timerStarted; }
    public boolean isGameEnded() { return gameEnded; }
    public boolean isWon() { return won; }

    public void reset() {
        mineCounter = 0;
        timerStarted = false;
        gameEnded = false;
        won = false;
    }
}