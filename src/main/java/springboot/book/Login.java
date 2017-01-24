package springboot.book;

import com.aspose.ocr.ImageStream;
import com.aspose.ocr.OcrEngine;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springboot.lianghua.MonitorData;
import springboot.module.CONC;

import java.util.ArrayList;
import java.util.List;

import static springboot.book.HttpClientKeepSession.post;

/**
 * Created by Transn on 2017-1-5.
 */
public class Login {
    public static Logger log = LoggerFactory.getLogger(Login.class);
//    public static String loginName = "18123953110";
//    public static String password = "YWJjZDEyMzQ=";//abcd1234
    public static String loginName = "15207109571";
    public static String password = "MTIzNDU2Nzhh";//12345678a
    public static String vcode = "";
    public static String remember = "false";


    public static String cImage() throws Exception{
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "image.gif";

        CloseableHttpResponse response = HttpClientKeepSession.get("http://demoweipan.gzqlec.com/App/Common/Common/vcode.php?height=40&width=100");
        byte[] a = EntityUtils.toByteArray(response.getEntity());
        FileDownloadUtil.download(a, path);

        OcrEngine ocr = new OcrEngine();
        ocr.setImage(ImageStream.fromFile(path));
        if (ocr.process())
        {
//            System.out.println(ocr.getText());
        }
//        System.out.println("验证码: " + ocr.getText());
        return ocr.getText().toString();
    }

    public static void main1(String[] args) throws Exception{
        CloseableHttpResponse response = null;
        HttpClientKeepSession.addCookie("PHPSESSID", "5cjofq3so9v7arctk5oa4bqb90", "demoweipan.gzqlec.com", "/");
        HttpClientKeepSession.addCookie("acw_tc", "AQAAAOmH9CrvrwIAkl+3PTR1zsgR4596", "demoweipan.gzqlec.com", "/");
        HttpClientKeepSession.addCookie("loginName", "15207109571", "demoweipan.gzqlec.com", "/");
        order(response);
    }

    public static void todayOrders()throws Exception{
        HttpClientKeepSession.addCookie("PHPSESSID", HttpClientKeepSession.PHPSESSID, "demoweipan.gzqlec.com", "/");
        HttpClientKeepSession.addCookie("acw_tc", HttpClientKeepSession.acw_tc, "demoweipan.gzqlec.com", "/");
        HttpClientKeepSession.addCookie("loginName", "15207109571", "demoweipan.gzqlec.com", "/");
        CloseableHttpResponse response = HttpClientKeepSession.get("http://demoweipan.gzqlec.com/index.php?m=admin&c=index&a=todayOrders");
        String status = EntityUtils.toString(response.getEntity(), "utf-8");
        if(status.contains("登录超时或已在别处登录")){
            Login.login(null);
        }else{
            log.debug(status);
        }
    }

    public static void getData()throws Exception{
        CloseableHttpResponse response = HttpClientKeepSession.get("http://demoweipan.gzqlec.com/?m=admin&c=index&a=getData&unit=1&symbol=CONC");
        String status = EntityUtils.toString(response.getEntity(), "utf-8");
        if(status.contains("登录超时或已在别处登录")){
            Login.login(null);
        }
    }

