package cz.cyberrange.platform.training.adaptive.rest.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cyberrange.platform.training.adaptive.rest.facade.annotations.security.IsDesignerOrAdmin;
import cz.cyberrange.platform.training.adaptive.rest.facade.annotations.transaction.TransactionalRO;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.InstanceModelUpdate;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.PhaseEvent;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.PhaseUserActions;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.QuestionnaireActions;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.TrainingInstanceInfo;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.ImportTrainingDefinition;
import cz.cyberrange.platform.training.adaptive.api.dto.responses.SuitableTaskResponse;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.sankey.SankeyDiagramDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.simulator.InstanceSimulatorDTO;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.BadRequestException;
import cz.cyberrange.platform.training.adaptive.service.InstanceSimulatorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Transactional
public class SankeySimulatorFacade {

    private final InstanceSimulatorService instanceSimulatorService;
    private final ObjectMapper objectMapper;

    static final Pattern sandboxDetailPattern = Pattern.compile("(sandbox-[a-z0-9-]{36}-details)");
    static final Pattern sandboxUserActionsPattern = Pattern.compile("(phase[0-9]+-useractions)");
    static final Pattern trainingRunDetailsPattern = Pattern.compile("(training_run-id[0-9]+-details)");
    static final Pattern trainingPhaseEventsPattern = Pattern.compile("(phase[0-9]+-events)");
    static final Pattern trainingRunQuestionnairesPattern = Pattern.compile("(training_run-id-[0-9]+-questionnaires)");
    static final Pattern trainingRunQuestionnaireAnswersPattern = Pattern.compile("(questionnaire_id-[0-9]+-answers)");
    static final Pattern sandboxIdPatter = Pattern.compile("(\"sandbox_id\":[a-z0-9-]{36})");
    static final Pattern trainingRunIdPattern = Pattern.compile("(training_run-id[0-9]+)");

    public SankeySimulatorFacade(InstanceSimulatorService instanceSimulatorService,
                                 ObjectMapper objectMapper) {
        this.instanceSimulatorService = instanceSimulatorService;
        this.objectMapper = objectMapper;
    }


    @IsDesignerOrAdmin
    @TransactionalRO
    public InstanceSimulatorDTO processTrainingInstance(byte[] zipFile) {
        try {
            return processInstanceZip(zipFile);
        } catch (IOException e) {
            throw new BadRequestException("The file was not processed. Unsupported data format. Please provide a zip file of adaptive training instance", e);
        }
    }

    @IsDesignerOrAdmin
    @TransactionalRO
    public SankeyDiagramDTO generateSankeyDiagram(InstanceModelUpdate instanceModelUpdate) {
        List<List<SuitableTaskResponse>> suitableTasks = instanceSimulatorService.getSuitableTasks(instanceModelUpdate.getCacheId(), instanceModelUpdate.getPhases());
        return instanceSimulatorService.getSankeyDiagram(suitableTasks, instanceModelUpdate.getPhases());
    }

    /**
     * Processes the zip file, stores the data into the cache and returns parsed sankey diagram and training definition
     * together with the cache key.
     * @param file input zip file provided by user.
     * @return parsed sankey diagram and training definition together with the cache key.
     * @throws IOException if the zip file is not valid.
     */
    private InstanceSimulatorDTO processInstanceZip(byte[] file) throws IOException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        ByteArrayInputStream byteStream = new ByteArrayInputStream(file);
        ZipInputStream zis = new ZipInputStream(byteStream);
        ZipEntry zipEntry;

        // Map<TrainingRunId, SandboxId>
        Map<Long, String> traineesIdentification = new HashMap<>();
        // Map<TrainingRunId, Map<phaseId, List<QuestionnaireActions>>>
        Map<Long, Map<Long, List<QuestionnaireActions>>> questionnaireActions = new HashMap<>();
        // Map<TrainingRunId, Map<phaseId, List<PhaseEvent>>>
        Map<Long, Map<Long, List<PhaseEvent>>> trainingEvents = new HashMap<>();
        // Map<SandboxId, Map<phaseId, List<PhaseUserActions>>>
        Map<String, Map<Long, List<PhaseUserActions>>> sandboxUseractions = new HashMap<>();

