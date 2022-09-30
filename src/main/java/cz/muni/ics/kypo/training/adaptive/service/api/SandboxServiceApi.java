package cz.muni.ics.kypo.training.adaptive.service.api;

import cz.muni.ics.kypo.training.adaptive.dto.responses.LockedPoolInfo;
import cz.muni.ics.kypo.training.adaptive.dto.responses.PoolInfoDTO;
import cz.muni.ics.kypo.training.adaptive.dto.responses.SandboxDefinitionInfo;
import cz.muni.ics.kypo.training.adaptive.dto.responses.SandboxInfo;
import cz.muni.ics.kypo.training.adaptive.exceptions.CustomWebClientException;
import cz.muni.ics.kypo.training.adaptive.exceptions.ForbiddenException;
import cz.muni.ics.kypo.training.adaptive.exceptions.MicroserviceApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


/**
 * The type User service.
 */
@Service
public class SandboxServiceApi {

    private static final Logger LOG = LoggerFactory.getLogger(SandboxServiceApi.class);
    private final WebClient sandboxServiceWebClient;


    /**
     * Instantiates a new SandboxService API.
     *
     * @param sandboxServiceWebClient the web client
     */
    public SandboxServiceApi(@Qualifier("sandboxServiceWebClient") WebClient sandboxServiceWebClient) {
        this.sandboxServiceWebClient = sandboxServiceWebClient;
    }


    public String getAndLockSandboxForTrainingRun(Long poolId) {
        try {
            SandboxInfo sandboxInfo = sandboxServiceWebClient
                    .get()
                    .uri("/pools/{poolId}/sandboxes/get-and-lock", poolId)
                    .retrieve()
                    .bodyToMono(SandboxInfo.class)
                    .block();
            return sandboxInfo.getId();
        } catch (CustomWebClientException ex) {
            if (ex.getStatusCode() == HttpStatus.CONFLICT) {
                throw new ForbiddenException("There is no available sandbox, wait a minute and try again or ask organizer to allocate more sandboxes.");
            }
            throw new MicroserviceApiException("Error when calling OpenStack Sandbox Service API to get unlocked sandbox from pool (ID: " + poolId + ").", ex);
        }
    }

    /**
     * Lock pool locked pool info.
     *
     * @param poolId the pool id
     * @return the locked pool info
     */
    public LockedPoolInfo lockPool(Long poolId) {
        try {
            return sandboxServiceWebClient
                    .post()
                    .uri("/pools/{poolId}/locks", poolId)
                    .body(Mono.just("{}"), String.class)
                    .retrieve()
                    .bodyToMono(LockedPoolInfo.class)
                    .block();
        } catch (CustomWebClientException ex) {
            throw new MicroserviceApiException("Currently, it is not possible to lock and assign pool with (ID: " + poolId + ").", ex);
        }
    }

    /**
     * Unlock pool.
     *
     * @param poolId the pool id
     */
    public void unlockPool(Long poolId) {
        try {
            // get lock id from pool
            PoolInfoDTO poolInfoDto = sandboxServiceWebClient
                    .get()
                    .uri("/pools/{poolId}", poolId)
                    .retrieve()
                    .bodyToMono(PoolInfoDTO.class)
                    .block();
            // unlock pool
            if (poolInfoDto != null && poolInfoDto.getLockId() != null) {
                sandboxServiceWebClient
                        .delete()
                        .uri("/pools/{poolId}/locks/{lockId}", poolId, poolInfoDto.getLockId())
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();
            }
        } catch (CustomWebClientException ex) {
            if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw new MicroserviceApiException("Currently, it is not possible to unlock a pool with (ID: " + poolId + ").", ex);
            }
        }
    }

    /**
     * Gets sandbox definition id.
     *
     * @param poolId the pool id
     * @return the sandbox definition id
     */
    public SandboxDefinitionInfo getSandboxDefinitionId(Long poolId) {
        try {
            return sandboxServiceWebClient
                    .get()
                    .uri("/pools/{poolId}/definition", poolId)
                    .retrieve()
                    .bodyToMono(SandboxDefinitionInfo.class)
                    .block();
        } catch (CustomWebClientException ex) {
            if (ex.getStatusCode() == HttpStatus.CONFLICT) {
                throw new ForbiddenException("There is no available sandbox definition for particular pool (ID: " + poolId + ").");
            }
            throw new MicroserviceApiException("Error when calling Python API to sandbox for particular pool (ID: " + poolId + ").", ex);
        }
    }
}
