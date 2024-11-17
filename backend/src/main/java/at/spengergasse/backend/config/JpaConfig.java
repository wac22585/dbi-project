package at.spengergasse.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "at.spengergasse.backend.relational.persistence"
)
public class JpaConfig { }
