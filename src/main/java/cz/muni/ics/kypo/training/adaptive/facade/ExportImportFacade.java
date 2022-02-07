package cz.muni.ics.kypo.training.adaptive.facade;

import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.csirt.kypo.events.adaptive.trainings.PhaseStarted;
import cz.muni.ics.kypo.training.adaptive.annotations.security.IsDesignerOrAdmin;
import cz.muni.ics.kypo.training.adaptive.annotations.security.IsDesignerOrOrganizerOrAdmin;
import cz.muni.ics.kypo.training.adaptive.annotations.security.IsOrganizerOrAdmin;
import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalRO;
import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalWO;
import cz.muni.ics.kypo.training.adaptive.domain.User;
import cz.muni.ics.kypo.training.adaptive.domain.phase.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.QuestionnairePhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.Question;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionAnswer;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.AbstractPhaseArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.questionnaire.QuestionAnswerArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.questionnaire.QuestionAnswersDetailsDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.training.TrainingDefinitionArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.training.TrainingInstanceArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.training.TrainingRunArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.FileToReturnDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.phases.AbstractPhaseExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.training.TrainingDefinitionWithPhasesExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.ImportTrainingDefinitionDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.AbstractPhaseImportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.access.AccessPhaseImportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.info.InfoPhaseImportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.questionnaire.QuestionnairePhaseImportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.training.TrainingPhaseImportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.responses.SandboxDefinitionInfo;
import cz.muni.ics.kypo.training.adaptive.dto.trainingdefinition.TrainingDefinitionByIdDTO;
import cz.muni.ics.kypo.training.adaptive.dto.visualizations.sankey.SankeyDiagramDTO;
import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;
import cz.muni.ics.kypo.training.adaptive.enums.TDState;
import cz.muni.ics.kypo.training.adaptive.exceptions.InternalServerErrorException;
import cz.muni.ics.kypo.training.adaptive.mapping.ExportImportMapper;
import cz.muni.ics.kypo.training.adaptive.mapping.PhaseMapper;
import cz.muni.ics.kypo.training.adaptive.mapping.QuestionPhaseRelationMapper;
import cz.muni.ics.kypo.training.adaptive.mapping.TrainingDefinitionMapper;
import cz.muni.ics.kypo.training.adaptive.service.ExportImportService;
import cz.muni.ics.kypo.training.adaptive.service.VisualizationService;
import cz.muni.ics.kypo.training.adaptive.service.api.ElasticsearchServiceApi;
import cz.muni.ics.kypo.training.adaptive.service.api.SandboxServiceApi;
import cz.muni.ics.kypo.training.adaptive.service.training.TrainingDefinitionService;
import cz.muni.ics.kypo.training.adaptive.utils.AbstractFileExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The type Export import facade.
 */
@Service
@Transactional
public class ExportImportFacade {

    private static final Logger LOG = LoggerFactory.getLogger(ExportImportFacade.class);
    private static final String LOGS_FOLDER = "logs";
    private static final String EVENTS_FOLDER = "training_events";
    private static final String QUESTIONNAIRES_ANSWERS_FOLDER = "questionnaires_answers";
    private static final String RUNS_FOLDER = "training_runs";

