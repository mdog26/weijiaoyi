package springboot.module;

/**
 * Created by Transn on 2017-1-14.
 */
public class Win {
    public String w;//涨跌平
    public int gap;//时间间隔
    public int timeDev;//涨跌平的时间偏差 偏差为0 表示最准确率高
    public double priceOffset;//价格偏差

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public int getTimeDev() {
        return timeDev;
    }

    public void setTimeDev(int timeDev) {
        this.timeDev = timeDev;
    }

    public double getPriceOffset() {
        return priceOffset;
    }

    public void setPriceOffset(double priceOffset) {
        this.priceOffset = priceOffset;
    }
}
