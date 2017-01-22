package springboot.book;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Transn on 2017-1-19.
 */
@Component
public class Jobs {
    public final static long Minute_1 =  60 * 1000;
    public final static long Minute_5 =  60 * 1000 * 5;

    @Scheduled(fixedRate=Minute_5)
    public void fixedRateJob(){
        try {
            Thread.currentThread().sleep(20000);
            Login.todayOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate=Minute_1)
    public void getDataJob(){
        try {
            Thread.currentThread().sleep(20000);
            Login.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
