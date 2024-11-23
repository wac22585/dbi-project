package at.spengergasse.backend.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

@Configuration
@EnableMongoRepositories(
        basePackages = {"at.spengergasse.backend.mongodb.persistenceRef", "at.spengergasse.backend.mongodb.persistence"}
)
public class MongoConfig {


    @Bean
    public MongoClient mongoClient() {
        Properties prop = loadProperties();

        MongoClientSettings settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(new ConnectionString(prop.getProperty("URI")))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        Properties prop = loadProperties();

        return new MongoTemplate(mongoClient, prop.getProperty("MONGO_DB"));
    }

    private Properties loadProperties() {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("env.properties")) {
            if (input == null) {
                throw new IllegalStateException("Sorry, unable to find env.properties");
            }
            prop.load(input);
        } catch (Exception e) {
            throw new IllegalStateException("Error reading properties file: " + e.getMessage(), e);
        }
        return prop;
    }
}