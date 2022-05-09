package t1lp.handle;

import java.util.Objects;

/**
 * 计算器主类
 * @author Brownlzy
 * @version 1.0
 */
public class T1LcdProgrammer {
    /**
     * 处理程序启动参数
     * @param args 程序启动参数
     * @author Brownlzy
     */
    public static void dealWithArgs(String[] args) {
        //检查启动参数第一个是否为“/debug”，是则启用控制台输出。
        if (Objects.equals(args[0], "/debug"))
            Config.isDebug = true;
    }

    /**
     * 程序主函数
     * @param args 程序启动参数
     * @author Brownlzy
     */
    public static void main(String[] args) {
        //处理启动参数
        if (args.length > 0) dealWithArgs(args);
        if (Config.isDebug) System.out.println("T1LcdProgrammer DEBUG Mode");
        //程序初始化
        Data.init();
    }
}