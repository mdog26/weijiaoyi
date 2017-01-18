package springboot.module;

import springboot.repository.Util;

import java.util.ArrayList;

/**
 * 趋势
 */
public class Trend {
    public int secondRegion = 1 * 60;//默认3分钟

    public long avgSecnod;
    public double avgPrice;


    public int pointXL = 0; //当前点对应前一点斜率
    public int xl = 0;//整体斜率
    public int changeXL = 0;//变化斜率
    public int lastXl = 0;//监控起点至终点斜率
    public int avgXl = 0;//相对均值变化斜率

    private ArrayList<CONC> list = new ArrayList<CONC>();
    private ArrayList<Integer> xlList = new ArrayList<Integer>();

    /**
     * 新增一个点
     * @param conc
     */
    public void put(CONC conc) {
        if(list.size() > 1 && list.get(list.size() - 1).getSecond() == conc.getSecond()){
            return;
        }
        //计算整体斜率
        this.calculate(conc);
        list.add(conc);
        if(list.size() > 1 && conc.getSecond() - list.get(0).getSecond() > secondRegion){

            //当区间超过设置的最大时间间隔，那么移除前一个数据
            list.remove(0);
        }

        System.out.println("斜率: " + xl +" 当前点斜率: " + pointXL +" 变化斜率: " + changeXL + " 相对均值变化斜率:" + avgXl + " " +
                "起点至终点斜率:" + lastXl + " price: " + conc.getPrice() + " time: " + conc.getTime());
    }

    private void calculate(CONC newConc){
        if (list.size() > 1) {
            //当前点变化值
            CONC lastC = list.get(list.size() - 1);
            long x = newConc.getSecond() - lastC.getSecond();
            double y = newConc.getPrice() - lastC.getPrice();
            this.pointXL = Util.xielv(x, y*10);

            //起点至终点斜率
            x = newConc.getSecond() - list.get(0).getSecond();
            y = newConc.getPrice() - list.get(0).getPrice();
            this.lastXl = Util.xielv(x, y*10);

            //平均值
            long sumSecnod = 0;
            double sumPrice = 0;
            for (CONC conc : list) {
                sumSecnod += conc.getSecond();
                sumPrice += conc.getPrice();
            }
            this.avgSecnod = sumSecnod/list.size();
            this.avgPrice = sumPrice/list.size();

            x = newConc.getSecond() - this.avgSecnod;
            y = newConc.getPrice() -  this.avgPrice;
            this.avgXl = Util.xielv(x, y*10);

            //整体值
            CONC beforeC = list.get(0);
            int xl = 0;
            for (int i = 1; i < list.size(); i++) {
                CONC conc = list.get(i);
                x = newConc.getSecond() - lastC.getSecond();
                y = newConc.getPrice() - lastC.getPrice();
                xl = Util.xielv(x, y*10);

                xlList.add(xl);

                beforeC = conc;
            }

            int sumXl = 0;
            for (Integer i : xlList) {
                sumXl += i;
            }
            this.xl = sumXl/xlList.size();

            this.changeXL = (pointXL + sumXl) / list.size() -  this.xl;
        }
    }
}
