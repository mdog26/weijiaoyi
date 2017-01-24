package springboot.book;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Transn on 2017-1-19.
 */
@Component
public class Jobs {

    @Value("${job.switch}")
    private boolean JOB_SWITCH;

    public final static long Minute_1 =  60 * 1000;
    public final static long Minute_5 =  60 * 1000 * 5;

    @Scheduled(fixedRate=Minute_5)
    public void fixedRateJob(){
        if(JOB_SWITCH){
            try {
                Thread.currentThread().sleep(20000);
                Login.todayOrders();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(fixedRate=Minute_1)
    public void getDataJob(){
        if(JOB_SWITCH) {
            try {
                Thread.currentThread().sleep(20000);
                Login.getData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
