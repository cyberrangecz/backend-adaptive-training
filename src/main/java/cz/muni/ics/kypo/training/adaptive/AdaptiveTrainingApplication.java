package cz.muni.ics.kypo.training.adaptive;

import cz.muni.ics.kypo.commons.security.config.ResourceServerSecurityConfig;
import cz.muni.ics.kypo.commons.startup.config.MicroserviceRegistrationConfiguration;
import cz.muni.ics.kypo.training.adaptive.config.AdaptiveTrainingWebMvcConfigurer;
import cz.muni.ics.kypo.training.adaptive.config.ObjectMappersConfiguration;
import cz.muni.ics.kypo.training.adaptive.config.ValidationMessagesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {
        MicroserviceRegistrationConfiguration.class,
        ValidationMessagesConfig.class,
        ObjectMappersConfiguration.class,
        AdaptiveTrainingWebMvcConfigurer.class,
        ResourceServerSecurityConfig.class
})
public class AdaptiveTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdaptiveTrainingApplication.class, args);
    }
}

