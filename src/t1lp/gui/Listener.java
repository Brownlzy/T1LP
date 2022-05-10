package t1lp.gui;

import t1lp.handle.Data;
import t1lp.handle.InputProcess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static t1lp.handle.Config.Log;

/**
 * 输入监听器类
 *
 * @author Brownlzy
 * @version 1.0
 */
class Listener {
    /**
     * 命令键输入监听器类
     *
     * @author Brownlzy
     * @version 1.0
     */
    static class CommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //e.getActionCommand()为按下按钮的标题
            Log("CommandListener", "actionPerformed(ActionEvent e:" + e.getActionCommand() + ")", "按钮（cmd）" + e.getActionCommand() + "被按下");
            //交由InputProcess处理
            InputProcess.dealCommand(e.getActionCommand());
            //使焦点处于主窗口上，接受下一个键盘输入
            Data.ui.requestFocusInWindow();
        }

    }

    /**
     * 输入键输入监听器类
     *
     * @author Brownlzy
     * @version 1.0
     */
    static class InsertListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //e.getActionCommand()为按下按钮的标题
            Log("InsertListener", "actionPerformed(ActionEvent e:" + e.getActionCommand() + ")", "按钮（ist）" + e.getActionCommand() + "被按下");
            //交由InputProcess处理
            InputProcess.dealInsert(e.getActionCommand());
            //使焦点处于主窗口上，接受下一个键盘输入
            Data.ui.requestFocusInWindow();
        }
    }

    /**
     * 键盘输入监听器类
     *
     * @author Brownlzy
     * @version 1.0
     */
    static class KeyboardListener implements KeyListener {
        /**
         * 接受键盘输入
         *
         * @param e KeyEvent
         * @author Brownlzy
         */
        @Override
        public void keyTyped(KeyEvent e) {
            Log("KeyboardListener", "keyTyped(KeyEvent e:" + e.getKeyCode() + ")", "接受按键" + e.getKeyCode() + "(" + e.getKeyChar() + ")");
            if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9' //数字0-9
                    || e.getKeyChar() >= 'a' && e.getKeyChar() <= 'f' //小写a-f
                    || e.getKeyChar() >= 'A' && e.getKeyChar() <= 'F' //大写A-F
                    || e.getKeyChar() == '.'  //小数点
            ) {
                InputProcess.dealInsert(String.valueOf(e.getKeyChar()));
            } else if ("+-*/=".contains(String.valueOf(e.getKeyChar()))) { //加、减、乘、除、等于
                InputProcess.dealCommand(String.valueOf(e.getKeyChar()));
            } else if (e.getKeyCode() == 0) {    //回车键
                InputProcess.dealCommand("=");
            } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {   //退格键
                InputProcess.dealCommand("BKS");
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
