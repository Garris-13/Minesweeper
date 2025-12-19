//package minesweeper;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.junit.jupiter.params.provider.MethodSource;
//
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GameBoardPanelParameterizedTest {
//    private GameBoardPanel board;
//
//    @BeforeEach
//    void setUp() {
//        MineSweeperMain controller = createMockController();
//        board = new GameBoardPanel(controller, 9, 9, 10);
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "9, 9, 10",
//            "16, 16, 40",
//            "16, 30, 99"
//    })
//    @DisplayName("测试不同难度级别的游戏板配置")
//    void testDifferentDifficultyConfigurations(int rows, int cols, int mines) {
//        MineSweeperMain controller = createMockController();
//        GameBoardPanel testBoard = new GameBoardPanel(controller, rows, cols, mines);
//
//        assertNotNull(testBoard);
//        assertEquals(rows, testBoard.boardRowSize);
//        assertEquals(cols, testBoard.boardColSize);
//        assertEquals(mines, testBoard.numMines);
//        assertEquals(rows, testBoard.cells.length);
//        assertEquals(cols, testBoard.cells[0].length);
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideSimpleMineScenarios")
//    @DisplayName("测试简单地雷布局的周围计数")
//    void testSimpleSurroundingMinesScenarios(int centerRow, int centerCol, int expectedMines, String scenario) {
//        setupSimpleMineScenario();
//        int actualMines = board.getSurroundingMines(centerRow, centerCol);
//        assertEquals(expectedMines, actualMines,
//                "场景 '" + scenario + "' - 位置(" + centerRow + "," + centerCol + ")周围地雷计数应该为" + expectedMines);
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "0, 0",
//            "0, 8",
//            "8, 0",
//            "8, 8",
//            "4, 4"
//    })
//    @DisplayName("测试边界位置的合理计数范围")
//    void testBoundaryPositionReasonableRanges(int row, int col) {
//        board.newGame();
//        int count = board.getSurroundingMines(row, col);
//
//        if (row == 0 || row == 8 || col == 0 || col == 8) {
//            assertTrue(count <= 5, "边界位置(" + row + "," + col + ")的计数应该<=5，实际为：" + count);
//        } else {
//            assertTrue(count <= 8, "内部位置(" + row + "," + col + ")的计数应该<=8，实际为：" + count);
//        }
//        assertTrue(count >= 0, "地雷计数不能为负数");
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "5, 5, 10, 10",
//            "8, 8, 15, 15",
//            "0, 0, 5, 5"
//    })
//    @DisplayName("测试边界情况的地雷生成")
//    void testEdgeCaseMineGeneration(int row, int col, int mines, int expectedMines) {
//        if (mines <= 100 - 9) {
//            MineSweeperMain controller = createMockController();
//            GameBoardPanel testBoard = new GameBoardPanel(controller, 10, 10, mines);
//            testBoard.newGame(10, 10, row, col);
//
//            int actualMines = countMinesOnBoard(testBoard);
//            assertEquals(expectedMines, actualMines, "边界配置应该生成正确数量的地雷");
//        }
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "71, 10, true",
//            "70, 10, false",
//            "81, 0, true",
//            "80, 1, false",
//            "50, 10, false",
//            "0, 10, false"
//    })
//    @DisplayName("测试胜利条件逻辑")
//    void testWinConditionLogic(int revealedCells, int minedCells, boolean shouldWin) {
//        resetBoard();
//        setupMines(minedCells);
//        setupRevealedCells(revealedCells);
//
//        boolean actualWin = board.hasWon();
//        assertEquals(shouldWin, actualWin,
//                "状态(翻开:" + revealedCells + ", 地雷:" + minedCells + ")应该" + (shouldWin ? "获胜" : "未获胜"));
//    }
//
//    /** ------------------- 辅助方法 ------------------- **/
//
//    private static Stream<Object[]> provideSimpleMineScenarios() {
//        return Stream.of(
//                new Object[]{1, 1, 0, "中心位置，无地雷"},
//                new Object[]{0, 0, 0, "左上角位置"},
//                new Object[]{0, 4, 0, "上边缘位置"},
//                new Object[]{4, 0, 0, "左边缘位置"}
//        );
//    }
//
//    private MineSweeperMain createMockController() {
//        return new MineSweeperMain() {
//            @Override
//            public void updateMineCounter(int numRest) {}
//            @Override
//            public void startTimer() {}
//            @Override
//            public void GameOver(boolean won) {}
//        };
//    }
//
//    private void setupSimpleMineScenario() {
//        for (int i = 0; i < 9; i++)
//            for (int j = 0; j < 9; j++)
//                board.cells[i][j].isMined = false;
//    }
//
//    private void resetBoard() {
//        for (int i = 0; i < 9; i++)
//            for (int j = 0; j < 9; j++) {
//                board.cells[i][j].isRevealed = false;
//                board.cells[i][j].isMined = false;
//                board.cells[i][j].isFlagged = false;
//            }
//    }
//
//    private void setupMines(int mineCount) {
//        int placed = 0;
//        for (int i = 0; i < 9 && placed < mineCount; i++) {
//            for (int j = 0; j < 9 && placed < mineCount; j++) {
//                board.cells[i][j].isMined = true;
//                placed++;
//            }
//        }
//    }
//private void setupMines(int mineCount) {
//    int placed = 0;
//    for (int i = 0; i < board.boardRowSize && placed < mineCount; i++) {
//        for (int j = 0; j < board.boardColSize && placed < mineCount; j++) {
//            if (!board.cells[i][j].isMined) {
//                board.cells[i][j].isMined = true;
//                placed++;
//            }
//        }
//    }
//    board.numMines = mineCount; // ✅ 同步更新 GameBoardPanel 的 numMines
//}
//
//    private void setupRevealedCells(int revealedCount) {
//        int revealed = 0;
//
//        // 先翻开非地雷单元格
//        for (int i = 0; i < 9 && revealed < revealedCount; i++) {
//            for (int j = 0; j < 9 && revealed < revealedCount; j++) {
//                if (!board.cells[i][j].isMined && !board.cells[i][j].isRevealed()) {
//                    board.cells[i][j].reveal();  // 使用 reveal() 方法
//                    revealed++;
//                }
//            }
//        }
//
//        // 如果还需要更多，翻开地雷单元格（测试特定场景时可能需要）
//        for (int i = 0; i < 9 && revealed < revealedCount; i++) {
//            for (int j = 0; j < 9 && revealed < revealedCount; j++) {
//                if (board.cells[i][j].isMined && !board.cells[i][j].isRevealed()) {
//                    board.cells[i][j].reveal();  // 使用 reveal() 方法
//                    revealed++;
//                }
//            }
//        }
//
//        // 调试输出
//        int actualRevealed = countRevealedCells();
//        if (actualRevealed != revealedCount) {
//            System.out.println("警告: 设置翻开数量不匹配，期望=" + revealedCount + ", 实际=" + actualRevealed);
//        }
//    }
//    /**
//     * 计算翻开的单元格数量
//     */
//    private int countRevealedCells() {
//        int count = 0;
//        for (int i = 0; i < 9; i++) {
//            for (int j = 0; j < 9; j++) {
//                if (board.cells[i][j].isRevealed()) {  // 注意这里调用方法
//                    count++;
//                }
//            }
//        }
//        return count;
//    }
//
//    private int countMinesOnBoard() {
//        int count = 0;
//        for (int i = 0; i < 9; i++)
//            for (int j = 0; j < 9; j++)
//                if (board.cells[i][j].isMined) count++;
//        return count;
//    }
//
//    private int countMinesOnBoard(GameBoardPanel targetBoard) {
//        int count = 0;
//        for (int i = 0; i < targetBoard.boardRowSize; i++)
//            for (int j = 0; j < targetBoard.boardColSize; j++)
//                if (targetBoard.cells[i][j].isMined) count++;
//        return count;
//    }
//}
//
package minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardPanelParameterizedTest {
    private GameBoardPanel board;

    @BeforeEach
    void setUp() {
        MineSweeperMain controller = createMockController();
        board = new GameBoardPanel(controller, 9, 9, 10);
    }

    @ParameterizedTest
    @CsvSource({
            "9, 9, 10",
            "16, 16, 40",
            "16, 30, 99"
    })
    @DisplayName("测试不同难度级别的游戏板配置")
    void testDifferentDifficultyConfigurations(int rows, int cols, int mines) {
        MineSweeperMain controller = createMockController();
        GameBoardPanel testBoard = new GameBoardPanel(controller, rows, cols, mines);

        assertNotNull(testBoard);
        assertEquals(rows, testBoard.boardRowSize);
        assertEquals(cols, testBoard.boardColSize);
        assertEquals(mines, testBoard.numMines);
        assertEquals(rows, testBoard.cells.length);
        assertEquals(cols, testBoard.cells[0].length);
    }

    @ParameterizedTest
    @MethodSource("provideSimpleMineScenarios")
    @DisplayName("测试简单地雷布局的周围计数")
    void testSimpleSurroundingMinesScenarios(int centerRow, int centerCol, int expectedMines, String scenario) {
        setupSimpleMineScenario();
        int actualMines = board.getSurroundingMines(centerRow, centerCol);
        assertEquals(expectedMines, actualMines,
                "场景 '" + scenario + "' - 位置(" + centerRow + "," + centerCol + ")周围地雷计数应该为" + expectedMines);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0", "0, 8", "8, 0", "8, 8", "4, 4"
    })
    @DisplayName("测试边界位置的合理计数范围")
    void testBoundaryPositionReasonableRanges(int row, int col) {
        board.newGame();
        int count = board.getSurroundingMines(row, col);
        if (row == 0 || row == 8 || col == 0 || col == 8) {
            assertTrue(count <= 5, "边界位置(" + row + "," + col + ")的计数应该<=5，实际为：" + count);
        } else {
            assertTrue(count <= 8, "内部位置(" + row + "," + col + ")的计数应该<=8，实际为：" + count);
        }
        assertTrue(count >= 0, "地雷计数不能为负数");
    }

    @ParameterizedTest
    @CsvSource({
            "5, 5", "0, 0", "8, 8"
    })
    @DisplayName("测试带安全点击的新游戏初始化")
    void testNewGameWithSafeClick(int clickRow, int clickCol) {
        board.newGame(9, 9, clickRow, clickCol);

        for (int i = Math.max(0, clickRow-1); i <= Math.min(8, clickRow+1); i++) {
            for (int j = Math.max(0, clickCol-1); j <= Math.min(8, clickCol+1); j++) {
                assertFalse(board.cells[i][j].isMine(),
                        "安全点击位置(" + clickRow + "," + clickCol + ")周围不应该有地雷");
            }
        }

        int totalMines = countMinesOnBoard();
        assertEquals(10, totalMines, "地雷总数应该为10");
    }

    @ParameterizedTest
//    @CsvSource({
//            "71, 10, true",
//            "70, 10, false",
//            "81, 0, true",
//            "80, 1, false",
//            "50, 10, false",
//            "0, 10, false"
//    })
    @CsvSource({
            "71, 10, true",   // 所有非地雷单元格被翻开
            "70, 10, false",  // 还有一个非地雷未翻开
            "81, 0, true",    // 无地雷，全部翻开
            "80, 1, true",    // 剩余1个地雷，80格翻开 => 已胜利
            "50, 10, false",  // 部分翻开，不应获胜
            "0, 10, false"    // 没有翻开，不应获胜
    })

    @DisplayName("测试胜利条件逻辑")
    void testWinConditionLogic(int revealedCells, int minedCells, boolean shouldWin) {
        resetBoard();
        setupMines(minedCells);
        setupRevealedCells(revealedCells);

        int totalCells = 9 * 9;
        int actualRevealed = countRevealedCells();
        int actualMines = countMinesOnBoard();
        int neededRevealed = totalCells - actualMines;

        System.out.println("测试状态: 总单元格=" + totalCells +
                ", 地雷=" + actualMines +
                ", 实际翻开=" + actualRevealed +
                ", 需要翻开=" + neededRevealed +
                ", 期望获胜=" + shouldWin);

        boolean actualWin = board.hasWon();
        assertEquals(shouldWin, actualWin,
                "状态(翻开:" + actualRevealed + ", 地雷:" + minedCells + ")应该" + (shouldWin ? "获胜" : "未获胜"));
    }

    // =================== 辅助方法 ===================

    private static Stream<Object[]> provideSimpleMineScenarios() {
        return Stream.of(
                new Object[]{1, 1, 0, "中心位置，无地雷"},
                new Object[]{0, 0, 0, "左上角位置"},
                new Object[]{0, 4, 0, "上边缘位置"},
                new Object[]{4, 0, 0, "左边缘位置"}
        );
    }

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

    private void setupSimpleMineScenario() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board.cells[i][j].isMined = false;
            }
        }
    }

    private void resetBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.cells[i][j].isRevealed()) board.cells[i][j].reveal(); // 确保先翻开再关闭
                board.cells[i][j].isRevealed = false;
                board.cells[i][j].isMined = false;
                board.cells[i][j].isFlagged = false;
            }
        }
    }

    private void setupMines(int mineCount) {
        int placed = 0;
        for (int i = 0; i < board.boardRowSize && placed < mineCount; i++) {
            for (int j = 0; j < board.boardColSize && placed < mineCount; j++) {
                if (!board.cells[i][j].isMined) {
                    board.cells[i][j].isMined = true;
                    placed++;
                }
            }
        }
        board.numMines = mineCount; // ✅ 同步更新 GameBoardPanel 的 numMines
    }

