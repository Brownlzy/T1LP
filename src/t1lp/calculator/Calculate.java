package t1lp.calculator;

import java.util.*;

import static t1lp.handle.Config.Log;

public class Calculate {
    private List<String> myFormula;
    private static final List<String> operator = Arrays.asList("ADD", "SUB", "MUL", "DIV", "OR", "AND", "XOR","SHF");
    private List<String> suffix = new ArrayList<>();

    public Calculate() {
        myFormula = Arrays.asList("NUM0d0", "#");
    }

    public Calculate(List<String> list) {
        setFormula(list);
    }

    public void setFormula(List<String> list) {
        Log("Calculate", "setFormula(List<String> list:" + list + ")", "");
        String symbolStr="";
        for (String s : list) {
            if (isOperator(s))
                symbolStr += "+";
            else if (isNumber(s))
                symbolStr += "1";
            else if (s.equals("("))
                symbolStr += "(";
            else if (s.equals(")"))
                symbolStr += ")";
        }
        Log("Calculate", "setFormula(List<String> list:" + list + ")", symbolStr);
        if(!isTrue(symbolStr)) {
            throw new RuntimeException("");
        }else {
            myFormula = list;
        }
    }
    /**
     * 表达式合法性检测
     *
     * @param str 待判断的表达式
     * @return boolean
     * @author ConradLee
    */
    public static boolean isTrue(String str){
        int flag=0;
        if (str.matches("(\\(*\\d+[+/*-])+((\\(*(\\d+[+/*-])*\\d+\\)*)[+/*-])*\\d+\\)*")) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == '(') {
                    flag++;
                }
                if (c == ')') {
                    flag--;
                }
            }
            return flag == 0;
        }
        return false;
    }

    /**
     * 对表达式中的括号进行优先级定义
     *
     * @return String
     * @author ConradLee
     */
    public MyNumber doCalculate() {
        int[] priorityArr = new int[myFormula.size()];
//        括号优先级
        int p = 0;
        String flag = "(";
        for (int i = 0; i < myFormula.size() - 1; i++) {
//            不是括号的优先级为-1
            Log("", "", myFormula.get(i));
            if (!Objects.equals(myFormula.get(i), "(") && !Objects.equals(myFormula.get(i), ")"))
                priorityArr[i] = -1;
//            若识别到的括号和上一个括号相同并且都是左括号，优先级+1
            else if (myFormula.get(i).equals(flag) && flag.equals("(")) {
                priorityArr[i] = ++p;
                flag = myFormula.get(i);
            }
//            若识别到的括号和上一个括号相同并且都是有括号，优先级-1
            else if (myFormula.get(i).equals(flag) && flag.equals(")")) {
                priorityArr[i] = --p;
                flag = myFormula.get(i);
            }
//            若识别到的括号和上一个括号不同，优先级不变
            else if (!myFormula.get(i).equals(flag)) {
                priorityArr[i] = p;
                flag = myFormula.get(i);
            }
        }
        priorityArr[myFormula.size() - 1] = -2;
//      寻找优先级最大的括号下标
        int maxPriority = -1;
        int leftAimBlock = 0;
        int rightAimBlock = 0;
        for (int j : priorityArr) {
            if (j > maxPriority) {
                maxPriority = j;
            }
        }
        for (int i = 0; i < priorityArr.length; i++) {
            if (priorityArr[i] == maxPriority && myFormula.get(i).equals("(")) {
                leftAimBlock = i;
                break;
            }
        }
        for (int i = 0; i < priorityArr.length; i++) {
            if (priorityArr[i] == maxPriority && myFormula.get(i).equals(")")) {
                rightAimBlock = i;
                break;
            }
        }
        if (maxPriority != -1) {
//        优先级最大的括号之间的内容放到tempFormula中
            List<String> tempFormula = new ArrayList<>();
            for (int i = leftAimBlock + 1; i < rightAimBlock; i++) {
                tempFormula.add(myFormula.get(i));
            }
            MyNumber result = doCalculate(tempFormula);
            myFormula.set(leftAimBlock, String.valueOf(result));
            for (int i = leftAimBlock + 1; i <= rightAimBlock; rightAimBlock--) {
                myFormula.remove(i);
            }
            return result;
        } else {
            MyNumber result = doCalculate(myFormula);
            myFormula = Arrays.asList(result.toString(), "#");
            return result;
        }
    }

    /**
     * 判断表达式是否读取完毕
     *
     * @return boolean
     * @author ConradLee
     */
    public boolean isFinished() {
        return myFormula.size() == 2 && myFormula.get(1).equals("#");
    }

    /**
     * 获取表达式
     *
     * @return List
     * @author ConradLee
     */
    public List<String> getFormula() {
        return myFormula;
    }

    /**
     * 执行运算
     *
     * @param formula 表达式
     * @author ConradLee
     */
    private MyNumber doCalculate(List<String> formula) {
        Log("Calculate", "MyNumber doCalculate(List<String>formula:" + formula + ")", "");
        suffix = doTransform(formula);
        return new MyNumber(calculate(suffix));
    }

    /**
     * 根据后缀表达式list计算结果
     *
     * @param list 要计算的后缀表达式
     * @return String
     * @author Brownlzy
     */
    private static String calculate(List<String> list) {
        Log("Calculate", "String calculate(List<String> list:" + list + ")", "");
        Stack<String> stack = new Stack<>();
        for (String item : list) {
            if (isNumber(item)) {
                //是数字
                stack.push(item);
            } else {
                //是操作符，取出栈顶两个元素
                MyNumber num2 = new MyNumber(stack.pop());
                MyNumber num1 = new MyNumber(stack.pop());
                MyNumber res;
                switch (item) {
                    case "ADD":
                        res = new Add(num1, num2).getResult();
                        break;
                    case "SUB":
                        res = new Sub(num1, num2).getResult();
                        break;
                    case "MUL":
                        res = new Mul(num1, num2).getResult();
                        break;
                    case "DIV":
                        res = new Div(num1, num2).getResult();
                        break;
                    case "OR":
                        res = new Or(num1, num2).getResult();
                        break;
                    case "AND":
                        res = new And(num1, num2).getResult();
                        break;
                    case "XOR":
                        res = new Xor(num1, num2).getResult();
                        break;
                    case "SHF":
                        res=new Shf(num1,num2).getResult();
                        break;
                    default:
                        throw new RuntimeException("运算符错误！");
                }
                stack.push(res.toString());
            }
        }
        return stack.pop();
    }

    /**
     * 中缀表达式转后缀表达式
     *
     * @param infix 中缀表达式
     * @return java.util.List java.lang.String
     * @author Brownlzy
     */
    private static List<String> doTransform(List<String> infix) {

        Stack<String> operatorStack = new Stack<>();
        List<String> suffix = new ArrayList<>();

        for (String item : infix) {
            //得到数或操作符
            if (Objects.equals(item, "#")) break;
            //结束标志
            if (isOperator(item)) {
                //是操作符 判断操作符栈是否为空
                if (operatorStack.isEmpty() || "(".equals(operatorStack.peek()) || priority(item) > priority(operatorStack.peek())) {
                    //为空或者栈顶元素为左括号或者当前操作符大于栈顶操作符直接压栈
                    operatorStack.push(item);
                } else {
                    //否则将栈中元素出栈如队，直到遇到大于当前操作符或者遇到左括号时
                    while (!operatorStack.isEmpty() && !"(".equals(operatorStack.peek())) {
                        if (priority(item) <= priority(operatorStack.peek())) {
                            suffix.add(operatorStack.pop());
                        }
                    }
                    //当前操作符压栈
                    operatorStack.push(item);
                }
            } else if (isNumber(item)) {
                //是数字则直接入队
                suffix.add(item);
            } else if ("(".equals(item)) {
                //是左括号，压栈
                operatorStack.push(item);
            } else if (")".equals(item)) {
                //是右括号 ，将栈中元素弹出入队，直到遇到左括号，左括号出栈，但不入队
                while (!operatorStack.isEmpty()) {
                    if ("(".equals(operatorStack.peek())) {
                        operatorStack.pop();
                        break;
                    } else {
                        suffix.add(operatorStack.pop());
                    }
                }
            } else {
                throw new RuntimeException("有非法字符！");
            }
        }
        //循环完毕，如果操作符栈中元素不为空，将栈中元素出栈入队
        while (!operatorStack.isEmpty()) {
            suffix.add(operatorStack.pop());
        }
        return suffix;
    }


    /**
     * 判断字符串是否为操作符
     *
     * @param op 要判断的字符串
     * @return boolean
     * @author Brownlzy
     */
    public static boolean isOperator(String op) {
        return operator.contains(op);
    }

    /**
     * 判断是否为数字
     *
     * @param num 要判断的字符串
     * @return boolean
     * @author Brownlzy
     */
    public static boolean isNumber(String num) {
        return num.startsWith("NUM");
    }

    /**
     * 获取操作符的优先级
     *
     * @param op 要判断的字符串
     * @return boolean
     * @author Brownlzy
     */
    public static int priority(String op) {
        if (op.equals("MUL") || op.equals("DIV")) {
            return 1;
        } else if (op.equals("ADD") || op.equals("SUB")) {
            return 0;
        }
        return -1;
    }


}

