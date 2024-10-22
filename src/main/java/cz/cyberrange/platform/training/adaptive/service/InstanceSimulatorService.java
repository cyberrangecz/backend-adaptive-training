package cz.cyberrange.platform.training.adaptive.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.OverallInstancePerformance;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.OverallInstanceStatistics;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.OverallPhaseStatistics;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.ParticipantPerformance;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.PhaseEvent;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.PhaseUserActions;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.QuestionnaireActions;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.TrainingInstanceInfo;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.AbstractPhaseImport;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.ImportTrainingDefinition;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.QuestionChoiceImport;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.QuestionImport;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.QuestionnairePhaseImport;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.TaskImport;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.TrainingPhaseImport;
import cz.cyberrange.platform.training.adaptive.api.dto.AdaptiveSmartAssistantInput;
import cz.cyberrange.platform.training.adaptive.api.dto.responses.SuitableTaskResponse;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.sankey.LinkDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.sankey.NodeDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.sankey.SankeyDiagramDTO;
import cz.cyberrange.platform.training.adaptive.persistence.enums.PhaseType;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.BadRequestException;
import cz.cyberrange.platform.training.adaptive.service.api.SmartAssistantServiceApi;
import cz.cyberrange.platform.training.adaptive.service.training.TrainingRunService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service for adaptive instance simulator.
 */
@Service
public class InstanceSimulatorService {

    // Map<CacheKey, OverallInstanceStatistics>
    private Map<String, OverallInstanceStatistics> cachedInstanceStatistics = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper;
    private final TrainingRunService trainingRunService;
    private final SmartAssistantServiceApi smartAssistantServiceApi;

    public InstanceSimulatorService(ObjectMapper objectMapper,
                                    TrainingRunService trainingRunService,
                                    SmartAssistantServiceApi smartAssistantServiceApi) {
        this.objectMapper = objectMapper;
        this.trainingRunService = trainingRunService;
        this.smartAssistantServiceApi = smartAssistantServiceApi;
    }

    /**
     * Parses trainees sandbox interactions and prepares the data for the simulator.
     * @param content trainees sandbox interactions. Commands are separated by newline with extended
     *               information about command entry.
     * @param sandboxId trainees' sandbox id.
     * @param phaseId phase id.
     * @param sandboxUseractions trainees' sandbox interactions - commands.
     * @return parsed sandbox interactions for the simulator.
     * @throws JsonProcessingException if error occurs during parsing of phase-useractions.json
     */
    public Map<String, Map<Long, List<PhaseUserActions>>> parseSandboxInteractions(
            String content,
            String sandboxId,
            Long phaseId,
            Map<String, Map<Long, List<PhaseUserActions>>> sandboxUseractions,
            Validator validator
    ) throws JsonProcessingException, BadRequestException {
        List<PhaseUserActions> phaseUserActionsCommands =
                sandboxUseractions.containsKey(sandboxId) && sandboxUseractions.get(sandboxId).containsKey(phaseId) ? sandboxUseractions.get(sandboxId).get(phaseId) : new ArrayList<>();
        Map<Long, List<PhaseUserActions>> phaseUserActions = sandboxUseractions.containsKey(sandboxId) ? sandboxUseractions.get(sandboxId) : new HashMap<>();

        if (!content.isEmpty()) {
            for (String command : content.split(System.lineSeparator())) {
                PhaseUserActions userActions = objectMapper.readValue(command, PhaseUserActions.class);
                Set<ConstraintViolation<PhaseUserActions>> violations = validator.validate(userActions);
                if (!violations.isEmpty()) {
                    throw new BadRequestException("Could not parse data in phase" + phaseId + "-useractions.json for sandbox with ID: " + sandboxId + ". Missing: " + violations.iterator().next().getMessage());
                }
                phaseUserActionsCommands.add(userActions);
            }
        }
        phaseUserActions.put(phaseId, phaseUserActionsCommands);
        sandboxUseractions.put(sandboxId, phaseUserActions);
        return sandboxUseractions;
    }