    private final ExportImportService exportImportService;
    private final TrainingDefinitionService trainingDefinitionService;
    private final VisualizationService visualizationService;
    private final SandboxServiceApi sandboxServiceApi;
    private final ElasticsearchServiceApi elasticsearchServiceApi;
    private final ExportImportMapper exportImportMapper;
    private final PhaseMapper phaseMapper;
    private final QuestionPhaseRelationMapper questionPhaseRelationMapper;
    private final TrainingDefinitionMapper trainingDefinitionMapper;
    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new Export import facade.
     *
     * @param exportImportService       the export import service
     * @param exportImportMapper        the export import mapper
     * @param phaseMapper               the phase mapper
     * @param trainingDefinitionService the training definition service
     * @param trainingDefinitionMapper  the training definition mapper
     * @param objectMapper              the object mapper
     */
    @Autowired
    public ExportImportFacade(ExportImportService exportImportService,
                              TrainingDefinitionService trainingDefinitionService,
                              VisualizationService visualizationService,
                              SandboxServiceApi sandboxServiceApi,
                              ElasticsearchServiceApi elasticsearchServiceApi,
                              ExportImportMapper exportImportMapper,
                              PhaseMapper phaseMapper,
                              QuestionPhaseRelationMapper questionPhaseRelationMapper,
                              TrainingDefinitionMapper trainingDefinitionMapper,
                              ObjectMapper objectMapper) {
        this.exportImportService = exportImportService;
        this.trainingDefinitionService = trainingDefinitionService;
        this.visualizationService = visualizationService;
        this.sandboxServiceApi = sandboxServiceApi;
        this.elasticsearchServiceApi = elasticsearchServiceApi;
        this.exportImportMapper = exportImportMapper;
        this.phaseMapper = phaseMapper;
        this.questionPhaseRelationMapper = questionPhaseRelationMapper;
        this.trainingDefinitionMapper = trainingDefinitionMapper;
        this.objectMapper = objectMapper;
    }

    /**
     * Exports Training Definition to file
     *
     * @param trainingDefinitionId the id of the definition to be exported
     * @return the file containing definition, {@link FileToReturnDTO}
     */
    @IsDesignerOrOrganizerOrAdmin
    @TransactionalRO
    public FileToReturnDTO dbExport(Long trainingDefinitionId) {
        TrainingDefinition td = exportImportService.findById(trainingDefinitionId);
        TrainingDefinitionWithPhasesExportDTO dbExport = exportImportMapper.mapToDTO(td);
        if (dbExport != null) {
            dbExport.setPhases(mapAbstractPhaseToAbstractPhaseDTO(trainingDefinitionId));
        }
        try {
            FileToReturnDTO fileToReturnDTO = new FileToReturnDTO();
            fileToReturnDTO.setContent(objectMapper.writeValueAsBytes(dbExport));
            if (dbExport != null && dbExport.getTitle() != null) {
                fileToReturnDTO.setTitle(dbExport.getTitle());
            } else {
                fileToReturnDTO.setTitle("");
            }
            return fileToReturnDTO;
        } catch (IOException ex) {
            throw new InternalServerErrorException(ex);
        }
    }

    private List<AbstractPhaseExportDTO> mapAbstractPhaseToAbstractPhaseDTO(Long trainingDefinitionId) {
        List<AbstractPhaseExportDTO> abstractPhaseExportDTOs = new ArrayList<>();
        List<AbstractPhase> abstractPhases = trainingDefinitionService.findAllPhasesFromDefinition(trainingDefinitionId);
        abstractPhases.forEach(phase ->
                abstractPhaseExportDTOs.add(phaseMapper.mapToExportDTO(phase)));
        return abstractPhaseExportDTOs;
    }

    private List<AbstractPhaseArchiveDTO> mapAbstractPhasesToArchiveDTO(Long trainingDefinitionId) {
        List<AbstractPhaseArchiveDTO> abstractPhaseArchiveDTOs = new ArrayList<>();
        List<AbstractPhase> abstractPhases = trainingDefinitionService.findAllPhasesFromDefinition(trainingDefinitionId);
        abstractPhases.forEach(phase ->
                abstractPhaseArchiveDTOs.add(phaseMapper.mapToArchiveDTO(phase)));
        return abstractPhaseArchiveDTOs;
    }

