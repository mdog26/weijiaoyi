package springboot.lianghua;

import com.alibaba.fastjson.JSON;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;
import springboot.module.CONC;
import springboot.module.Pro;
import springboot.module.Win;
import springboot.repository.CONCRepository;
import springboot.repository.ProRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Transn on 2017-1-11.
 * 将基础数据，整理成每秒高低 (单秒)数据
 * 区分时间段
 *
 * 趋势分析
 * 每一点都有对应的趋势变化（趋势变化是什么，简单说每秒。每2秒都有趋势变化）
 *
 * 趋势变化如何计算：2个点之间的斜率，每分钟趋势，（所有连续趋势的品均值/起点和终点的趋势值）
 *
 * 设置大趋势和小趋势的权重值（训练出合适的权重值）
 *
 * 判断出合适出击点
 */
@Repository
public class ProData {
    public static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public static String WIN = "win3";
    public static int HIGH_SECOND = 50;//涨幅跌幅超10点

    @Value("${second.piancha}")
    private int SECOND_PIANCHA;

    @Autowired
    private CONCRepository concRepository;

    @Autowired
    private ProRepository proRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void pro() {
        int group = 1;
        long second = 0;
        //# 查找本表最后一条数据
        Pro lastPor = null;
        List<Pro> listPro = proRepository.findAll(new Sort(Sort.Direction.DESC, "second"));
        if (listPro.size() > 0) {
            lastPor = listPro.get(0);
            group = lastPor.getGroup();
            second = lastPor.getSecond();
        }
        System.out.println("当前组: " + group + " 时间戳(秒): " +second + " 时间: " + new DateTime(second*1000).toLocalDateTime());

        //# 根据最后一条数据的时间，获取基础数据表 中 下一个连续段的数据
        List<CONC> proCONC = new ArrayList<CONC>();
        List<CONC> listCONC = null;
        if (second == 0) {
            //第一次导入
            listCONC = concRepository.findAll(new Sort(Sort.Direction.ASC, "second"));
        } else {
            BasicQuery query = new BasicQuery("{ second : { $gt : "+second+" } }");
            query.with(new Sort(Sort.Direction.ASC, "second"));
            listCONC = mongoTemplate.find(query, CONC.class);
        }

        System.out.println("获取原数据"+listCONC.size()+"条");
        //# 整理获取的数据成最后一秒对应数据
        //去重
        for (int i = 0; i < listCONC.size(); i++) {
            CONC conc = listCONC.get(i);
            long second1 = conc.getSecond();

            CONC nextConc = null;
            if (i != listCONC.size() - 1) {
                nextConc = listCONC.get(i + 1);
            } else {
                proCONC.add(conc);
                break;
            }
            long second2 = nextConc.getSecond();

            // 比较second
            if (second1 != second2) {
                proCONC.add(conc);
            }
        }
        //# 保存新表
        List<Pro> insertProList = new ArrayList<Pro>();
        for (int i = 0; i < proCONC.size(); i++) {
            CONC conc = proCONC.get(i);
            CONC nextConc = null;
            if (i != proCONC.size() - 1) {
                nextConc = proCONC.get(i + 1);
            } else {
                break;
            }

            Pro pro = new Pro(conc);

            //比较前后数据，相差多少秒任务连续数据
            pro.setGroup(group);
            Date time = pro.getTime();
            DateTime dt = new DateTime(time);
            pro.setDate(dt.toString(formatter));
            insertProList.add(pro);

            //判断下一个数据是否是新的开始
            if( i == 0 && lastPor != null && Math.abs(conc.getSecond() - lastPor.getSecond()) > 30){
                //证明是连续的
            }else if (Math.abs(nextConc.getSecond() - conc.getSecond()) > 30){
                //下一个数据才是新的分组
                group++;
            }
        }
        proRepository.save(insertProList);
        System.out.println("过滤数据"+insertProList.size()+"条");
    }

    /**
     * 清理处理有的数据，去掉少于一小时的 group
     */
    public  void clean(){

    }

    /**
     * 计算出每个点的 正确
     * @param timeOfSecond 间隔时间
     */
    public void calculate(int timeOfSecond) {
        List<Pro> groupProList = new ArrayList<Pro>();

        //#先找到未计算的数据
        BasicQuery query = new BasicQuery("{ "+WIN+" : { $ne : null } }");
        query.with(new Sort(Sort.Direction.DESC, "second"));
        groupProList = mongoTemplate.find(query, Pro.class);

        int group = 0;
        if (groupProList.size() > 0) {
            group = groupProList.get(0).getGroup();
        }

        //#计算单组
        group++;
        while (this.calculateGroup(group, timeOfSecond)) {
            group++;
            break;
        }
    }