    /**
     * Parses trainees phase events and prepares the data for the simulator.
     * @param content trainees events. Events are separated by newline with extended
     *      *               information about the event.
     * @param trainingRunId training run id.
     * @param phaseId phase id.
     * @param trainingEvents map storing all training events of all trainees.
     * @return parsed training events for the simulator.
     * @throws JsonProcessingException if error occurs during parsing of phase-events.json
     */
    public Map<Long, Map<Long, List<PhaseEvent>>> parseTrainingEvents(
            String content,
            Long trainingRunId,
            Long phaseId,
            Map<Long, Map<Long, List<PhaseEvent>>> trainingEvents,
            Validator validator
    ) throws JsonProcessingException, BadRequestException {
        List<PhaseEvent> phaseEvents = trainingEvents.containsKey(trainingRunId) && trainingEvents.get(trainingRunId).containsKey(phaseId) ? trainingEvents.get(trainingRunId).get(phaseId) : new ArrayList<>();
        Map<Long, List<PhaseEvent>> userTrainingEvents = trainingEvents.containsKey(trainingRunId) ? trainingEvents.get(trainingRunId) : new HashMap<>();

        if (!content.isEmpty()) {
            for (String event : content.split(System.lineSeparator())) {
                PhaseEvent phaseEvent = objectMapper.readValue(event, PhaseEvent.class);
                Set<ConstraintViolation<PhaseEvent>> violations = validator.validate(phaseEvent);
                if (!violations.isEmpty()) {
                    throw new BadRequestException("Could not parse data in training_run-id" + trainingRunId + "-details.json. Missing: " + violations.iterator().next().getMessage());
                }
                phaseEvents.add(phaseEvent);
            }
        } else {
            phaseEvents.add(new PhaseEvent());
        }
        userTrainingEvents.put(phaseId, phaseEvents);
        trainingEvents.put(trainingRunId, userTrainingEvents);
        return trainingEvents;
    }

    /**
     * Parses trainees questionnaire answers and prepares them for the simulator.
     * @param content questionnaire answers. Answers are separated by newline.
     * @param trainingRunId training run id.
     * @param phaseId phase id.
     * @param questionnaireActions map storing all questionnaire actions of all trainees.
     * @return parsed questionnaire actions for the simulator.
     * @throws JsonProcessingException if error occurs during parsing of questionnaire_id-answers.json
     */
    public Map<Long, Map<Long, List<QuestionnaireActions>>> parseQuestionnaireAnswers(
            String content,
            Long trainingRunId,
            Long phaseId,
            Map<Long, Map<Long, List<QuestionnaireActions>>> questionnaireActions,
            Validator validator
    ) throws JsonProcessingException, BadRequestException {
        List<QuestionnaireActions> questionAnswers = questionnaireActions.containsKey(trainingRunId) && questionnaireActions.get(trainingRunId).containsKey(phaseId) ? questionnaireActions.get(trainingRunId).get(phaseId) : new ArrayList<>();
        Map<Long, List<QuestionnaireActions>> userQuestionAnswers = questionnaireActions.containsKey(trainingRunId) ? questionnaireActions.get(trainingRunId) : new HashMap<>();

        if (!content.isEmpty()) {
            for (String question : content.split(System.lineSeparator())) {
                QuestionnaireActions questionnaireAnswer = objectMapper.readValue(question, QuestionnaireActions.class);
                Set<ConstraintViolation<QuestionnaireActions>> violations = validator.validate(questionnaireAnswer);
                if (!violations.isEmpty()) {
                    throw new BadRequestException("Could not parse data in questionnaire_id-" + phaseId + "-answers.json for training run with ID: " + trainingRunId + ". Missing: " + violations.iterator().next().getMessage());
                }
                questionAnswers.add(questionnaireAnswer);
            }
        } else {
            questionAnswers.add(new QuestionnaireActions());
        }
        userQuestionAnswers.put(phaseId, questionAnswers);
        questionnaireActions.put(trainingRunId, userQuestionAnswers);
        return questionnaireActions;
    }

