package springboot.lianghua;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import springboot.module.CONC;
import springboot.module.Trend;
import springboot.repository.CONCRepository;
import springboot.repository.ProRepository;

/**
 * 监控数据
 */
@Repository
public class MonitorData {
    @Autowired
    private CONCRepository concRepository;

    @Autowired
    private ProRepository proRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Trend trend = new Trend();

    public void monitor(CONC conc) {
        trend.put(conc);
    }
}
