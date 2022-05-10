package t1lp.handle;

import java.util.Objects;

import static t1lp.handle.Config.Log;

/**
 * 计算器主类
 *
 * @author Brownlzy
 * @version 1.0
 */
public class T1LcdProgrammer {
    /**
     * 处理程序启动参数
     *
     * @param args 程序启动参数
     * @author Brownlzy
     */
    public static void dealWithArgs(String[] args) {
        //检查启动参数第一个是否为“/debug”，是则启用控制台输出。
        if (args.length > 0 && Objects.equals(args[0], "/debug")) {
            Config.isDebug = true;
            Log("T1LcdProgrammer", "main(" + args[0] + ")", "T1LcdProgrammer DEBUG Mode");
        }
    }

    /**
     * 程序主函数
     *
     * @param args 程序启动参数
     * @author Brownlzy
     */
    public static void main(String[] args) {
        //处理启动参数
        dealWithArgs(args);
        //程序初始化
        Data.init();
    }
}