    /**
     * Stores processed data from exported instance to cache. Creates unique key combined from access token and instance id.
     * @param traineesIdentification map of trainee sandbox id and training run id combination.
     * @param questionnaireActions processed questionnaire actions.
     * @param trainingEvents processed training events.
     * @param sandboxUseractions processed sandbox useractions.
     * @param trainingDefinition processed training definition.
     * @param trainingInstanceInfo processed training instance info.
     * @return key to the cached instance statistics.
     */
    @Cacheable(value = "traineesPerformance", key = "#trainingInstanceInfo.getId() + '-' + #trainingInstanceInfo.getAccessToken()")
    public String cacheTraineesPerformance(
            Map<Long, String> traineesIdentification,
            Map<Long, Map<Long, List<QuestionnaireActions>>> questionnaireActions,
            Map<Long, Map<Long, List<PhaseEvent>>> trainingEvents,
            Map<String, Map<Long, List<PhaseUserActions>>> sandboxUseractions,
            ImportTrainingDefinition trainingDefinition,
            TrainingInstanceInfo trainingInstanceInfo
    ) {
        List<ParticipantPerformance> participantsPerformance = new ArrayList<>();
        OverallInstanceStatistics overallInstanceStatistics = new OverallInstanceStatistics();

        traineesIdentification.keySet().forEach(trainingRunId -> {
            List<OverallPhaseStatistics> overallAllPhasesStatistics = new ArrayList<>();
            trainingDefinition.getPhases().forEach(phase -> {
                OverallPhaseStatistics overallPhaseStatistics = new OverallPhaseStatistics();
                overallPhaseStatistics.setPhaseId(phase.getId());
                overallAllPhasesStatistics.add(overallPhaseStatistics);
            });
            ParticipantPerformance participantPerformance = new ParticipantPerformance();
            participantPerformance.setPerformance(computePhaseStatistics(overallAllPhasesStatistics, trainingRunId, traineesIdentification.get(trainingRunId), questionnaireActions, trainingEvents, sandboxUseractions, trainingDefinition));
            participantPerformance.setTraineeID(trainingRunId);
            participantsPerformance.add(participantPerformance);
        });
        String cacheKey = String.format("%d-%s", trainingInstanceInfo.getId(), trainingInstanceInfo.getAccessToken());
        overallInstanceStatistics.setParticipantsPerformance(participantsPerformance);
        overallInstanceStatistics.setTrainingDefinition(trainingDefinition);
        cachedInstanceStatistics.put(cacheKey, overallInstanceStatistics);
        return cacheKey;
    }

    /**
     * Retrieves suitable tasks for phases for all trainees
     * @param cacheId key to the cached instance statistics.
     * @param phases list of phases changed by Instructor in simulator.
     * @return Suitable tasks for all trainees in all phases.
     */
    public List<List<SuitableTaskResponse>> getSuitableTasks(String cacheId, List<AbstractPhaseImport> phases) {
        OverallInstanceStatistics overallInstanceStatistics = cachedInstanceStatistics.get(cacheId);
        List<OverallInstancePerformance> overallInstancePerformances = new ArrayList<>();
        int maxOrder = getPhasesMaxOrder(phases);

        overallInstanceStatistics.getParticipantsPerformance().forEach(participant -> {
            List<OverallPhaseStatistics> overallPhaseStatistics = participant.getPerformance();
            List<AdaptiveSmartAssistantInput> traineeSmartAssistantInput = new ArrayList<>();
            phases.forEach(phase -> {
                int currentPhaseOrder = phase.getOrder();
                if (maxOrder == currentPhaseOrder) {
                    return;
                }
                AbstractPhaseImport nextPhase = phases.get(currentPhaseOrder + 1);
                if (nextPhase instanceof TrainingPhaseImport) {
                    OverallPhaseStatistics currentPhaseStatistics = overallPhaseStatistics.stream()
                            .filter(statistic -> statistic.getPhaseId().equals(nextPhase.getId()))
                            .findFirst().orElse(null);
                    currentPhaseStatistics.setPhaseOrder(Long.valueOf(nextPhase.getOrder()));
                    traineeSmartAssistantInput.add(trainingRunService.gatherInputDataForSmartAssistant(participant.getTraineeID(), (TrainingPhaseImport) nextPhase, phases, currentPhaseStatistics, (QuestionnairePhaseImport) getPreTrainingQuestionnaire(overallInstanceStatistics.getTrainingDefinition())));
                }
            });
            Map<Long, OverallPhaseStatistics> overallPhaseStatisticsMap = overallPhaseStatistics.stream()
                    .collect(Collectors.toMap(OverallPhaseStatistics::getPhaseId, Function.identity()));
            overallInstancePerformances.add(new OverallInstancePerformance(participant.getTraineeID(), traineeSmartAssistantInput, overallPhaseStatisticsMap));
        });
        return smartAssistantServiceApi.findSuitableTasksForInstance(overallInstancePerformances);
    }

