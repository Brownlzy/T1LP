package t1lp.calculator;

import sun.util.locale.provider.LocaleServiceProviderPool;
import t1lp.handle.Config;

import java.util.Objects;

import static t1lp.handle.Config.Log;

public class MyNumber {
    private String number;
    private int scale;

    public MyNumber(double num) {
        setNumber(num);
    }

    public MyNumber(long num) {
        setNumber(num);
    }

    public MyNumber(String myNumber) {
        setNumber(myNumber);
    }

    public void setNumber(double num) {
        if (Config.isDebug) System.out.println("[MyNumber][setNumber(num)]double num = " + num);
        number = String.valueOf(num);
        for (int i = number.length() - 1; i > 0; i--) {
            if (number.charAt(i) == '0') {
                number = number.substring(0, number.length() - 1);
            } else if (number.charAt(i) == '.') {
                number = number.split("\\.")[0];
                break;
            } else {
                break;
            }
        }
        scale = 10;
        Log("MyNumber", "setNumber(double num:" + num + ")", "String number = " + number);
    }

    public void setNumber(long num) {
        number = String.valueOf(num);
        scale = 10;
    }

    public void setNumber(String myNumber) {
        if (myNumber.startsWith("NUM")) {
            switch (myNumber.substring(3, 5)) {
                case "0o":
                    scale = 8;
                    //number = String.valueOf(Long.valueOf(myNumber.substring(5), 8));
                    number = myNumber.substring(5).split("\\.")[0];
                    break;
                case "0d":
                    //if(myNumber.contains("E"))
                    //scale = 10;
                    setNumber(Double.parseDouble(myNumber.substring(5)));
                    break;
                case "0x":
                    scale = 16;
                    number = myNumber.substring(5).split("\\.")[0];
                    //number = String.valueOf(Long.valueOf(myNumber.substring(5), 16));
                    break;
                default:
            }
        } else {
            if (Config.isDebug) System.out.println("Number Error");
        }
    }

    public String toString() {
        return toString(scale);
    }

    public String toString(int s) {
        Log("MyNumber", "toString(int s:" + s + ")", scale + " " + number);
        if (s != scale)
            setScale(s);
        switch (scale) {
            case 10:
                return "NUM0d" + number;
            case 8:
                if (isPositive()) {
                    return "NUM0o" + Long.toString(Long.valueOf(number.split("\\.")[0], scale), s);
                } else {
                    String binstr = Integer.toBinaryString(Integer.valueOf(number, 8));
                    String result = "";
                    for (int i = 0; i < 8; i++) {
                        result += Integer.toHexString(Integer.valueOf(binstr.substring(8 + 3 * i, 8 + 3 * (i + 1)), 2));
                    }
                    System.out.println(binstr + "--" + binstr.length());
                    return "NUM0o" + result;
                }
            case 16:
                if (isPositive()) {
                    return "NUM0x" + Long.toString(Long.valueOf(number.split("\\.")[0], scale), s);
                } else {
                    String binstr = Integer.toBinaryString(Integer.valueOf(number, 16));
                    String result = "";
                    for (int i = 0; i < 8; i++) {
                        result += Integer.toHexString(Integer.valueOf(binstr.substring(4 * i, 4 * (i + 1)), 2));
                    }
                    System.out.println(binstr + "--" + binstr.length());
                    return "NUM0x" + result;
                }
        }
        return null;
    }

    public String toStringL(int length) {
        String n = toString(scale).substring(5);
        boolean isError = false;
        int m = n.replace("-", "").split("\\.")[0].length() - 1;
        String pureNum = n.replace("-", "").replace(".", "");
        if (n.replace("-", "").replace(".", "").length() > length) {
            if (n.contains("-"))
                n = "-";
            else n = "";
            n += pureNum.charAt(0) + "." + pureNum.substring(1, length - 1 - String.valueOf(m).length()) + "E" + m;
            isError = true;
            throw new IllegalStateException(scale == 10 ? ("NUM0d" + n) : ("NUM0" + (scale == 8 ? "o" : "x") + n));
        }
        if (scale == 10)
            return (isError ? "ERR0d" : "NUM0d") + n;
        else
            return (isError ? "ERR0" : "NUM0") + (scale == 8 ? "o" : "x") + n;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int s) {
        //ToDo:16进制有问题
        if (s == 10 && scale == 10) return;
        number = Long.toString(Long.valueOf(number.split("\\.")[0], scale), s);
        scale = s;
    }

    public boolean isPositive() {
        return !number.startsWith("-");
    }

    public void changeSign() {
        if (Objects.equals(number, "0") || Objects.equals(number, "0.")) {
            number = "0";
        } else if (number.startsWith("-")) {
            number = number.substring(1);
        } else {
            number = "-" + number;
        }
    }

    /**
     * 追加输入
     *
     * @param n 新加的字符
     * @author Brownlzy
     */
    public void append(String n) {
        if (number.equals("0")) {
            if (n.equals("."))
                number += n;
            else
                number = n;
        } else if (number.equals("-0")) {
            if (n.equals("."))
                number += n;
            else
                number = "-" + n;
        } else {
            number += n;
        }
    }

    public void backSpace() {
        if (number.length() == 1) {
            number = "0";
        } else {
            number = number.substring(0, number.length() - 1);
        }
    }

    public void cleanEntry() {
        number = "0";
        Log("MyNumber", "cleanEntry()", "");
    }

    public int length() {
        return number.length();
    }


    public boolean isOverflow(int length) {
        String pureNum = number.replace("-", "").replace(".", "");
        return number.replace("-", "").replace(".", "").length() > length;
    }

    public boolean contains(String m) {
        return number.contains(m);
    }

    public void setInverse() {
        System.out.println(Long.parseLong(number, scale) + "---" + ~Long.parseLong(number, scale));
        number = Long.toString(~Long.parseLong(number, scale), scale);
    }
}

