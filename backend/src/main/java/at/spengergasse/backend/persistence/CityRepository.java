package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.City;
import org.springframework.data.repository.Repository;

public interface CityRepository extends Repository<City, Long>
{
    City save(City city);
    City findById(Long id);
    City findByName(String name);
    void deleteById(Long id);

    void deleteAll();
}
