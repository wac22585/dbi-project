package at.spengergasse.backend.relational.persistence;

import at.spengergasse.backend.relational.model.City;
import org.springframework.data.repository.Repository;

public interface JpaCityRepository extends Repository<City, Long>
{
    City save(City city);
    City findById(Long id);
    City findByCity(String city);
    void deleteById(Long id);

    void deleteAll();
}
