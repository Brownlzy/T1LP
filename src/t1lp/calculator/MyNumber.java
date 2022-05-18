package t1lp.calculator;

import t1lp.handle.Config;

import java.util.Objects;

import static t1lp.handle.Config.Log;

public class MyNumber {
    private String number;
    private int radix;

    public MyNumber(double num) {
        setNumber(num);
    }

    public MyNumber(Integer num) {
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
        radix = 10;
        Log("MyNumber", "setNumber(double num:" + num + ")", "String number = " + number);
    }

    public void setNumber(Integer num) {
        number = String.valueOf(num);
        radix = 10;
    }

    public void setNumber(String myNumber) {
        if (myNumber.startsWith("NUM")) {
            switch (myNumber.substring(3, 5)) {
                case "0o":
                    radix = 8;
                    number = myNumber.substring(5).split("\\.")[0];
                    break;
                case "0d":
                    setNumber(Double.parseDouble(myNumber.substring(5)));
                    break;
                case "0x":
                    radix = 16;
                    number = myNumber.substring(5).split("\\.")[0];
                    break;
                case "0b":
                    radix = 2;
                    number = myNumber.substring(5).split("\\.")[0];
                default:
            }
        } else {
            if (Config.isDebug) System.out.println("Number Error");
        }
    }

    public String toString() {
        return toString(radix);
    }

    public String toString(int s) {
        //Todo:负数进制转换有问题
        Log("MyNumber", "toString(int s:" + s + ")", radix + " " + number);
        if (s != radix)
            setRadix(s);
        switch (radix) {
            case 10:
                return "NUM0d" + number;
            case 8:
                return "NUM0o" + number;
            case 16:
                return "NUM0x" + number;
            case 2:
                return "NUM0b" + number;
        }
        return null;
    }

    public int getRadix() {
        return radix;
    }
    public String toRadixString(int num,int radix){
        return null;
    }
    public void setRadix(int s) {
        if (s == 10 && radix == 10) return;
        number = Integer.toString(valueOf(number.split("\\.")[0], radix), s);
        radix = s;
        switch (s){
            case 16:
                if(number.contains("-"))
                {
                    char[] tmp=new char[8];
                    for (int i=7;i>=0;i--){
                        if(number.length()+i-8>0){
                            tmp[i]=number.charAt(number.length()+i-8);
                        }else{
                            tmp[i]='0';
                        }
                    }
                    tmp[0]=Integer.toHexString(Integer.valueOf(String.valueOf(tmp[0]),16)+8).charAt(0);
                    number=String.valueOf(tmp);
                }
        }
    }

    public boolean isPositive() {
        return isPositive(number,radix);
    }

    public boolean isPositive(String number,int radix) {
        boolean result;
        switch (radix){
            case 10:
                result=!(number.contains("-"));
                break;
            case 2:
                result=!(number.length()>=32&&number.charAt(0)>='1');
                break;
            case 8:
                result=!(number.length()>=8&&number.charAt(0)>='4');
                break;
            case 16:
                result=!(number.length()>=8&&number.charAt(0)>='8');
                break;
            default:
                result =true;
        }
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

    private int valueOf(String s,int radix){
        String tmp="";
        tmp+=s;
        switch (radix) {
            case 2:
                if (!isPositive(s, radix)) {
                    tmp = "-" + s.substring(1);
                }
                return Integer.valueOf(tmp, radix);
            case 8:
                if (!isPositive(s, radix)) {
                    tmp = "-"+(s.charAt(0)-4) + s.substring(1);
                }
                return Integer.valueOf(tmp, radix);
            case 16:
                if (!isPositive(s, radix)) {
                    tmp = "-" +Integer.toHexString(Integer.valueOf(String.valueOf(s.charAt(0)),16)-8) + s.substring(1);
                }
                return Integer.valueOf(tmp, radix);
            case 10:
                    return Integer.valueOf(tmp, radix);
            default:
                return 0;
        }
    }

    /**
     * 追加输入
     *
     * @param n 新加的字符
     * @author Brownlzy
     */
    public void append(String n) {
        n=n.toLowerCase();
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
        System.out.println(Long.parseLong(number, radix) + "---" + ~Long.parseLong(number, radix));
        number = Long.toString(~Long.parseLong(number, radix), radix);
    }

    private int findZero(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Objects.equals(number.charAt(i), '0'))
                return i;
        }
        return 0;
    }

    public void toNOT() {
        int s= radix;
        toString(2);
        String result = "";
        Log("MyNumber","toNOT()","number="+number);
        switch (s){
            case 16:
                for (int i = 0; i < 32; i++) {
                    if (i > 31 - number.length())
                        if(number.charAt(i - 32 + number.length())=='-')
                            result=result+'0';
                        else
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
        setRadix(s);
    }
    public void toNOT2(){
        int s= radix;
        toNOT();
        setRadix(2);
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
        setRadix(s);
    }
}

