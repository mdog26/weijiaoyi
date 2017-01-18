package springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import springboot.module.CONC;

/**
 * Created by Transn on 2017-1-11.
 */
public interface CONCRepository extends MongoRepository<CONC, String> {
}
