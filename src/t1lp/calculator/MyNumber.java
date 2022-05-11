package t1lp.calculator;

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
                    number = myNumber.substring(5).split("\\.")[0];
                    break;
                case "0d":
                    setNumber(Double.parseDouble(myNumber.substring(5)));
                    break;
                case "0x":
                    scale = 16;
                    number = myNumber.substring(5).split("\\.")[0];
                    break;
                case "0b":
                    scale = 2;
                    number = myNumber.substring(5).split("\\.")[0];
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
        //Todo:负数进制转换有问题
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
                    String binstr = Long.toBinaryString(Long.valueOf(number, 8));
                    String result = "";
                    for (int i = 0; i < 8; i++) {
                        result += Long.toHexString(Long.valueOf(binstr.substring(8 + 3 * i, 8 + 3 * (i + 1)), 2));
                    }
                    System.out.println(binstr + "--" + binstr.length());
                    return "NUM0o" + result;
                }
            case 16:
                if (isPositive()) {
                    return "NUM0x" + Long.toString(Long.valueOf(number.split("\\.")[0], scale), s);
                } else {
                    String binstr = Long.toBinaryString(Long.valueOf(number, 16));
                    String result = "";
                    for (int i = 0; i < 8; i++) {
                        result += Long.toHexString(Long.valueOf(binstr.substring(4 * i, 4 * (i + 1)), 2));
                    }
                    System.out.println(binstr + "--" + binstr.length());
                    return "NUM0x" + result;
                }
            case 2:
                String binstr = Long.toBinaryString(Long.valueOf(number, scale));
                if (binstr.length() != 32) {
                    for (int i = binstr.length(); i < 32; i++) {
                        binstr = "0" + binstr;
                    }
                }
                Log("MyNumber", "toString(int s:" + s + ")", 2 + " " + binstr);
                return "NUM0b" + binstr;
        }
        return null;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int s) {
        if (s == 10 && scale == 10) return;
        number = Long.toString(Long.valueOf(number.split("\\.")[0], scale), s);
        scale = s;
    }

    public boolean isPositive() {
        int s = scale;
        boolean result;
        result = !toString(10).contains("-");
        setScale(s);
        return result;
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
        if (isPositive()) {
            if (number.length() == 1) {
                number = "0";
            } else {
                number = number.substring(0, number.length() - 1);
            }
        } else {
            if (Objects.equals(number, "-0.")) {
                number = "0";
            } else if (number.length() == 2) {
                number = "0";
            } else {
                number = number.substring(0, number.length() - 1);
            }
        }
    }

    public void cleanEntry() {
        number = "0";
        Log("MyNumber", "cleanEntry()", "");
    }

    public int length() {
        return number.length();
    }

    public boolean contains(String m) {
        return number.contains(m);
    }

    public void setInverse() {
        System.out.println(Long.parseLong(number, scale) + "---" + ~Long.parseLong(number, scale));
        number = Long.toString(~Long.parseLong(number, scale), scale);
    }

    private int findZero(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Objects.equals(number.charAt(i), '0'))
                return i;
        }
        return 0;
    }

    public void toNOT() {
        int s=scale;
        toString(2);
        String result = "";
        switch (s){
            case 16:
                for (int i = 0; i < 32; i++) {
                    if (i > 31 - number.length())
                        result = result + ('1' - number.charAt(i - 32 + number.length()));
                    else
                        result = result + '1';
                }
                setNumber("NUM0b" + result);
                break;
            case 8:
                for (int i = 0; i < 24; i++) {
                    if (i > 23 - number.length())
                        result = result + ('1' - number.charAt(i - 24 + number.length()));
                    else
                        result = result + '1';
                }
                setNumber("NUM0b" + result);
                break;
        }
        setScale(s);
    }
    public void toNOT2(){
        int s=scale;
        toNOT();
        setScale(2);
        char[] result;
        switch (s) {
            case 16:
                result = new char[32];
                for (int i = 31; i >= 0; i--) {
                    if (i > 31 - number.length())
                        result[i] = number.charAt(i - 32 + number.length());
                    else
                        result[i] = '0';
                }
                for (int i = 31; i >= 0; i--) {
                    if (result[i] == '0') {
                        result[i] = '1';
                        break;
                    } else if (result[i] == '1') {
                        result[i] = '0';
                        continue;
                    }
                    throw new RuntimeException("");
                }
                setNumber("NUM0b" + String.valueOf(result));
                break;
            case 8:
                result = new char[24];
                for (int i = 23; i >= 0; i--) {
                    if (i > 23 - number.length())
                        result[i] = number.charAt(i - 24 + number.length());
                    else
                        result[i] = '0';
                }
                for (int i = 23; i >= 0; i--) {
                    if (result[i] == '0') {
                        result[i] = '1';
                        break;
                    } else if (result[i] == '1') {
                        result[i] = '0';
                        continue;
                    }
                    throw new RuntimeException("");
                }
                setNumber("NUM0b" + String.valueOf(result));

                break;
        }
        setScale(s);
    }
}