    /**
     * 跟进单组计算
     * @return
     */
    private boolean calculateGroup(int group, int timeOfSecond){
        List<Pro> groupProList = new ArrayList<Pro>();
        BasicQuery basicQuery = new BasicQuery("{ group : "+group+" }");
        basicQuery.with(new Sort(Sort.Direction.ASC, "second"));
        groupProList = mongoTemplate.find(basicQuery, Pro.class);

        System.out.println("组: " + group + " 计算: " + groupProList.size() + "条");

        if (groupProList == null || groupProList.size() == 0) {
            return false;
        }
        for (Pro pro : groupProList) {
            long startSecond = pro.getSecond();
            long endSecond = startSecond + timeOfSecond;
            basicQuery = new BasicQuery("{ second : "+endSecond+" }");

            //获取假定 结算点
            Pro calculatePro = mongoTemplate.findOne(basicQuery, Pro.class);
            if (calculatePro != null) {
                this.setWin(pro, calculatePro, timeOfSecond);
            } else {
                //首次偏差 获取结算点
                this.calculatePro(timeOfSecond, this.SECOND_PIANCHA - 1, pro);
            }
            System.out.println(JSON.toJSONString(pro));
//          mongoTemplate.updateFirst(query(where("_id").is(pro.getId())), update(WIN, pro.getWin1()), Pro.class);
        }
        return true;
    }

    private void calculatePro(int timeOfSecond, int piancha, Pro pro) {
        /**
         * 偏差值 出现方式
         * 5 -> -5 -> 4 -> -4 -> 3 -> -3 -> 2 -> -2 -> 1 -> -1 -> 0
         * 影响时间对应值
         * 1 -> -1 -> 2 -> -2 -> 3 -> -3 -> 4 -> -4 -> 5 -> -5 -> 0
         */
        int more = 0;
        if(piancha != 0){
            //计算对向数字位，如果偏差为负，那么影响就为负
            more = (this.SECOND_PIANCHA - Math.abs(piancha)) * piancha / Math.abs(piancha);
        }

        //尝试获取结算点
        BasicQuery basicQuery;
        long startSecond = pro.getSecond();
        long endSecond = startSecond + timeOfSecond + more;
        basicQuery = new BasicQuery("{ second : "+endSecond+" }");

        //获取假定 结算点
        Pro calculatePro = mongoTemplate.findOne(basicQuery, Pro.class);
//      calculatePro = mongoTemplate.findOne(query(where("second ").is(endSecond)), Pro.class);
        if (calculatePro != null) {
            this.setWin(pro, calculatePro, timeOfSecond);
        } else {
            //根据偏差，来偏差1秒的单位计算
            if (piancha > 0) {
                this.calculatePro(timeOfSecond, piancha * -1, pro);
            } else if (piancha < 0) {
                this.calculatePro(timeOfSecond, piancha * -1 - 1, pro);
            } else {
                //在偏差时间范围内，无法计算出 正确与否
                Win winNull = new Win();
                winNull.setW("无");
                if (WIN.equals("win1")){
                    pro.setWin1(winNull);
                } else if (WIN.equals("win3")) {
                    pro.setWin3(winNull);
                } else if (WIN.equals("win5")) {
                    pro.setWin5(winNull);
                }
            }
        }
    }

    private void setWin(Pro startPro, Pro endPro, int timeOfSecond) {
        double startPrice = startPro.getPrice();
        double endPrice = endPro.getPrice();

        String w = null;
        if (startPrice - endPrice > 0 + HIGH_SECOND) {
            w = "↓";
        } else if (startPrice - endPrice < 0 - HIGH_SECOND) {
            w = "↑";
        } else if(startPrice - endPrice == 0){
            w = "-";
        } else {
            w = "无";
        }

        int timeDev = (int) Math.abs(startPro.getSecond() - endPro.getSecond());
        double priceOffset = Math.abs(startPro.getPrice() - endPro.getPrice());

        Win win = new Win();
        win.setW(w);
        win.setGap(timeOfSecond);
        win.setTimeDev(timeDev);
        win.setPriceOffset(priceOffset);

        if (WIN.equals("win1")){
            startPro.setWin1(win);
        } else if (WIN.equals("win3")) {
            startPro.setWin3(win);
        } else if (WIN.equals("win5")) {
            startPro.setWin5(win);
        } else {
            startPro.setWin1(win);
        }

    }

    public int getSECOND_PIANCHA() {
        return SECOND_PIANCHA;
    }

    public void setSECOND_PIANCHA(int SECOND_PIANCHA) {
        this.SECOND_PIANCHA = SECOND_PIANCHA;
    }

}
