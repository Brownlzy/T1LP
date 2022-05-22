package t1lp.calculator;

import static t1lp.handle.Config.Log;

abstract class Operator {
    boolean judge=false;//辨别传回数值形式的标识符
    char True = '1';
    String str="00000000000000000000000000000000";//32个字节的字符串，用于移位后的补0
    protected double X;//第一个操作数
    protected double Y;//第二个操作数
    protected double Z;//以double形式反馈的结果
    protected String Xb;//第一个操作数的二进制形式
    protected String Yb;//第二个操作数的二进制形式
    protected String Zb="";//以string形式返回的结果
    public Operator(MyNumber x,MyNumber y){
        X=Double.parseDouble(x.toString(10).substring(5));
        Y=Double.parseDouble(y.toString(10).substring(5));
        Xb=x.toString(2).substring(5);
        Yb=y.toString(2).substring(5);
        Log("Operator","Operator(MyNumber x:"+x+",MyNumber y:"+y+")","X:"+X+"\tY:"+Y+"\tXb:"+Xb+"\tYb:"+Yb);
        Log("Operator","getResult()","Z:"+Z+"\tZb:"+Zb);
        doCalculate();
    }
    protected abstract void doCalculate();
    public MyNumber getResult(){
        if(judge==true)
            return new MyNumber(Z);
        else
            return new MyNumber("NUM0b"+Zb);
    }
}

/**
 * 各种运算
 *
 * @author cjn——worker
 */
class Add extends Operator{

    public Add(MyNumber x, MyNumber y) {
        super(x, y);
    }

    @Override
    protected void doCalculate() {
        Z=X+Y;
        judge=true;
    }
}

class Sub extends Operator{

    public Sub(MyNumber x, MyNumber y) {
        super(x, y);
    }

    @Override
    protected void doCalculate() {
        Z=X-Y;
        judge=true;
    }
}

class Mul extends Operator{

    public Mul(MyNumber x, MyNumber y) {super(x, y);}

    @Override
    protected void doCalculate() {
        Z=X*Y;
        judge=true;
    }
}

class Div extends Operator{

    public Div(MyNumber x, MyNumber y) {super(x, y);}

    @Override
    protected void doCalculate() {
        Z=X/Y;
        judge=true;
    }
}

class Or extends Operator{

    public Or(MyNumber x, MyNumber y) {super(x, y);}

    @Override
    protected void doCalculate() {
        for(int i=0;i<Yb.length();i++){
            if(Xb.charAt(i)==True | Yb.charAt(i)==True)
                Zb+="1";
            else{
                Zb+="0";
            }
        }
    }
}

class And extends Operator{

    public And(MyNumber x, MyNumber y) {super(x, y);}

    @Override
    protected void doCalculate() {
        for(int i=0;i<Yb.length();i++){
            if(Xb.charAt(i)==True & Yb.charAt(i)==True)
                Zb+="1";
            else{
                Zb+="0";
            }
        }
    }
}

class Xor extends Operator{

    public Xor(MyNumber x, MyNumber y) {super(x, y);}

    @Override
    protected void doCalculate() {
        for(int i=0;i<Yb.length();i++){
            if(Xb.charAt(i)==Yb.charAt(i))
                Zb+="0";
            else{
                Zb+="1";
            }
        }
    }
}

class Shf extends Operator{

    public Shf(MyNumber x, MyNumber y){super(x,y);}

    @Override
    protected void doCalculate(){
        int change = (int)(Y);
        if(change>0) {
            String newStr=str+Xb+str;
            for (int i = Xb.length()+change-1; i < 33+Xb.length()+change-1; i++) {
                Zb += newStr.charAt(i);
            }
        }
        else{
            String newStr=str+Xb;
            for (int i = newStr.length()-Xb.length(); i < newStr.length(); i++) {
                Zb+=newStr.charAt(i+change);
            }
        }

    }
}