    public static void login(String[] args)throws Exception{
        int n = 1;
        for (int i = 0; i < 10; i++) {
            vcode = Login.cImage().replace("\n","");
            if(vcode.length() < 4){
                continue;
            }
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("loginName",loginName));
            nvps.add(new BasicNameValuePair("password",password));
            nvps.add(new BasicNameValuePair("remember",remember));
            nvps.add(new BasicNameValuePair("vcode",vcode));
            CloseableHttpResponse response = post("http://demoweipan.gzqlec.com/index" +
                    ".php?m=admin&c=index&a=login&dosubmit=1", nvps);
            String status = EntityUtils.toString(response.getEntity(), "utf-8");
//            HttpClientKeepSession.printCookies();
            if(status.contains("true") && status.contains("info")) {
                n++;
                System.out.println("登陆成功");
//                order(response);
//                todayOrders();
                if (n ==  2) {
                    break;
                }
            } else {
                continue;
            }
        }

    }

    public static void order(CloseableHttpResponse response) throws Exception{
        System.out.println(System.currentTimeMillis());
        long time = System.currentTimeMillis();
        String value = String.valueOf(time);
        if (value.matches(".*00.{2,2}")) {
        }else{
            long sp = Long.parseLong(value.substring(value.length() - 4, value.length()));
            Thread.currentThread().sleep(10000 - sp -200);
        }
        System.out.println(System.currentTimeMillis());


        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("optionType", "60S"));//下单时间60S 180S 300S 15M 30M 1H
        nvps.add(new BasicNameValuePair("position", "put"));//put 买跌 call 买涨
        nvps.add(new BasicNameValuePair("symbolID", "3"));//1 阴极铜 //3 芳烃 //2 电解铝
        nvps.add(new BasicNameValuePair("originalRate", price(response)));//当前价格
        nvps.add(new BasicNameValuePair("amount", "300"));//投资金额必须是50的倍数
        nvps.add(new BasicNameValuePair("payout", "77"));//收益比例
        nvps.add(new BasicNameValuePair("symbolName", "芳烃"));//1 阴极铜 //3 芳烃 //2 电解铝

        response = HttpClientKeepSession.post("http://demoweipan.gzqlec" +
                ".com/?m=admin&c=index&a=sixtyAdd", nvps);
        System.out.println("下单时间" + System.currentTimeMillis());
        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));

        HttpClientKeepSession.printCookies();

    }

    public static void order(String call, CONC conc) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("optionType", "60S"));//下单时间60S 180S 300S 15M 30M 1H
        nvps.add(new BasicNameValuePair("position", call));//put 买跌 call 买涨
        nvps.add(new BasicNameValuePair("symbolID", "3"));//1 阴极铜 //3 芳烃 //2 电解铝
        nvps.add(new BasicNameValuePair("originalRate", conc.getPrice() + ""));//当前价格
        nvps.add(new BasicNameValuePair("amount", "100"));//投资金额必须是50的倍数
        nvps.add(new BasicNameValuePair("payout", "87"));//收益比例
        nvps.add(new BasicNameValuePair("symbolName", "芳烃"));//1 阴极铜 //3 芳烃 //2 电解铝

        try {
            CloseableHttpResponse response = HttpClientKeepSession.post("http://demoweipan.gzqlec" +
                    ".com/?m=admin&c=index&a=sixtyAdd", nvps);
            String res = EntityUtils.toString(response.getEntity(), "utf-8");
            if(res.contains("成功")){
                System.out.println(conc);
            } else if(res.contains("下单异常")){
                log.error(res);
            } else if(res.contains("登录超时或已在别处登录")){
                Login.login(null);
            }else{
                System.out.println(res);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String price(CloseableHttpResponse response) throws Exception{
        response = HttpClientKeepSession.get("http://demoweipan.gzqlec" +
                ".com/?m=admin&c=index&a=getData&unit=1&symbol=CONC");
        System.out.println("价格时间" + System.currentTimeMillis());
        String arr = EntityUtils.toString(response.getEntity(), "utf-8");
//        Object objs = JSONArray.parseArray(arr);
        System.out.println(arr);
//        JSONArray.parse(arr);
        String current = arr.substring(arr.lastIndexOf("[") + 1, arr.indexOf("]]"));
        String[] number = current.split(",");
        long time = Long.parseLong(number[0]);
        System.out.println(number[4]);
        String result = number[4].substring(0,number[4].indexOf("."))  + ".11";
        System.out.println(result);
        System.out.println(Double.parseDouble(number[4]) + 0.05);
        return String.valueOf(Double.parseDouble(number[4]) + 0.05);
    }

}