    /**
     * Imports training definition.
     *
     * @param importTrainingDefinitionDTO the training definition to be imported
     * @return the {@link TrainingDefinitionByIdDTO}
     */
    @IsDesignerOrAdmin
    @TransactionalWO
    public TrainingDefinitionByIdDTO dbImport(ImportTrainingDefinitionDTO importTrainingDefinitionDTO) {
        importTrainingDefinitionDTO.setState(TDState.UNRELEASED);
        if (importTrainingDefinitionDTO.getTitle() != null && !importTrainingDefinitionDTO.getTitle().startsWith("Uploaded")) {
            importTrainingDefinitionDTO.setTitle("Uploaded " + importTrainingDefinitionDTO.getTitle());
        }

        TrainingDefinition newDefinition = exportImportMapper.mapToEntity(importTrainingDefinitionDTO);
        newDefinition.setEstimatedDuration(computeEstimatedDuration(importTrainingDefinitionDTO));
        TrainingDefinition newTrainingDefinition = trainingDefinitionService.create(newDefinition, false);
        List<AbstractPhaseImportDTO> phases = importTrainingDefinitionDTO.getPhases();
        phases.forEach(phase -> {
            if (phase.getPhaseType().equals(PhaseType.TRAINING)) {
                exportImportService.createTrainingPhase(phaseMapper.mapToEntity((TrainingPhaseImportDTO) phase), newTrainingDefinition);
            } else if (phase.getPhaseType().equals(PhaseType.INFO)) {
                exportImportService.createInfoPhase(phaseMapper.mapToEntity((InfoPhaseImportDTO) phase), newTrainingDefinition);
            } else if (phase.getPhaseType().equals(PhaseType.ACCESS)) {
                exportImportService.createAccessPhase(phaseMapper.mapToEntity((AccessPhaseImportDTO) phase), newTrainingDefinition);
            }
        });
        phases.forEach(phase -> {
            if (phase.getPhaseType().equals(PhaseType.QUESTIONNAIRE)) {
                QuestionnairePhase createdQuestionnairePhase = exportImportService.createQuestionnairePhase(phaseMapper.mapToEntity((QuestionnairePhaseImportDTO) phase), newTrainingDefinition);
                ((QuestionnairePhaseImportDTO) phase).getPhaseRelations().forEach(questionPhaseRelation ->
                        exportImportService.createQuestionPhaseRelationPhase(
                                questionPhaseRelationMapper.mapToEntity(questionPhaseRelation),
                                createdQuestionnairePhase,
                                questionPhaseRelation.getPhaseOrder(),
                                questionPhaseRelation.getQuestionOrders()));
            }
        });
        return trainingDefinitionMapper.mapToDTOById(newTrainingDefinition);
    }

    /**
     * Exports Training Instance to file
     *
     * @param trainingInstanceId the id of the instance to be exported
     * @return the file containing instance, {@link FileToReturnDTO}
     */
    @IsOrganizerOrAdmin
    @TransactionalRO
    public FileToReturnDTO archiveTrainingInstance(Long trainingInstanceId) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            TrainingInstance trainingInstance = exportImportService.findInstanceById(trainingInstanceId);

            TrainingInstanceArchiveDTO archivedInstance = exportImportMapper.mapToDTO(trainingInstance);
            archivedInstance.setDefinitionId(trainingInstance.getTrainingDefinition().getId());
            Set<Long> organizersRefIds = trainingInstance.getOrganizers().stream()
                    .map(User::getUserRefId)
                    .collect(Collectors.toSet());
            archivedInstance.setOrganizersRefIds(new HashSet<>(organizersRefIds));

            writeTrainingInstanceGeneralInfo(zos, trainingInstance.getId(), archivedInstance);
            writeTrainingDefinitionInfo(zos, trainingInstance);
            writeTrainingRunsInfo(zos, trainingInstance);
            writeSandboxDefinitionInfo(zos, trainingInstance);
            writeSankeyDiagramData(zos, trainingInstance);

