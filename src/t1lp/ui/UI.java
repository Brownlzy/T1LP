package t1lp.ui;

public interface UI {
    /**
     * 修改LCD显示屏上的数字和数制
     * @author Brownlzy
     * @param number 带有数字进制的字符串，超出长度自动处理 e.g.
     *              NUM0d-12345678.90 NUM0o12345670 NUM0x1234567890ABCDEF
     */
    public void setLedNumber(String number);
    /**
     * 显示或隐藏LCD显示屏上的错误E标
     * @author Brownlzy
     * @param isError 是否在LCD屏上显示错误E标
     */
    public void setLedState(boolean isError);
    /**
     * 同时设置LCD数字和功能区
     * @author Brownlzy
     * @param ledNumber 带有数字进制的字符串
     * @param isError 是否在LCD屏上显示错误E标
     */
    public void setLcdScreen(String ledNumber, boolean isError);
    /**
     * 设置电源
     * @author Brownlzy
     * @param isOpen 电源是否开启
     */
    public void setPower(boolean isOpen);
}
