package springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import springboot.lianghua.DownData;
import springboot.repository.CONCRepository;
import springboot.lianghua.ProData;
import springboot.repository.ProRepository;

import java.net.URI;

/**
 * Created by Transn on 2017-1-11.
 */
@SpringBootApplication
public class RunApplication implements CommandLineRunner {
    @Autowired
    private CONCRepository concRepository;

    @Autowired
    private ProRepository proRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProData proData;

    public static void main(String[] args) throws Exception {
        final ApplicationContext applicationContext = SpringApplication.run(RunApplication.class, args);
        Beans.setApplicationContext(applicationContext);

        DownData downData = new DownData(new URI("ws://112.74.190.166:5000"));
        downData.connect();
    }

    @Override
    public void run(String... args) throws Exception {
//        proRepository.deleteAll();
//        proData.pro();
//        proData.calculate(60);
    }
}
