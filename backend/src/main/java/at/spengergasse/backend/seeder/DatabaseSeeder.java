package at.spengergasse.backend.seeder;

import at.spengergasse.backend.mongodb.persistence.MongoUserRepository;
import at.spengergasse.backend.mongodb.service.MongoUserService;
import at.spengergasse.backend.relational.model.User;
import at.spengergasse.backend.relational.persistence.JpaUserRepository;
import at.spengergasse.backend.relational.service.JpaUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DatabaseSeeder
{
    @Value("${seeder.enabled:true}")
    private boolean seederEnabled;

    private final MongoUserService mongoUserService;
    private final JpaUserService jpaUserService;

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

        //Create
        long mongoCreate = benchmarkOperation(() -> mongoUserRepository.saveAll(mongoUsers));
        results.put("CREATE MongoDB", TimeUnit.NANOSECONDS.toMillis(mongoCreate));

        long jpaCreate = benchmarkOperation(() -> jpaUserRepository.saveAll(jpaUsers));
        results.put("CREATE JPA", TimeUnit.NANOSECONDS.toMillis(jpaCreate));

        //Update
        long mongoUpdate = benchmarkOperation(() -> mongoUserService.updateUserName("username0", "newUsername0"));
        results.put("UPDATE MongoDB", TimeUnit.NANOSECONDS.toMillis(mongoUpdate));

        long jpaUpdate = benchmarkOperation(() -> jpaUserService.updateUserName("username0", "newUsername0"));
        results.put("UPDATE JPA", TimeUnit.NANOSECONDS.toMillis(jpaUpdate));

        //Delete

        return results;
    }
}
