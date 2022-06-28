package cz.muni.ics.kypo.training.adaptive;

import cz.muni.ics.kypo.commons.security.config.ResourceServerSecurityConfig;
import cz.muni.ics.kypo.commons.startup.config.MicroserviceRegistrationConfiguration;
import cz.muni.ics.kypo.training.adaptive.config.AdaptiveTrainingWebMvcConfigurer;
import cz.muni.ics.kypo.training.adaptive.config.CacheConfig;
import cz.muni.ics.kypo.training.adaptive.config.ObjectMappersConfiguration;
import cz.muni.ics.kypo.training.adaptive.config.ValidationMessagesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Import(value = {
        MicroserviceRegistrationConfiguration.class,
        ValidationMessagesConfig.class,
        ObjectMappersConfiguration.class,
        AdaptiveTrainingWebMvcConfigurer.class,
        ResourceServerSecurityConfig.class,
        CacheConfig.class
})
@EnableTransactionManagement
public class AdaptiveTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdaptiveTrainingApplication.class, args);
    }
}

