package minesweeper;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

import static minesweeper.MineSweeperConstants.*;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // 定义每一个单元格的大小（单位为像素）
    public static final int CELL_SIZE = 60;
    public static final int CANVAS_WIDTH = CELL_SIZE * COLS; // Game board width/height
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;


    /**游戏的整个界面面板应该包含的单元格数量是：ROWS*COLS*/
    Cell[][] cells;

    /**地雷的数量，在后续的应用改善中，地雷的数量应该随面板的大小，随游戏的难度级数而不同，不应该像下面的代码这样被固定化了。*/
    int numMines;

    /**游戏开始第一次点击标示，第一次点击后才开始布雷，并启动计时。*/
    boolean bFirstClick = true;

    //游戏面板的大小，由用户开始选择游戏难度时确定。
    int boardRowSize;
    int boardColSize;

    //游戏控制主界面引用，操作控制面板的显示控件，计时器操作，游戏进程。
    MineSweeperMain controller;

    //标记剩余的雷数。
    int numRestMine;

    //导入地雷及旗帜图标。
    ImageIcon flagIcon = new ImageIcon(".//minesweeper//src//minesweeper//flag2.jpg");
    ImageIcon mineIcon = new ImageIcon(".//minesweeper//src//minesweeper//mine.jpg");


    //使用动画
    Timer animTimer;
    java.util.List<Animation> animations = new ArrayList<>();

    //构造函数，生成布局
    public GameBoardPanel(MineSweeperMain controller,int rowSize, int colSize,int numMines) {
        this.controller = controller;
        this.numMines = numMines;
        this.numRestMine = numMines;
        boardRowSize = rowSize;
        boardColSize = colSize;
        cells = new Cell[boardRowSize][boardColSize];
        super.setLayout(new GridLayout(boardRowSize, boardColSize, 2, 2));  // JPanel

        // 将每一个Cell单元格对象加入的面板中.
        for (int row = 0; row < boardRowSize; ++row) {
            for (int col = 0; col < boardColSize; ++col) {
                cells[row][col] = new Cell(row, col);
                cells[row][col].paint();
                super.add(cells[row][col]);
            }
        }

        // [TODO 3] 创建一个MouseEventListener对象，所有的Cell对象都共用这一个事件监听器对象。
        CellMouseListener cellmouselistener = new CellMouseListener();

        // [TODO 4] 将每一个Cell对象都加入到事件监听器中。
        for (int row = 0; row < boardRowSize; row++)
            for (int col = 0; col < boardColSize; col++)
                cells[row][col].addMouseListener(cellmouselistener);

        int currSize = EASY_CELL;
        switch (numMines)
        {
            case EASY_MINES:
                currSize = EASY_CELL;
                break;
            case MEDIUM_MINES:
                currSize = MEDIUM_CELL;
                break;
            case DIFFICULT_MINES:
                currSize = DIFFICULT_CELL;
        }
        super.setPreferredSize(new Dimension(currSize*boardColSize, currSize*boardRowSize));//设置合适的大小,根据难度定
        flagIcon.setImage(flagIcon.getImage().getScaledInstance(currSize,currSize,currSize));
        mineIcon.setImage(mineIcon.getImage().getScaledInstance(currSize,currSize,currSize));


        animTimer = new Timer(16, e ->{
            boolean needsRepaint = false;
            synchronized (animations)//确保线程的同步异步安全
            {
                for (int i = animations.size() - 1; i >= 0; i--)//逆向遍历与移除
                {
                    Animation anim = animations.get(i);
                    if (anim.update())//仅有动画更新时才进行重新绘制
                        needsRepaint = true;
                    else animations.remove(i);
                }
            }
            if (needsRepaint)
                repaint();
        });
        animTimer.start();
    }


    // 初始化一个新游戏所需要调用的方法
    public void newGame() {
        // 首先获得一个地雷分布地图对象
        MineMap mineMap = new MineMap();
        mineMap.newMineMap(numMines);
        bFirstClick = true;
        numRestMine = numMines;
        controller.updateMineCounter(numRestMine);

        // 根据地雷地图中的数据，将每一个Cell对象按照初始的状态进行绘制
        for (int row = 0; row < boardRowSize; row++) {
            for (int col = 0; col < boardColSize; col++) {
                cells[row][col].newGame(mineMap.isMined[row][col]);
            }
        }
    }

    //初始化一个新游戏所需要调用的方法,点击的位置及周围不生成地雷
    public void newGame(int boardRow, int boardCol, int clickRow, int clickCol) {
        // 首先获得一个地雷分布地图对象
        MineMap mineMap = new MineMap(boardRow, boardCol);
        mineMap.newMineMap(numMines, clickRow, clickCol);
        bFirstClick = true;
        numRestMine = numMines;
        controller.updateMineCounter(numRestMine);

        // 根据地雷地图中的数据，将每一个Cell对象按照初始的状态进行绘制
        for (int row = 0; row < boardRowSize; row++) {
            for (int col = 0; col < boardColSize; col++) {
                cells[row][col].newGame(mineMap.isMined[row][col]);
            }
        }
    }

