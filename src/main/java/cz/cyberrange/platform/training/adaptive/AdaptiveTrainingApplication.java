package cz.cyberrange.platform.training.adaptive;

import cz.cyberrange.platform.commons.security.config.ResourceServerSecurityConfig;
import cz.cyberrange.platform.commons.startup.config.MicroserviceRegistrationConfiguration;
import cz.cyberrange.platform.training.adaptive.definition.config.AdaptiveTrainingWebMvcConfigurer;
import cz.cyberrange.platform.training.adaptive.definition.config.CacheConfig;
import cz.cyberrange.platform.training.adaptive.definition.config.ObjectMappersConfiguration;
import cz.cyberrange.platform.training.adaptive.definition.config.ValidationMessagesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
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

