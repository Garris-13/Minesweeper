package minesweeper;

public class GameBoardPanelTestAdapter extends GameBoardPanel {

    public GameBoardPanelTestAdapter(MineSweeperMain controller, int rows, int cols, int mines) {
        super(controller, rows, cols, mines);

        // 禁用动画（避免异步导致测试不稳定）
        animTimer.stop();
    }

    /** 递归 reveal 时禁用延迟，使其同步执行 */
    @Override
    void revealCell(int r, int c) {
        super.cells[r][c].isRevealed = true;
        int count = getSurroundingMines(r, c);

        if (count > 0) {
            cells[r][c].setText(Integer.toString(count));
            return;
        }

        // 直接同步递归（无 Timer）
        for (int i = Math.max(0, r - 1); i <= Math.min(boardRowSize - 1, r + 1); i++) {
            for (int j = Math.max(0, c - 1); j <= Math.min(boardColSize - 1, c + 1); j++) {
                if (!cells[i][j].isRevealed && !cells[i][j].isFlagged) {
                    revealCell(i, j);
                }
            }
        }
    }
}
