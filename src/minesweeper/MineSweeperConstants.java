package minesweeper;

import java.awt.*;

/**
 * 为这个游戏定义全局通用的具名常量.
 * 在游戏后续的开发中，如果还需要其他的常量，都可以定义在这个类型中。
 */
public class MineSweeperConstants {
    /** 该游戏的行数 */
    public static final int ROWS = 10;
    /** 该游戏的列数 */
    public static final int COLS = 10;

    /** 该游戏的大小、颜色、选项等常量 */
    public static final int EASY_ROWS = 9;
    public static final int EASY_COLS = 9;
    public static final int EASY_MINES = 10;
    public static final int MEDIUM_ROWS = 16;
    public static final int MEDIUM_COLS = 16;
    public static final int MEDIUM_MINES = 40;
    public static final int DIFFICULT_ROWS = 16;
    public static final int DIFFICULT_COLS = 30;
    public static final int DIFFICULT_MINES = 99;
    public static final int EASY_CELL = 60;
    public static final int MEDIUM_CELL = 40;
    public static final int DIFFICULT_CELL = 30;
    public static final Color fieldColor = new Color(0xAF0000);//主界面颜色,暗红色
    public static final Color alarmMineColor = new Color(0x8080FF);//地雷警报色,浅蓝色
    public static final Font FONT_RESTMINE = new Font("Microsoft YaHei", Font.BOLD, 30);//雅黑字体,为了美观显示

    //颜色数组,方便后续直接使用
    public static final Color[] numberColor = {Color.BLUE,Color.GREEN,Color.CYAN,Color.ORANGE,Color.MAGENTA,Color.RED,Color.BLACK,Color.GRAY};

    //方便设计用户交互页面使用
    public static final String[] DIFFICULTY_OPTION = {
            "Easy  (9 X 9, 10 Mines)",
            "InterMediate  (16X16, 40 Mines)",
            "Difficult     (30X16, 99 Mines)"
    };
}