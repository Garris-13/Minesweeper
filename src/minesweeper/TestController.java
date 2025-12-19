package minesweeper;

/**
 * 测试用控制器（Mock / Spy）
 *
 * 说明：
 * - 继承自 MineSweeperMain，重写了与测试相关的外部接口方法以避免真实 UI 操作对测试的干扰。
 * - 由于 MineSweeperMain 的构造器会做 UI 初始化，在测试中建议以 headless 模式运行（System.setProperty("java.awt.headless","true")）
 *   或者直接在测试中使用匿名子类来替代。这个类尽量把副作用最小化。
 */
public class TestController extends MineSweeperMain {
    private int mineCounter = 0;
    private boolean timerStarted = false;
    private boolean gameEnded = false;
    private boolean won = false;

    // 由于父类无参构造会运行 UI 初始化，如果你希望避免它在构造时执行，可以
    // 在测试中通过创建匿名子类或使用另一个工厂方法来替换。
    public TestController() {
        // 不要在这里调用 super 的 UI 逻辑（父类构造会被隐式调用），
        // 运行测试时建议设置 headless 模式 System.setProperty("java.awt.headless", "true");
    }

    @Override
    public void updateMineCounter(int numRest) {
        this.mineCounter = numRest;
    }

    @Override
    public void startTimer() {
        this.timerStarted = true;
    }

    @Override
    public void GameOver(boolean won) {
        this.gameEnded = true;
        this.won = won;
    }

    // 供测试断言使用的访问器
    public int getMineCounter() {
        return mineCounter;
    }

    public boolean isTimerStarted() {
        return timerStarted;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public boolean isWon() {
        return won;
    }

    public void reset() {
        mineCounter = 0;
        timerStarted = false;
        gameEnded = false;
        won = false;
    }
}
