package at.spengergasse.backend.seeder;

import at.spengergasse.backend.mongodb.modelRef.Itinerary;
import at.spengergasse.backend.mongodb.persistence.MongoItineraryRepository;
import at.spengergasse.backend.mongodb.persistence.MongoUserRepository;
import at.spengergasse.backend.mongodb.persistenceRef.*;
import at.spengergasse.backend.mongodb.service.MongoUserService;
import at.spengergasse.backend.relational.model.User;
import at.spengergasse.backend.relational.persistence.JpaItineraryRepository;
import at.spengergasse.backend.relational.persistence.JpaUserRepository;
import at.spengergasse.backend.relational.service.JpaUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static at.spengergasse.backend.seeder.RandomDateTimeGenerator.getRandomDateTime;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DatabaseSeeder
{
    private final JpaUserRepository jpaUserRepository;
    private final JpaItineraryRepository jpaItineraryRepository;

    private final MongoUserRepository mongoUserRepository;
    private final MongoItineraryRepository mongoItineraryRepository;

    private final MongoUserRefRepository mongoUserRefRepository;
    private final MongoItineraryRefRepository mongoItineraryRefRepository;

    private final MongoUserService mongoUserService;
    private final JpaUserService jpaUserService;

    private List<at.spengergasse.backend.mongodb.modelRef.User> mongoRefUsers = new ArrayList<>();
    private List<Itinerary> mongoRefItineraries = new ArrayList<>();

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
            LocalDateTime startDate = RandomDateTimeGenerator.getRandomDateTime(LocalDateTime.now(), LocalDateTime.now().plusDays(100));
            LocalDateTime endDate = startDate.plusDays(7);

            users.add(at.spengergasse.backend.mongodb.model.User.builder()
                    .username(prefix + "username" + i)
                    .email(prefix + "email" + i)
                    .password("password")
                    .itineraries(List.of(
                        at.spengergasse.backend.mongodb.model.Itinerary.builder()
                            .uuid(UUID.randomUUID())
                            .name(prefix + "name" + i)
                            .startDate(startDate)
                            .endDate(endDate)
                            .build()
                    ))
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
                    .itineraries(List.of(
                        at.spengergasse.backend.relational.model.Itinerary.builder()
                            .uuid(UUID.randomUUID())
                            .name("name" + i)
                            .startDate(getRandomDateTime(LocalDateTime.now(), LocalDateTime.now().plusDays(1000)))
                            .endDate(getRandomDateTime(LocalDateTime.now().plusDays(1000), LocalDateTime.now().plusDays(2000)))
                            .build()
                    ))
                    .build());
        }
        return users;
    }

    private void generateMongoReferencingTestData(int size, String prefix)
    {
        this.mongoRefUsers = new ArrayList<>();
        this.mongoRefItineraries = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            LocalDateTime startDate = RandomDateTimeGenerator.getRandomDateTime(LocalDateTime.now(), LocalDateTime.now().plusDays(100));
            LocalDateTime endDate = startDate.plusDays(7);
            Itinerary itinerary = Itinerary.builder()
                    .uuid(UUID.randomUUID())
                    .name(prefix + " European Adventure" + i)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();

            mongoRefItineraries.add(itinerary);

            mongoRefUsers.add(at.spengergasse.backend.mongodb.modelRef.User.builder()
                    .username(prefix + "username" + i)
                    .email(prefix + "email" + i)
                    .password("password")
                    .itineraries(List.of(itinerary))
                    .build());
        }
    }

    //FIXME: This method is not complete Itineraries are not being created yet
    public Map<String, List<Long>> benchmarks()
    {
        mongoItineraryRepository.deleteAll();
        jpaItineraryRepository.deleteAll();
        mongoUserRepository.deleteAll();
        jpaUserRepository.deleteAll();

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

        //Find without filter
        long mongoFindAll = benchmarkOperation(mongoUserRepository::findAll);
        long jpaFindAll = benchmarkOperation(jpaUserRepository::findAll);

        results.put("Find all", List.of(mongoFindAll, jpaFindAll));

        //Find with filter
        long mongoFindByUsername = benchmarkOperation(() -> mongoUserRepository.findByUsername("User1000username100"));
        long jpaFindByUsername = benchmarkOperation(() -> jpaUserRepository.findByUsername("User1000username100"));

        results.put("Find with filter", List.of(mongoFindByUsername, jpaFindByUsername));

        //Find with filter and projection
        long mongoFindByEmailProjectUsernameAndEmail = benchmarkOperation(() -> mongoUserRepository.findUserByEmail("User1000email100"));
        long jpaFindByEmailProjectUsernameAndEmail = benchmarkOperation(() -> jpaUserRepository.findUserByEmail("User1000email100"));

        results.put("Find with filter and projection", List.of(mongoFindByEmailProjectUsernameAndEmail, jpaFindByEmailProjectUsernameAndEmail));

        //Find with filter and projection and sort
        long mongoFindByUsernameProjectUsernameAndEmailSortByCreatedAt = benchmarkOperation(() -> mongoUserRepository.findUsersByItinerariesSizeGreaterThanOrderByItinerariesAsc(5));
        long jpaFindByUsernameProjectUsernameAndEmailSortByCreatedAt = benchmarkOperation(() -> jpaUserRepository.findUsersByItinerariesSizeGreaterThanOrderByItinerariesAsc(5));

        results.put("Find with filter and projection and sort", List.of(mongoFindByUsernameProjectUsernameAndEmailSortByCreatedAt, jpaFindByUsernameProjectUsernameAndEmailSortByCreatedAt));

        //Update User
        long mongoUpdate = benchmarkOperation(() -> mongoUserService.updateUserName("User1000username100", "newUsername0"));
        long jpaUpdate = benchmarkOperation(() -> jpaUserService.updateUserName("User1000username100", "newUsername0"));

        results.put("Update", List.of(mongoUpdate, jpaUpdate));

        //Delete all Users
        long mongoDelete = benchmarkOperation(mongoUserRepository::deleteAll);
        long jpaDelete = benchmarkOperation(jpaUserRepository::deleteAll);

        results.put("Delete", List.of(mongoDelete, jpaDelete));

        return results;
    }

    public Map<String, List<Long>> benchmarksRef()
    {
        Map<String, List<Long>> results = new HashMap<>();
        mongoItineraryRefRepository.deleteAll();
        mongoUserRefRepository.deleteAll();
        mongoUserRepository.deleteAll();
        mongoItineraryRepository.deleteAll();

        //Create Embedding Objects
        List<at.spengergasse.backend.mongodb.model.User> Emb100 = generateMongoUserTestData(100, "Emb100");
        List<at.spengergasse.backend.mongodb.model.User> Emb1000 = generateMongoUserTestData(1000, "Emb1000");
        List<at.spengergasse.backend.mongodb.model.User> Emb10000 = generateMongoUserTestData(10000, "Emb10000");

        //Create Referencing Objects
        generateMongoReferencingTestData(100, "Ref100");
        List<at.spengergasse.backend.mongodb.modelRef.User> Ref100 = mongoRefUsers;
        List<Itinerary> Ref100Itineraries = mongoRefItineraries;
        generateMongoReferencingTestData(1000, "Ref1000");
        List<at.spengergasse.backend.mongodb.modelRef.User> Ref1000 = mongoRefUsers;
        List<Itinerary> Ref1000Itineraries = mongoRefItineraries;
        generateMongoReferencingTestData(10000, "Ref10000");
        List<at.spengergasse.backend.mongodb.modelRef.User> Ref10000 = mongoRefUsers;
        List<Itinerary> Ref10000Itineraries = mongoRefItineraries;

        //Create Mongo Users and Itineraries using referencing
        long createRef100 = benchmarkOperation(() -> mongoItineraryRefRepository.saveAll(Ref100Itineraries)) + benchmarkOperation(() -> mongoUserRefRepository.saveAll(Ref100));
        long createRef1000 = benchmarkOperation(() -> mongoItineraryRefRepository.saveAll(Ref1000Itineraries)) + benchmarkOperation(() -> mongoUserRefRepository.saveAll(Ref1000));
        long createRef10000 = benchmarkOperation(() -> mongoItineraryRefRepository.saveAll(Ref10000Itineraries)) + benchmarkOperation(() -> mongoUserRefRepository.saveAll(Ref10000));

        long createEmb100 = benchmarkOperation(() -> mongoUserRepository.saveAll(Emb100));
        long createEmb1000 = benchmarkOperation(() -> mongoUserRepository.saveAll(Emb1000));
        long createEmb10000 = benchmarkOperation(() -> mongoUserRepository.saveAll(Emb10000));

        results.put("Create 100", List.of(createRef100, createEmb100));
        results.put("Create 1000", List.of(createRef1000, createEmb1000));
        results.put("Create 10000", List.of(createRef10000, createEmb10000));

        //Find without filter
        long refFindAll = benchmarkOperation(mongoUserRefRepository::findAll);
        long embFindAll = benchmarkOperation(mongoUserRepository::findAll);

        results.put("Find all", List.of(refFindAll, embFindAll));

        //Find with filter
        long refFindByUsername = benchmarkOperation(() -> mongoUserRefRepository.findByUsername("Ref1000username100"));
        long embFindByUsername = benchmarkOperation(() -> mongoUserRepository.findByUsername("Emb1000username100"));

        results.put("Find with filter", List.of(refFindByUsername, embFindByUsername));

        //Find with filter and projection
        long refFindByEmailProjectUsernameAndEmail = benchmarkOperation(() -> mongoUserRefRepository.findUserByEmail("Ref1000email100"));
        long embFindByEmailProjectUsernameAndEmail = benchmarkOperation(() -> mongoUserRepository.findUserByEmail("Emb1000email100"));

        results.put("Find with filter and projection", List.of(refFindByEmailProjectUsernameAndEmail, embFindByEmailProjectUsernameAndEmail));

        //Find with filter and projection and sort
//        long refFindByUsernameProjectUsernameAndEmailSortByCreatedAt = benchmarkOperation(() -> mongoUserRefRepository.findUsersByItinerariesSizeGreaterThanOrderByItinerariesAsc(2));
//        long embFindByUsernameProjectUsernameAndEmailSortByCreatedAt = benchmarkOperation(() -> mongoUserRepository.findUsersByItinerariesSizeGreaterThanOrderByItinerariesAsc(2));

//        results.put("Find with filter and projection and sort", List.of(refFindByUsernameProjectUsernameAndEmailSortByCreatedAt, embFindByUsernameProjectUsernameAndEmailSortByCreatedAt));

        //Update User
        long refUpdate = benchmarkOperation(() -> mongoUserService.updateUserName("Ref1000username100", "newUsername0"));
        long embUpdate = benchmarkOperation(() -> mongoUserService.updateUserName("Emb1000username100", "newUsername0"));

        results.put("Update", List.of(refUpdate, embUpdate));

        //Delete all Users
        long refDelete = benchmarkOperation(mongoUserRefRepository::deleteAll);
        long embDelete = benchmarkOperation(mongoUserRepository::deleteAll);

        results.put("Delete", List.of(refDelete, embDelete));

        return results;
    }
}
