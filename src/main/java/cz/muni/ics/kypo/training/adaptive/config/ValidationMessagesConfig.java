package cz.muni.ics.kypo.training.adaptive.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

/**
 * The type Validation messages config.
 */
@Configuration
public class ValidationMessagesConfig {

    /**
     * Prints available locales. It is useful to set up appropriate ValidationMessages.properties file name,
     * e.g. messages_en_US.properties
     * <p>
     * en_US
     * <p>
     * en -> language; US -> country
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Locale[] locales = Locale.getAvailableLocales();
        Arrays.sort(locales, Comparator.comparing(Locale::toString));
        for (Locale l : locales) {
            System.out.println(l.toString());
        }
    }

    /**
     * Message source validation message source.
     *
     * @return the message source
     */
    @Bean
    public MessageSource messageSourceValidation() {
        final ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:locale/ValidationMessages");
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        source.setCacheSeconds(0);
        return source;
    }

    /**
     * Gets validator.
     *
     * @return the validator
     */
    @Bean
    @Primary
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSourceValidation());
        return bean;
    }

}
