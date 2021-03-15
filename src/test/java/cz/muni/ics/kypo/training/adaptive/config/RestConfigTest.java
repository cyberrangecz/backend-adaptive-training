package cz.muni.ics.kypo.training.adaptive.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.muni.ics.kypo.training.adaptive.service.api.ElasticsearchServiceApi;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

@Configuration
@ComponentScan(basePackages = {"cz.muni.ics.kypo.training.adaptive.mapping", "cz.muni.ics.kypo.training.adaptive.facade", "cz.muni.ics.kypo.training.adaptive.service"})
@EntityScan(basePackages = {"cz.muni.ics.kypo.training.adaptive.domain"},  basePackageClasses = Jsr310JpaConverters.class)
@EnableJpaRepositories(basePackages = {"cz.muni.ics.kypo.training.adaptive.repository"})
public class RestConfigTest {
	private static final Logger LOG = LoggerFactory.getLogger(RestConfigTest.class);


	@Bean
	@Qualifier("userManagementExchangeFunction")
	public ExchangeFunction userManagementExchangeFunction(){
		return Mockito.mock(ExchangeFunction.class);
	}

	@Bean
	@Qualifier("sandboxManagementExchangeFunction")
	public ExchangeFunction sandboxManagementExchangeFunction(){
		return Mockito.mock(ExchangeFunction.class);
	}

	@Bean
	@Qualifier("elasticsearchExchangeFunction")
	public ExchangeFunction elasticsearchExchangeFunction(){
		return Mockito.mock(ExchangeFunction.class);
	}

	@Bean
	@Primary
	@Qualifier("userManagementServiceWebClient")
	public WebClient userManagementServiceWebClient(){
		return WebClient.builder()
				.exchangeFunction(userManagementExchangeFunction())
				.build();
	}

	@Bean
	@Qualifier("sandboxServiceWebClient")
	public WebClient sandboxServiceWebClient(){
		return WebClient.builder()
				.exchangeFunction(sandboxManagementExchangeFunction())
				.build();
	}

	@Bean
	@Qualifier("elasticsearchServiceWebClient")
	public WebClient elasticsearchServiceWebClient(){
		return WebClient.builder()
				.exchangeFunction(elasticsearchExchangeFunction())
				.build();
	}

	@Bean
	@Qualifier("smartAssistantServiceWebClient")
	public WebClient smartAssistantServiceWebClient(){
		return WebClient.builder()
				.exchangeFunction(elasticsearchExchangeFunction())
				.build();
	}

	@Bean
	@Primary
	@Qualifier("objMapperRESTApi")
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		return mapper;
	}

	@Bean
	@Qualifier("objMapperForElasticsearch")
	public ObjectMapper elasticsearchObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return objectMapper;
	}

	@Bean
	@Primary
	public ElasticsearchServiceApi elasticsearchApiServiceMock(){
		return Mockito.mock(ElasticsearchServiceApi.class);
	}

	@Bean
	public EmailValidator usernameValidator() {
		LOG.debug("usernameValidator()");
		return new EmailValidator();
	}

	@Bean
	public HttpServletRequest httpServletRequest(){
		return new HttpServletRequestWrapper(new Request(new Connector()));
	}

}
