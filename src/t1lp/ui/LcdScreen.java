package t1lp.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 绘制计算器LCD液晶显示面板，派生自JPanel
 *
 * @author Brownlzy
 * @version 1.0
 */
class LcdScreen extends JPanel {
    private JLabel labNumber;   //显示LED图片的JLabel控件
    private LedNumber ledNumber;      //用于生成LED数字图片的对象
    private LedLabel labFunction;   //显示数制、E标的JLabel控件

    /**
     * 构造函数，初始化UI
     *
     * @author Brownlzy
     */
    public LcdScreen() {
        initGUI();
        //修改Swing显示风格为Windows
        String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(LcdScreen.this);
    }

    /**
     * 初始化LCD面板UI
     *
     * @author Brownlzy
     */
    private void initGUI() {
        //设置边框布局
        this.setLayout(new BorderLayout());
        labNumber = new JLabel();
        //初始化对象，设置生成LED图片颜色
        ledNumber = new LedNumber(Color.black, new Color(240, 240, 240), Color.white, 36, 63, 7);
        //添加到上方
        this.add(labNumber, BorderLayout.NORTH);

        labFunction = new LedLabel();
        //添加到下方
        this.add(labFunction, BorderLayout.SOUTH);
    }

    /**
     * 修改LED数字显示内容
     *
     * @param strLedNumber 新的LED数字字符串 NUM0d123456789.0
     * @author Brownlzy
     */
    public void changeLedNumber(String strLedNumber) {
        if (isMyNumber(strLedNumber)) { //是符合标准的数字
            int scale = getScale(strLedNumber);
            //使数字符合LED显示的宽度
            String number = formatNumber(strLedNumber);
            //显示数字，宽度为9
            labNumber.setIcon(new ImageIcon(ledNumber.getLedImage(number, 9)));
            labFunction.changeState(scale);
        } else {    //不符合标准，抛出错误
            System.out.println(strLedNumber);
            throw new UnsupportedOperationException();
        }
    }

