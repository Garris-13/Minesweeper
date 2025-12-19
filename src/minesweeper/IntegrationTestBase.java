package minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 集成测试基类，提供通用的测试工具方法
 */
public class IntegrationTestBase {

    protected TestController createTestController() {
        return new TestController();
    }

    protected void setupHeadlessMode() {
        System.setProperty("java.awt.headless", "true");
    }
}