    /**
     * Computes overall instance statistics for given trainee based on the pre-processed data.
     * @param trainingDefinition training definition.
     * @param participantPerformance participant performance in inspected instance.
     * @param trainingRunId training run id of the participant.
     * @param sandboxId sandbox id of the participant.
     * @param questionnaireActions questionnaire actions of all participants.
     * @param trainingEvents training events of all participants.
     * @param sandboxUseractions sandbox useractions of all participants.
     * @return performance of the trainee in the inspected instance.
     */
    private List<OverallPhaseStatistics> computePhaseStatistics(
            List<OverallPhaseStatistics> participantPerformance,
            Long trainingRunId,
            String sandboxId,
            Map<Long, Map<Long, List<QuestionnaireActions>>> questionnaireActions,
            Map<Long, Map<Long, List<PhaseEvent>>> trainingEvents,
            Map<String, Map<Long, List<PhaseUserActions>>> sandboxUseractions,
            ImportTrainingDefinition trainingDefinition
    ) {
        QuestionnairePhaseImport questionnaire = (QuestionnairePhaseImport) getPreTrainingQuestionnaire(trainingDefinition);

        List<QuestionnaireActions> preGameQuestionnaireActions = questionnaireActions.get(trainingRunId).get(questionnaire.getId());
        QuestionnairePhaseImport preGameQuestionnaire = (QuestionnairePhaseImport) getPreTrainingQuestionnaire(trainingDefinition);
        List<QuestionnaireActions> sortedPreGameQuestionnaireActions = new ArrayList<>();
        while(sortedPreGameQuestionnaireActions.size() < preGameQuestionnaire.getQuestions().size()) sortedPreGameQuestionnaireActions.add(new QuestionnaireActions());

        preGameQuestionnaire.getQuestions()
                .forEach(question -> sortedPreGameQuestionnaireActions.set(question.getOrder(), preGameQuestionnaireActions.stream()
                .filter(questionAnswer -> questionAnswer.getQuestion().equals(question.getText()))
                .findFirst().orElse(null)));

        List<QuestionImport> questionnaireTemp = questionnaire.getQuestions();
        OverallPhaseStatistics overallQuestionnairePhaseStatistics = participantPerformance.remove(participantPerformance.indexOf(participantPerformance.stream()
                .filter(phase -> questionnaire.getId().equals(phase.getPhaseId()))
                .findFirst().orElse(null)));
        List<Boolean> questionnaireAnswers = overallQuestionnairePhaseStatistics.getQuestionsAnswer();
        for(int i = 0; i < questionnaireTemp.size(); i++) {
            if (i >= sortedPreGameQuestionnaireActions.size()) {
                questionnaireAnswers.add(false);
            } else {
                int finalI = i;
                questionnaireAnswers.add(questionnaireTemp
                        .get(i)
                        .getChoices()
                        .stream()
                        .filter(QuestionChoiceImport::getCorrect)
                        .anyMatch(choice -> Objects.equals(choice.getText(), sortedPreGameQuestionnaireActions.get(finalI).getAnswer().get(0))));
            }
        }
        overallQuestionnairePhaseStatistics.setQuestionsAnswer(questionnaireAnswers);
        participantPerformance.add(overallQuestionnairePhaseStatistics);

        Map<Long, List<PhaseEvent>> events = trainingEvents.get(trainingRunId);
        events.keySet().forEach(phaseId -> {
            OverallPhaseStatistics overallPhaseStatistics = participantPerformance.remove(participantPerformance.indexOf(participantPerformance.stream()
                    .filter(phase -> phaseId.equals(phase.getPhaseId()))
                    .findFirst().orElse(null)));
            List<PhaseEvent> phaseEvents = events.get(phaseId);
            if (overallPhaseStatistics != null) {
                overallPhaseStatistics.setPhaseId(phaseId);
                overallPhaseStatistics.setPhaseOrder(phaseEvents.get(0).getPhaseOrder());
                overallPhaseStatistics.setQuestionsAnswer(questionnaireAnswers);
                overallPhaseStatistics.setPhaseTime(phaseEvents.get(phaseEvents.size() - 1).getTimestamp() - phaseEvents.get(0).getTimestamp());
                overallPhaseStatistics.setWrongAnswers(phaseEvents.stream()
                        .map(PhaseEvent::getType)
                        .filter(type -> Objects.equals(type, "cz.cyberrange.platform.events.adaptive.trainings.WrongAnswerSubmitted"))
                        .collect(Collectors.toList()));
                overallPhaseStatistics.setSolutionDisplayed(phaseEvents.stream()
                        .anyMatch(event -> Objects.equals(event.getType(), "cz.cyberrange.platform.events.adaptive.trainings.SolutionDisplayed")));
            }

            if (phaseEvents.get(0).getSandboxId() != null) {
                if (overallPhaseStatistics != null) {
                    overallPhaseStatistics.setSandboxId(phaseEvents.get(0).getSandboxId());
                }
            }
            participantPerformance.add(overallPhaseStatistics);
        });

        if (sandboxId != null) {
            Map<Long, List<PhaseUserActions>> userActions = sandboxUseractions.get(sandboxId);
            userActions.keySet().forEach(phaseId -> {
                OverallPhaseStatistics overallPhaseStatisticsTemp = participantPerformance.stream()
                        .filter(phase -> phaseId.equals(phase.getPhaseId()))
                        .findFirst().orElse(null);
                if (overallPhaseStatisticsTemp != null) {
                    OverallPhaseStatistics overallPhaseStatistics1 = participantPerformance.remove(participantPerformance.indexOf(overallPhaseStatisticsTemp));
                    overallPhaseStatistics1.setNumberOfCommands(userActions.get(phaseId).size());
                    participantPerformance.add(overallPhaseStatistics1);
                }
            });
        }
        return participantPerformance;
    }