            zos.closeEntry();
            zos.close();
            FileToReturnDTO fileToReturnDTO = new FileToReturnDTO();
            fileToReturnDTO.setContent(baos.toByteArray());
            fileToReturnDTO.setTitle(trainingInstance.getTitle());
            return fileToReturnDTO;
        } catch (IOException ex) {
            throw new InternalServerErrorException("The .zip file was not created since there were some processing error.", ex);
        }
    }

    private void writeTrainingInstanceGeneralInfo(ZipOutputStream zos, Long trainingInstanceId, TrainingInstanceArchiveDTO archivedInstance) throws IOException {
        ZipEntry instanceEntry = new ZipEntry("training_instance-id" + trainingInstanceId + AbstractFileExtensions.JSON_FILE_EXTENSION);
        zos.putNextEntry(instanceEntry);
        zos.write(objectMapper.writeValueAsBytes(archivedInstance));
    }

    private void writeTrainingRunsInfo(ZipOutputStream zos, TrainingInstance trainingInstance) throws IOException {
        Set<TrainingRun> runs = exportImportService.findRunsByInstanceId(trainingInstance.getId());
        Map<Long, Map<Long, QuestionAnswersDetailsDTO>> questionnairesDetails = new HashMap<>();
        for (TrainingRun run : runs) {
            TrainingRunArchiveDTO archivedRun = exportImportMapper.mapToArchiveDTO(run);
            archivedRun.setInstanceId(trainingInstance.getId());
            archivedRun.setParticipantRefId(run.getParticipantRef().getUserRefId());
            ZipEntry runEntry = new ZipEntry(RUNS_FOLDER + "/training_run-id" + run.getId() + AbstractFileExtensions.JSON_FILE_EXTENSION);
            zos.putNextEntry(runEntry);
            zos.write(objectMapper.writeValueAsBytes(archivedRun));

            writeQuestionsAnswers(zos, run, questionnairesDetails);
            List<Map<String, Object>> events = elasticsearchServiceApi.findAllEventsFromTrainingRun(run);
            if (events.isEmpty()) {
                continue;
            }
            Map<Integer, Long> phaseStartTimestampMapping = writeEventsAndGetPhaseStartTimestampMapping(zos, run, events);
            writeEventsByPhases(zos, run, events);

            List<Map<String, Object>> consoleCommands = getConsoleCommands(trainingInstance, run);
            Integer sandboxId = (Integer) events.get(0).get("sandbox_id");
            writeConsoleCommands(zos, sandboxId, consoleCommands);
            writeConsoleCommandsDetails(zos, trainingInstance, run, sandboxId, phaseStartTimestampMapping);
        }
        writeQuestionnairesDetails(zos, questionnairesDetails);
    }

    private List<Map<String, Object>> getConsoleCommands(TrainingInstance instance, TrainingRun run) {
        if (instance.isLocalEnvironment()) {
            return elasticsearchServiceApi.findAllConsoleCommandsByAccessTokenAndUserId(instance.getAccessToken(), run.getParticipantRef().getUserRefId());
        }
        Long sandboxId = run.getSandboxInstanceRefId() == null ? run.getPreviousSandboxInstanceRefId() : run.getSandboxInstanceRefId();
        return elasticsearchServiceApi.findAllConsoleCommandsBySandbox(sandboxId);
    }

    private Map<Integer, Long> writeEventsAndGetPhaseStartTimestampMapping(ZipOutputStream zos, TrainingRun run, List<Map<String, Object>> events) throws IOException {
        ZipEntry eventsEntry = new ZipEntry(EVENTS_FOLDER + "/training_run-id" + run.getId() + "-events" + AbstractFileExtensions.JSON_FILE_EXTENSION);
        zos.putNextEntry(eventsEntry);
        //Obtain start timestamp of each phase, so it can be used later
        Map<Integer, Long> phaseStartTimestampMapping = new LinkedHashMap<>();

        for (Map<String, Object> event : events) {
            zos.write(objectMapper.writer(new MinimalPrettyPrinter()).writeValueAsBytes(event));
            zos.write(System.lineSeparator().getBytes());
            if (event.get("type").equals(PhaseStarted.class.getCanonicalName())) {
                phaseStartTimestampMapping.put(((Integer) event.get("phase_id")), (Long) event.get("timestamp"));
            }
        }
        return phaseStartTimestampMapping;
    }

    private void writeEventsByPhases(ZipOutputStream zos, TrainingRun run, List<Map<String, Object>> events) throws IOException {
        Integer currentPhase = ((Integer) events.get(0).get("phase_id"));
        ZipEntry eventsDetailEntry = new ZipEntry(EVENTS_FOLDER + "/training_run-id" + run.getId() + "-details" + "/phase" + currentPhase + "-events" + AbstractFileExtensions.JSON_FILE_EXTENSION);
        zos.putNextEntry(eventsDetailEntry);
        for (Map<String, Object> event : events) {
            if (!event.get("phase_id").equals(currentPhase)) {
                currentPhase = ((Integer) event.get("phase_id"));
                eventsDetailEntry = new ZipEntry(EVENTS_FOLDER + "/training_run-id" + run.getId() + "-details" + "/phase" + currentPhase + "-events" + AbstractFileExtensions.JSON_FILE_EXTENSION);
                zos.putNextEntry(eventsDetailEntry);
            }
            zos.write(objectMapper.writer(new MinimalPrettyPrinter()).writeValueAsBytes(event));
            zos.write(System.lineSeparator().getBytes());
        }
    }

    private void writeQuestionsAnswers(ZipOutputStream zos, TrainingRun run, Map<Long, Map<Long, QuestionAnswersDetailsDTO>> questionnairesDetails) throws IOException {
        Map<Long, List<QuestionAnswer>> questionsAnswersByAssessments = exportImportService.findQuestionsAnswersOfQuestionnaires(run.getId());
        for (Map.Entry<Long, List<QuestionAnswer>> questionAnswersByQuestionnaire : questionsAnswersByAssessments.entrySet()) {
            ZipEntry eventsDetailEntry = new ZipEntry(QUESTIONNAIRES_ANSWERS_FOLDER + "/training_run-id-" + run.getId() + "-questionnaires" + "/questionnaire_id-" + questionAnswersByQuestionnaire.getKey() + "-answers" + AbstractFileExtensions.JSON_FILE_EXTENSION);
            zos.putNextEntry(eventsDetailEntry);

            Map<Long, QuestionAnswersDetailsDTO> questionAnswersDetails = questionnairesDetails.getOrDefault(questionAnswersByQuestionnaire.getKey(), new HashMap<>());
            for (QuestionAnswer questionAnswer : questionAnswersByQuestionnaire.getValue()) {
                Question question = questionAnswer.getQuestion();
                if (!questionAnswersDetails.containsKey(question.getId())) {
                    questionAnswersDetails.put(question.getId(), new QuestionAnswersDetailsDTO(questionAnswer.getQuestion().getText()));
                }
                questionAnswersDetails.get(question.getId()).addAnswers(questionAnswer.getAnswers());
                zos.write(objectMapper.writer(new MinimalPrettyPrinter()).writeValueAsBytes(new QuestionAnswerArchiveDTO(question.getText(), questionAnswer.getAnswers())));
                zos.write(System.lineSeparator().getBytes());
            }
            questionnairesDetails.putIfAbsent(questionAnswersByQuestionnaire.getKey(), questionAnswersDetails);

        }
    }

    private void writeQuestionnairesDetails(ZipOutputStream zos, Map<Long, Map<Long, QuestionAnswersDetailsDTO>> questionnairesDetails) throws IOException {
        for (Map.Entry<Long, Map<Long, QuestionAnswersDetailsDTO>> questionnaireDetails : questionnairesDetails.entrySet()) {
            ZipEntry assessmentDetailsEntry = new ZipEntry(QUESTIONNAIRES_ANSWERS_FOLDER + "/questionnaire-id-" + questionnaireDetails.getKey() + "-details" + AbstractFileExtensions.JSON_FILE_EXTENSION);
            zos.putNextEntry(assessmentDetailsEntry);
            zos.write(objectMapper.writer().writeValueAsBytes(questionnaireDetails.getValue().values()));
        }
    }

    private void writeConsoleCommands(ZipOutputStream zos, Integer sandboxId, List<Map<String, Object>> consoleCommands) throws IOException {
        ZipEntry consoleCommandsEntry = new ZipEntry(LOGS_FOLDER + "/sandbox-" + sandboxId + "-useractions" + AbstractFileExtensions.JSON_FILE_EXTENSION);
        zos.putNextEntry(consoleCommandsEntry);
        for (Map<String, Object> command : consoleCommands) {
            zos.write(objectMapper.writer(new MinimalPrettyPrinter()).writeValueAsBytes(command));
            zos.write(System.lineSeparator().getBytes());
        }
    }

    private void writeConsoleCommandsDetails(ZipOutputStream zos, TrainingInstance instance, TrainingRun run, Integer sandboxId, Map<Integer, Long> phaseStartTimestampMapping) throws IOException {
        List<Long> phaseTimestampRanges = new ArrayList<>(phaseStartTimestampMapping.values());
        List<Integer> phaseIds = new ArrayList<>(phaseStartTimestampMapping.keySet());
        phaseTimestampRanges.add(Long.MAX_VALUE);

        for (int i = 0; i < phaseIds.size(); i++) {
            List<Map<String, Object>> consoleCommandsByPhase = getConsoleCommandsWithinTimeRange(instance, run, sandboxId, phaseTimestampRanges.get(i), phaseTimestampRanges.get(i + 1));
            ZipEntry consoleCommandsEntryDetails = new ZipEntry(LOGS_FOLDER + "/sandbox-" + sandboxId + "-details" + "/phase" + phaseIds.get(i) + "-useractions" + AbstractFileExtensions.JSON_FILE_EXTENSION);
            zos.putNextEntry(consoleCommandsEntryDetails);
            for (Map<String, Object> command : consoleCommandsByPhase) {
                zos.write(objectMapper.writer(new MinimalPrettyPrinter()).writeValueAsBytes(command));
                zos.write(System.lineSeparator().getBytes());
            }
        }
    }
    private List<Map<String, Object>> getConsoleCommandsWithinTimeRange(TrainingInstance instance, TrainingRun run, Integer sandboxId, Long from, Long to) {
        if(instance.isLocalEnvironment()) {
            return elasticsearchServiceApi.findAllConsoleCommandsByAccessTokenAndUserIdAndTimeRange(instance.getAccessToken(), run.getParticipantRef().getUserRefId(), from, to);
        }
        return elasticsearchServiceApi.findAllConsoleCommandsBySandboxAndTimeRange(sandboxId, from, to);
    }


    private void writeTrainingDefinitionInfo(ZipOutputStream zos, TrainingInstance trainingInstance) throws IOException {
        TrainingDefinitionArchiveDTO tD = exportImportMapper.mapToArchiveDTO(exportImportService.findById(trainingInstance.getTrainingDefinition().getId()));
        if (tD != null) {
            tD.setPhases(mapAbstractPhasesToArchiveDTO(trainingInstance.getTrainingDefinition().getId()));
            ZipEntry definitionEntry = new ZipEntry("training_definition-id" + trainingInstance.getTrainingDefinition().getId() + AbstractFileExtensions.JSON_FILE_EXTENSION);
            zos.putNextEntry(definitionEntry);
            zos.write(objectMapper.writeValueAsBytes(tD));
        }
    }

    private void writeSankeyDiagramData(ZipOutputStream zos, TrainingInstance trainingInstance) throws IOException {
        SankeyDiagramDTO sankeyDiagram = visualizationService.getSankeyDiagram(trainingInstance);
        ZipEntry sankeyDiagramEntry = new ZipEntry("sankey_diagram_data" + AbstractFileExtensions.JSON_FILE_EXTENSION);
        zos.putNextEntry(sankeyDiagramEntry);
        zos.write(objectMapper.writeValueAsBytes(sankeyDiagram));
    }

    private void writeSandboxDefinitionInfo(ZipOutputStream zos, TrainingInstance trainingInstance) throws IOException {
        if (trainingInstance.getPoolId() != null) {
            SandboxDefinitionInfo sandboxDefinitionInfo = sandboxServiceApi.getSandboxDefinitionId(trainingInstance.getPoolId());
            ZipEntry sandboxDefinitionEntry = new ZipEntry("sandbox_definition-id" + sandboxDefinitionInfo.getId() + AbstractFileExtensions.JSON_FILE_EXTENSION);
            zos.putNextEntry(sandboxDefinitionEntry);
            zos.write(objectMapper.writeValueAsBytes(sandboxDefinitionInfo));
        }
    }

    private int computeEstimatedDuration(ImportTrainingDefinitionDTO importedTrainingDefinition) {
        return importedTrainingDefinition.getPhases().stream()
                .filter(phase -> phase.getClass() == TrainingPhaseImportDTO.class)
                .mapToInt(trainingPhase -> ((TrainingPhaseImportDTO) trainingPhase).getEstimatedDuration())
                .sum();
    }
}
