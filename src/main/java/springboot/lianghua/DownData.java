package springboot.lianghua;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import springboot.Beans;
import springboot.module.CONC;
import springboot.repository.CONCRepository;

import java.net.URI;
import java.util.Date;

/**
 * Created by Transn on 2017-1-11.
 * 通过WS获取基础数据
 */
public class DownData extends WebSocketClient {
    public static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");

    private CONCRepository repository = Beans.getBean(CONCRepository.class);

    private MonitorData monitorData = Beans.getBean(MonitorData.class);

    public DownData(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println(serverHandshake);
    }

    @Override
    public void onMessage(String s) {
//        long now = System.currentTimeMillis();
        if (s.contains("CONC")) {
            String[] arr = s.split("[|]");
            DateTime dateTime = formatter.parseDateTime(arr[2]);

            //够建对象
            String name = arr[0];
            double price = Double.parseDouble(arr[1]);
            Date date = dateTime.toLocalDateTime().toDate();
            long second = dateTime.getMillis()/1000;

            //
            CONC conc = new CONC(name, price, date, second);
//            repository.save(conc);
            monitorData.monitor(conc);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println(i);
        System.out.println(b);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
