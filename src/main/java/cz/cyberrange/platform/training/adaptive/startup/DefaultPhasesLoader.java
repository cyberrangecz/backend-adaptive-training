package cz.cyberrange.platform.training.adaptive.startup;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Component
public class DefaultPhasesLoader {

    @Value("${path.to.default.phases:}")
    private String pathToDefaultPhases;
    private DefaultPhases defaultPhases;
    private final Validator validator;

    @Autowired
    public DefaultPhasesLoader(Validator validator) {
        this.validator = validator;
    }

    @PostConstruct
    private void loadDefaultPhases() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        try {
            InputStream inputStream = pathToDefaultPhases.isBlank() ? getClass().getResourceAsStream("/default-phases.json") : new FileInputStream(pathToDefaultPhases);
            defaultPhases = mapper.readValue(inputStream, DefaultPhases.class);
            Set<ConstraintViolation<DefaultPhases>> violations = this.validator.validate(defaultPhases);
            if (!violations.isEmpty()) {
                throw new InternalServerErrorException("Could not load the default phases. Reason: " + violations.stream()
                        .map(ConstraintViolation::getMessage).toList());
            }
        } catch (IOException e) {
            throw new InternalServerErrorException("Could not load file with the default phases.", e);
        }
    }

    public DefaultAccessPhase getDefaultAccessPhase() {
        return this.defaultPhases.getDefaultAccessPhase();
    }

    public DefaultInfoPhase getDefaultInfoPhase() {
        return this.defaultPhases.getDefaultInfoPhase();
    }
}
