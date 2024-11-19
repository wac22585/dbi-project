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

    //itinerary.getStartDate() filter endpoint für frontend
//    @GetMapping(value = "/find-tests", produces = "application/json")
//    public Map<String, Object> findPerformanceTests(
//            @RequestParam(required = false) String filterField,
//            @RequestParam(required = false) String filterValue,
//            @RequestParam(required = false) String projectionField,
//            @RequestParam(required = false) String sortField,
//            @RequestParam(defaultValue = "asc") String sortDirection
//    ) {
//        Map<String, Object> results = new HashMap<>();
//
//        // 1. Find ohne Filter
//        results.put("mongoFindAll", mongoUserRepository.findAll());
//        results.put("jpaFindAll", jpaUserRepository.findAll());
//
//        // 2. Find mit Filter
//        if (filterField != null && filterValue != null) {
//            results.put("mongoFindWithFilter", mongoUserRepository.findByField(filterField, filterValue));
//            results.put("jpaFindWithFilter", jpaUserRepository.findByField(filterField, filterValue));
//        }
//
//        // 3. Find mit Filter & Projektion
//        if (filterField != null && filterValue != null && projectionField != null) {
//            results.put("mongoFindWithFilterAndProjection", mongoUserRepository.findWithProjection(filterField, filterValue, projectionField));
//            results.put("jpaFindWithFilterAndProjection", jpaUserRepository.findWithProjection(filterField, filterValue, projectionField));
//        }
//
//        // 4. Find mit Filter, Projektion & Sortierung
//        if (filterField != null && filterValue != null && projectionField != null && sortField != null) {
//            boolean isAscending = sortDirection.equalsIgnoreCase("asc");
//            results.put("mongoFindWithFilterProjectionAndSort", mongoUserRepository.findWithProjectionAndSorting(filterField, filterValue, projectionField, sortField, isAscending));
////            results.put("jpaFindWithFilterProjectionAndSort", jpaUserRepository.findWithProjectionAndSorting(filterField, filterValue, projectionField, sortField, isAscending));
////        }
//
//        return results;
//    }
}
