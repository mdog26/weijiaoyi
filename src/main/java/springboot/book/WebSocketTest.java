package springboot.book;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.net.URI;
import java.util.Date;

/**
 * 获取价格
 */
public class WebSocketTest {
    public static void main1(final String[] args) throws Exception {
//        SpringApplication.run(RunApplication.class, args);
        WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://112.74.190.166:5000")) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                System.out.println(serverHandshake);
            }

            @Override
            public void onMessage(String s) {
//                long now = System.currentTimeMillis();
//                System.out.println(now);
                if (s.contains("CONC")) {
                    String[] arr = s.split("[|]");
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");
                    DateTime dateTime = formatter.parseDateTime(arr[2]);
                    Date date = dateTime.toDate();

                    System.out.println(s);
                    System.out.println(date);
                    System.out.println(arr[2]);
                    System.out.println();
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
        };

        //open websocket
        webSocketClient.connect();
//        Thread.currentThread().sleep(3000);
//        webSocketClient.close();
    }
}