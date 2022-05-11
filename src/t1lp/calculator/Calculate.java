package t1lp.calculator;

import java.util.*;

public class Calculate {
    private static final List<String> operator = Arrays.asList("ADD", "SUB", "MUL", "DIV","OR","AND","XOR","SHF");
    private static List<String> suffix =new ArrayList<>();
    public static MyNumber doCalculate(List<String>formula) {
        suffix=doTransform(formula);
        return new MyNumber(calculate(suffix));
    }
    /**
     * 根据后缀表达式list计算结果
     * @author Brownlzy
     * @param list 要计算的后缀表达式
     * @return String
     */
    private static String calculate(List<String> list) {
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
                        res = new Shf(num1, num2).getResult();
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
     * @author Brownlzy
     * @param infix 中缀表达式
     * @return java.util.List java.lang.String
     */
    private static List<String> doTransform(List<String> infix) {

        Stack<String> operatorStack=new Stack<>();
        List<String> suffix =new ArrayList<>();

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
     * @author Brownlzy
     * @return boolean
     * @param op 要判断的字符串
     */
    public static boolean isOperator(String op) {
        return operator.contains(op);
    }

    /**
     * 判断是否为数字
     * @author Brownlzy
     * @return boolean
     * @param num 要判断的字符串
     */
    public static boolean isNumber(String num) {
        return num.startsWith("NUM");
    }

    /**
     * 获取操作符的优先级
     * @author Brownlzy
     * @return boolean
     * @param op 要判断的字符串
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

