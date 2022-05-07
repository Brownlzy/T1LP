package t1lp.operator;

import t1lp.calculator.Calculate;
import t1lp.calculator.MyNumber;
import t1lp.data.Config;
import t1lp.data.Data;
import t1lp.ui.MainWindow;

import java.util.Objects;

public class InputProcess {

    public static void dealInsert(String input) {
        if (!(Data.isOpen && !Data.isError)) {
            if (Config.isDebug) System.out.println("由于计算器未开机或出现错误，不予处理。");
            return;
        }
        String sOCT = "89AbCdEF.";
        String sDEC = "AbCdEF";
        String sHEX = ".";
        if (Data.ledNumber.getScale() == 8 && sOCT.contains(input) ||
                Data.ledNumber.getScale() == 10 && sDEC.contains(input) ||
                Data.ledNumber.getScale() == 16 && sHEX.contains(input) ||
                input.equals(".") && Data.ledNumber.contains(".") ||
                Data.ledNumber.contains(".") && Data.ledNumber.contains("-") && Data.ledNumber.length() >= 10 ||
                !Data.ledNumber.contains(".") && Data.ledNumber.contains("-") && Data.ledNumber.length() >= 9 ||
                Data.ledNumber.contains(".") && !Data.ledNumber.contains("-") && Data.ledNumber.length() >= 9 ||
                !Data.ledNumber.contains(".") && !Data.ledNumber.contains("-") && Data.ledNumber.length() >= 8) {
            if (Config.isDebug) System.out.println("未满足输入限制条件，ledNumber未改变");
        } else {
            if (Data.formula.size() == 2 && Objects.equals(Data.formula.get(Data.formula.size() - 1), "#")) {
                if (Config.isDebug)
                    System.out.println("[InputProgress][dealInsert]" + Data.formula.get(Data.formula.size() - 1));
                Data.formula.clear();
                Data.ledNumber.setNumber(0);
            }
            Data.ledNumber.append(input);
            Data.isNextNum = true;
            setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
        }
    }

    public static void dealCommand(String actionCommand) {
        if (!(actionCommand.equals("ON/C") || actionCommand.equals("OFF") || Data.isOpen && !Data.isError || Data.isOpen && actionCommand.equals("CE"))) {
            if (Config.isDebug) System.out.println("由于计算器未开机或出现错误，不予处理。");
            return;
        }
        MainWindow ui = Data.ui;
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
                Data.resetCalculator();
                Data.isOpen = false;
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
            case "(":

                break;
            case ")":

                break;
            case "SHF":

                break;
            case "1'sC":
                Data.ledNumber.setInverse();
                setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
                break;
            case "OR":
                //continueInput();
                addNumberToFormula();
                addOperatorToFormula("OR");
                break;
            case "AND":
                //continueInput();
                addNumberToFormula();
                addOperatorToFormula("AND");
                break;
            case "XOR":
                //continueInput();
                addNumberToFormula();
                addOperatorToFormula("XOR");
                break;
            case "CE":
                Data.resetCalculator();
                setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
                break;
            case "K":

                break;
            case "÷":
            case "/":
                //continueInput();
                addNumberToFormula();
                addOperatorToFormula("DIV");
                break;
            case "×":
            case "*":
                //continueInput();
                addNumberToFormula();
                addOperatorToFormula("MUL");
                break;
            case "-":
                //continueInput();
                addNumberToFormula();
                addOperatorToFormula("SUB");
                break;
            case "+":
                //continueInput();
                addNumberToFormula();
                addOperatorToFormula("ADD");
                break;
            case "=":
                if (Config.isDebug) System.out.println("Data.formula:" + Data.formula);
                addNumberToFormula();
                addOperatorToFormula("#");
                if (Config.isDebug) System.out.println("Data.formula:" + Data.formula);
                Data.result = Calculate.doCalculate(Data.formula);
                Data.result.setScale(Data.ledNumber.getScale());
                Data.formula.clear();
                Data.formula.add(Data.result.toString());
                Data.formula.add("#");
                Data.ledNumber = Data.result;
                setLcdScreen(Data.result, Data.isOpen, Data.isError);
                break;
            case "+/-":
                Data.ledNumber.changeSign();
                setLcdScreen(Data.ledNumber, Data.isOpen, Data.isError);
                break;
            case "BKS":

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + actionCommand);
        }
        if (Config.isDebug) System.out.println("Data.formula:" + Data.formula);
    }

    public static void addNumberToFormula() {
        if (Data.isNextNum) Data.formula.add(Data.ledNumber.toString());
    }

    public static void addOperatorToFormula(String operator) {
        if (Data.isNextNum) {
            if (Data.formula.size() == 2 && Objects.equals(Data.formula.get(Data.formula.size() - 1), "#")) {
                if (Config.isDebug)
                    System.out.println("[InputProgress][continueInput]" + Data.formula.get(Data.formula.size() - 1));
                Data.ledNumber.setNumber(Data.formula.get(0));
                Data.formula.clear();
            }
            Data.formula.add(operator);
            int scale = Data.ledNumber.getScale();
            Data.ledNumber.setNumber(0);
            Data.ledNumber.setScale(scale);
            Data.isNextNum = false;
        } else {
        Data.formula.remove(Data.formula.size() - 1);
            Data.formula.add(operator);
            int scale = Data.ledNumber.getScale();
            Data.ledNumber.setNumber(0);
            Data.ledNumber.setScale(scale);
            Data.isNextNum = false;
        }
    }

    public static void setLcdScreen(MyNumber myNumber, boolean isOpen, boolean isError) {
        //String s = "";
        //try {
            //s = myNumber.toStringL(8);
        //} catch (IllegalStateException e) {
            //Data.isError = true;
            //Data.ui.setLcdScreen(e.getMessage(),true);
        //}
        //if(s.startsWith("ERR"))
        //Data.t1lp.ui.refreshLcdScreen(s, scale, isOpen, Data.isError=true);
        //else
        Data.ui.setLedNumber(myNumber.toString());
        Data.ui.setLedState(isError);
        Data.ui.setPower(isOpen);
    }
}
