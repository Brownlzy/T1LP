package t1lp.calculator;

abstract class Operator {
    boolean judge=false;
    protected double X;
    protected double Y;
    protected double Z;
    protected String Xb;
    protected String Yb;
    protected String Zb="";
    public Operator(MyNumber x,MyNumber y){
        X=Double.parseDouble(x.toString(10).substring(5));
        Y=Double.parseDouble(y.toString(10).substring(5));
        Xb=x.toString(2).substring(5);
        Yb=y.toString(2).substring(5);
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
        Z=(long)X|(long)Y;
    }
}

class And extends Operator{

    public And(MyNumber x, MyNumber y) {super(x, y);}

    @Override
    protected void doCalculate() {
        Z=(long)X&(long)Y;
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


    protected void doCalculate(){

    }
}