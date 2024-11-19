package at.spengergasse.backend.performancetesting;

import at.spengergasse.backend.mongodb.persistence.MongoUserRepository;
import at.spengergasse.backend.relational.persistence.JpaUserRepository;
import at.spengergasse.backend.seeder.DatabaseSeeder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = PerformanceController.PERFORMANCE_PATH)
public class PerformanceController
{
    public static final String PERFORMANCE_PATH = "api/performance";

    private final MongoUserRepository mongoUserRepository;
    private final JpaUserRepository jpaUserRepository;
    private final DatabaseSeeder databaseSeeder;

    @GetMapping(value = "/crud", produces = "application/json")
    public Map<String, Long> crudPerformance(@RequestParam int size)
    {
        return databaseSeeder.seedAndBenchmarkDatabase(mongoUserRepository, jpaUserRepository, size);
    }
}
