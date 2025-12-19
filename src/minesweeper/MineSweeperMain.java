
package minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static minesweeper.MineSweeperConstants.*;
/**
 * 扫雷游戏的简单规则介绍：
 * 对单元格执行鼠标左键单击操作的功能是打开这个单元格；
 * 对单元格执行鼠标右键单击操作的功能是为这个单元格进行旗帜标记的添加或者删除；
 * 当所有的没有地雷的单元格被打开了，那么玩家就赢得了该轮游戏；
 * 如果有一个包含地雷的单元格被打开了，那么玩家就输掉了该轮游戏。
 */
public class MineSweeperMain extends JFrame{
    private static final long serialVersionUID = 1L;  // to prevent serial warning
    //状态条容器
    //JPanel statusPanel;

    //剩余地雷数标签、计时时间显示标签
    JLabel mineField;
    JLabel timeField;

    //表情标签，点击复位游戏
    JButton btnShow;

    //计时器变量
    Timer timer;

    //计时器时间计数
    int secondTime;

    //游戏当前选定难度的尺寸、地雷数
    int currRows;
    int currCols;
    int currMines;

    //游戏面板对象
    GameBoardPanel board;

    //开始新游戏按钮
    JButton btnNewGame;
    //应用程序图标

    //表情图标
    ImageIcon calmIcon = new ImageIcon(".//minesweeper//src//minesweeper//trophy.jpg");

    // Constructor to set up all the UI and game components
    public MineSweeperMain() {


        createMenuBar();
        showSelectDialog();
        setupGame();

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Minesweeper");
        setVisible(true);   // show it
    }

    //构建游戏组件，为了ResetGame功能将构建部件从构造函数提出
    public void setupGame()
    {
        //导入游戏图标
        ImageIcon appIcon = new ImageIcon(".//minesweeper//src//minesweeper//mine1.jpg");
        if (board != null)
            remove(board);
        if (timer != null)
            stopTimer();

        board = new GameBoardPanel(this,currRows,currCols,currMines);
        //btnNewGame = new JButton("New Game");
        btnNewGame = new JButton("New Game");
        Container cp = this.getContentPane();           // JFrame's content-pane
        setIconImage(appIcon.getImage());
        cp.setLayout(new BorderLayout()); // in 10x10 GridLayout
        cp.add(board, BorderLayout.CENTER);

        // [TODO 1] 可以将btnNewGame这个Button加入到该容器的南区（BorderLayout.SOUTH），在应用的后续完善中，还需要添加对这个按钮的事件监听器
        btnNewGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    dispose();//销毁资源
                    new MineSweeperMain();
                }
            }
        });
        cp.add(btnNewGame,BorderLayout.SOUTH);

        //状态条
        JPanel statusPanel = new JPanel(new BorderLayout());
        //剩余时间与剩余雷数数量标签
        //mineField = new JLabel("Rest Mine:    " + currMines,SwingConstants.LEFT);
        mineField = new JLabel(String.format("%03d",currMines),SwingConstants.RIGHT);
        mineField.setPreferredSize(new Dimension(65,30));
        mineField.setOpaque(true);//不透明度
        mineField.setBackground(Color.BLACK);
        mineField.setForeground(fieldColor);
        mineField.setFont(FONT_RESTMINE);


        //timeField = new JLabel("Time:     0",SwingConstants.LEFT);
        timeField = new JLabel(String.format("%04d",secondTime),SwingConstants.RIGHT);
        timeField.setPreferredSize(new Dimension(85,30));
        timeField.setOpaque(true);
        timeField.setBackground(Color.BLACK);
        timeField.setForeground(fieldColor);
        //timeField.setForeground(Color.RED);
        timeField.setFont(FONT_RESTMINE);

        btnShow = new JButton("");
        btnShow.setPreferredSize(new Dimension(35,30));//调整大小使显示美观
        //将图片的大小设置为与容器适配
        calmIcon.setImage(calmIcon.getImage().getScaledInstance(30,30,30));

        btnShow.setBackground(new Color(0xD8D8D8));
        btnShow.setIcon(calmIcon);
        btnShow.setFocusPainted(false);//失去焦点保证图片无边缘
        btnShow.addActionListener(e->resetGame());//鼠标点击可复位游戏  s

        //创建一个具有 "蚀刻" 效果的边框，模拟被雕刻或压印的外观
        mineField.setBorder(BorderFactory.createEtchedBorder());
        timeField.setBorder(BorderFactory.createEtchedBorder());

        statusPanel.add(mineField,BorderLayout.WEST);
        statusPanel.add(timeField,BorderLayout.EAST);
        statusPanel.add(btnShow,BorderLayout.CENTER);
        cp.add(statusPanel,BorderLayout.NORTH);

        //计时器对象,由事件触发
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondTime++;
                //设置时间上限
                if (secondTime >= 9999)
                    secondTime = 9999;
                //timeField.setText("Time:     " + secondTime);
                timeField.setText(String.format("%04d",secondTime));
            }
        });
    }
    public void startTimer()
    {
        secondTime = 0;
        //timeField.setText("Time:     0");
        timeField.setText(String.format("%04d",secondTime));
        timer.start();
    }

    public void stopTimer()
    {
        timer.stop();
    }

    public void updateMineCounter(int numRest) {
        //mineField.setText("Rest Mine:    " + numRest);
        mineField.setText(String.format("%03d", numRest));
        //数量错误
        if (numRest < 0)
            mineField.setForeground(Color.YELLOW);
        else mineField.setForeground(fieldColor);
    }

    public void GameOver(boolean won) {

        //导入胜利与失败照片
        ImageIcon cryIcon = new ImageIcon(".//minesweeper//src//minesweeper//surrender.jpg");
        ImageIcon laughIcon = new ImageIcon(".//minesweeper//src//minesweeper//thumb.jpg");
        //设置合适的尺寸与大小
        cryIcon.setImage(cryIcon.getImage().getScaledInstance(30,30,30));
        laughIcon.setImage(laughIcon.getImage().getScaledInstance(30,30,30));
        timer.stop();
        String msg = won ? "You are win! use " + secondTime + " second" :
                "Game Over! use " + secondTime + " second";
        //游戏状态图标显示
        btnShow.setIcon(won ? laughIcon : cryIcon);
        int msgType = won ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        //游戏结果提示框
        JOptionPane.showMessageDialog(this,msg,"GAME OVER",msgType);
        resetGame();
    }

    private void createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu FileMenu = new JMenu("File");

        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_DOWN_MASK));//快捷键设置
        newGameItem.addActionListener(e->newGame());

        JMenuItem resetGameItem = new JMenuItem("Reset Game");
        resetGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,InputEvent.CTRL_DOWN_MASK));
        resetGameItem.addActionListener(e->resetGame());

        JMenuItem exitGameItem = new JMenuItem("Exit Game");
        exitGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,InputEvent.CTRL_DOWN_MASK));
        exitGameItem.addActionListener(e->System.exit(0));

        FileMenu.add(newGameItem);
        FileMenu.add(resetGameItem);
        FileMenu.addSeparator();
        FileMenu.add(exitGameItem);

        menuBar.add(FileMenu);
        setJMenuBar(menuBar);
    }
