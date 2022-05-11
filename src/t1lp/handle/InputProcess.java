package t1lp.handle;

import t1lp.calculator.Calculate;
import t1lp.calculator.MyNumber;

import static t1lp.handle.Config.Log;

/**
 * 处理来自用户的输入
 *
 * @author Brownlzy
 * @version 1.0
 */
public class InputProcess {
    /**
     * 处理字符输入
     *
     * @param input 输入的字符（命令）
     * @author Brownlzy
     */
    public static void dealInsert(String input) {
        //判断计算器当前状态
        if (!(Data.isOpen && !Data.isError)) {
            Log("InputProcess", "dealInsert(String input:" + input + ")", "由于计算器未开机或出现错误，不予处理。");
            return;
        }
        String sOCT = "89AbCdEF.";
        String sDEC = "AbCdEF";
        String sHEX = ".";
        if (Data.ledNumber.getScale() == 8 && sOCT.contains(input)
                || Data.ledNumber.getScale() == 10 && sDEC.contains(input)
                || Data.ledNumber.getScale() == 16 && sHEX.contains(input) //输入的是对应进制不应该出现的字符
                || input.equals(".") && Data.ledNumber.contains(".") //第二次输入小数点
                || Data.inState==0&&Data.ledNumber.contains(".") && Data.ledNumber.contains("-") && Data.ledNumber.length() >= 10 //含有-和.的长度不超过10
                || Data.inState==0&&!Data.ledNumber.contains(".") && Data.ledNumber.contains("-") && Data.ledNumber.length() >= 9 //只含有-长度不超过9
                || Data.inState==0&&Data.ledNumber.contains(".") && !Data.ledNumber.contains("-") && Data.ledNumber.length() >= 9 //只含有.长度不超过9
                || Data.inState==0&&!Data.ledNumber.contains(".") && !Data.ledNumber.contains("-") && Data.ledNumber.length() >= 8 //不含有-和.长度不超过8
        ) {
            Log("InputProcess", "dealInsert(String input:" + input + ")", "未满足输入限制条件，ledNumber未改变");
        } else {
            //满足输入调件
            Log("InputProcess", "dealInsert(String input:" + input + ")", "Data.inState=" + Data.inState);
            int tmpScale = Data.ledNumber.getScale();
            switch (Data.inState) { //0：输入本次计算式，1：上次分步运算未结束，2：运算结束,3：运算符已输入
                case 0:
                    Data.ledNumber.append(input);
                    setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
                    break;
                case 1:
                case 2:
                    Data.inState = 0;
                    Data.formula.clear();
                    Data.ledNumber.setNumber(0);
                    Data.ledNumber.setScale(tmpScale);
                    Data.ledNumber.append(input);
                    setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
                    break;
                case 3:
                    Data.inState=0;
                    Data.ledNumber.setNumber(0);
                    Data.ledNumber.setScale(tmpScale);
                    Data.ledNumber.append(input);
                    setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
                    break;
                    default:
                    throw new UnsupportedOperationException("未知的inState");
            }
            Log("InputProcess", "dealInsert(String input:" + input + ")", "Data.formula:" + Data.formula + Data.ledNumber);
        }
    }

