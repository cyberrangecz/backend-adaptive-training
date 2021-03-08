package cz.muni.ics.kypo.training.adaptive.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"cz.muni.ics.kypo.training.adaptive.domain", "cz.muni.ics.kypo.training.adaptive.repository"})
@EntityScan(basePackages = "cz.muni.ics.kypo.training.adaptive.domain")
@EnableJpaRepositories(basePackages = "cz.muni.ics.kypo.training.adaptive.repository")
public class PersistenceConfigTest {

}
