package t1lp.calculator;

abstract class Operator {
    protected double X;
    protected double Y;
    protected double Z;
    public Operator(MyNumber x,MyNumber y){
        X=Double.parseDouble(x.toString(10).substring(5));
        Y=Double.parseDouble(y.toString(10).substring(5));
        doCalculate();
    }
    protected abstract void doCalculate();
    public MyNumber getResult(){
        return new MyNumber(Z);
    }
}

class Add extends Operator{

    public Add(MyNumber x, MyNumber y) {
        super(x, y);
    }

    @Override
    protected void doCalculate() {
        Z=X+Y;
    }
}

class Sub extends Operator{

    public Sub(MyNumber x, MyNumber y) {
        super(x, y);
    }

    @Override
    protected void doCalculate() {
        Z=X-Y;
    }
}

class Mul extends Operator{

    public Mul(MyNumber x, MyNumber y) {super(x, y);}

    @Override
    protected void doCalculate() {
        Z=X*Y;
    }
}

class Div extends Operator{

    public Div(MyNumber x, MyNumber y) {super(x, y);}

    @Override
    protected void doCalculate() {
        Z=X/Y;
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
        Z=(long)X^(long)Y;
    }
}