        SankeyDiagramDTO sankeyDiagramDTO = new SankeyDiagramDTO();
        ImportTrainingDefinition trainingDefinition = new ImportTrainingDefinition();
        TrainingInstanceInfo trainingInstanceInfo = new TrainingInstanceInfo();

        try {
            zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                String zipEntryName = zipEntry.getName();
                String fileContent = new String(zis.readAllBytes(), StandardCharsets.UTF_8);
                if (!zipEntry.isDirectory() && Pattern.matches("training_definition.*", zipEntryName)) {
                    trainingDefinition = this.processDefinition(fileContent, validator, zipEntryName);
                } else if (!zipEntry.isDirectory() && Pattern.matches("sankey_diagram.*", zipEntryName)) {
                    sankeyDiagramDTO = this.processSankey(fileContent, validator, zipEntryName);
                } else if (Pattern.matches("logs/sandbox-[a-z0-9-]{36}-details/phase[0-9]+-useractions\\.json$", zipEntryName)) {
                    sandboxUseractions = Optional.ofNullable(this.processSandboxLogs(fileContent, zipEntryName, sandboxUseractions, validator)).orElse(sandboxUseractions);
                } else if (Pattern.matches("training_events/training_run-id[0-9]+-details/phase[0-9]+-events\\.json$", zipEntryName)) {
                    trainingEvents = Optional.ofNullable(this.processPhaseEvents(fileContent, zipEntryName, trainingEvents, validator)).orElse(trainingEvents);
                } else if (Pattern.matches("questionnaires_answers/training_run-id-[0-9]+-questionnaires/questionnaire_id-[0-9]+-answers\\.json", zipEntryName)) {
                    questionnaireActions = Optional.ofNullable(this.processQuestionnaire(fileContent, zipEntryName, questionnaireActions, validator)).orElse(questionnaireActions);
                } else if (Pattern.matches("training_events/training_run-id[0-9]+-events\\.json", zipEntryName)) {
                    this.processTrainingEvents(fileContent, zipEntryName, traineesIdentification);
                } else if (Pattern.matches("training_instance-id[0-9]+\\.json", zipEntryName)) {
                    trainingInstanceInfo = this.processInstance(fileContent, validator, zipEntryName);
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (IOException e) {
            zis.closeEntry();
            zis.close();
            throw new BadRequestException("The file was not processed. Unsupported data format. Please provide a zip file of adaptive training instance", e);
        }
        String cacheKey = instanceSimulatorService.cacheTraineesPerformance(traineesIdentification, questionnaireActions, trainingEvents, sandboxUseractions, trainingDefinition, trainingInstanceInfo);

        if (trainingDefinition.getId() == null || trainingDefinition.getTitle() == null) {
            throw new BadRequestException("The file was not processed. Unsupported data format. Please provide a zip file of adaptive training instance");
        }
        return new InstanceSimulatorDTO(sankeyDiagramDTO, trainingDefinition, cacheKey);
    }

    private ImportTrainingDefinition processDefinition(String fileContent, Validator validator, String fileName) throws JsonProcessingException, BadRequestException {
        ImportTrainingDefinition trainingDefinition = objectMapper.readValue(fileContent, ImportTrainingDefinition.class);
        Set<ConstraintViolation<ImportTrainingDefinition>> violations = validator.validate(trainingDefinition);
        if (!violations.isEmpty()) {
            throw new BadRequestException("Could not parse Training definition from: " + fileName + " Missing: " + violations.iterator().next().getMessage());
        }
        return trainingDefinition;
    }

    private TrainingInstanceInfo processInstance(String fileContent, Validator validator, String fileName) throws JsonProcessingException, BadRequestException {
        TrainingInstanceInfo trainingInstanceInfo = objectMapper.readValue(fileContent, TrainingInstanceInfo.class);
        Set<ConstraintViolation<TrainingInstanceInfo>> violations = validator.validate(trainingInstanceInfo);
        if (!violations.isEmpty()) {
            throw new BadRequestException("Could not parse Training definition from: " + fileName + " Missing: " + violations.iterator().next().getMessage());
        }
        return trainingInstanceInfo;
    }

    private SankeyDiagramDTO processSankey(String fileContent, Validator validator, String fileName) throws JsonProcessingException, BadRequestException {
        SankeyDiagramDTO sankeyDiagramDTO = objectMapper.readValue(fileContent, SankeyDiagramDTO.class);
        Set<ConstraintViolation<SankeyDiagramDTO>> violations = validator.validate(sankeyDiagramDTO);
        if (!violations.isEmpty()) {
            throw new BadRequestException("Could not parse initial Sankey diagram from: " + fileName + " Missing: " + violations.iterator().next().getMessage());
        }
        return sankeyDiagramDTO;
    }

    private Map<String, Map<Long, List<PhaseUserActions>>> processSandboxLogs(
            String fileContent,
            String zipEntryName,
            Map<String, Map<Long, List<PhaseUserActions>>> sandboxUseractions,
            Validator validator
    ) throws JsonProcessingException, BadRequestException {
        Matcher matcher = sandboxDetailPattern.matcher(zipEntryName);
        if (matcher.find()) {
            String sandboxId = matcher.group(1).replaceAll("[a-z0-9-]{36}","");
            matcher = sandboxUserActionsPattern.matcher(zipEntryName);
            if (matcher.find()) {
                Long phaseId = Long.parseLong(matcher.group(1).replaceAll("[^0-9]+",""));
                return instanceSimulatorService.parseSandboxInteractions(fileContent, sandboxId, phaseId, sandboxUseractions, validator);
            }
        }
        return null;
    }

    private Map<Long, Map<Long, List<PhaseEvent>>> processPhaseEvents(
            String fileContent,
            String zipEntryName,
            Map<Long, Map<Long, List<PhaseEvent>>> trainingEvents,
            Validator validator
    ) throws JsonProcessingException, BadRequestException {
        Matcher matcher = trainingRunDetailsPattern.matcher(zipEntryName);
        if (matcher.find()) {
            Long trainingRunId = Long.parseLong(matcher.group(1).replaceAll("[^0-9]+", ""));
            matcher = trainingPhaseEventsPattern.matcher(zipEntryName);
            if (matcher.find()) {
                Long phaseId = Long.parseLong(matcher.group(1).replaceAll("[^0-9]+",""));
                return instanceSimulatorService.parseTrainingEvents(fileContent, trainingRunId, phaseId, trainingEvents, validator);
            }
        }
        return null;
    }

    private Map<Long, Map<Long, List<QuestionnaireActions>>> processQuestionnaire(
            String fileContent,
            String zipEntryName,
            Map<Long, Map<Long, List<QuestionnaireActions>>> questionnaireActions,
            Validator validator
    ) throws JsonProcessingException, BadRequestException {
        Matcher matcher = trainingRunQuestionnairesPattern.matcher(zipEntryName);
        if (matcher.find()) {
            Long trainingRunId = Long.parseLong(matcher.group(1).replaceAll("[^0-9]+", ""));
            matcher = trainingRunQuestionnaireAnswersPattern.matcher(zipEntryName);
            if (matcher.find()) {
                Long phaseId = Long.parseLong(matcher.group(1).replaceAll("[^0-9]+",""));
                return instanceSimulatorService.parseQuestionnaireAnswers(fileContent, trainingRunId, phaseId, questionnaireActions, validator);
            }
        }
        return null;
    }

    private void processTrainingEvents(String fileContent, String zipEntryName, Map<Long, String> traineesIdentification) {
        Matcher matcher = trainingRunIdPattern.matcher(zipEntryName);

        if (matcher.find()) {
            Long trainingRunId = Long.parseLong(matcher.group(1).replaceAll("[^0-9]+", ""));
            matcher = sandboxIdPatter.matcher(fileContent);
            if (matcher.find()) {
                String sandboxId = matcher.group(1).replaceAll("[a-z0-9-]{36}", "");
                traineesIdentification.put(trainingRunId, sandboxId);
            }
        }
    }
}
