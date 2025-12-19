package minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

/**
 * 完整游戏流程集成测试
 * 模拟真实用户操作场景
 */
class CompleteGameFlowIntegrationTest {

    private TestController controller;
    private GameBoardPanel board;

    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");
        controller = new TestController();
        board = new GameBoardPanel(controller, 9, 9, 5); // 使用较少地雷便于测试
    }

    @Test
    @DisplayName("测试完整胜利游戏流程")
    void testCompleteWinGameFlow() {
        // 1. 游戏初始化
        board.newGame();
        verifyInitialGameState();

        // 2. 第一次点击
        performFirstClick(4, 4);
        verifyPostFirstClickState();

        // 3. 逐步翻开所有安全单元格
        revealAllSafeCells();

        // 4. 验证胜利
        assertTrue(board.hasWon(), "游戏应该胜利");

        // 5. 验证游戏结束状态
        assertTrue(controller.isGameEnded(), "游戏应该结束");
        assertTrue(controller.isWon(), "应该标记为胜利");
    }

    @Test
    @DisplayName("测试完整失败游戏流程")
    void testCompleteLoseGameFlow() {
        // 1. 游戏初始化
        board.newGame();
        verifyInitialGameState();

        // 2. 第一次点击
        performFirstClick(4, 4);
        verifyPostFirstClickState();

        // 3. 直接点击地雷导致失败
        Cell mineCell = findFirstMine();
        assertNotNull(mineCell, "应该找到地雷");

        // 模拟点击地雷导致游戏结束
        controller.GameOver(false);

        // 4. 验证失败状态
        assertTrue(controller.isGameEnded(), "游戏应该结束");
        assertFalse(controller.isWon(), "应该标记为失败");
    }

    @Test
    @DisplayName("测试标记和取消标记完整流程")
    void testCompleteFlaggingFlow() {
        board.newGame();

        int initialMineCount = controller.getMineCounter();

        // 标记一个单元格
        Cell cell = board.cells[0][0];
        cell.isFlagged = true;
        board.numRestMine = initialMineCount - 1;
        controller.updateMineCounter(initialMineCount - 1);

        // 验证标记状态
        assertTrue(cell.isFlagged, "单元格应该被标记");
        assertEquals(initialMineCount - 1, board.numRestMine, "剩余地雷应该减少");
        assertEquals(initialMineCount - 1, controller.getMineCounter(), "控制器计数应该更新");

        // 取消标记
        cell.isFlagged = false;
        board.numRestMine = initialMineCount;
        controller.updateMineCounter(initialMineCount);

        // 验证取消标记状态
        assertFalse(cell.isFlagged, "单元格应该取消标记");
        assertEquals(initialMineCount, board.numRestMine, "剩余地雷应该恢复");
        assertEquals(initialMineCount, controller.getMineCounter(), "控制器计数应该恢复");
    }

    @Test
    @DisplayName("测试游戏重置流程")
    void testGameResetFlow() {
        // 初始游戏状态
        board.newGame();

        // 改变一些状态
        board.cells[0][0].isRevealed = true;
        board.cells[0][1].isFlagged = true;
        board.numRestMine = 3;
        board.bFirstClick = false;
        controller.updateMineCounter(3);

        // 重置游戏
        board.newGame();

        // 验证状态完全重置
        verifyInitialGameState();
    }

    // 辅助方法
    private void verifyInitialGameState() {
        assertEquals(5, board.numRestMine, "初始剩余地雷应该为5");
        assertTrue(board.bFirstClick, "初始第一次点击标志应该为true");

        assertEquals(5, controller.getMineCounter(), "控制器地雷计数应该为5");
        assertFalse(controller.isTimerStarted(), "初始计时器不应该启动");
        assertFalse(controller.isGameEnded(), "初始游戏不应该结束");
    }

    private void performFirstClick(int row, int col) {
        try {
            // 使用反射调用带安全点击的新游戏
            Method newGameMethod = GameBoardPanel.class.getDeclaredMethod(
                    "newGame", int.class, int.class, int.class, int.class);
            newGameMethod.setAccessible(true);
            newGameMethod.invoke(board, 9, 9, row, col);
        } catch (Exception e) {
            fail("第一次点击执行失败: " + e.getMessage());
        }
    }

    private void verifyPostFirstClickState() {
        assertTrue(controller.isTimerStarted(), "第一次点击后计时器应该启动");
        assertFalse(board.bFirstClick, "第一次点击后标志应该为false");
    }

    private void revealAllSafeCells() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!board.cells[i][j].isMined) {
                    board.cells[i][j].isRevealed = true;
                }
            }
        }
    }

    private Cell findFirstMine() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.cells[i][j].isMined) {
                    return board.cells[i][j];
                }
            }
        }
        return null;
    }
}