    /**
     * 获取数制信息
     *
     * @param strLedNumber 数字（NUM0d123456789.0）
     * @return int
     * @author Brownlzy
     */
    private int getScale(String strLedNumber) {
        switch (strLedNumber.charAt(4)) {
            case 'd':
                return 10;
            case 'o':
                return 8;
            case 'x':
                return 16;
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * 格式化数字字符串，使其适合LCD显示屏
     *
     * @param strLedNumber 待处理的字符串
     * @return java.lang.String
     * @author Brownlzy
     */
    private String formatNumber(String strLedNumber) {
        String strNum = "";
        String n = strLedNumber.substring(5);
        boolean isNegative = n.contains("-");
        boolean isDouble = n.contains(".");
        if (isNegative) {
            n = n.substring(1);
            //strNum=strNum+'-';
        }
        if (!isDouble) {
            n = n + ".";
        }
        String pureNum = n.replace("-", "").replace(".", "");

        int dotIndex = n.split("\\.")[0].length();
        if (dotIndex == 8) {
            strNum = strNum + n.split("\\.")[0];
        } else if (dotIndex < 8) {
            if (isDouble)
                strNum = strNum + n.split("\\.")[0] + '.' + n.substring(dotIndex, Integer.min(9, n.length()));
            else
                strNum = strNum + n.split("\\.")[0];
        } else if (dotIndex > 8) {
            strNum = strNum + n.charAt(0) + '.' + n.substring(2, 8 - String.valueOf(dotIndex - 1).length()) + 'E' + (dotIndex - 1);
        }
        return strNum;
    }

    /**
     * 判断数字字符串是否符号标准
     *
     * @param strLedNumber 数字（NUM0d123456789.0）
     * @return boolean
     * @author Brownlzy
     */
    private boolean isMyNumber(String strLedNumber) {
        if (strLedNumber.startsWith("NUM0")) {
            switch (strLedNumber.charAt(4)) {
                case 'd':
                    for (int i = 5; i < strLedNumber.length(); i++) {
                        if (!"-0123456789.".contains(String.valueOf(strLedNumber.charAt(i))))
                            return false;
                    }
                    return true;
                case 'x':
                    for (int i = 5; i < strLedNumber.length(); i++) {
                        if (!"0123456789ABCDEFabcdef".contains(String.valueOf(strLedNumber.charAt(i))))
                            return false;
                    }
                    return true;
                case 'o':
                    for (int i = 5; i < strLedNumber.length(); i++) {
                        if (!"01234567".contains(String.valueOf(strLedNumber.charAt(i))))
                            return false;
                    }
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 修改LCD功能区显示状态
     *
     * @param scale   新的进制
     * @param isError 是否出现错误
     * @author Brownlzy
     */
    private void changeLedState(int scale, boolean isError) {
        labFunction.changeState(scale, isError);
    }

    /**
     * 修改LCD功能区显示状态
     *
     * @param isError 是否出现错误
     * @author Brownlzy
     */
    void changeLedState(boolean isError) {
        labFunction.changeState(isError);
    }

    /**
     * 隐藏LCD屏内容
     *
     * @author Brownlzy
     */
    void hideMe() {
        labNumber.setVisible(false);
        labFunction.setVisible(false);
    }

    /**
     * 显示LCD屏内容
     *
     * @author Brownlzy
     */
    void showMe() {
        labNumber.setVisible(true);
        labFunction.setVisible(true);
    }

    /**
     * 显示计算器功能区状态的JLabel
     *
     * @author Brownlzy
     * @version 2.0
     */
    static class LedLabel extends JLabel {
        private boolean isError;    //是否显示E标
        private int scale;  //当前进制

        /**
         * 构造函数
         *
         * @author Brownlzy
         */
        public LedLabel() {
            isError = false;
            scale = 10;
            this.setFont(new Font("Consolas", Font.BOLD, 18));
        }

        /**
         * 构造函数，初始化成员变量
         *
         * @param scale   进制
         * @param isError E标
         * @author Brownlzy
         */
        public LedLabel(int scale, boolean isError) {
            this.isError = isError;
            this.scale = scale;
            this.setFont(new Font("Consolas", Font.BOLD, 18));
            refreshText();
        }

        public void changeState(int scale, boolean isError) {
            this.isError = isError;
            this.scale = scale;
            refreshText();
        }

        public void changeState(int scale) {
            this.scale = scale;
            refreshText();
        }

        public void changeState(boolean isError) {
            this.isError = isError;
            refreshText();
        }

        private void refreshText() {
            String funText = " E              DEC      HEX      OCT ";
            if (!isError) funText = funText.replace(" E ", "   ");
            if (scale != 16) funText = funText.replace("HEX", "   ");
            if (scale != 10) funText = funText.replace("DEC", "   ");
            if (scale != 8) funText = funText.replace("OCT", "   ");
            this.setText(funText);
        }
    }

    /**
     * 绘制LED液晶数字与部分字符
     *
     * @author Brownlzy
     * @version 1.0
     */
    static class LedNumber extends Component {
        private Polygon[] segmentPolygon;   //储存7个二极管轮廓
        /*
         -----    --0--
        |     |  5     1
         -----    --6--
        |     |  4     2
         -----    --3--
         */
        private final int[][] numberSegment = {
                {0, 1, 2, 3, 4, 5}, // 0
                {1, 2}, // 1
                {0, 1, 3, 4, 6}, // 2
                {0, 1, 2, 3, 6}, // 3
                {1, 2, 5, 6}, // 4
                {0, 2, 3, 5, 6}, // 5
                {0, 2, 3, 4, 5, 6}, // 6
                {0, 1, 2}, // 7
                {0, 1, 2, 3, 4, 5, 6}, // 8
                {0, 1, 2, 3, 5, 6}, // 9
                {0, 1, 2, 4, 5, 6},  //A
                {2, 3, 4, 5, 6},  //b
                {0, 3, 4, 5},  //C
                {1, 2, 3, 4, 6},  //d
                {0, 3, 4, 5, 6},  //E
                {0, 4, 5, 6}, //F
                {6},  //-
                {}  //
        };  //储存每个字符需要点亮的二极管ID

        private Image[] numberImage;    //储存所有LED字符的图片
        private Color fontColor = Color.red;    //字体颜色
        private Color bgColor = Color.black;    //背景颜色
        private Color maskColor = Color.darkGray;   //未点亮二极管颜色
        private int dWidth = 12;    //单个数字宽度
        private int dHeight = 21;   //数字高度
        private int dGasp = 7;  //数字之间的间隔

        public LedNumber() {
            init();
        }

        public LedNumber(int width, int height, int gasp) {
            dWidth = width;
            dHeight = height;
            dGasp = gasp;
            init();
        }

        /**
         * 构造函数，设置生成的Led数字的参数
         *
         * @param fc     字体颜色
         * @param bgc    背景颜色
         * @param mc     遮盖颜色
         * @param width  数字宽度
         * @param height 数字高度
         * @param gasp   数字之间的间隔
         * @author Brownlzy
         */
        public LedNumber(Color fc, Color bgc, Color mc, int width, int height, int gasp) {
            fontColor = fc;
            bgColor = bgc;
            maskColor = mc;
            dWidth = width;
            dHeight = height;
            dGasp = gasp;
            init();
        }

        /**
         * 输出数组字符串图片
         *
         * @param str   要输出图片的字符串
         * @param width 图片的宽度（8的个数，不含小数点）
         * @return java.awt.Image
         * @author Brownlzy
         */
        public Image getLedImage(String str, int width) {
            int bound = str.length() - 1;
            Image image = new BufferedImage((dWidth + dGasp) * width, dHeight, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            g.setColor(bgColor);
            g.fillRect(0, 0, (dWidth + dGasp) * width, dHeight);
            g.setColor(fontColor);
            for (int i = 0, dotCount = 0; i < width; ) {
                if (i + dotCount > bound) {
                    g.drawImage(numberImage[17], (width - 1 - i) * (dWidth + dGasp), 0, this);
                    i++;
                } else {
                    if (str.charAt(bound - dotCount - i) == '.') {
                        g.fillRect((width - i) * (dWidth + dGasp) - dGasp, dHeight - 4 * dHeight / 21, 3 * dWidth / 12, 3 * dHeight / 21);
                        dotCount++;
                    } else {
                        g.drawImage(numberImage[charToLedNumber(str.charAt(bound - dotCount - i))], (width - 1 - i) * (dWidth + dGasp), 0, this);
                        i++;
                    }
                }
            }
            return image;
        }

        /**
         * 字符转数组下标
         * @param c 字符
         * @return int
         * @author Brownlzy
         */
        private int charToLedNumber(char c) {
            if (Character.isDigit(c)) return c - 48;
            switch (c) {
                //大小写不同下标相同
                case 'A':
                case 'a':
                    return 10;
                case 'B':
                case 'b':
                    return 11;
                case 'C':
                case 'c':
                    return 12;
                case 'D':
                case 'd':
                    return 13;
                case 'E':
                case 'e':
                    return 14;
                case 'F':
                case 'f':
                    return 15;
                case '-':
                    return 16;
                default:
                    return 17;
            }
        }

        /**
         * 生成所有字符的led图片
         *
         * @author Brownlzy
         */
        private void init() {
            segmentPolygon = new Polygon[7];
            numberImage = new Image[numberSegment.length];
            setNumberPolygon();
            setNumberImage();
        }

        /**
         * 生成每个字符的图片并保存
         *
         * @author Brownlzy
         */
        private void setNumberImage() {
            int i = 0;
            int j;
            Graphics g;
            while (i < numberSegment.length) {
                numberImage[i] = new BufferedImage(dWidth, dHeight, BufferedImage.TYPE_INT_RGB);
                g = numberImage[i].getGraphics();
                g.setColor(bgColor);
                g.fillRect(0, 0, dWidth, dHeight);
                g.setColor(maskColor);
                j = 0;
                int k;
                while (j < numberSegment[8].length) {
                    k = numberSegment[8][j];
                    g.fillPolygon(segmentPolygon[k]);
                    j++;
                }
                g.setColor(fontColor);
                j = 0;
                while (j < numberSegment[i].length) {
                    k = numberSegment[i][j];
                    g.fillPolygon(segmentPolygon[k]);
                    j++;
                }
                i++;
            }
        }

        /**
         * 设置LED数字每个显示管的图形
         *
         * @author Brownlzy
         */
        private void setNumberPolygon() {
            int mid = dHeight / 2 + dHeight / 21;
            segmentPolygon[0] = new Polygon();
            segmentPolygon[0].addPoint(2 * dWidth / 12, dHeight / 21);
            segmentPolygon[0].addPoint(dWidth - 2 * dWidth / 12, dHeight / 21);
            segmentPolygon[0].addPoint(dWidth - 5 * dWidth / 12, 4 * dHeight / 21);
            segmentPolygon[0].addPoint(5 * dWidth / 12, 4 * dHeight / 21);
            segmentPolygon[1] = new Polygon();
            segmentPolygon[1].addPoint(dWidth - dWidth / 12, dHeight / 21);
            segmentPolygon[1].addPoint(dWidth - dWidth / 12, mid - dHeight / 21);
            segmentPolygon[1].addPoint(dWidth - 2 * dWidth / 12, mid - dHeight / 21);
            segmentPolygon[1].addPoint(dWidth - 4 * dWidth / 12, mid - 3 * dHeight / 21);
            segmentPolygon[1].addPoint(dWidth - 4 * dWidth / 12, 4 * dHeight / 21);
            segmentPolygon[2] = new Polygon();
            segmentPolygon[2].addPoint(dWidth - dWidth / 12, mid);
            segmentPolygon[2].addPoint(dWidth - dWidth / 12, dHeight - 2 * dHeight / 21);
            segmentPolygon[2].addPoint(dWidth - 4 * dWidth / 12, dHeight - 5 * dHeight / 21);
            segmentPolygon[2].addPoint(dWidth - 4 * dWidth / 12, mid + dHeight / 21);
            segmentPolygon[2].addPoint(dWidth - 3 * dWidth / 12, mid);
            segmentPolygon[3] = new Polygon();
            segmentPolygon[3].addPoint(dWidth - dWidth / 12, dHeight - dHeight / 21);
            segmentPolygon[3].addPoint(dWidth / 12, dHeight - dHeight / 21);
            segmentPolygon[3].addPoint(4 * dWidth / 12, dHeight - 4 * dHeight / 21);
            segmentPolygon[3].addPoint(dWidth - 4 * dWidth / 12, dHeight - 4 * dHeight / 21);
            segmentPolygon[4] = new Polygon();
            segmentPolygon[4].addPoint(dWidth / 12, dHeight - 2 * dHeight / 21);
            segmentPolygon[4].addPoint(dWidth / 12, mid);
            segmentPolygon[4].addPoint(3 * dWidth / 12, mid);
            segmentPolygon[4].addPoint(4 * dWidth / 12, mid + dHeight / 21);
            segmentPolygon[4].addPoint(4 * dWidth / 12, dHeight - 5 * dHeight / 21);
            segmentPolygon[5] = new Polygon();
            segmentPolygon[5].addPoint(dWidth / 12, mid - dHeight / 21);
            segmentPolygon[5].addPoint(dWidth / 12, dHeight / 21);
            segmentPolygon[5].addPoint(4 * dWidth / 12, 4 * dHeight / 21);
            segmentPolygon[5].addPoint(4 * dWidth / 12, mid - 3 * dHeight / 21);
            segmentPolygon[5].addPoint(2 * dWidth / 12, mid - dHeight / 21);
            segmentPolygon[6] = new Polygon();
            segmentPolygon[6].addPoint(3 * dWidth / 12, mid - dHeight / 21);
            segmentPolygon[6].addPoint(4 * dWidth / 12, mid - 2 * dHeight / 21);
            segmentPolygon[6].addPoint(dWidth - 4 * dWidth / 12, mid - 2 * dHeight / 21);
            segmentPolygon[6].addPoint(dWidth - 3 * dWidth / 12, mid - dHeight / 21);
            segmentPolygon[6].addPoint(dWidth - 5 * dWidth / 12, mid + dHeight / 21);
            segmentPolygon[6].addPoint(5 * dWidth / 12, mid + dHeight / 21);
        }
    }
}

