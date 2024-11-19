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

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static at.spengergasse.backend.seeder.RandomDateTimeGenerator.getRandomDateTime;

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

    public static List<at.spengergasse.backend.mongodb.model.User> generateMongoUserTestData(int size)
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

    public static List<at.spengergasse.backend.relational.model.User> generateRelationalUserTestData(int size)
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

    public static List<at.spengergasse.backend.mongodb.model.Itinerary> generateMongoItineraryTestData(int size)
    {
        List<at.spengergasse.backend.mongodb.model.Itinerary> itineraries = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            itineraries.add(at.spengergasse.backend.mongodb.model.Itinerary.builder()
                            .uuid(UUID.randomUUID())
                            .name("name" + i)
                            .startDate(getRandomDateTime(LocalDateTime.now(), LocalDateTime.now().plusDays(1000)))
                            .endDate(getRandomDateTime(LocalDateTime.now().plusDays(1000), LocalDateTime.now().plusDays(2000)))
                            .itinerarySteps(List.of(at.spengergasse.backend.mongodb.model.ItineraryStep.builder()
                                    .name("name" + i)
                                    .stepDate(getRandomDateTime(LocalDateTime.now(), LocalDateTime.now().plusDays(1000)))
                                    .build()))
                    .build());
        }
        return itineraries;
    }

    public static List<at.spengergasse.backend.relational.model.Itinerary> generateRelationalItinerariesTestData(int size)
    {
        List<at.spengergasse.backend.relational.model.Itinerary> itineraries = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            itineraries.add(at.spengergasse.backend.relational.model.Itinerary.builder()
                    .uuid(UUID.randomUUID())
                    .name("name" + i)
                    .startDate(getRandomDateTime(LocalDateTime.now(), LocalDateTime.now().plusDays(1000)))
                    .endDate(getRandomDateTime(LocalDateTime.now().plusDays(1000), LocalDateTime.now().plusDays(2000)))
                    .itinerarySteps(List.of(at.spengergasse.backend.relational.model.ItineraryStep.builder()
                            .name("name" + i)
                            .stepDate(getRandomDateTime(LocalDateTime.now(), LocalDateTime.now().plusDays(1000)))
                            .build()))
                    .build());
        }
        return itineraries;
    }
//FIXME: This method is not complete Itineraries are not being created yet
    public Map<String, Long> seedAndBenchmarkDatabase(
            MongoUserRepository mongoUserRepository,
            JpaUserRepository jpaUserRepository,
            int size)
    {

        mongoUserRepository.deleteAll();
        jpaUserRepository.deleteAll();

        Map<String, Long> results = new HashMap<>();

        List<at.spengergasse.backend.mongodb.model.User> mongoUsers = generateMongoUserTestData(size);
        List<at.spengergasse.backend.relational.model.User> jpaUsers = generateRelationalUserTestData(size);

        List<at.spengergasse.backend.mongodb.model.Itinerary> mongoItineraries = generateMongoItineraryTestData(size);
        List<at.spengergasse.backend.relational.model.Itinerary> jpaItineraries = generateRelationalItinerariesTestData(size);

        long mongoCreate = benchmarkOperation(() -> mongoUserRepository.saveAll(mongoUsers));
        results.put("MongoDB CREATE", TimeUnit.NANOSECONDS.toMillis(mongoCreate));

        long jpaCreate = benchmarkOperation(() -> jpaUserRepository.saveAll(jpaUsers));
        results.put("JPA CREATE", TimeUnit.NANOSECONDS.toMillis(jpaCreate));

        //Update
        long mongoUpdate = benchmarkOperation(() -> mongoUserService.updateUserName("username0", "newUsername0"));
        results.put("MongoDB UPDATE", TimeUnit.NANOSECONDS.toMillis(mongoUpdate));

        long jpaUpdate = benchmarkOperation(() -> jpaUserService.updateUserName("username0", "newUsername0"));
        results.put("JPA UPDATE", TimeUnit.NANOSECONDS.toMillis(jpaUpdate));

        //Delete
        long mongoDelete = benchmarkOperation(mongoUserRepository::deleteAll);
        results.put("MongoDB DELETE", TimeUnit.NANOSECONDS.toMillis(mongoDelete));

        long jpaDelete = benchmarkOperation(jpaUserRepository::deleteAll);
        results.put("JPA DELETE", TimeUnit.NANOSECONDS.toMillis(jpaDelete));

        //Find All
        long mongoFindAll = benchmarkOperation(mongoUserRepository::findAll);
        results.put("MongoDB FIND ALL", TimeUnit.NANOSECONDS.toMillis(mongoFindAll));

        long jpaFindAll = benchmarkOperation(jpaUserRepository::findAll);
        results.put("JPA FIND ALL", TimeUnit.NANOSECONDS.toMillis(jpaFindAll));

        //Find By Username
        long mongoFindByUsername = benchmarkOperation(() -> mongoUserRepository.findByUsername("username0"));
        results.put("MongoDB FIND BY USERNAME", TimeUnit.NANOSECONDS.toMillis(mongoFindByUsername));

        long jpaFindByUsername = benchmarkOperation(() -> jpaUserRepository.findByUsername("username0"));
        results.put("JPA FIND BY USERNAME", TimeUnit.NANOSECONDS.toMillis(jpaFindByUsername));

        //Find by Email Project Username and Email
        long mongoFindByEmailProjectUsernameAndEmail = benchmarkOperation(() -> mongoUserRepository.findByEmail("email0"));
        results.put("MongoDB FIND BY EMAIL PROJECT USERNAME AND EMAIL", TimeUnit.NANOSECONDS.toMillis(mongoFindByEmailProjectUsernameAndEmail));

        long jpaFindByEmailProjectUsernameAndEmail = benchmarkOperation(() -> jpaUserRepository.findByEmail("email0"));
        results.put("JPA FIND BY EMAIL PROJECT USERNAME AND EMAIL", TimeUnit.NANOSECONDS.toMillis(jpaFindByEmailProjectUsernameAndEmail));

        //Find by User Project Username and ItineraryName and Sort by CreatedAt
        long mongoCreateItineraries = benchmarkOperation(() -> mongoUserRepository.saveAll(mongoUsers));
        long mongoFindByUserProjectUsernameAndItineraryNameAndSortByCreatedAt = benchmarkOperation(() -> mongoUserRepository.findByUsername("username0"));

        return results;
    }
}
