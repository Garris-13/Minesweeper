package minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameBoardDeepDiagnosisTest {

    @Test
    @DisplayName("深度诊断hasWon方法")
    void testDeepDiagnosisHasWon() {
        MineSweeperMain controller = createMockController();
        GameBoardPanel board = new GameBoardPanel(controller, 9, 9, 10);

        System.out.println("=== 深度诊断 ===");
        System.out.println("构造函数参数 - 地雷数: 10");
        System.out.println("实际 numMines: " + board.numMines);
        System.out.println("boardRowSize: " + board.boardRowSize);
        System.out.println("boardColSize: " + board.boardColSize);
        System.out.println("总单元格: " + (board.boardRowSize * board.boardColSize));

        // 检查实际地雷分布
        int actualMines = countActualMines(board);
        System.out.println("实际地雷数量: " + actualMines);

        // 手动计算hasWon的逻辑
        int numRevealed = 0;
        for (int i = 0; i < board.boardRowSize; i++) {
            for (int j = 0; j < board.boardColSize; j++) {
                if (board.cells[i][j].isRevealed) {
                    numRevealed++;
                }
            }
        }

        System.out.println("翻开的单元格: " + numRevealed);
        System.out.println("需要翻开的单元格 (总-地雷): " + (board.boardRowSize * board.boardColSize - board.numMines));

        boolean manualCalculation = (numRevealed == board.boardRowSize * board.boardColSize - board.numMines);
        boolean hasWonResult = board.hasWon();

        System.out.println("手动计算结果: " + manualCalculation);
        System.out.println("hasWon() 结果: " + hasWonResult);

        // 如果结果不一致，说明hasWon方法有问题
        if (manualCalculation != hasWonResult) {
            System.out.println("!!! hasWon方法实现可能有问题 !!!");
        }

        System.out.println("=== 诊断完成 ===");
    }

    @Test
    @DisplayName("测试newGame方法后的状态")
    void testAfterNewGame() {
        MineSweeperMain controller = createMockController();
        GameBoardPanel board = new GameBoardPanel(controller, 9, 9, 10);

        System.out.println("=== newGame方法测试 ===");
        System.out.println("调用newGame前 - numMines: " + board.numMines);

        // 调用newGame
        board.newGame();

        System.out.println("调用newGame后 - numMines: " + board.numMines);
        System.out.println("调用newGame后 - 实际地雷: " + countActualMines(board));

        // 翻开所有单元格测试
        revealAllCells(board);
        System.out.println("翻开所有单元格后 - hasWon(): " + board.hasWon());
        System.out.println("需要翻开: " + (81 - board.numMines));
        System.out.println("实际翻开: " + countRevealedCells(board));
    }

    @Test
    @DisplayName("测试hasWon方法的具体实现")
    void testHasWonImplementation() {
        MineSweeperMain controller = createMockController();
        GameBoardPanel board = new GameBoardPanel(controller, 9, 9, 10);

        System.out.println("=== hasWon方法实现测试 ===");

        // 查看hasWon方法的实际代码
        System.out.println("让我们查看hasWon方法的实现...");

        // 模拟hasWon方法的逻辑
        int numRevealed = 0;
        for (int i = 0; i < board.boardRowSize; i++) {
            for (int j = 0; j < board.boardColSize; j++) {
                if (board.cells[i][j].isRevealed) {
                    numRevealed++;
                }
            }
        }

        int totalCells = board.boardRowSize * board.boardColSize;
        int expectedRevealed = totalCells - board.numMines;

        System.out.println("总单元格: " + totalCells);
        System.out.println("numMines: " + board.numMines);
        System.out.println("需要翻开的单元格: " + expectedRevealed);
        System.out.println("实际翻开的单元格: " + numRevealed);
        System.out.println("计算结果: " + (numRevealed == expectedRevealed));
        System.out.println("hasWon() 结果: " + board.hasWon());

        // 如果numMines为0，那么需要翻开81个单元格
        if (board.numMines == 0) {
            System.out.println("注意: numMines为0，需要翻开所有81个单元格");
            revealAllCells(board);
            System.out.println("翻开所有后 hasWon(): " + board.hasWon());
        }
    }

    @Test
    @DisplayName("测试无地雷的特殊情况")
    void testZeroMinesSpecialCase() {
        MineSweeperMain controller = createMockController();
        GameBoardPanel board = new GameBoardPanel(controller, 9, 9, 0);

        System.out.println("=== 无地雷特殊情况测试 ===");
        System.out.println("numMines: " + board.numMines);
        System.out.println("实际地雷: " + countActualMines(board));

        // 情况1: 没有翻开任何单元格
        System.out.println("没有翻开任何单元格 - hasWon(): " + board.hasWon());

        // 情况2: 翻开部分单元格
        revealSomeCells(board, 50);
        System.out.println("翻开50个单元格 - hasWon(): " + board.hasWon());

        // 情况3: 翻开所有单元格
        revealAllCells(board);
        System.out.println("翻开所有单元格 - hasWon(): " + board.hasWon());
        System.out.println("实际翻开数量: " + countRevealedCells(board));
        System.out.println("需要翻开数量: " + (81 - board.numMines));
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
     * 计算实际地雷数量
     */
    private int countActualMines(GameBoardPanel board) {
        int count = 0;
        for (int i = 0; i < board.boardRowSize; i++) {
            for (int j = 0; j < board.boardColSize; j++) {
                if (board.cells[i][j].isMined) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 计算翻开的单元格数量
     */
    private int countRevealedCells(GameBoardPanel board) {
        int count = 0;
        for (int i = 0; i < board.boardRowSize; i++) {
            for (int j = 0; j < board.boardColSize; j++) {
                if (board.cells[i][j].isRevealed) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 翻开所有单元格
     */
    private void revealAllCells(GameBoardPanel board) {
        for (int i = 0; i < board.boardRowSize; i++) {
            for (int j = 0; j < board.boardColSize; j++) {
                board.cells[i][j].isRevealed = true;
            }
        }
    }

    /**
     * 翻开指定数量的单元格
     */
    private void revealSomeCells(GameBoardPanel board, int count) {
        int revealed = 0;
        for (int i = 0; i < board.boardRowSize && revealed < count; i++) {
            for (int j = 0; j < board.boardColSize && revealed < count; j++) {
                board.cells[i][j].isRevealed = true;
                revealed++;
            }
        }
    }
}