//    private void setupRevealedCells(int revealedCount) {
//        int revealed = 0;
//        // 先翻开非地雷单元格
//        for (int i = 0; i < 9 && revealed < revealedCount; i++) {
//            for (int j = 0; j < 9 && revealed < revealedCount; j++) {
//                if (!board.cells[i][j].isMined && !board.cells[i][j].isRevealed()) {
//                    board.cells[i][j].reveal(); // 使用 reveal() 方法
//                    revealed++;
//                }
//            }
//        }
//
//        // 如果还需要，翻开地雷单元格
//        for (int i = 0; i < 9 && revealed < revealedCount; i++) {
//            for (int j = 0; j < 9 && revealed < revealedCount; j++) {
//                if (board.cells[i][j].isMined && !board.cells[i][j].isRevealed()) {
//                    board.cells[i][j].reveal(); // 使用 reveal() 方法
//                    revealed++;
//                }
//            }
//        }
//    }
private void setupRevealedCells(int revealedCount) {
    int revealed = 0;

    for (int i = 0; i < board.boardRowSize && revealed < revealedCount; i++) {
        for (int j = 0; j < board.boardColSize && revealed < revealedCount; j++) {
            Cell cell = board.cells[i][j];
            if (!cell.isMined && !cell.isRevealed) {
                cell.reveal(); // 使用 reveal() 更新状态
                revealed++;
            }
        }
    }
}




    private int countMinesOnBoard() {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.cells[i][j].isMined) count++;
            }
        }
        return count;
    }

    private int countRevealedCells() {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.cells[i][j].isRevealed()) count++;
            }
        }
        return count;
    }
}
