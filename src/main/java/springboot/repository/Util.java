package springboot.repository;

/**
 * Created by Transn on 2017-1-17.
 */
public class Util {

    public static void main(String[] args) {
        System.out.println(Util.xielv(1,-1));
    }

    /**
     * 斜率
     *
     * @param x 时间 秒差
     * @param y 点数 差
     * @return
     */
    public static int xielv(long x, double y) {
        // 初始化数据
        double c = Math.sqrt(x * x + y * y);
        // 计算弧度表示的角
        double B = Math.acos((x * x + c * c - y * y) / (2.0 * x * c));
        // 用角度表示的角
        B = Math.toDegrees(B);
        // 格式化数据，保留两位小数
//        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
//        String temp = df.format(B);
        return (int) (Math.round(B) * Math.abs(y) / y);
    }
}
