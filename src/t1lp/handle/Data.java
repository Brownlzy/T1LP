package t1lp.handle;

import t1lp.calculator.MyNumber;
import t1lp.ui.MainWindow;

import java.util.ArrayList;
import java.util.List;

import static t1lp.handle.Config.Log;

/**
 * 数据类，用于储存程序运行所需数据
 * @author Brownlzy
 * @version 1.0
 */
public class Data {
    public static boolean isInit = false;   //程序是否初始化

    public static boolean isOpen = false;   //计算器是否打开

    public static MyNumber ledNumber; //计算器LED字符串
    public static List<String> formula=new ArrayList<>();   //已输入的计算式
    public static MyNumber result;    //计算结果
    public static boolean isError;  //计算器是否出错
    public static boolean isNextNum;
    public static int inState;  //计算器当前状态   0：等待输入数字（括号），1：等待输入数字或符号，2:等待更改运算符、输入括号或新数字，3：等待输入符号，4：计算完成等待输入数字或符号继续计算

    public static MainWindow ui;    //计算器主窗口
    /**
     * 实例化UI界面，设置默认数据
     * @author Brownlzy
     */
    public static void init() {
        ui = new MainWindow();
        //初始化计算器内存内容
        resetCalculator();
        //已初始化标记
        isInit = true;
    }
    /**
     * 重置计算器显示数据
     * @author Brownlzy
     */
    public static void resetCalculator() {
        ledNumber = new MyNumber(0);
        formula.clear();
        result = new MyNumber(0);
        isError = false;
        isNextNum=false;
        inState=0;
        Log("Data","resetCalculator()","计算器已重置");
    }

}
