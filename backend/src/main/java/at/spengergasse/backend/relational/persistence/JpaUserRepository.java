package at.spengergasse.backend.relational.persistence;

import at.spengergasse.backend.relational.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaUserRepository extends JpaRepository<User, Long>
{
    User findByUsername(String username);
//    List<User> findAll(Specification<User> spec);
//    @Query("SELECT u FROM User u WHERE u.:fieldName = :value")
    // Filter
//    List<User> findByField(@Param("fieldName") String fieldName, @Param("value") String value);
//
//    // 2. Find with filter and projection
////    @Query("SELECT u.:projectionField FROM User u WHERE u.:fieldName = :value")
//    List<Object> findWithProjection(@Param("fieldName") String fieldName, @Param("value") String value, @Param("projectionField") String projectionField);
//
//    // 3. Find with filter, projection, and sorting
////    @Query("SELECT u.:projectionField FROM User u WHERE u.:fieldName = :value ORDER BY u.:sortField :sortDirection")
//    @Query(value = "SELECT :projectionField FROM users WHERE :fieldName = :value ORDER BY :sortField :sortDirection", nativeQuery = true)
//    List<Object> findWithProjectionAndSorting(
//            @Param("fieldName") String fieldName,
//            @Param("value") String value,
//            @Param("projectionField") String projectionField,
//            @Param("sortField") String sortField,
//            @Param("sortDirection") String sortDirection
//    );
    UserWithoutPwd findUserByEmail(String email);
    User findByEmail(String email);

    UserWithItineraryNames findUserByUsername(String username);
    void deleteByUsername(String username);
}
