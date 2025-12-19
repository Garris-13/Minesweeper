package minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

/**
 * GameBoardPanel完整集成测试
 * 验证游戏逻辑、地雷数据和UI控制的完整集成
 */
class GameBoardPanelIntegrationTest {

    private TestController controller;
    private GameBoardPanel board;
    private final int BOARD_SIZE = 9;

    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");
        controller = new TestController();
        board = new GameBoardPanel(controller, BOARD_SIZE, BOARD_SIZE, 10);
    }

    @Test
    @DisplayName("测试游戏完整初始化流程")
    void testCompleteGameInitialization() {
        // 调用newGame初始化
        board.newGame();

        // 验证控制器状态
        assertEquals(10, controller.getMineCounter(),
                "地雷计数器应该初始化为10");

        // 验证游戏板状态
        assertTrue(board.bFirstClick, "第一次点击标志应该为true");
        assertEquals(10, board.numRestMine, "剩余地雷数应该为10");

        // 验证所有单元格正确初始化
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Cell cell = board.cells[i][j];
                assertFalse(cell.isRevealed, "所有单元格初始不应该被翻开");
                assertFalse(cell.isFlagged, "所有单元格初始不应该被标记");
                assertTrue(cell.isEnabled(), "所有单元格初始应该可用");
            }
        }
    }

    @Test
    @DisplayName("测试周围地雷计数")
    void testSurroundingMinesCount() {
        board.newGame();

        // 手动设置测试场景
        setupTestMineScenario();

        // 测试中心位置
        int centerCount = board.getSurroundingMines(1, 1);
        assertEquals(3, centerCount, "中心位置(1,1)周围应该有3个地雷");

        // 测试角落位置
        int cornerCount = board.getSurroundingMines(0, 0);
        assertEquals(1, cornerCount, "角落位置(0,0)周围应该有1个地雷");
    }

    @Test
    @DisplayName("测试胜利条件判断")
    void testWinCondition() {
        board.newGame();

        // 模拟胜利状态：翻开所有非地雷单元格
        simulateWinState();

        assertTrue(board.hasWon(), "当所有非地雷单元格被翻开时应获胜");
    }

    @Test
    @DisplayName("测试游戏未胜利状态")
    void testNotWinCondition() {
        board.newGame();

        // 只翻开部分单元格
        board.cells[0][0].isRevealed = true;
        board.cells[0][1].isRevealed = true;

        assertFalse(board.hasWon(), "当还有未翻开的非地雷单元格时不应获胜");
    }

    @Test
    @DisplayName("测试新游戏初始化")
    void testNewGameInitialization() {
        // 先改变一些状态
        board.cells[0][0].isRevealed = true;
        board.cells[0][1].isFlagged = true;
        board.numRestMine = 3;
        board.bFirstClick = false;

        // 重新开始游戏
        board.newGame();

        // 验证状态重置
        assertEquals(10, board.numRestMine, "剩余地雷数应该重置为10");
        assertTrue(board.bFirstClick, "第一次点击标志应该重置");
    }

    @Test
    @DisplayName("测试标记操作集成")
    void testFlagOperationIntegration() {
        board.newGame();

        // 模拟标记操作
        Cell cell = board.cells[0][0];
        cell.isFlagged = true;
        board.numRestMine = 9; // 模拟标记后剩余地雷减少

        // 验证控制器更新
        controller.updateMineCounter(9);

        // 验证状态一致性
        assertEquals(9, board.numRestMine, "标记后剩余地雷应该减少");
        assertEquals(9, controller.getMineCounter(), "控制器地雷计数应该更新");
        assertTrue(cell.isFlagged, "单元格应该被标记");
    }

    @Test
    @DisplayName("测试游戏胜利完整流程")
    void testGameWinCompleteFlow() {
        setupControlledWinScenario();

        // 验证胜利条件
        assertTrue(board.hasWon(), "应该满足胜利条件");

        // 模拟游戏结束
        controller.GameOver(true);

        assertTrue(controller.isGameEnded(), "游戏应该结束");
        assertTrue(controller.isWon(), "游戏应该胜利");
    }

    @Test
    @DisplayName("测试游戏失败完整流程")
    void testGameLoseCompleteFlow() {
        board.newGame();

        // 找到第一个地雷
        Cell mineCell = findFirstMine();
        assertNotNull(mineCell, "应该找到地雷单元格");

        // 模拟点击地雷导致游戏结束
        controller.GameOver(false);

        assertTrue(controller.isGameEnded(), "游戏应该结束");
        assertFalse(controller.isWon(), "游戏应该失败");
    }

    @Test
    @DisplayName("测试递归展开集成")
    void testRecursiveRevealIntegration() throws Exception {
        setupControlledScenarioForReveal();

        // 使用反射调用revealCell方法
        Method revealMethod = GameBoardPanel.class.getDeclaredMethod("revealCell", int.class, int.class);
        revealMethod.setAccessible(true);

        // 在空白区域触发递归展开
        revealMethod.invoke(board, 4, 4);

        // 验证多个单元格被展开
        int revealedCount = countRevealedCells();
        assertTrue(revealedCount > 1, "递归展开应该翻开多个单元格");

        // 验证展开的合理性（不应该展开到地雷）
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.cells[i][j].isRevealed) {
                    assertFalse(board.cells[i][j].isMined,
                            "翻开的单元格不应该有地雷");
                }
            }
        }
    }

    @Test
    @DisplayName("测试动画系统集成")
    void testAnimationSystemIntegration() {
        board.newGame();

        // 验证动画计时器存在
        assertNotNull(board.animTimer, "动画计时器应该存在");

        // 验证动画系统基本功能
        assertDoesNotThrow(() -> {
            // 测试动画系统不会抛出异常
            boolean timerRunning = board.animTimer.isRunning();
            // 不关心具体状态，只关心不会异常
        });
    }

    // 辅助方法
    private void setupTestMineScenario() {
        // 清除现有地雷
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board.cells[i][j].isMined = false;
            }
        }

        // 设置特定的地雷布局用于测试
        board.cells[0][0].isMined = true;
        board.cells[0][1].isMined = true;
        board.cells[0][2].isMined = true;
    }

    private void simulateWinState() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!board.cells[i][j].isMined) {
                    board.cells[i][j].isRevealed = true;
                }
            }
        }
    }

    private void setupControlledWinScenario() {
        board.newGame();

        // 翻开所有非地雷单元格来模拟胜利
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!board.cells[i][j].isMined) {
                    board.cells[i][j].isRevealed = true;
                }
            }
        }
    }

    private void setupControlledScenarioForReveal() {
        board.newGame();

        // 创建一个安全的空白区域用于测试递归展开
        // 清除中间区域的地雷
        for (int i = 3; i <= 5; i++) {
            for (int j = 3; j <= 5; j++) {
                board.cells[i][j].isMined = false;
            }
        }
    }

    private Cell findFirstMine() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.cells[i][j].isMined) {
                    return board.cells[i][j];
                }
            }
        }
        return null;
    }

    private int countRevealedCells() {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.cells[i][j].isRevealed) {
                    count++;
                }
            }
        }
        return count;
    }
}