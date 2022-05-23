package t1lp.calculator;

import static t1lp.handle.Config.Log;


/**
 * 处理用户输入的两位操作数
 *
 * @author cjn-worker
 * @version 1.0
 */
abstract class Operator {
    boolean judge = false;//辨别传回数值形式的标识符
    char True = '1';
    String str = "00000000000000000000000000000000";//32个字节的字符串，用于移位后的补0
    protected double X;//第一个操作数
    protected double Y;//第二个操作数
    protected double Z;//以double形式反馈的结果
    protected String Xb;//第一个操作数的二进制形式
    protected String Yb;//第二个操作数的二进制形式
    protected String Zb = "";//以string形式返回的结果

    public Operator(MyNumber x, MyNumber y) {
        X = Double.parseDouble(x.toString(10).substring(5));//将X转换为十进制
        Y = Double.parseDouble(y.toString(10).substring(5));//将Y转换为十进制
        Xb = x.toString(2).substring(5);//将X转换为二进制
        Yb = y.toString(2).substring(5);//将Y转换为二进制
        if (Xb.length() < 32) {
            char[] chars = new char[32 - Xb.length()];
            for (int i = 0; i < 32 - Xb.length(); i++) {
                chars[i]='0';
            }
            Xb=String.valueOf(chars)+Xb;
        }
        if (Yb.length() < 32) {
            char[] chars = new char[32 - Yb.length()];
            for (int i = 0; i < 32 - Yb.length(); i++) {
                chars[i]='0';
            }
            Yb=String.valueOf(chars)+Yb;
        }
        Log("Operator", "Operator(MyNumber x:" + x + ",MyNumber y:" + y + ")", "X:" + X + "\tY:" + Y + "\tXb:" + Xb + "\tYb:" + Yb);
        doCalculate();
    }

    /**
     * 实现各种方法的计算
     *
     * @author cjn-worker
     */
    protected abstract void doCalculate();

    public MyNumber getResult() {
        Log("Operator", "getResult()", "Z:" + Z + "\tZb:" + Zb);
        if (judge)
            return new MyNumber(Z);
        else
            return new MyNumber("NUM0b" + Zb);
    }
}

/**
 * 加法运算
 *
 * @author cjn-worker
 * @version 1.0
 */
class Add extends Operator {

    public Add(MyNumber x, MyNumber y) {
        super(x, y);
    }

    @Override
    protected void doCalculate() {
        Z = X + Y;
        judge = true;
    }
}

/**
 * 减法运算
 *
 * @author cjn-worker
 * @version 1.0
 */
class Sub extends Operator {

    public Sub(MyNumber x, MyNumber y) {
        super(x, y);
    }

    @Override
    protected void doCalculate() {
        Z = X - Y;
        judge = true;
    }
}

/**
 * 乘法运算
 *
 * @author cjn-worker
 * @version 1.0
 */
class Mul extends Operator {

    public Mul(MyNumber x, MyNumber y) {
        super(x, y);
    }

    @Override
    protected void doCalculate() {
        Z = X * Y;
        judge = true;
    }
}

/**
 * 除法运算
 *
 * @author cjn-worker
 * @version 1.0
 */
class Div extends Operator {

    public Div(MyNumber x, MyNumber y) {
        super(x, y);
    }

    @Override
    protected void doCalculate() {
        if(Y==0) throw new ArithmeticException("/ by zero");
        Z = X / Y;
        judge = true;
    }
}

/**
 * 或运算
 *
 * @author cjn-worker
 * @version 1.0
 */
class Or extends Operator {

    public Or(MyNumber x, MyNumber y) {
        super(x, y);
    }

    @Override
    protected void doCalculate() {
        StringBuilder ZbBuilder = new StringBuilder(Zb);
        for (int i = 0; i < Yb.length(); i++) {
            if (Xb.charAt(i) == True | Yb.charAt(i) == True)
                ZbBuilder.append("1");
            else {
                ZbBuilder.append("0");
            }
        }
        Zb = ZbBuilder.toString();
    }
}

/**
 * 与运算
 *
 * @author cjn-worker
 * @version 1.0
 */
class And extends Operator {

    public And(MyNumber x, MyNumber y) {
        super(x, y);
    }

    @Override
    protected void doCalculate() {
        StringBuilder ZbBuilder = new StringBuilder(Zb);
        for (int i = 0; i < Yb.length(); i++) {
            if (Xb.charAt(i) == True & Yb.charAt(i) == True)
                ZbBuilder.append("1");
            else {
                ZbBuilder.append("0");
            }
        }
        Zb = ZbBuilder.toString();
    }
}

/**
 * 异或运算
 *
 * @author cjn-worker
 * @version 1.0
 */
class Xor extends Operator {

    public Xor(MyNumber x, MyNumber y) {
        super(x, y);
    }

    @Override
    protected void doCalculate() {
        StringBuilder ZbBuilder = new StringBuilder(Zb);
        for (int i = 0; i < Yb.length(); i++) {
            if (Xb.charAt(i) == Yb.charAt(i))
                ZbBuilder.append("0");
            else {
                ZbBuilder.append("1");
            }
        }
        Zb = ZbBuilder.toString();
    }
}

/**
 * 移位运算
 *
 * @author cjn-worker
 * @version 1.0
 */
class Shf extends Operator {

    public Shf(MyNumber x, MyNumber y) {
        super(x, y);
    }

    @Override
    protected void doCalculate() {
        StringBuilder ZbBuilder = new StringBuilder(Zb);
        int change = (int) (Y);
        if (change > 0) {
            String newStr = Xb + str;
            for (int i = 0; i < Xb.length(); i++) {
                ZbBuilder.append(newStr.charAt(i + change));
            }
        } else {
            String newStr = str + Xb;
            for (int i = newStr.length() - Xb.length(); i < newStr.length(); i++) {
                ZbBuilder.append(newStr.charAt(i + change));
            }
        }
        Zb = ZbBuilder.toString();
    }
}

