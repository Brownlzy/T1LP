package t1lp.gui;

import javax.swing.*;
import java.awt.*;

import static t1lp.handle.Config.Log;

/**
 * 计算器用户界面类
 *
 * @author Brownlzy
 * @version 1.0
 */
public class MainWindow extends JFrame {
    private LcdScreen lcdScreen;
    private ControlPanel ctrlPanel;

    public MainWindow() {
        InitUi();
    }

    /**
     * 初始化UI
     *
     * @author Brownlzy
     */
    public void InitUi() {
        SwingUtilities.invokeLater(() -> {
            Log("MainWindow", "InitUi()", "开始初始化GUI");

            //创建计算器主窗体
            this.setTitle("T1LcdProgrammer");
            this.setSize(435, 730);
            //使窗口位于屏幕中间
            this.setLocationRelativeTo(null);
            //设置关闭窗口退出程序
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Image icon = Toolkit.getDefaultToolkit().getImage("res/icon.png");
            this.setIconImage(icon);
            //设置键盘监听器
            Listener.KeyboardListener keyboardListener = new Listener.KeyboardListener();
            //设置容器Panel
            JLayeredPane mainPane = new JLayeredPane();
            mainPane.setBounds(0, 0, 420, 700);

            //设置背景图片组件，层级为1
            ImageComponent backgroundImage = new ImageComponent(420, 700, "res/BackgroundImage.png");
            backgroundImage.setBounds(0, -5, 420, 970);
            mainPane.add(backgroundImage, 1);

            //创建LCD显示屏，置于顶层
            lcdScreen = new LcdScreen();
            lcdScreen.setBounds(17, 25, 387, 90);
            mainPane.add(lcdScreen, 0);
            //创建按钮面板，置于顶层
            ctrlPanel = new ControlPanel();
            ctrlPanel.setBounds(17, 143, 387, 532);
            mainPane.add(ctrlPanel, 0);

            //设置窗口容器
            this.setContentPane(mainPane);
            //固定窗口大小
            this.setResizable(false);
            this.setVisible(true);
            //添加键盘监听器
            this.addKeyListener(keyboardListener);
            //获得焦点
            this.requestFocusInWindow();

            Log("MainWindow", "InitUi()", "GUI初始化完成。");
        });
    }

    /**
     * 刷新LCD显示器的显示内容
     *
     * @param ledNumber led数字
     * @param isError   是否显示E标
     * @author Brownlzy
     */
    public void setLcdScreen(String ledNumber, boolean isError) {
        Log("MainWindow", "setLcdScreen(String ledNumber:" + ledNumber + ", boolean isError:" + isError + ")", "Lcd显示屏修改内容为:" + ledNumber + (isError ? " [E]" : ""));
        lcdScreen.changeLedNumber(ledNumber);
        lcdScreen.changeLedState(isError);
    }

    /**
     * 设置电源开关
     *
     * @param isOpen 新电源状态
     * @author Brownlzy
     */
    public void setPower(boolean isOpen) {
        if (isOpen) {
            lcdScreen.showMe();
        } else {
            lcdScreen.hideMe();
        }
        Log("MainWindow", "setPower(boolean isOpen:" + isOpen + ")", "计算器" + (isOpen ? "已开启" : "已关闭"));
    }

    /**
     * 设置LED显示数字
     *
     * @param number LED显示数字
     * @author Brownlzy
     */
    public void setLedNumber(String number) {
        Log("MainWindow", "setLedNumber(String number:" + number + ")", "Lcd显示屏数字修改为:" + number);
        lcdScreen.changeLedNumber(number);
    }

    /**
     * 设置E标显示
     *
     * @param isError 是否显示E标
     * @author Brownlzy
     */
    public void setLedState(boolean isError) {
        Log("MainWindow", "setLedState(boolean isError:" + isError + ")", "Lcd状态显示屏修改为:错误：" + isError);
        lcdScreen.changeLedState(isError);
    }

    /**
     * 一个图像组件
     */
    public static class ImageComponent extends JComponent {
        private int imageWidth;
        private int imageHeight;

        private final Image image;

        public ImageComponent(int width, int height, String filename) {
            imageWidth = width;
            imageHeight = height;
            //从文件中读取图片
            image = new ImageIcon(filename).getImage();
        }

        public void paintComponent(Graphics g) {
            if (image == null) return;
            g.drawImage(image, 0, 0, null);
        }

        public Dimension getPreferredSize() {
            return new Dimension(imageWidth, imageHeight);
        }
    }

}
