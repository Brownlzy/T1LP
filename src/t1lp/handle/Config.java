package t1lp.handle;

/**
 * 配置类，存储程序设置
 *
 * @author Brownlzy
 * @version 1.0
 */
public class Config {
    public static boolean isDebug = false;

    /**
     * 统一调试输出方法
     * import static t1lp.handle.Config.Log;
     * 示例：Log("InputProcess","dealCommand(String actionCommand:"+actionCommand+")","由于计算器未开机或出现错误，不予处理。");
     *
     * @param sourceClass  源类名
     * @param sourceMethod 源方法名
     * @param msg          显示的消息
     * @author Brownlzy
     */
    public static void Log(String sourceClass, String sourceMethod, String msg) {
        if (isDebug)
            System.out.println(String.format("[ %-16s ]", sourceClass) + "\t" + String.format("[ %-55s ]", sourceMethod) + "\t\t" + msg);
    }
}
