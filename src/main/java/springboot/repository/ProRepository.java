package springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import springboot.module.Pro;

/**
 * Created by Transn on 2017-1-11.
 */
public interface ProRepository extends MongoRepository<Pro, String> {
}
