package at.spengergasse.backend.seeder;

import at.spengergasse.backend.mongodb.persistence.MongoUserRepository;
import at.spengergasse.backend.relational.model.User;
import at.spengergasse.backend.relational.persistence.JpaUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class DatabaseSeeder
{

    @Value("${seeder.enabled:true}")
    private boolean seederEnabled;

    private long benchmarkOperation(Runnable operation) {
        long startTime = System.nanoTime();
        operation.run();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public static List<at.spengergasse.backend.mongodb.model.User> generateMongoTestData(int size)
    {
        List<at.spengergasse.backend.mongodb.model.User> users = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            users.add(at.spengergasse.backend.mongodb.model.User.builder()
                    .username("username" + i)
                    .email("email" + i)
                    .password("password")
                    .build());
        }
        return users;
    }

    public static List<at.spengergasse.backend.relational.model.User> generateRelationalTestData(int size)
    {
        List<at.spengergasse.backend.relational.model.User> users = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            users.add(User.builder()
                    .username("username" + i)
                    .email("email" + i)
                    .password("password")
                    .build());
        }
        return users;
    }

    public Map<String, Long> seedAndBenchmarkDatabase(
            MongoUserRepository mongoUserRepository,
            JpaUserRepository jpaUserRepository,
            int size)
    {

        mongoUserRepository.deleteAll();
        jpaUserRepository.deleteAll();

        Map<String, Long> results = new HashMap<>();

        List<at.spengergasse.backend.mongodb.model.User> mongoUsers = generateMongoTestData(size);
        List<at.spengergasse.backend.relational.model.User> jpaUsers = generateRelationalTestData(size);

        long mongoCreate = benchmarkOperation(() -> mongoUserRepository.saveAll(mongoUsers));
        results.put("MongoDB CREATE", TimeUnit.NANOSECONDS.toMillis(mongoCreate));

        long jpaCreate = benchmarkOperation(() -> jpaUserRepository.saveAll(jpaUsers));
        results.put("JPA CREATE", TimeUnit.NANOSECONDS.toMillis(jpaCreate));

        //Update

        //Delete

        return results;
    }

    /*@Bean
    CommandLineRunner initDatabase(
            MongoUserRepository mongoUserRepository,
            JpaUserRepository jpaUserRepository) {
        if (!seederEnabled) {
            return args -> System.out.println("Seeder is disabled.");
        }

        return args -> {
            //Generate data
            List<at.spengergasse.backend.mongodb.model.User> mongoUsers = generateMongoTestData(1000);
            List<at.spengergasse.backend.relational.model.User> jpaUsers = generateRelationalTestData(1000);

            //Benchmark CREATE
            long mongoCreate = benchmarkOperation(() -> mongoUserRepository.saveAll(mongoUsers));
            long jpaCreate = benchmarkOperation(() -> jpaUserRepository.saveAll(jpaUsers));
            System.out.println("MongoDB CREATE: " + TimeUnit.NANOSECONDS.toMillis(mongoCreate) + "ms");
            System.out.println("JPA CREATE: " + TimeUnit.NANOSECONDS.toMillis(jpaCreate) + "ms");
        };
    }*/
}