    /**
     * Retrieves pre-training questionnaire from the training definition.
     * @param trainingDefinition training definition.
     * @return questionnaire phase.
     */
    private AbstractPhaseImport getPreTrainingQuestionnaire(ImportTrainingDefinition trainingDefinition) {
        int i = 0;
        List<AbstractPhaseImport> phases = trainingDefinition.getPhases();
        for (AbstractPhaseImport phase : phases) {
            if (phase.getPhaseType().equals(PhaseType.TRAINING)) {
                break;
            }
            i++;
        }
        // pre game assessment is always present before training phase
        return phases.get(i - 1);
    }

    /**
     * Retrieves maximum order of the phases in the training definition.
     * @param phases phases of the training definition.
     * @return maximum order of the phases.
     */
    private int getPhasesMaxOrder(List<AbstractPhaseImport> phases) {
        AtomicInteger max = new AtomicInteger();
        phases.forEach(phase -> max.set((phase.getOrder() > max.get()) ? phase.getOrder() : max.get()));
        return max.get();
    }

    /**
     * Gets sankey graph.
     * @param suitableTasks Suitable tasks for all trainees in all phases.
     * @param abstractPhases list of phases changed by Instructor in simulator.
     * @return the sankey graph data
     */
    public SankeyDiagramDTO getSankeyDiagram(List<List<SuitableTaskResponse>> suitableTasks, List<AbstractPhaseImport> abstractPhases) {
        List<TrainingPhaseImport> trainingPhasesImport = abstractPhases.stream()
                .filter(phase -> phase instanceof TrainingPhaseImport)
                .map(phase -> (TrainingPhaseImport) phase)
                .collect(Collectors.toList());
        List<LinkDTO> resultLinks = new ArrayList<>();

        // start to get ssh
        LinkDTO initialLink = new LinkDTO();
        initialLink.setSource(0);
        initialLink.setTarget(1);
        initialLink.setValue((long) suitableTasks.size());
        resultLinks.add(initialLink);

        int globalOrder = 1; // because first is start task with order 0
        Set<Integer> visitedTasks;
        for (int phaseIndex = 0; phaseIndex < trainingPhasesImport.size(); phaseIndex++) {
            visitedTasks = getVisitedTasksForPhase(suitableTasks, phaseIndex);
            List<TaskImport> phaseTasks = trainingPhasesImport.get(phaseIndex).getTasks();
            for (Integer taskOrder : visitedTasks) {
                phaseTasks.get(taskOrder).setGlobalOrder(globalOrder);
                globalOrder++;
            }
        }

        for (List<SuitableTaskResponse> traineeTasks : suitableTasks) {
            for (int traineePhaseOrder = 0; traineePhaseOrder < traineeTasks.size(); traineePhaseOrder++) {
                if (traineePhaseOrder + 1 < traineeTasks.size()) {
                    // -1 because suitable tasks are numbered from 1
                    int currentTaskLocal = traineeTasks.get(traineePhaseOrder).getSuitableTask() - 1;
                    int nextTaskLocal = traineeTasks.get(traineePhaseOrder + 1).getSuitableTask() - 1;

                    int currentTaskGlobal = trainingPhasesImport.get(traineePhaseOrder).getTasks().get(currentTaskLocal).getGlobalOrder();
                    int nextTaskGlobal = trainingPhasesImport.get(traineePhaseOrder + 1).getTasks().get(nextTaskLocal).getGlobalOrder();
                    LinkDTO linkDTO = resultLinks.stream()
                            .filter(link -> link.getSource() == currentTaskGlobal && link.getTarget() == nextTaskGlobal)
                            .findFirst()
                            .orElse(new LinkDTO(currentTaskGlobal, nextTaskGlobal, 0L));
                    if (linkDTO.getValue() == 0) {
                        resultLinks.add(linkDTO);
                    }
                    linkDTO.setValue(linkDTO.getValue() + 1);
                }
            }
        }

        List<NodeDTO> allNodes = createSankeyNodes(trainingPhasesImport);
        return new SankeyDiagramDTO(allNodes, resultLinks);
    }