    /**
     * 处理命令输入
     *
     * @param actionCommand 按下的命令按钮
     * @author Brownlzy
     */
    public static void dealCommand(String actionCommand) {
        if (!(actionCommand.equals("ON/C") || actionCommand.equals("OFF") || Data.isOpen && !Data.isError)) {
            Log("InputProcess", "dealCommand(String actionCommand:" + actionCommand + ")", "由于计算器未开机或出现错误，不予处理。");
            return;
        }
        Log("InputProcess", "dealCommand(String actionCommand:" + actionCommand + ")", "Data.inState=" + Data.inState);
        switch (actionCommand) {
            case "HEX": {
                Data.ledNumber.setScale(16);
                setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
            }
            break;
            case "DEC": {
                Data.ledNumber.setScale(10);
                setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
            }
            break;
            case "OCT": {
                Data.ledNumber.setScale(8);
                setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
            }
            break;
            case "OFF":
                Data.isOpen = false;
                Data.resetCalculator();
                setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
                break;
            case "ON/C":
                Data.isOpen = true;
                Data.resetCalculator();
                setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
                break;
            case "STO":

                break;
            case "RCL":

                break;
            case "SUM":

                break;
            //0：等待输入数字（括号），1：等待输入数字或符号，2:等待更改运算符、输入括号或新数字，3：等待输入符号，4：计算完成等待输入数字或符号继续计算，5：等待输入数字，6：分步计算未算完
            case "(":
                if(Data.inState==0||Data.inState==3){
                if(Data.formula.size()==0||(Data.formula.get(Data.formula.size() - 1).equals("(")||Data.inState==3)){
                        Data.formula.add("(");}
                }else{
                    Data.formula.clear();
                    int tmpScale = Data.ledNumber.getScale();
                    Data.formula.add("(");
                    Data.ledNumber.setNumber(0);
                    Data.ledNumber.setScale(tmpScale);
                    setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
                }

                break;
            case ")":
                if(Data.inState==0&&Data.formula.size()>0&&Calculate.isOperator(Data.formula.get(Data.formula.size() - 1))){
                    Data.formula.add(Data.ledNumber.toString());
                    Data.formula.add(")");
                }else if(Data.inState==0&&Data.formula.size()>0&&Data.formula.get(Data.formula.size() - 1).equals(")")){
                    Data.formula.add(")");
                }
                break;
            case "1'sC":
                Data.ledNumber.toNOT();
                setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
                break;
            case "OR":
            case "AND":
            case "XOR":
            case "SHF":
                if (Data.ledNumber.getScale() == 10) {  //10进制下不支持位运算！
                    Log("InputProcess", "dealCommand(String actionCommand:" + actionCommand + ")", "10进制下不支持位运算！");
                    return;
                }
                addOperatorToFormula(actionCommand);
                break;
            case "CE":
                Data.ledNumber.cleanEntry();
                setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
                break;
            case "K":

                break;
            case "÷":
            case "/":
                addOperatorToFormula("DIV");
                break;
            case "×":
            case "*":
                addOperatorToFormula("MUL");
                break;
            case "-":
                addOperatorToFormula("SUB");
                break;
            case "+":
                addOperatorToFormula("ADD");
                break;
            case "=":
                addOperatorToFormula("#");
                Log("InputProcess", "dealCommand(String actionCommand:" + actionCommand + ")", "Data.formula:" + Data.formula + Data.ledNumber);
                if(Data.formula.size()==2&& Data.formula.get(1).equals("#"))
                {
                    Data.result.setNumber(Data.formula.get(0));
                    Data.inState = 2;//计算完毕
                    Data.formula.clear();
                    setLcdScreen(Data.result, Data.isOpen, Data.isError);
                    Data.ledNumber = Data.result;
                    break;
                }
                if (Data.calculate.isFinished()) {
                    try {
                        Data.calculate.setFormula(Data.formula);
                    } catch (RuntimeException e) {
                        Log("InputProcess", "dealCommand(String actionCommand:" + actionCommand + ")", "setFormula:" + e.getMessage());
                        Data.isError = true;
                        setLcdScreen(Data.ledNumber, Data.isOpen, true);
                    }
                }
                Data.result = Data.calculate.doCalculate();
                Data.result.setScale(Data.ledNumber.getScale());
                if (Data.calculate.isFinished()) {
                    Data.inState = 2;//计算完毕
                    Data.formula.clear();
                } else {
                    Data.inState = 1;//未计算完毕
                    Data.formula = Data.calculate.getFormula();
                }
                setLcdScreen(Data.result, Data.isOpen, Data.isError);
                Data.ledNumber = Data.result;

                break;
            case "+/-":
                if(Data.ledNumber.getScale()==10)
                    Data.ledNumber.changeSign();
                else
                    Data.ledNumber.toNOT2();
                setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
                break;
            case "BKS":
                Data.ledNumber.backSpace();
                setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
                break;
            default:
                throw new UnsupportedOperationException("Unexpected value: " + actionCommand);
        }
        Log("InputProcess", "dealCommand(String actionCommand:" + actionCommand + ")", "Data.formula:" + Data.formula + Data.ledNumber);
    }

