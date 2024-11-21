package at.spengergasse.backend.seeder;

import at.spengergasse.backend.mongodb.persistence.MongoItineraryRepository;
import at.spengergasse.backend.mongodb.persistence.MongoUserRepository;
import at.spengergasse.backend.mongodb.service.MongoItineraryService;
import at.spengergasse.backend.mongodb.service.MongoUserService;
import at.spengergasse.backend.relational.model.User;
import at.spengergasse.backend.relational.persistence.JpaItineraryRepository;
import at.spengergasse.backend.relational.persistence.JpaUserRepository;
import at.spengergasse.backend.relational.service.JpaItineraryService;
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
    private final MongoItineraryService mongoItineraryService;
    private final JpaUserService jpaUserService;
    private final JpaItineraryService jpaItineraryService;

    private long benchmarkOperation(Runnable operation) {
        long startTime = System.nanoTime();
        operation.run();
        long endTime = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
    }

    public static List<at.spengergasse.backend.mongodb.model.User> generateMongoUserTestData(int size, String prefix)
    {
        List<at.spengergasse.backend.mongodb.model.User> users = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            users.add(at.spengergasse.backend.mongodb.model.User.builder()
                    .username(prefix + "username" + i)
                    .email(prefix + "email" + i)
                    .password("password")
                    .build());
        }
        return users;
    }

    public static List<at.spengergasse.backend.relational.model.User> generateRelationalUserTestData(int size, String prefix)
    {
        List<at.spengergasse.backend.relational.model.User> users = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            users.add(User.builder()
                    .username(prefix + "username" + i)
                    .email(prefix + "email" + i)
                    .password("password")
                    .build());
        }
        return users;
    }

    public static List<at.spengergasse.backend.mongodb.model.Itinerary> generateMongoItineraryTestData(int size, String prefix)
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

    public static List<at.spengergasse.backend.relational.model.Itinerary> generateRelationalItineraryTestData(int size, String prefix)
    {
        List<at.spengergasse.backend.relational.model.Itinerary> itineraries = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            itineraries.add(at.spengergasse.backend.relational.model.Itinerary.builder()
                    .uuid(UUID.randomUUID())
                    .name("name" + i)
                    .user(User.builder()
                                    .username("username7")
                                    .email("email7")
                                    .password("password")
                                    .build())
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
    public Map<String, List<Long>> seedAndBenchmarkDatabase(
            MongoUserRepository mongoUserRepository,
            JpaUserRepository jpaUserRepository,
            MongoItineraryRepository mongoItineraryRepository,
            JpaItineraryRepository jpaItineraryRepository)
    {
        mongoUserRepository.deleteAll();
        jpaUserRepository.deleteAll();
        mongoItineraryRepository.deleteAll();
        jpaItineraryRepository.deleteAll();

        Map<String, List<Long>> results = new HashMap<>();

        //Create 100 Users
        List<at.spengergasse.backend.mongodb.model.User> mongoUsers100 = generateMongoUserTestData(100, "User100");
        List<at.spengergasse.backend.relational.model.User> jpaUsers100 = generateRelationalUserTestData(100, "User100");

        //Create 1000 Users
        List<at.spengergasse.backend.mongodb.model.User> mongoUsers1000 = generateMongoUserTestData(1000, "User1000");
        List<at.spengergasse.backend.relational.model.User> jpaUsers1000 = generateRelationalUserTestData(1000, "User1000");

        //Create 10000 Users
        List<at.spengergasse.backend.mongodb.model.User> mongoUsers10000 = generateMongoUserTestData(10000, "User10000");
        List<at.spengergasse.backend.relational.model.User> jpaUsers10000 = generateRelationalUserTestData(10000, "User10000");

        // List<at.spengergasse.backend.mongodb.model.Itinerary> mongoItineraries = generateMongoItineraryTestData(size);
        // List<at.spengergasse.backend.relational.model.Itinerary> jpaItineraries = generateRelationalItineraryTestData(size);

        //Create Mongo Users
        long mongoCreate100 = benchmarkOperation(() -> mongoUserRepository.saveAll(mongoUsers100));
        long mongoCreate1000 = benchmarkOperation(() -> mongoUserRepository.saveAll(mongoUsers1000));
        long mongoCreate10000 = benchmarkOperation(() -> mongoUserRepository.saveAll(mongoUsers10000));
        //Create JPA Users
        long jpaCreate100 = benchmarkOperation(() -> jpaUserRepository.saveAll(jpaUsers100));
        long jpaCreate1000 = benchmarkOperation(() -> jpaUserRepository.saveAll(jpaUsers1000));
        long jpaCreate10000 = benchmarkOperation(() -> jpaUserRepository.saveAll(jpaUsers10000));

        results.put("Create 100", List.of(mongoCreate100, jpaCreate100));
        results.put("Create 1000", List.of(mongoCreate1000, jpaCreate1000));
        results.put("Create 10000", List.of(mongoCreate10000, jpaCreate10000));

        System.out.println("Size: " + mongoUserRepository.findAll().size());

        //Find without filter
        long mongoFindAll = benchmarkOperation(mongoUserRepository::findAll);
        long jpaFindAll = benchmarkOperation(jpaUserRepository::findAll);

        results.put("Find all", List.of(mongoFindAll, jpaFindAll));

        //Find with filter
        long mongoFindByUsername = benchmarkOperation(() -> mongoUserRepository.findByUsername("User1000username0"));
        long jpaFindByUsername = benchmarkOperation(() -> jpaUserRepository.findByUsername("User1000username0"));

        results.put("Find with filter", List.of(mongoFindByUsername, jpaFindByUsername));

        //Find with filter and projection
        long mongoFindByEmailProjectUsernameAndEmail = benchmarkOperation(() -> mongoUserRepository.findUserByEmail("User1000email0"));
        long jpaFindByEmailProjectUsernameAndEmail = benchmarkOperation(() -> jpaUserRepository.findByEmail("User1000email0"));

        results.put("Find with filter and projection", List.of(mongoFindByEmailProjectUsernameAndEmail, jpaFindByEmailProjectUsernameAndEmail));

        //Find with filter and projection and sort
        //long mongoFindByUsernameProjectUsernameAndEmailSortByCreatedAt = benchmarkOperation(() -> mongoUserRepository.findByUsername("User1000username0"));
        //long jpaFindByUsernameProjectUsernameAndEmailSortByCreatedAt = benchmarkOperation(() -> jpaUserRepository.findByUsername("User1000username0"));

        //results.put("Find with filter and projection and sort", List.of(mongoFindByUsernameProjectUsernameAndEmailSortByCreatedAt, jpaFindByUsernameProjectUsernameAndEmailSortByCreatedAt));

        //Update User
        long mongoUpdate = benchmarkOperation(() -> mongoUserService.updateUserName("User1000username0", "newUsername0"));
        long jpaUpdate = benchmarkOperation(() -> jpaUserService.updateUserName("User1000username0", "newUsername0"));

        results.put("Update", List.of(mongoUpdate, jpaUpdate));

        //Delete all Users
        long mongoDelete = benchmarkOperation(mongoUserRepository::deleteAll);
        long jpaDelete = benchmarkOperation(jpaUserRepository::deleteAll);

        results.put("Delete", List.of(mongoDelete, jpaDelete));

        return results;
    }
}
