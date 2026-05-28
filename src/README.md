# 扫雷游戏 — 源代码说明

基于 Java Swing 的经典扫雷游戏，源代码位于本目录。

## 快速开始

**入口类：** `minesweeper.MineSweeperMain`

在 IntelliJ IDEA 中打开项目，运行 `MineSweeperMain` 即可。启动后会弹出难度选择对话框，随后进入游戏主界面。

> 图标资源引用相对路径（如 `minesweeper/src/minesweeper/mine1.jpg`），请从项目根目录 `minesweeper/` 启动程序。

## 目录结构

```
src/
├── Main.java                    # 默认模板，非游戏入口
└── minesweeper/
    ├── MineSweeperMain.java     # 主窗口：菜单、计时、难度、状态栏
    ├── GameBoardPanel.java      # 棋盘：交互逻辑、胜负判定、动画
    ├── Cell.java                # 单元格模型（继承 JButton）
    ├── MineMap.java             # 地雷分布与随机布雷
    ├── MineSweeperConstants.java # 难度、颜色、尺寸等全局常量
    ├── Animation.java           # 动画时间轴
    ├── AnimationCallback.java   # 动画更新回调
    ├── *.jpg                    # 界面图标（地雷、旗帜等）
    └── *Test*.java              # JUnit 5 测试
```

## 架构概览

```
MineSweeperMain (JFrame)
    ├── 难度选择 / 菜单 / 计时器 / 剩余雷数
    └── GameBoardPanel (JPanel)
            ├── Cell[][]          单元格网格
            ├── MineMap           地雷数据
            └── Animation         翻开动画
```

| 类 | 说明 |
|---|---|
| `MineSweeperMain` | 程序入口，管理 UI 框架与游戏生命周期 |
| `GameBoardPanel` | 处理鼠标事件、首次点击布雷、连锁展开、插旗计数 |
| `Cell` | 维护单格状态（地雷 / 翻开 / 插旗）并负责绘制 |
| `MineMap` | 生成地雷二维数组，首击位置保证无雷 |
| `MineSweeperConstants` | 三档难度参数与界面配色 |

## 游戏规则

- **左键单击** — 翻开格子；空白格自动展开相邻区域
- **右键单击** — 插旗 / 取消插旗
- **胜利** — 所有非地雷格子均被翻开
- **失败** — 踩中地雷

首次左键点击后才开始布雷并启动计时，保证首击安全。

## 难度配置

| 难度 | 棋盘尺寸 | 地雷数 | 单元格像素 |
|------|----------|--------|------------|
| Easy | 9 × 9 | 10 | 60 |
| Intermediate | 16 × 16 | 40 | 40 |
| Difficult | 30 × 16 | 99 | 30 |

参数定义见 `MineSweeperConstants.java`。

## 测试

项目使用 **JUnit 5** 编写单元测试与集成测试，覆盖单元格、地雷图、棋盘逻辑及完整游戏流程。

| 辅助类 | 用途 |
|--------|------|
| `IntegrationTestBase` | 集成测试基类，提供无头模式等工具 |
| `TestController` / `MockController` | 模拟主窗口控制器 |
| `GameBoardPanelTestAdapter` | 测试用棋盘适配器 |

在 IDE 中对 `minesweeper` 包或任意 `*Test.java` 执行 Run Tests 即可。

## 技术栈

- Java SE（Swing / AWT）
- JUnit 5（测试）
- Mockito（部分测试，见 `.idea/libraries/mockito_core.xml`）

## 注意事项

- 根目录 `Main.java` 仅为 IDE 默认生成，**不是**游戏启动类。
- 图片资源需放置于 `src/minesweeper/` 目录下，文件名与代码中引用保持一致。
