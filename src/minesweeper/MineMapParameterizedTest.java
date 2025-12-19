package minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class MineMapParameterizedTest {
    private MineMap mineMap;

    @BeforeEach
    void setUp() {
        mineMap = new MineMap(10, 10);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    @DisplayName("测试带安全点击的不同地雷数量生成")
    void testVariousMineCountsWithSafeClick(int mineCount) {
        // 使用带安全点击的版本，这个版本应该能正确生成指定数量的地雷
        int safeRow = 5, safeCol = 5;
        mineMap.newMineMap(mineCount, safeRow, safeCol);

        int generatedMines = countMines(mineMap.isMined);
        assertEquals(mineCount, generatedMines,
                "带安全点击应该生成" + mineCount + "个地雷");

        // 验证安全区域
        verifySafetyArea(safeRow, safeCol);
    }

    @ParameterizedTest
    @ValueSource(ints = {10})
    @DisplayName("测试固定地雷生成方法 - 已知生成10个地雷")
    void testFixedMineGeneration(int expectedMines) {
        // 已知newMineMap(int)方法固定生成10个地雷
        mineMap.newMineMap(expectedMines);

        int actualMines = countMines(mineMap.isMined);
        assertEquals(expectedMines, actualMines,
                "固定地雷生成方法应该生成" + expectedMines + "个地雷");
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",    // 左上角
            "0, 9",    // 右上角
            "9, 0",    // 左下角
            "9, 9",    // 右下角
            "5, 5",    // 中心
            "0, 5",    // 上边缘
            "5, 0",    // 左边缘
            "9, 5",    // 下边缘
            "5, 9"     // 右边缘
    })
    @DisplayName("测试不同位置的第一次点击安全保护")
    void testFirstClickSafetyVariousPositions(int clickRow, int clickCol) {
        int mineCount = 15; // 使用合理的数量
        mineMap.newMineMap(mineCount, clickRow, clickCol);

        // 验证安全区域
        verifySafetyArea(clickRow, clickCol);

        // 验证总地雷数量正确
        int totalMines = countMines(mineMap.isMined);
        assertEquals(mineCount, totalMines,
                "位置(" + clickRow + "," + clickCol + ")的地雷数量应该正确");
    }

    @ParameterizedTest
    @CsvSource({
            "5, 5, 10",
            "3, 3, 5",
            "8, 8, 15",
            "1, 1, 20"
    })
    @DisplayName("测试不同安全位置和地雷数量的组合")
    void testVariousSafePositionsAndMineCounts(int safeRow, int safeCol, int mines) {
        mineMap.newMineMap(mines, safeRow, safeCol);

        int actualMines = countMines(mineMap.isMined);
        assertEquals(mines, actualMines,
                "配置(" + safeRow + "," + safeCol + "," + mines + ")应该生成正确数量的地雷");

        // 验证安全区域
        verifySafetyArea(safeRow, safeCol);
    }

    @ParameterizedTest
    @CsvSource({
            "5, 5, 10, 10",
            "8, 8, 15, 15",
            "0, 0, 5, 5"
    })
    @DisplayName("测试边界情况的地雷生成")
    void testEdgeCaseMineGeneration(int row, int col, int mines, int expectedMines) {
        // 对于边界情况，确保不会因为安全区域而无法生成足够的地雷
        if (mines <= 100 - 9) { // 确保有足够的空间放置地雷（100个格子 - 3x3安全区域）
            mineMap.newMineMap(mines, row, col);

            int actualMines = countMines(mineMap.isMined);
            assertEquals(expectedMines, actualMines,
                    "边界配置应该生成正确数量的地雷");
        }
    }

    /**
     * 验证安全区域的辅助方法
     */
    private void verifySafetyArea(int centerRow, int centerCol) {
        for (int i = Math.max(0, centerRow-1); i <= Math.min(9, centerRow+1); i++) {
            for (int j = Math.max(0, centerCol-1); j <= Math.min(9, centerCol+1); j++) {
                assertFalse(mineMap.isMined[i][j],
                        "安全区域(" + i + "," + j + ")不应该有地雷");
            }
        }
    }

    /**
     * 计算地雷数量的辅助方法
     */
    private int countMines(boolean[][] mineField) {
        int count = 0;
        for (boolean[] row : mineField) {
            for (boolean cell : row) {
                if (cell) count++;
            }
        }
        return count;
    }
}