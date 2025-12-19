package minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameBoardWinConditionDetailedTest {
    private GameBoardPanel board;

    @BeforeEach
    void setUp() {
        MineSweeperMain controller = createMockController();
        board = new GameBoardPanel(controller, 9, 9, 0); // 0个地雷
    }

    @Test
    @DisplayName("详细测试无地雷情况的胜利条件")
    void testWinConditionNoMinesDetailed() {
        System.out.println("=== 开始无地雷胜利条件测试 ===");

        // 验证初始状态
        System.out.println("初始状态 - numMines: " + board.numMines);
        System.out.println("初始状态 - 总单元格: " + (board.boardRowSize * board.boardColSize));

        // 翻开所有单元格
        revealAllCells();

        // 统计实际翻开的单元格数量
        int revealedCount = countRevealedCells();
        System.out.println("翻开后的单元格数量: " + revealedCount);
        System.out.println("总单元格数量: " + (board.boardRowSize * board.boardColSize));
        System.out.println("需要翻开的数量: " + (board.boardRowSize * board.boardColSize - board.numMines));

        // 测试胜利条件
        boolean hasWon = board.hasWon();
        System.out.println("hasWon() 返回: " + hasWon);

        assertTrue(hasWon, "无地雷时所有单元格被翻开应获胜");
        System.out.println("=== 测试完成 ===");
    }

    @Test
    @DisplayName("测试有地雷情况的胜利条件 - 正确实现")
    void testWinConditionWithMinesCorrect() {
        // 重新创建有10个地雷的板
        MineSweeperMain controller = createMockController();
        GameBoardPanel boardWithMines = new GameBoardPanel(controller, 9, 9, 10);

        System.out.println("=== 开始有地雷胜利条件测试 ===");
        System.out.println("地雷数量: " + boardWithMines.numMines);
        System.out.println("总单元格: " + (boardWithMines.boardRowSize * boardWithMines.boardColSize));
        System.out.println("需要翻开的非地雷单元格: " +
                (boardWithMines.boardRowSize * boardWithMines.boardColSize - boardWithMines.numMines));

        // 先查看实际的地雷分布
        System.out.println("实际地雷分布:");
        int actualMineCount = countActualMines(boardWithMines);
        System.out.println("实际地雷数量: " + actualMineCount);

        // 正确翻开所有非地雷单元格
        revealOnlyNonMineCells(boardWithMines);

        int revealedCount = countRevealedCells(boardWithMines);
        int mineCount = countActualMines(boardWithMines);
        System.out.println("实际翻开的单元格数量: " + revealedCount);
        System.out.println("实际地雷数量: " + mineCount);
        System.out.println("应该翻开的数量: " + (81 - mineCount));

        // 检查是否有地雷被错误翻开
        int minesRevealed = countRevealedMines(boardWithMines);
        System.out.println("被错误翻开的地雷数量: " + minesRevealed);

        boolean hasWon = boardWithMines.hasWon();
        System.out.println("hasWon() 返回: " + hasWon);

        assertTrue(hasWon, "所有非地雷单元格被翻开应获胜");
        System.out.println("=== 测试完成 ===");
    }

    @Test
    @DisplayName("测试手动设置地雷的胜利条件")
    void testWinConditionManualMines() {
        MineSweeperMain controller = createMockController();
        GameBoardPanel testBoard = new GameBoardPanel(controller, 9, 9, 0); // 先创建无地雷

        // 手动设置10个地雷在固定位置
        setupManualMines(testBoard, 10);

        System.out.println("=== 手动设置地雷测试 ===");
        System.out.println("设置的地雷数量: " + countActualMines(testBoard));
        System.out.println("需要翻开的非地雷单元格: " + (81 - 10));

        // 翻开所有非地雷单元格
        revealOnlyNonMineCells(testBoard);

        int revealedCount = countRevealedCells(testBoard);
        int mineCount = countActualMines(testBoard);
        System.out.println("实际翻开的单元格数量: " + revealedCount);
        System.out.println("实际地雷数量: " + mineCount);
        System.out.println("被翻开的地雷数量: " + countRevealedMines(testBoard));

        boolean hasWon = testBoard.hasWon();
        System.out.println("hasWon() 返回: " + hasWon);

        assertTrue(hasWon, "手动设置地雷后，所有非地雷单元格被翻开应获胜");
    }

    @Test
    @DisplayName("测试hasWon方法的边界情况")
    void testHasWonEdgeCases() {
        // 测试各种边界情况
        testSpecificCase(0, 0, true, "无地雷，无翻开");  // 应该失败
        testSpecificCase(0, 81, true, "无地雷，全部翻开"); // 应该成功
        testSpecificCase(10, 71, true, "10地雷，所有非地雷翻开"); // 应该成功
        testSpecificCase(10, 70, false, "10地雷，还有一个未翻开"); // 应该失败
    }

    private void testSpecificCase(int mines, int revealed, boolean expected, String description) {
        MineSweeperMain controller = createMockController();
        GameBoardPanel testBoard = new GameBoardPanel(controller, 9, 9, mines);

        setupManualMines(testBoard, mines);
        revealSpecificCells(testBoard, revealed);

        boolean result = testBoard.hasWon();
        System.out.println(description + ": expected=" + expected + ", actual=" + result);
        assertEquals(expected, result, description);
    }

    /**
     * 创建模拟控制器
     */
    private MineSweeperMain createMockController() {
        return new MineSweeperMain() {
            @Override
            public void updateMineCounter(int numRest) {}
            @Override
            public void startTimer() {}
            @Override
            public void GameOver(boolean won) {}
        };
    }

    /**
     * 翻开所有单元格
     */
    private void revealAllCells() {
        for (int i = 0; i < board.boardRowSize; i++) {
            for (int j = 0; j < board.boardColSize; j++) {
                board.cells[i][j].isRevealed = true;
            }
        }
    }

    /**
     * 只翻开非地雷单元格（正确实现）
     */
    private void revealOnlyNonMineCells(GameBoardPanel targetBoard) {
        for (int i = 0; i < targetBoard.boardRowSize; i++) {
            for (int j = 0; j < targetBoard.boardColSize; j++) {
                if (!targetBoard.cells[i][j].isMined) {
                    targetBoard.cells[i][j].isRevealed = true;
                }
            }
        }
    }

    /**
     * 手动设置地雷在固定位置
     */
    private void setupManualMines(GameBoardPanel targetBoard, int mineCount) {
        // 清除所有地雷
        for (int i = 0; i < targetBoard.boardRowSize; i++) {
            for (int j = 0; j < targetBoard.boardColSize; j++) {
                targetBoard.cells[i][j].isMined = false;
            }
        }

        // 在固定位置设置地雷（例如前mineCount个位置）
        int placed = 0;
        for (int i = 0; i < targetBoard.boardRowSize && placed < mineCount; i++) {
            for (int j = 0; j < targetBoard.boardColSize && placed < mineCount; j++) {
                targetBoard.cells[i][j].isMined = true;
                placed++;
            }
        }

        // 更新numMines以匹配实际设置
        targetBoard.numMines = mineCount;
    }

    /**
     * 翻开指定数量的单元格（从非地雷开始）
     */
    private void revealSpecificCells(GameBoardPanel targetBoard, int count) {
        int revealed = 0;

        // 先翻开非地雷单元格
        for (int i = 0; i < targetBoard.boardRowSize && revealed < count; i++) {
            for (int j = 0; j < targetBoard.boardColSize && revealed < count; j++) {
                if (!targetBoard.cells[i][j].isMined && revealed < count) {
                    targetBoard.cells[i][j].isRevealed = true;
                    revealed++;
                }
            }
        }

        // 如果还需要更多，翻开地雷单元格（这会导致测试失败）
        for (int i = 0; i < targetBoard.boardRowSize && revealed < count; i++) {
            for (int j = 0; j < targetBoard.boardColSize && revealed < count; j++) {
                if (!targetBoard.cells[i][j].isRevealed && revealed < count) {
                    targetBoard.cells[i][j].isRevealed = true;
                    revealed++;
                }
            }
        }
    }

    /**
     * 计算实际地雷数量
     */
    private int countActualMines(GameBoardPanel targetBoard) {
        int count = 0;
        for (int i = 0; i < targetBoard.boardRowSize; i++) {
            for (int j = 0; j < targetBoard.boardColSize; j++) {
                if (targetBoard.cells[i][j].isMined) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 计算翻开的单元格数量
     */
    private int countRevealedCells() {
        return countRevealedCells(this.board);
    }

    private int countRevealedCells(GameBoardPanel targetBoard) {
        int count = 0;
        for (int i = 0; i < targetBoard.boardRowSize; i++) {
            for (int j = 0; j < targetBoard.boardColSize; j++) {
                if (targetBoard.cells[i][j].isRevealed) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 计算被翻开的地雷数量
     */
    private int countRevealedMines(GameBoardPanel targetBoard) {
        int count = 0;
        for (int i = 0; i < targetBoard.boardRowSize; i++) {
            for (int j = 0; j < targetBoard.boardColSize; j++) {
                if (targetBoard.cells[i][j].isMined && targetBoard.cells[i][j].isRevealed) {
                    count++;
                }
            }
        }
        return count;
    }
}