//    // 这个方法用来对位置为（srcRow,srcCol）的单元格的8个邻居统计地雷数量，并将这个数量作为函数的返回值
//    public int getSurroundingMines(int srcRow, int srcCol) {
//        int numMines = 0;
//        // [TODO 8] 实现统计周边地雷数量的代码，注意边界的单元格并不是有8个邻居。
//        for (int i = Math.max(0, srcRow - 1); i <= Math.min(boardRowSize - 1, srcRow + 1); i++)
//            for (int j = Math.max(0, srcCol - 1); j <= Math.min(boardColSize - 1, srcCol + 1); j++)
//                if (cells[i][j].isMined)
//                    numMines++;
//        return numMines;
//    }
    // 在 GameBoardPanel.java 中修复这个方法
public int getSurroundingMines(int srcRow, int srcCol) {
        int numMines = 0;
        for (int i = Math.max(0, srcRow - 1); i <= Math.min(boardRowSize - 1, srcRow + 1); i++) {
            for (int j = Math.max(0, srcCol - 1); j <= Math.min(boardColSize - 1, srcCol + 1); j++) {
                // 排除当前位置本身，只计算周围的8个邻居
                if (!(i == srcRow && j == srcCol) && cells[i][j].isMined) {
                    numMines++;
                }
            }
        }
        return numMines;
    }


    // 位置为(srcRow, srcCol)的单元格执行打开操作（Reveal）
    // 如果打开的这个单元格的地雷数量是0，那么游戏必须要递归地将8个邻居中地雷数量为0的单元格依次打开。
//    private void revealCell(int srcRow, int srcCol) {
//        // Color[] c = {Color.BLUE,Color.GREEN,Color.CYAN,Color.ORANGE,Color.MAGENTA,Color.RED,Color.BLACK,Color.GRAY};
//
//        if (cells[srcRow][srcCol].isRevealed || cells[srcRow][srcCol].isFlagged)
//            return;
//        cells[srcRow][srcCol].isRevealed = true;
//        //cells[srcRow][srcCol].setBackground(Color.DARK_GRAY);
//        cells[srcRow][srcCol].paint();
//        cells[srcRow][srcCol].setEnabled(false);//防止重复点击
//
//        int numMines = getSurroundingMines(srcRow, srcCol);
//        // [TODO 9] 实现将地雷数为零的单元格打开并递归地将其8个邻居中地雷数量为零的单元格依次打开。
//        if (numMines > 0) {
//            //cells[srcRow][srcCol].setEnabled(true);
//            cells[srcRow][srcCol].setForeground(numberColor[numMines-1]);//用不同颜色的来标记数量,美观考虑
//            cells[srcRow][srcCol].setEnabled(true);
//            cells[srcRow][srcCol].setFocusable(false);//避免单元格获得焦点后显示边框，保持界面整洁
//            cells[srcRow][srcCol].setText(Integer.toString(numMines));
//            cells[srcRow][srcCol].setFont(new Font("Arial", Font.BOLD, 20));
//        } else {
//            for (int i = Math.max(0, srcRow - 1); i <= Math.min(boardRowSize - 1, srcRow + 1); i++)
//                for (int j = Math.max(0, srcCol - 1); j <= Math.min(boardColSize - 1, srcCol + 1); j++)
//                    if (!cells[i][j].isRevealed && !cells[i][j].isFlagged)
//                        revealCell(i, j);
//        }
//        cells[srcRow][srcCol].setIcon(null);
//    }


    void revealCell(int srcRow, int srcCol) {
        // Color[] c = {Color.BLUE,Color.GREEN,Color.CYAN,Color.ORANGE,Color.MAGENTA,Color.RED,Color.BLACK,Color.GRAY};

        if (cells[srcRow][srcCol].isRevealed || cells[srcRow][srcCol].isFlagged)
            return;
        cells[srcRow][srcCol].isRevealed = true;
        //cells[srcRow][srcCol].setBackground(Color.DARK_GRAY);
        cells[srcRow][srcCol].paint();
        cells[srcRow][srcCol].setEnabled(false);

        int numMines = getSurroundingMines(srcRow, srcCol);
        // [TODO 9] 实现将地雷数为零的单元格打开并递归地将其8个邻居中地雷数量为零的单元格依次打开。
        if (numMines > 0) {
            //cells[srcRow][srcCol].setEnabled(true);
            cells[srcRow][srcCol].setForeground(numberColor[numMines-1]);//不同的雷数有不同的颜色,与静态颜色数组里的顺序一致
            //cells[srcRow][srcCol].setEnabled(true);
            cells[srcRow][srcCol].setFocusable(false);//失去焦点保证图片无边缘
            cells[srcRow][srcCol].setText(Integer.toString(numMines));//显示数字

            //动画
            final float scale = 0.5f;
            cells[srcRow][srcCol].setFont(getFont().deriveFont(Font.BOLD, 20 * scale));//设置字体(美观考虑)
            animations.add(new Animation(100, t -> {
                float f = scale + (1 - scale) * t;
                cells[srcRow][srcCol].setFont(getFont().deriveFont(Font.BOLD, 20 * f));
            }));
        } else {
            for (int i = Math.max(0, srcRow - 1); i <= Math.min(boardRowSize - 1, srcRow + 1); i++) {
                for (int j = Math.max(0, srcCol - 1); j <= Math.min(boardColSize - 1, srcCol + 1); j++) {
                    if (!cells[i][j].isRevealed && !cells[i][j].isFlagged) {
                        final int delay = (Math.abs(i - srcRow) + Math.abs(j - srcCol)) * 30;
                        int finalI = i;
                        int finalJ = j;
                        Timer delayTimer = new Timer(delay, e -> {
                            revealCell(finalI, finalJ);//递归调用
                        });
                        delayTimer.setRepeats(false);
                        delayTimer.start();

                    }
                }
            }
        }
        cells[srcRow][srcCol].setIcon(null);
    }

     //如果玩家将所有的没有地雷的单元格打开，那么就判断该玩家赢了比赛，返回true。
