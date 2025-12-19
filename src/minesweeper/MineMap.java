package minesweeper;

// “import static”的导入方式可以让我们的代码在后续引入常量ROWS的时候，不用使用MineSweeperConstants.ROWS
// 而是直接使用简化方式：ROWS

import java.util.Random;

import static minesweeper.MineSweeperConstants.ROWS;
import static minesweeper.MineSweeperConstants.COLS;
/**
 * 这个类型的目的是为了生成当前游戏中地雷的分布位置，形象的用Map来命名
 */
public class MineMap {
    int numMines;
    boolean[][] isMined;

    public MineMap()
    {
        isMined = new boolean[10][10];
    }

    public MineMap(int boardRow, int boardCol)
    {
        isMined = new boolean[boardRow][boardCol];
    }

    // 在后续的应用完善中，行列数不应该是固定的，而是可以定制的。
    public void newMineMap(int numMines) {
        this.numMines = numMines;
        // 下面的这种方式是一种示例，它把地雷固定了，作为游戏，显然地雷的分布应该是随机的
        // 所以在后续的应用完善中，应该修改下面的代码

        isMined[0][0] = true;
        isMined[5][2] = true;
        isMined[9][5] = true;
        isMined[6][7] = true;
        isMined[8][2] = true;
        isMined[2][4] = true;
        isMined[5][7] = true;
        isMined[7][7] = true;
        isMined[3][6] = true;
        isMined[4][8] = true;
    }

    //随机布置地雷
    public void newMineMap(int numMines, int firstClickRow, int firstClickCol)
    {
        //利用随机数来埋雷
        Random random = new Random();
        int numMinesPlaced = 0;
        this.numMines = numMines;
        //循坏埋雷直至达到预期数量且新雷的位置符合题目条件
        while (numMinesPlaced < this.numMines) {
            int row = random.nextInt(isMined.length);
            int col = random.nextInt(isMined[0].length);
            //确定随机埋的类不在初始3x3的范围内
            boolean nearFirstClick = Math.abs(row - firstClickRow) <= 1 && Math.abs(col - firstClickCol) <= 1;
            if (!isMined[row][col] && !nearFirstClick) {
                isMined[row][col] = true;
                numMinesPlaced++;
            }
        }
    }
}