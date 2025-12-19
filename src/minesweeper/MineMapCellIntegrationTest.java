package minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * MineMap和Cell的集成测试
 * 验证地雷生成与单元格状态的集成
 */
class MineMapCellIntegrationTest {

    private MineMap mineMap;
    private Cell[][] cells;
    private final int BOARD_SIZE = 9;

    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");
        mineMap = new MineMap(BOARD_SIZE, BOARD_SIZE);
        cells = new Cell[BOARD_SIZE][BOARD_SIZE];

        // 初始化单元格数组
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    @Test
    @DisplayName("测试地雷分布与单元格状态同步")
    void testMineDistributionAndCellStateSync() {
        // 使用中心位置，确保安全区域在边界内
        int centerRow = 4;
        int centerCol = 4;

        // 生成地雷分布
        mineMap.newMineMap(10, centerRow, centerCol);

        // 将地雷分布同步到单元格
        syncMineMapToCells();

        // 验证地雷数量一致
        int mineCountInMap = countMinesInMap();
        int mineCountInCells = countMinesInCells();
        assertEquals(mineCountInMap, mineCountInCells,
                "地雷在Map和Cells中的数量应该一致");

        // 验证安全区域
        verifySafetyArea(centerRow, centerCol);

        // 验证单元格初始状态
        verifyCellInitialStates();
    }

    @Test
    @DisplayName("测试第一次点击安全区域集成")
    void testFirstClickSafetyIntegration() {
        // 使用中心位置，确保安全区域在边界内
        int safeRow = 4;
        int safeCol = 4;

        // 生成带安全保护的地雷
        mineMap.newMineMap(10, safeRow, safeCol);
        syncMineMapToCells();

        // 验证安全区域单元格都没有地雷
        for (int i = safeRow-1; i <= safeRow+1; i++) {
            for (int j = safeCol-1; j <= safeCol+1; j++) {
                if (i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE) {
                    assertFalse(cells[i][j].isMined,
                            "安全区域单元格(" + i + "," + j + ")不应该有地雷");
                    assertFalse(cells[i][j].isRevealed,
                            "安全区域单元格初始不应该被翻开");
                }
            }
        }
    }

    @Test
    @DisplayName("测试单元格状态重置集成")
    void testCellStateResetIntegration() {
        // 第一次设置 - 使用中心位置
        mineMap.newMineMap(5, 4, 4);
        syncMineMapToCells();

        // 改变一些单元格状态
        cells[0][0].isRevealed = true;
        cells[0][1].isFlagged = true;

        // 重新生成地雷并同步 - 使用不同的中心位置
        mineMap.newMineMap(8, 2, 2);
        syncMineMapToCells();

        // 验证状态被正确重置（除了地雷状态）
        assertFalse(cells[0][0].isRevealed, "单元格应该重置为未翻开");
        assertFalse(cells[0][1].isFlagged, "单元格应该重置为未标记");
    }

    @Test
    @DisplayName("测试边界位置的安全保护")
    void testEdgePositionSafety() {
        // 测试左上角边界
        mineMap.newMineMap(5, 0, 0);
        syncMineMapToCells();
        verifySafetyArea(0, 0);

        // 测试右下角边界
        mineMap = new MineMap(BOARD_SIZE, BOARD_SIZE);
        mineMap.newMineMap(5, BOARD_SIZE-1, BOARD_SIZE-1);
        syncMineMapToCells();
        verifySafetyArea(BOARD_SIZE-1, BOARD_SIZE-1);
    }

    // 辅助方法
    private void syncMineMapToCells() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                cells[i][j].newGame(mineMap.isMined[i][j]);
            }
        }
    }

    private int countMinesInMap() {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (mineMap.isMined[i][j]) count++;
            }
        }
        return count;
    }

    private int countMinesInCells() {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (cells[i][j].isMined) count++;
            }
        }
        return count;
    }

    private void verifySafetyArea(int centerRow, int centerCol) {
        for (int i = Math.max(0, centerRow-1); i <= Math.min(BOARD_SIZE-1, centerRow+1); i++) {
            for (int j = Math.max(0, centerCol-1); j <= Math.min(BOARD_SIZE-1, centerCol+1); j++) {
                assertFalse(mineMap.isMined[i][j],
                        "安全区域(" + i + "," + j + ")不应该有地雷");
            }
        }
    }

    private void verifyCellInitialStates() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Cell cell = cells[i][j];
                assertFalse(cell.isRevealed, "单元格初始不应该被翻开");
                assertFalse(cell.isFlagged, "单元格初始不应该被标记");
                assertTrue(cell.isEnabled(), "单元格初始应该可用");
            }
        }
    }
}