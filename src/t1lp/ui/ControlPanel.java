package t1lp.ui;

import javax.swing.*;
import java.awt.*;
/**
 * 包含计算器所有按钮的面板
 * @author Brownlzy
 * @version 1.0
 */
class ControlPanel extends JPanel {
    private JButton[] ctrlButton;
    private final String[] funName = {
            "DEC", "HEX", "OCT", "OFF", "ON/C",
            "STO", "RCL", "SUM", "(", ")",
            "SHF", "d", "E", "F", "K",
            "1'sC", "A", "b", "C", "÷",
            "OR", "7", "8", "9", "×",
            "AND", "4", "5", "6", "-",
            "XOR", "1", "2", "3", "+",
            "CE", "0", ".", "+/-", "="
    };
    /**
     * 构造函数，使用Windows风格UI
     * @author Brownlzy
     */
    public ControlPanel() {
        initGUI();
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
     * @author Brownlzy
     */
    public void initGUI() {
        this.setBackground(null);
        this.setOpaque(false);
        this.setLayout(null);
        ctrlButton = new JButton[40];
        Listener.InsertListener insertListener = new Listener.InsertListener();
        Listener.CommandListener commandListener = new Listener.CommandListener();
        for (int i = 0; i < 40; i++) {
            ctrlButton[i] = new JButton(funName[i]);
            ctrlButton[i].setBounds(82 * (i % 5), 71 * (i / 5), 60, 35);
            if (i >= 10 && i % 5 <= 3 && i % 5 >= 1 && i!=38) {
                ctrlButton[i].addActionListener(insertListener);
                ctrlButton[i].setFont(new Font("Consolas", Font.BOLD, 22));
            }else {
                ctrlButton[i].addActionListener(commandListener);
                ctrlButton[i].setFont(new Font("Consolas", Font.BOLD, 13));
            }
            this.add(ctrlButton[i]);
        }
    }
}
