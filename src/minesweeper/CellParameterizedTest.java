package minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

class CellParameterizedTest {
    private Cell cell;

    @BeforeEach
    void setUp() {
        cell = new Cell(0, 0);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "5, 5",
            "9, 9",
            "0, 9",
            "9, 0",
            "3, 7"
    })
    @DisplayName("测试不同位置的单元格构造")
    void testCellConstructionVariousPositions(int row, int col) {
        Cell testCell = new Cell(row, col);

        assertEquals(row, testCell.row, "行坐标应该匹配");
        assertEquals(col, testCell.col, "列坐标应该匹配");
        assertNotNull(testCell.getFont(), "字体不应该为null");
        assertEquals(Cell.FONT_NUMBERS, testCell.getFont(), "应该使用预定义字体");
    }

    @ParameterizedTest
    @CsvSource({
            "true, true, false",   // 有地雷，翻开，不标记
            "true, false, true",   // 有地雷，不翻开，标记
            "false, true, false",  // 无地雷，翻开，不标记
            "false, false, true",  // 无地雷，不翻开，标记
            "true, false, false",  // 有地雷，不翻开，不标记
            "false, true, true"    // 无地雷，翻开，标记（特殊情况）
    })
    @DisplayName("测试各种状态组合")
    void testVariousStateCombinations(boolean hasMine, boolean isRevealed, boolean isFlagged) {
        cell.newGame(hasMine);
        cell.isRevealed = isRevealed;
        cell.isFlagged = isFlagged;

        assertEquals(hasMine, cell.isMined, "地雷状态应该正确");
        assertEquals(isRevealed, cell.isRevealed, "翻开状态应该正确");
        assertEquals(isFlagged, cell.isFlagged, "标记状态应该正确");

        // 验证绘制方法在不同状态下的行为
        cell.paint();
        if (isRevealed) {
            assertEquals(Cell.FG_REVEALED, cell.getForeground(), "翻开状态前景色");
            assertEquals(Cell.BG_REVEALED, cell.getBackground(), "翻开状态背景色");
        } else {
            assertEquals(Cell.FG_NOT_REVEALED, cell.getForeground(), "未翻开状态前景色");
            assertEquals(Cell.BG_NOT_REVEALED, cell.getBackground(), "未翻开状态背景色");
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8})
    @DisplayName("测试周围地雷数字显示")
    void testSurroundingMineNumberDisplay(int mineCount) {
        cell.newGame(false);
        cell.isRevealed = true;

        // 模拟显示周围地雷数量
        if (mineCount > 0) {
            cell.setText(String.valueOf(mineCount));
            cell.setForeground(MineSweeperConstants.numberColor[mineCount - 1]);
        } else {
            cell.setText("");
        }

        if (mineCount > 0) {
            assertEquals(String.valueOf(mineCount), cell.getText(), "应该显示地雷数量");
            assertEquals(MineSweeperConstants.numberColor[mineCount - 1], cell.getForeground(),
                    "颜色应该匹配地雷数量");
        } else {
            assertEquals("", cell.getText(), "无地雷时文本应该为空");
        }
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 0, 0",   // 最小位置
            "5, 5, 5, 5",   // 正常位置
            "99, 99, 99, 99" // 大数字位置
    })
    @DisplayName("测试极端位置值的单元格")
    void testExtremePositionValues(int row1, int col1, int row2, int col2) {
        Cell cell1 = new Cell(row1, col1);
        Cell cell2 = new Cell(row2, col2);

        assertEquals(row1, cell1.row);
        assertEquals(col1, cell1.col);
        assertEquals(row2, cell2.row);
        assertEquals(col2, cell2.col);

        // 验证它们可以正常初始化
        cell1.newGame(true);
        cell2.newGame(false);

        assertTrue(cell1.isMined);
        assertFalse(cell2.isMined);
    }
}