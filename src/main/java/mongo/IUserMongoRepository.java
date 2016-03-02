package main.java.mongo;


import main.java.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUserMongoRepository extends MongoRepository<User, String> {
    List<User> findByEmail(@Param("email") String email);
}