/*
private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu FileMenu = new JMenu("File");

    // newGame
    JMenuItem newGameItem = new JMenuItem("New Game");
    newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
    newGameItem.addActionListener(new NewGameListener());

    //resetGame
    JMenuItem resetGameItem = new JMenuItem("Reset Game");
    resetGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
    resetGameItem.addActionListener(new ResetGameListener());

    // exitGame
    JMenuItem exitGameItem = new JMenuItem("Exit Game");
    exitGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
    exitGameItem.addActionListener(new ExitGameListener());

    FileMenu.add(newGameItem);
    FileMenu.add(resetGameItem);
    FileMenu.addSeparator();
    FileMenu.add(exitGameItem);

    menuBar.add(FileMenu);
    setJMenuBar(menuBar);
}

//内部类实现newGame监听器
private class NewGameListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        newGame();
    }
}

//内部类实现resetGame监听器
private class ResetGameListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        resetGame();
    }
}

//内部类实现exitGame监听器
private class ExitGameListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
*/

    public void resetGame() {
        timeField.setText(String.format("%04d",0));
        mineField.setText(String.format("%03d",currMines));
        btnShow.setIcon(calmIcon);
        btnShow.setFocusPainted(false);
        setupGame();
        revalidate();
        repaint();
    }

    private void newGame()
    {
        dispose();//销毁所有资源
        new MineSweeperMain();
    }

    private void showSelectDialog() {
       //难度选择的提示对话框
        int choice = JOptionPane.showOptionDialog(this,"Select difficulty","Difficulty select",
                JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,
                DIFFICULTY_OPTION,DIFFICULTY_OPTION[0]);

        //根据用户选择的难度设置游戏基本参数
        switch(choice)
        {
            case 0:
                currRows = EASY_ROWS;
                currCols = EASY_COLS;
                currMines = EASY_MINES;
                break;
            case 1:
                currRows = MEDIUM_ROWS;
                currCols = MEDIUM_COLS;
                currMines = MEDIUM_MINES;
                break;
            case 2:
                currRows = DIFFICULT_ROWS;
                currCols = DIFFICULT_COLS;
                currMines = DIFFICULT_MINES;
                break;
            default:
                currRows = EASY_ROWS;
                currCols = EASY_COLS;
                currMines = EASY_MINES;
                System.exit(0);//不选择时关闭页面
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new MineSweeperMain();
            }
        });
    }
}

/*
private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu FileMenu = new JMenu("File");

    // 创建菜单项
    JMenuItem newGameItem = new JMenuItem("New Game");
    newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));

    JMenuItem resetGameItem = new JMenuItem("Reset Game");
    resetGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));

    JMenuItem exitGameItem = new JMenuItem("Exit Game");
    exitGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));

    // 创建统一的监听器实例
    MenuActionListener listener = new MenuActionListener();

    // 为菜单项添加监听器
    newGameItem.addActionListener(listener);
    resetGameItem.addActionListener(listener);
    exitGameItem.addActionListener(listener);

    // 添加菜单项到菜单
    FileMenu.add(newGameItem);
    FileMenu.add(resetGameItem);
    FileMenu.addSeparator();
    FileMenu.add(exitGameItem);

    menuBar.add(FileMenu);
    setJMenuBar(menuBar);
}

// 统一的菜单动作监听器内部类
private class MenuActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        // 获取触发事件的菜单项文本
        String command = ((JMenuItem) e.getSource()).getText();

        switch(command) {
            case "New Game":
                newGame();
                break;

            case "Reset Game":
                resetGame();
                break;

            case "Exit Game":
                System.exit(0);
                break;

            default:
                System.err.println("未知菜单命令: " + command);
        }
    }
}
 */


