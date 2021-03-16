package cz.muni.ics.kypo.training.adaptive.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.dto.AdaptiveSmartAssistantInput;
import cz.muni.ics.kypo.training.adaptive.dto.responses.SuitableTaskResponse;
import cz.muni.ics.kypo.training.adaptive.exceptions.CustomWebClientException;
import cz.muni.ics.kypo.training.adaptive.exceptions.MicroserviceApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The type Smart Assistant Service API.
 */
@Service
public class SmartAssistantServiceApi {

    private static final Logger LOG = LoggerFactory.getLogger(SmartAssistantServiceApi.class);
    private final WebClient smartAssistantServiceWebClient;
    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new SmartAssistantSearchService API.
     *
     * @param smartAssistantServiceWebClient the web client
     */
    public SmartAssistantServiceApi(@Qualifier("smartAssistantServiceWebClient") WebClient smartAssistantServiceWebClient,
                                    ObjectMapper objectMapper) {
        this.smartAssistantServiceWebClient = smartAssistantServiceWebClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Obtain suitable task for the current phase.
     *
     * @param smartAssistantInput input data for smart assistant to obtain suitable task.
     * @throws MicroserviceApiException error with specific message when calling elasticsearch microservice.
     */
    public SuitableTaskResponse findSuitableTaskInPhase(AdaptiveSmartAssistantInput smartAssistantInput) {
        try {
            return smartAssistantServiceWebClient
                    .post()
                    .uri("/adaptive-phases")
                    .body(Mono.just(objectMapper.writeValueAsString(smartAssistantInput)), String.class)
                    .retrieve()
                    .bodyToMono(SuitableTaskResponse.class)
                    .block();
        } catch (IOException ex) {
            throw new SecurityException("Error while parsing roles for microservices", ex);
        } catch (CustomWebClientException ex) {
            throw new MicroserviceApiException("Error when calling Smart Assistant Service API to obtain suitable task for phase (ID: " + smartAssistantInput.getPhaseX() + ")", ex.getApiSubError());
        }
    }
}
