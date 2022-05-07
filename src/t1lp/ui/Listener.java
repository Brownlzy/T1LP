package t1lp.ui;

import t1lp.data.Config;
import t1lp.data.Data;
import t1lp.operator.InputProcess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Listener {
    static class CommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //输入 e.getActionCommand()
            if (Config.isDebug) System.out.println("按钮（cmd）" + e.getActionCommand() + "被按下");
            InputProcess.dealCommand(e.getActionCommand());
            Data.ui.requestFocusInWindow();
        }

    }

    static class InsertListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //输入 e.getActionCommand()
            if (Config.isDebug) System.out.println("按钮（ist）" + e.getActionCommand() + "被按下");
            InputProcess.dealInsert(e.getActionCommand());
            Data.ui.requestFocusInWindow();
        }
    }

    static class KeyboardListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            if(Config.isDebug) System.out.println("[KeyboardListener]接受按键"+e.getKeyCode()+"("+e.getKeyChar()+")");
            if(e.getKeyChar()>='0'&&e.getKeyChar()<='9'
                    || e.getKeyChar()>='a'&&e.getKeyChar()<='f'
                    || e.getKeyChar()>='A'&&e.getKeyChar()<='F'
                    || e.getKeyChar()=='.'){
                InputProcess.dealInsert(String.valueOf(e.getKeyChar()));
            }else if("+-*/=".contains(String.valueOf(e.getKeyChar()))){
                InputProcess.dealCommand(String.valueOf(e.getKeyChar()));
            }else if(e.getKeyCode()==KeyEvent.VK_ENTER){
                InputProcess.dealCommand("=");
            }else if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
                InputProcess.dealCommand("BKS");
                //退格
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}
    }
}