//    public boolean hasWon() {
//        // [TODO 10] 判断玩家是否赢得了比赛
//        int numRevealed = 0;
//        for (int i = 0; i < boardRowSize; i++)
//            for (int j = 0; j < boardColSize; j++)
//                if (cells[i][j].isRevealed)
//                    numRevealed++;
//        return (numRevealed == boardRowSize * boardColSize - numMines);
//    }
    // 在 GameBoardPanel.java 中检查这个方法
    public boolean hasWon() {
        int numRevealed = 0;
        for (int i = 0; i < boardRowSize; i++) {
            for (int j = 0; j < boardColSize; j++) {
                if (cells[i][j].isRevealed) {
                    numRevealed++;
                }
            }
        }
        return (numRevealed == boardRowSize * boardColSize - numMines);
    }


    // [TODO 2] 使用内部类的方式实现监听器类型，这个监听器用来监听对每一个单元格的鼠标点击事件（尤其是要区分鼠标左键单击和鼠标右键单击）这个监听器类创建的并不完整

    class CellMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {         // Get the source object that fired the Event
            Cell sourceCell = (Cell) e.getSource();
            sourceCell.setFocusable(false);
            // 鼠标左键单击单元格的功能是打开（Reveal）单元格；
            // 鼠标右键单击单元格的功能是给单元格添加/删除旗帜标记，这个标记的作用是用来标记地雷。
            if (e.getButton() == MouseEvent.BUTTON1) {  // 鼠标左键单击
                // [TODO 5]  如果当前单元格里面有地雷，那么游戏结束；如果当前单元格里没有地雷，那么就执行对该单元格的打开（Reveal）操作。
                if (bFirstClick) {
                    newGame(boardRowSize, boardColSize, sourceCell.row, sourceCell.col);
                    bFirstClick = false;
                    controller.startTimer();
                }
                //若点击到雷,则展示所有雷并结束游戏
                if (sourceCell.isMined) {
                    for (int i = 0; i < boardRowSize; i++)
                        for (int j = 0; j < boardColSize; j++) {
                            if (cells[i][j].isMined)
                                cells[i][j].setIcon(mineIcon);
//                                cells[i][j].setText("X");
                            //cells[i][j].setEnabled(true);
                        }
                    controller.GameOver(false);
                } else revealCell(sourceCell.row, sourceCell.col);

            } else if (e.getButton() == MouseEvent.BUTTON3) { // 鼠标右键单击
                // [TODO 6] 如果当前单元格已经有旗帜标记，那么就删除掉这个旗帜标记；如果当前单元格没有旗帜标记，那么就添加旗帜标记。
                if (!sourceCell.isRevealed) {
                    if (sourceCell.isFlagged) {
                        sourceCell.isFlagged = false;
                        sourceCell.setEnabled(true);
                        sourceCell.setIcon(null);
                        sourceCell.setText("");
                        numRestMine++;
                    } else {
                        sourceCell.isFlagged = true;
                        sourceCell.setEnabled(true);
                        sourceCell.setIcon(flagIcon);
                        //                       sourceCell.setText("O");
                        numRestMine--;
                    }
                    controller.updateMineCounter(numRestMine);
                }
            }

            // [TODO 7] 在对一个单元格执行了打开操作之后，请判断一下玩家是否已经赢取了比赛。
            if (hasWon()) {
                for (int i = 0; i < boardRowSize; i++)
                    for (int j = 0; j < boardColSize; j++) {
                        if (!cells[i][j].isRevealed)
                            cells[i][j].setIcon(flagIcon);
                    }
                controller.updateMineCounter(0);
                controller.GameOver(true);
            }
        }
    }
}
