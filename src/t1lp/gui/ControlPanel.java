package t1lp.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * 包含计算器所有按钮的面板
 *
 * @author Brownlzy
 * @version 1.0
 */
class ControlPanel extends JPanel {
    private final String[] funName = {
            "DEC", "HEX", "OCT", "OFF", "ON/C",
            "STO", "RCL", "SUM", "(", ")",
            "SHF", "d", "E", "F", "K",
            "1'sC", "A", "b", "C", "÷",
            "OR", "7", "8", "9", "×",
            "AND", "4", "5", "6", "-",
            "XOR", "1", "2", "3", "+",
            "CE", "0", ".", "+/-", "="
    };  //按钮标题数组
    private static final List<String> disabledName = Arrays.asList("STO", "RCL", "SUM", "K");

    /**
     * 构造函数，使用Windows风格UI
     *
     * @author Brownlzy
     */
    public ControlPanel() {
        initGUI();
        //设置UI风格为Windows
        String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(ControlPanel.this);
    }

    /**
     * 初始化按钮面板
     *
     * @author Brownlzy
     */
    public void initGUI() {
        //使Panel透明，不会遮挡背景图片
        this.setBackground(null);
        this.setOpaque(false);
        //不需要Layout，按键将用坐标排列
        this.setLayout(null);
        JButton[] ctrlButton = new JButton[40];
        //设置两个监听器，分别监听输入和命令按钮
        Listener.InsertListener insertListener = new Listener.InsertListener();
        Listener.CommandListener commandListener = new Listener.CommandListener();
        for (int i = 0; i < 40; i++) {
            //初始按钮，标题来自funName数组
            ctrlButton[i] = new JButton(funName[i]);
            //设置每个按钮坐标
            ctrlButton[i].setBounds(82 * (i % 5), 71 * (i / 5), 60, 35);
            //分别设置对应监听器并设置字体
            if (i >= 10 && i % 5 <= 3 && i % 5 >= 1 && i != 38) {
                //输入键
                ctrlButton[i].addActionListener(insertListener);
                ctrlButton[i].setFont(new Font("Consolas", Font.BOLD, 22));
            } else {
                //命令键
                ctrlButton[i].addActionListener(commandListener);
                ctrlButton[i].setFont(new Font("Consolas", Font.BOLD, 13));
            }
            if (disabledName.contains(ctrlButton[i].getText()))
                ctrlButton[i].setEnabled(false);
            //加入控制面板
            this.add(ctrlButton[i]);
        }
    }
}
