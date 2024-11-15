package at.spengergasse.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "at.spengergasse.backend.mongodb.persistence"
)
public class MongoConfig { }