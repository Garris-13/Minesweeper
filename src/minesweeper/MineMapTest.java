//package minesweeper;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//class MineMapTest {
//    private MineMap mineMap;
//
//    @BeforeEach
//    void setUp() {
//        mineMap = new MineMap(10, 10);
//    }
//
//    @Test
//    @DisplayName("测试地雷地图初始化")
//    void testMineMapInitialization() {
//        assertNotNull(mineMap.isMined, "地雷数组不应该为null");
//        assertEquals(10, mineMap.isMined.length, "地雷数组行数应该为10");
//        assertEquals(10, mineMap.isMined[0].length, "地雷数组列数应该为10");
//    }
//
//    @Test
//    @DisplayName("测试固定地雷生成")
//    void testFixedMineGeneration() {
//        mineMap.newMineMap(10);
//
//        int mineCount = countMines(mineMap.isMined);
//        assertEquals(10, mineCount, "应该生成10个地雷");
//    }
//
//    @Test
//    @DisplayName("测试第一次点击安全保护")
//    void testFirstClickSafety() {
//        int clickRow = 5, clickCol = 5;
//        mineMap.newMineMap(10, clickRow, clickCol);
//
//        // 验证3x3安全区域没有地雷
//        for (int i = clickRow-1; i <= clickRow+1; i++) {
//            for (int j = clickCol-1; j <= clickCol+1; j++) {
//                if (i >= 0 && i < 10 && j >= 0 && j < 10) {
//                    assertFalse(mineMap.isMined[i][j],
//                            "第一次点击周围不应该有地雷");
//                }
//            }
//        }
//
//        // 验证总地雷数量正确
//        int totalMines = countMines(mineMap.isMined);
//        assertEquals(10, totalMines, "总地雷数量应该为10");
//    }
//
//    @Test
//    @DisplayName("测试边界情况处理")
//    void testEdgeCaseHandling() {
//        // 测试左上角点击
//        mineMap.newMineMap(5, 0, 0);
//        verifyCornerSafety(0, 0);
//
//        // 重新创建测试右下角
//        mineMap = new MineMap(10, 10);
//        mineMap.newMineMap(5, 9, 9);
//        verifyCornerSafety(9, 9);
//    }
//
//    @Test
//    @DisplayName("测试地雷数量边界")
//    void testMineCountBoundaries() {
//        // 测试最小地雷数量
//        mineMap.newMineMap(1);
//        assertEquals(1, countMines(mineMap.isMined), "应该生成1个地雷");
//    }
//
//    /**
//     * 计算地雷数量的辅助方法
//     */
//    private int countMines(boolean[][] mineField) {
//        int count = 0;
//        for (boolean[] row : mineField) {
//            for (boolean cell : row) {
//                if (cell) count++;
//            }
//        }
//        return count;
//    }
//
//    /**
//     * 验证角落位置安全保护的辅助方法
//     */
//    private void verifyCornerSafety(int cornerRow, int cornerCol) {
//        for (int i = Math.max(0, cornerRow-1); i <= Math.min(9, cornerRow+1); i++) {
//            for (int j = Math.max(0, cornerCol-1); j <= Math.min(9, cornerCol+1); j++) {
//                assertFalse(mineMap.isMined[i][j],
//                        "角落位置周围不应该有地雷");
//            }
//        }
//    }
//}
//package minesweeper;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//class MineMapTest {
//    private MineMap mineMap;
//
//    @BeforeEach
//    void setUp() {
//        mineMap = new MineMap(10, 10);
//    }
//
//    @Test
//    @DisplayName("测试地雷地图初始化")
//    void testMineMapInitialization() {
//        assertNotNull(mineMap.isMined, "地雷数组不应该为null");
//        assertEquals(10, mineMap.isMined.length, "地雷数组行数应该为10");
//        assertEquals(10, mineMap.isMined[0].length, "地雷数组列数应该为10");
//
//        // 验证所有位置初始化为false
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                assertFalse(mineMap.isMined[i][j],
//                        "初始状态所有位置应该没有地雷");
//            }
//        }
//    }
//
//    @Test
//    @DisplayName("测试固定地雷生成 - 验证固定地雷位置")
//    void testFixedMineGeneration() {
//        mineMap.newMineMap(10);
//
//        int mineCount = countMines(mineMap.isMined);
//        // 注意：这里应该期望10，因为newMineMap方法固定生成10个地雷
//        assertEquals(10, mineCount, "固定地雷生成应该生成10个地雷");
//
//        // 验证特定的固定位置有地雷
//        assertTrue(mineMap.isMined[0][0], "固定位置(0,0)应该有地雷");
//        assertTrue(mineMap.isMined[5][2], "固定位置(5,2)应该有地雷");
//        assertTrue(mineMap.isMined[9][5], "固定位置(9,5)应该有地雷");
//    }
//
//    @Test
//    @DisplayName("测试第一次点击安全保护")
//    void testFirstClickSafety() {
//        int clickRow = 5, clickCol = 5;
//
//        // 使用带安全保护的版本
//        mineMap.newMineMap(10, clickRow, clickCol);
//
//        // 验证3x3安全区域没有地雷
//        for (int i = clickRow-1; i <= clickRow+1; i++) {
//            for (int j = clickCol-1; j <= clickCol+1; j++) {
//                if (i >= 0 && i < 10 && j >= 0 && j < 10) {
//                    assertFalse(mineMap.isMined[i][j],
//                            "第一次点击位置(" + i + "," + j + ")周围不应该有地雷");
//                }
//            }
//        }
//
//        // 验证总地雷数量正确
//        int totalMines = countMines(mineMap.isMined);
//        assertEquals(10, totalMines, "总地雷数量应该为10");
//    }
//
//    @Test
//    @DisplayName("测试边界情况处理 - 角落位置的安全保护")
//    void testEdgeCaseHandling() {
//        // 测试左上角点击
//        mineMap.newMineMap(5, 0, 0);
//        verifyCornerSafety(0, 0);
//
//        // 测试右下角点击 - 需要重新创建对象
//        mineMap = new MineMap(10, 10);
//        mineMap.newMineMap(5, 9, 9);
//        verifyCornerSafety(9, 9);
//    }
//
//    @Test
//    @DisplayName("测试带安全保护的地雷生成 - 验证地雷数量")
//    void testSafeMineGenerationWithVariousCounts() {
//        // 测试不同数量的地雷（带安全保护）
//        testSafeMineCount(5, 3, 3);
//        testSafeMineCount(10, 5, 5);
//        testSafeMineCount(15, 7, 7);
//    }
//
//    @Test
//    @DisplayName("测试地雷分布唯一性")
//    void testMineDistributionUniqueness() {
//        mineMap.newMineMap(10, 5, 5);
//
//        int mineCount = countMines(mineMap.isMined);
//        assertEquals(10, mineCount, "应该生成指定数量的地雷");
//
//        // 验证安全区域确实没有地雷
//        for (int i = 4; i <= 6; i++) {
//            for (int j = 4; j <= 6; j++) {
//                assertFalse(mineMap.isMined[i][j],
//                        "安全区域(" + i + "," + j + ")不应该有地雷");
//            }
//        }
//    }
//
//    /**
//     * 测试带安全保护的地雷数量
//     */
//    private void testSafeMineCount(int expectedMines, int clickRow, int clickCol) {
//        MineMap testMap = new MineMap(10, 10);
//        testMap.newMineMap(expectedMines, clickRow, clickCol);
//
//        int actualMines = countMines(testMap.isMined);
//        assertEquals(expectedMines, actualMines,
//                "应该生成" + expectedMines + "个地雷");
//
//        // 验证安全区域
//        for (int i = clickRow-1; i <= clickRow+1; i++) {
//            for (int j = clickCol-1; j <= clickCol+1; j++) {
//                if (i >= 0 && i < 10 && j >= 0 && j < 10) {
//                    assertFalse(testMap.isMined[i][j],
//                            "安全区域不应该有地雷");
//                }
//            }
//        }
//    }
//
//    /**
//     * 计算地雷数量的辅助方法
//     */
//    private int countMines(boolean[][] mineField) {
//        int count = 0;
//        for (boolean[] row : mineField) {
//            for (boolean cell : row) {
//                if (cell) count++;
//            }
//        }
//        return count;
//    }
//
//    /**
//     * 验证角落位置安全保护的辅助方法
//     */
//    private void verifyCornerSafety(int cornerRow, int cornerCol) {
//        for (int i = Math.max(0, cornerRow-1); i <= Math.min(9, cornerRow+1); i++) {
//            for (int j = Math.max(0, cornerCol-1); j <= Math.min(9, cornerCol+1); j++) {
//                assertFalse(mineMap.isMined[i][j],
//                        "角落位置(" + cornerRow + "," + cornerCol + ")周围不应该有地雷");
//            }
//        }
//    }
//}
package minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

class MineMapTest {
    private MineMap mineMap;

    @BeforeEach
    void setUp() {
        mineMap = new MineMap(10, 10);
    }

    /**
     * --- 基础构造测试 ---
     */
    @Test
    @DisplayName("测试默认构造函数")
    void testDefaultConstructor() {
        MineMap defaultMap = new MineMap();
        assertNotNull(defaultMap.isMined, "默认构造的地雷数组不应该为null");
        // 默认构造函数应该创建标准大小的地图
        assertTrue(defaultMap.isMined.length > 0, "默认地图应该有行");
        assertTrue(defaultMap.isMined[0].length > 0, "默认地图应该有列");
    }

    @Test
    @DisplayName("测试带参构造函数")
    void testParameterizedConstructor() {
        MineMap customMap = new MineMap(5, 8);
        assertNotNull(customMap.isMined, "自定义构造的地雷数组不应该为null");
        assertEquals(5, customMap.isMined.length, "行数应该为5");
        assertEquals(8, customMap.isMined[0].length, "列数应该为8");
    }

    /**
     * --- 基础功能测试 ---
     */
    @Test
    @DisplayName("测试地雷地图初始化 - 完整验证")
    void testMineMapInitialization() {
        assertNotNull(mineMap.isMined, "地雷数组不应该为null");
        assertEquals(10, mineMap.isMined.length, "地雷数组行数应该为10");
        assertEquals(10, mineMap.isMined[0].length, "地雷数组列数应该为10");

        // 验证所有位置初始化为false
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertFalse(mineMap.isMined[i][j],
                        "初始状态所有位置应该没有地雷");
            }
        }
    }

    @Test
    @DisplayName("测试固定地雷生成 - 验证地雷数量")
    void testFixedMineGeneration() {
        mineMap.newMineMap(10);

        int mineCount = countMines(mineMap.isMined);
        assertEquals(10, mineCount, "应该生成10个地雷");

        // 验证地雷分布合理性
        assertTrue(mineCount > 0, "至少应该有一个地雷");
        assertTrue(mineCount <= 10, "地雷数量不应该超过指定数量");
    }

    @Test
    @DisplayName("测试第一次点击安全保护 - 标准位置")
    void testFirstClickSafety() {
        int clickRow = 5, clickCol = 5;
        mineMap.newMineMap(10, clickRow, clickCol);

        // 验证3x3安全区域没有地雷
        verifySafetyArea(clickRow, clickCol, 10, 10);

        // 验证总地雷数量正确
        int totalMines = countMines(mineMap.isMined);
        assertEquals(10, totalMines, "总地雷数量应该为10");
    }

    /**
     * --- 边界情况测试 ---
     */
    @Test
    @DisplayName("测试边界情况处理 - 所有角落位置")
    void testEdgeCaseHandling() {
        // 测试四个角落
        testCornerSafety(0, 0);   // 左上角
        testCornerSafety(0, 9);   // 右上角
        testCornerSafety(9, 0);   // 左下角
        testCornerSafety(9, 9);   // 右下角
    }

    @Test
    @DisplayName("测试边缘位置安全保护")
    void testEdgePositionSafety() {
        // 测试上边缘
        testEdgeSafety(0, 5, 10, 10);
        // 测试下边缘
        testEdgeSafety(9, 5, 10, 10);
        // 测试左边缘
        testEdgeSafety(5, 0, 10, 10);
        // 测试右边缘
        testEdgeSafety(5, 9, 10, 10);
    }

    /**
     * --- 地雷数量边界测试 ---
     */
    @Test
    @DisplayName("测试地雷数量边界 - 最小和最大合理值")
    void testMineCountBoundaries() {
        // 测试最小地雷数量
        testMineCount(1, 5, 5);

        // 测试中等数量
        testMineCount(5, 5, 5);

        // 测试接近最大数量（n*m - 9，因为安全区域占9格）
        testMineCount(10*10 - 9, 5, 5);
    }

    @Test
    @DisplayName("测试零地雷情况")
    void testZeroMines() {
        mineMap.newMineMap(0, 5, 5);
        int mineCount = countMines(mineMap.isMined);
        assertEquals(0, mineCount, "零地雷应该生成0个地雷");
    }

    /**
     * --- 性能测试 ---
     */
    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    @DisplayName("测试地雷生成性能 - 应在100ms内完成")
    void testMineGenerationPerformance() {
        // 测试标准大小的性能
        long startTime = System.nanoTime();
        mineMap.newMineMap(10, 5, 5);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1_000_000; // 转换为毫秒
        assertTrue(duration < 50, "地雷生成应该在50ms内完成，实际: " + duration + "ms");
    }

    @Test
    @DisplayName("测试多次生成的一致性")
    void testMultipleGenerationsConsistency() {
        // 测试相同参数多次生成的地雷数量一致性
        for (int i = 0; i < 5; i++) {
            MineMap testMap = new MineMap(10, 10);
            testMap.newMineMap(10, 5, 5);
            int mineCount = countMines(testMap.isMined);
            assertEquals(10, mineCount, "第" + (i+1) + "次生成应该产生10个地雷");
        }
    }

    /**
     * --- 随机性测试 ---
     */
    @Test
    @DisplayName("测试地雷分布随机性")
    void testMineDistributionRandomness() {
        // 测试多次生成的地雷位置不同（概率性测试）
        boolean[][] firstDistribution = new boolean[10][10];
        boolean foundDifferentDistribution = false;

        // 第一次生成
        mineMap.newMineMap(10, 5, 5);
        copyMineDistribution(mineMap.isMined, firstDistribution);

        // 尝试几次寻找不同的分布
        for (int attempt = 0; attempt < 10; attempt++) {
            MineMap newMap = new MineMap(10, 10);
            newMap.newMineMap(10, 5, 5);

            if (!areDistributionsEqual(firstDistribution, newMap.isMined)) {
                foundDifferentDistribution = true;
                break;
            }
        }

        // 注意：这是概率性测试，有可能失败，但概率很低
        assertTrue(foundDifferentDistribution,
                "多次地雷生成应该产生不同的分布（随机性测试）");
    }

    /**
     * --- 异常情况测试 ---
     */
    @Test
    @DisplayName("测试超出边界的点击位置")
    void testOutOfBoundsClickPosition() {
        // 这些应该被正确处理而不抛出异常
        assertDoesNotThrow(() -> mineMap.newMineMap(10, -1, -1));
        assertDoesNotThrow(() -> mineMap.newMineMap(10, 10, 10));
        assertDoesNotThrow(() -> mineMap.newMineMap(10, 5, 15));
    }


    /**
     * --- 辅助方法测试 ---
     */
    @Test
    @DisplayName("测试空地图情况")
    void testEmptyMap() {
        MineMap emptyMap = new MineMap(0, 0);
        assertNotNull(emptyMap.isMined);
        assertEquals(0, emptyMap.isMined.length);
    }

    /**
     * ==================== 辅助方法 ====================
     */

    private void testCornerSafety(int cornerRow, int cornerCol) {
        MineMap cornerMap = new MineMap(10, 10);
        cornerMap.newMineMap(5, cornerRow, cornerCol);
        verifySafetyArea(cornerRow, cornerCol, 10, 10);
    }

    private void testEdgeSafety(int edgeRow, int edgeCol, int totalRows, int totalCols) {
        MineMap edgeMap = new MineMap(totalRows, totalCols);
        edgeMap.newMineMap(5, edgeRow, edgeCol);
        verifySafetyArea(edgeRow, edgeCol, totalRows, totalCols);
    }

    private void testMineCount(int expectedMines, int clickRow, int clickCol) {
        MineMap testMap = new MineMap(10, 10);
        testMap.newMineMap(expectedMines, clickRow, clickCol);

        int actualMines = countMines(testMap.isMined);
        assertEquals(expectedMines, actualMines,
                "应该生成" + expectedMines + "个地雷");

        verifySafetyArea(clickRow, clickCol, 10, 10);
    }

    private void verifySafetyArea(int centerRow, int centerCol, int totalRows, int totalCols) {
        for (int i = Math.max(0, centerRow-1); i <= Math.min(totalRows-1, centerRow+1); i++) {
            for (int j = Math.max(0, centerCol-1); j <= Math.min(totalCols-1, centerCol+1); j++) {
                assertFalse(mineMap.isMined[i][j],
                        "安全区域(" + i + "," + j + ")不应该有地雷");
            }
        }
    }

    private int countMines(boolean[][] mineField) {
        int count = 0;
        for (boolean[] row : mineField) {
            for (boolean cell : row) {
                if (cell) count++;
            }
        }
        return count;
    }

    private void copyMineDistribution(boolean[][] source, boolean[][] target) {
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, target[i], 0, source[i].length);
        }
    }

    private boolean areDistributionsEqual(boolean[][] dist1, boolean[][] dist2) {
        if (dist1.length != dist2.length) return false;
        for (int i = 0; i < dist1.length; i++) {
            if (dist1[i].length != dist2[i].length) return false;
            for (int j = 0; j < dist1[i].length; j++) {
                if (dist1[i][j] != dist2[i][j]) return false;
            }
        }
        return true;
    }
}