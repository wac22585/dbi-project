package at.spengergasse.backend.performancetesting;

import at.spengergasse.backend.mongodb.persistence.MongoItineraryRepository;
import at.spengergasse.backend.mongodb.persistence.MongoUserRepository;
import at.spengergasse.backend.relational.persistence.JpaItineraryRepository;
import at.spengergasse.backend.relational.persistence.JpaUserRepository;
import at.spengergasse.backend.seeder.DatabaseSeeder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = PerformanceController.PERFORMANCE_PATH)
public class PerformanceController
{
    public static final String PERFORMANCE_PATH = "api/performance";
    private final DatabaseSeeder databaseSeeder;

    @GetMapping(value = "/benchmarks-mongo-sql", produces = "application/json")
    public Map<String, List<Long>> benchmarks()
    {
        return databaseSeeder.benchmarks();
    }

    @GetMapping(value = "/benchmarks-embedding-referencing", produces = "application/json")
    public Map<String, List<Long>> benchmarksRef()
    {
       return databaseSeeder.benchmarksRef();
    }
}
