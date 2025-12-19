package minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    private Cell cell;

    @BeforeEach
    void setUp() {
        cell = new Cell(2, 3);
    }

    @Test
    @DisplayName("测试单元格构造和基础属性")
    void testCellConstruction() {
        assertEquals(2, cell.row);
        assertEquals(3, cell.col);
        assertNotNull(cell.getFont());
    }

    @Test
    @DisplayName("测试新游戏初始化 - 无地雷")
    void testNewGameWithoutMine() {
        cell.newGame(false);

        assertFalse(cell.isRevealed, "初始化后不应该被翻开");
        assertFalse(cell.isFlagged, "初始化后不应该被标记");
        assertFalse(cell.isMined, "初始化后不应该有地雷");
        assertTrue(cell.isEnabled(), "初始化后应该可用");
        assertEquals("", cell.getText(), "初始化后文本应该为空");
    }

    @Test
    @DisplayName("测试新游戏初始化 - 有地雷")
    void testNewGameWithMine() {
        cell.newGame(true);

        assertFalse(cell.isRevealed, "初始化后不应该被翻开");
        assertFalse(cell.isFlagged, "初始化后不应该被标记");
        assertTrue(cell.isMined, "初始化后应该有地雷");
        assertTrue(cell.isEnabled(), "初始化后应该可用");
    }

    @Test
    @DisplayName("测试标记功能")
    void testFlaggingFunctionality() {
        cell.newGame(false);

        // 测试标记单元格
        cell.isFlagged = true;
        assertTrue(cell.isFlagged, "单元格应该被标记");

        // 测试取消标记
        cell.isFlagged = false;
        assertFalse(cell.isFlagged, "单元格应该取消标记");
    }

    @Test
    @DisplayName("测试绘制方法")
    void testPaintMethod() {
        cell.newGame(false);

        // 测试未翻开状态
        cell.paint();
        assertEquals(Cell.FG_NOT_REVEALED, cell.getForeground(),
                "未翻开状态前景色应该正确");
        assertEquals(Cell.BG_NOT_REVEALED, cell.getBackground(),
                "未翻开状态背景色应该正确");

        // 测试翻开状态
        cell.isRevealed = true;
        cell.paint();
        assertEquals(Cell.FG_REVEALED, cell.getForeground(),
                "翻开状态前景色应该正确");
        assertEquals(Cell.BG_REVEALED, cell.getBackground(),
                "翻开状态背景色应该正确");
    }

    @Test
    @DisplayName("测试多次初始化")
    void testMultipleInitializations() {
        // 第一次初始化 - 有地雷
        cell.newGame(true);
        assertTrue(cell.isMined, "第一次初始化应该有地雷");

        // 第二次初始化 - 无地雷
        cell.newGame(false);
        assertFalse(cell.isMined, "第二次初始化应该无地雷");
        assertFalse(cell.isRevealed, "应该重置为未翻开状态");
        assertFalse(cell.isFlagged, "应该重置为未标记状态");
    }
}