    /**
     * 把运算符加入公式
     *
     * @param operator 输入的运算符
     * @author Brownlzy
     */
    public static void addOperatorToFormula(String operator) {
        switch (Data.inState) { //0：等待输入数字（括号），1：等待输入数字或符号，2:等待更改运算符、输入括号或新数字，3：等待输入符号，4：计算完成等待输入数字或符号继续计算，5：等待输入数字，6：分步计算未算完
            case 0:
                if (Data.formula.size()>0&&Data.formula.get(Data.formula.size() - 1).equals("(")) {
                    Data.formula.add(Data.ledNumber.toString());
                    Data.formula.add(operator);
                } else if (Data.formula.size()>0&&Data.formula.get(Data.formula.size() - 1).equals(")")) {
                    Data.formula.add(operator);
                } else {
                    Data.formula.add(Data.ledNumber.toString());
                    Data.formula.add(operator);
                }
                Data.inState=3;
                break;
            case 1:
                break;
            case 2:
                Data.inState = 0;
                Data.formula.clear();
                Data.formula.add(Data.result.toString());
                Data.formula.add(operator);
                Data.inState=3;
                break;
            case 3:
                Data.formula.remove(Data.formula.size() - 1);
                Data.formula.add(operator);
                break;
            default:
                throw new UnsupportedOperationException("未知的inState");
        }
    }

    /**
     * 设置显示屏显示内容
     *
     * @param myNumber 需要在LCD屏幕上显示的数字
     * @param isOpen   计算器电源状态
     * @param isError  是否显示E标
     * @author Brownlzy
     */
    public static void setLcdScreen(MyNumber myNumber, boolean isOpen, boolean isError) {
        String strNumber = myNumber.toString();
        try {
            strNumber = formatNumber(strNumber);
        } catch (RuntimeException e) //检查溢出错误
        {
            strNumber = e.getMessage();
            Data.isError = true;
            isError = true;
        }
        Data.ui.setLedNumber(strNumber);
        Data.ui.setLedState(isError);
        Data.ui.setPower(isOpen);
    }

    /**
     * 格式化数字字符串，使其适合LCD显示屏
     *
     * @param strLedNumber 待处理的字符串
     * @return java.lang.String
     * @author Brownlzy
     */
    private static String formatNumber(String strLedNumber) {
        Log("InputProcess", "formatNumber(String strLedNumber:" + strLedNumber + ")", "格式化之前：" + strLedNumber);
        String strNum = strLedNumber.substring(0, 5);
        String n = strLedNumber.substring(5);
        boolean isNegative = n.contains("-");
        boolean isDouble = n.contains(".");
        if (isNegative) {
            n = n.substring(1);
            strNum = strNum + '-';
        }
        if (!isDouble) {
            n = n + ".";
        }
        int dotIndex = n.split("\\.")[0].length();
        if (dotIndex == 1 && n.contains("E") && isDouble) { //如果传入的是科学计数法
            strNum = strNum + n.substring(0, Integer.min(n.length() - 1, 8) - n.split("E")[1].length()) + "E" + n.split("E")[1];
            throw new RuntimeException(strNum);
        } else if (dotIndex == 8) { //整数位数等于8
            strNum = strNum + n.split("\\.")[0];
        } else if (dotIndex < 8) {  //整数位数小于8
            if (isDouble)
                strNum = strNum + n.split("\\.")[0] + '.' + n.substring(dotIndex + 1, Integer.min(9, n.length()));
            else
                strNum = strNum + n.split("\\.")[0];
        } else { //dotIndex > 8 //整数位数大于8
            strNum = strNum + n.charAt(0) + '.' + n.substring(2, 8 - String.valueOf(dotIndex - 1).length()) + 'E' + (dotIndex - 1);
            throw new RuntimeException(strNum);
        }
        Log("InputProcess", "formatNumber(String strLedNumber:" + strLedNumber + ")", "格式化之后：" + strNum);
        return strNum;
    }
}
