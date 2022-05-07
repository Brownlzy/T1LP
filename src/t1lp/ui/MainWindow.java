package t1lp.ui;

import t1lp.data.Config;

import javax.swing.*;
import java.awt.*;

/**
 * 计算器用户界面类
 * @author Brownlzy
 * @version 1.0
 */
public class MainWindow extends JFrame implements UI{
    private LcdScreen lcdScreen;
    private ControlPanel ctrlPanel;
    public MainWindow(){
        InitUi();
    }
    /**
     * 初始化UI
     * @author Brownlzy
     */
    public void InitUi() {
        SwingUtilities.invokeLater(() -> {
            if (Config.isDebug) System.out.println("开始初始化GUI");

            //创建计算器主窗体
            this.setTitle("T1LcdProgrammer");
            this.setSize(435, 730);
            //使窗口位于屏幕中间
            this.setLocationRelativeTo(null);
            //设置关闭窗口退出程序
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );

            //设置键盘监听器
            Listener.KeyboardListener keyboardListener=new Listener.KeyboardListener();
            //设置容器Panel
            JLayeredPane mainPane = new JLayeredPane();
            mainPane.setBounds(0,0,420,700);

            //设置背景图片组件，层级为1
            ImageComponent backgroundImage = new ImageComponent(420, 700, "res/BackgroundImage.png");
            backgroundImage.setBounds(0, -5, 420, 970);
            mainPane.add(backgroundImage,1);

            //创建LCD显示屏，置于顶层
            lcdScreen = new LcdScreen();
            lcdScreen.setBounds(17,25,387,90);
            mainPane.add(lcdScreen, 0);
            //创建按钮面板，置于顶层
            ctrlPanel = new ControlPanel();
            ctrlPanel.setBounds(17,143,387,532);
            mainPane.add(ctrlPanel,0);

            //设置窗口容器
            this.setContentPane(mainPane);
            //固定窗口大小
            this.setResizable(false);
            this.setVisible(true);
            //添加键盘监听器
            this.addKeyListener(keyboardListener);
            //获得焦点
            this.requestFocusInWindow();

            if (Config.isDebug) System.out.println("GUI初始化完成。");
        });
    }
    /**
     * 刷新LCD显示器的显示内容
     * @author Brownlzy
     * @param ledNumber led数字
     * @param isError 是否显示E标
     */
    @Override
    public void setLcdScreen(String ledNumber, boolean isError){
            if (Config.isDebug) System.out.println("Lcd显示屏修改内容为:" + ledNumber.substring(5));
            lcdScreen.changeLedNumber(ledNumber);
            if (Config.isDebug) System.out.println("Lcd状态显示屏修改内容为:错误：" + isError);
            lcdScreen.changeLedState(isError);
    }
    /**
     * 设置电源开关
     * @author Brownlzy
     * @param isOpen 新电源状态
     */
    @Override
    public void setPower(boolean isOpen) {
        if(isOpen) {
            if (Config.isDebug) System.out.println("计算器已开启");
            lcdScreen.showMe();
        }else{
            if (Config.isDebug) System.out.println("计算器已关闭");
            lcdScreen.hideMe();
        }
    }
    /**
     * 设置LED显示数字
     * @author Brownlzy
     * @param number LED显示数字
     */
    @Override
    public void setLedNumber(String number) {
        if (Config.isDebug) System.out.println("Lcd显示屏修改内容为:" + number);
        lcdScreen.changeLedNumber(number);
    }
    /**
     * 设置E标显示
     * @author Brownlzy
     * @param isError 是否显示E标
     */
    @Override
    public void setLedState(boolean isError) {
        if (Config.isDebug) System.out.println("Lcd状态显示屏修改内容为:错误：" + isError);
        lcdScreen.changeLedState(isError);
    }

    /**
     * 一个带有图像组件的面板
     */
    public static class ImagePanel extends JPanel
    {
        public ImagePanel(int width,int height,String filename)
        {
            add(new ImageComponent(width, height, filename));
        }
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
