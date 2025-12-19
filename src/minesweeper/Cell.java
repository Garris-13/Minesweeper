package minesweeper;
import java.awt.*;
import javax.swing.JButton;
/**
 *   类是用来建模挖雷游戏的单元格，它具有显示功能、具有对鼠标左键和右键点击的响应能力。故将这个单元格创建为javax.swing.Button的子类类型。
 *   这样做的好处就是可以拥有JButton类型对象的所有功能，同时还可以存储与地雷游戏相关的属性，比如这个单元格的行列号（用于定位）
 *   和这个单元格的状态（是否包含有地雷，是否已经被暴露等）
 *   要体会面向对象通过继承将代码复用的做法。
 */
public class Cell extends JButton {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // 为Cell定义其父类可以根据不同状态而采用的配色方案所使用的颜色常量，方便使用。
    public static final Color BG_NOT_REVEALED =new Color(0X0808080);
    public static final Color FG_NOT_REVEALED = Color.RED;    // flag, mines
    public static final Color BG_REVEALED = Color.DARK_GRAY;
    public static final Color FG_REVEALED = Color.YELLOW; // number of mines
    public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

    /** The row and column number of the cell */
    int row, col;
    /** Already revealed? */
    boolean isRevealed;
    /** Is a mine? */
    boolean isMined;
    /** Is Flagged by player? */
    boolean isFlagged;


    /** Constructor */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        super.setMargin(new Insets(0,0,0,0));//移除按钮的默认内边距，使按钮内容（如图标/文字）紧贴边缘显示
        // 设置JButton所使用的字体
        super.setFont(FONT_NUMBERS);
    }

    /** 如果重新开始一局游戏，可以使用这个方法为当前的Cell对象进行所有属性值的初始化。 */
    public void newGame(boolean isMined) {
        this.isRevealed = false; // default
        this.isFlagged = false;  // default
        this.isMined = isMined;  // given
        super.setEnabled(true);  // enable button
        super.setText("");       // display blank
        paint();
    }

    /** 对Cell外观的绘制就是在这个方法里实现 */
    public void paint() {
        super.setForeground(isRevealed? FG_REVEALED: FG_NOT_REVEALED);
        super.setBackground(isRevealed? BG_REVEALED: BG_NOT_REVEALED);
    }

    // 返回当前Cell是否已被翻开
    public boolean isRevealed() {
        return isRevealed;
    }

    // 返回当前Cell是否是雷
    public boolean isMine() {
        return isMined;
    }

    // 翻开Cell
    public void reveal() {
        isRevealed = true;
        paint();  // 更新外观
    }



}



