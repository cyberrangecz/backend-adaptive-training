package cz.cyberrange.platform.training.adaptive.service.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cyberrange.platform.events.adaptive.trainings.AbstractAuditAdaptivePOJO;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.ElasticsearchTrainingServiceLayerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * The type Audit service.
 */
@Service
public class AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new Audit service.
     *
     * @param objectMapper the object mapper
     */
    @Autowired
    public AuditService(@Qualifier("objMapperForElasticsearch") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Method for saving general class into Elasticsearch under specific index and type.
     *
     * @param <T>       the type parameter
     * @param pojoClass class saving to Elasticsearch
     * @throws ElasticsearchTrainingServiceLayerException the elasticsearch training service layer exception
     */
    public <T extends AbstractAuditAdaptivePOJO> void saveTrainingRunEvent(T pojoClass, long timestampDelay) {
        Assert.notNull(pojoClass, "Null class could not be saved via audit method.");
        try {
            pojoClass.setTimestamp(System.currentTimeMillis() + timestampDelay);
            pojoClass.setType(pojoClass.getClass().getName());

            logger.info(objectMapper.writeValueAsString(pojoClass));
        } catch (IOException ex) {
            throw new ElasticsearchTrainingServiceLayerException(ex);
        }
    }

}