    /**
     * Gets visited tasks for given phase.
     * @param suitableTasks Suitable tasks for all trainees in all phases.
     * @param order order of the inspected phase.
     * @return set of visited tasks for the inspected phase.
     */
    private Set<Integer> getVisitedTasksForPhase(List<List<SuitableTaskResponse>> suitableTasks, int order) {
        Set<Integer> visitedTasks = new HashSet<>();
        suitableTasks.forEach(trainee -> visitedTasks.add(trainee.get(order).getSuitableTask() - 1)); // -1 because suitable tasks are numbered from 1
        return visitedTasks;
    }

    /**
     * Creates sankey diagram nodes.
     * @param trainingPhasesImport training phases present in the inspected instance.
     * @return list of nodes for sankey diagram.
     */
    private List<NodeDTO> createSankeyNodes(List<TrainingPhaseImport> trainingPhasesImport) {
        List<NodeDTO> allNodes = new ArrayList<>();
        // start node
        allNodes.add(0, new NodeDTO(null, null, null, null, -1, null));

        for (int phaseOrder = 0; phaseOrder < trainingPhasesImport.size(); phaseOrder++) {
            List<TaskImport> tasks = trainingPhasesImport.get(phaseOrder).getTasks();
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getGlobalOrder() != null) {
                    NodeDTO node = new NodeDTO();
                    node.setPhaseId(trainingPhasesImport.get(phaseOrder).getId());
                    node.setPhaseOrder(phaseOrder);
                    node.setPhaseTitle(trainingPhasesImport.get(phaseOrder).getTitle());
                    node.setTaskId(tasks.get(i).getId());
                    node.setTaskOrder(i);
                    node.setTaskTitle(tasks.get(i).getTitle());
                    allNodes.add(node);
                }
            }
        }
        return allNodes;
    }
}
