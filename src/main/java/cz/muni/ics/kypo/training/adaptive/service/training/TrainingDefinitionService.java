package cz.muni.ics.kypo.training.adaptive.service.training;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.training.adaptive.domain.User;
import cz.muni.ics.kypo.training.adaptive.domain.phase.*;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.Question;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionChoice;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionPhaseRelation;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionType;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionnaireType;
import cz.muni.ics.kypo.training.adaptive.enums.TDState;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityConflictException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.exceptions.InternalServerErrorException;
import cz.muni.ics.kypo.training.adaptive.mapping.CloneMapper;
import cz.muni.ics.kypo.training.adaptive.repository.UserRefRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.AbstractPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.InfoPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.QuestionnairePhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.TrainingPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingDefinitionRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingInstanceRepository;
import cz.muni.ics.kypo.training.adaptive.service.api.UserManagementServiceApi;
import cz.muni.ics.kypo.training.adaptive.startup.DefaultPhases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Training definition service.
 */
@Service
public class TrainingDefinitionService {

    public static final String ARCHIVED_OR_RELEASED = "Cannot edit released or archived training definition.";
    private static final Logger LOG = LoggerFactory.getLogger(TrainingDefinitionService.class);
    private static final String PHASE_NOT_FOUND = "Phase not found.";
    private final TrainingDefinitionRepository trainingDefinitionRepository;
    private final TrainingInstanceRepository trainingInstanceRepository;
    private final AbstractPhaseRepository abstractPhaseRepository;
    private final TrainingPhaseRepository trainingPhaseRepository;
    private final InfoPhaseRepository infoPhaseRepository;
    private final QuestionnairePhaseRepository questionnairePhaseRepository;
    private final UserRefRepository userRefRepository;
    private final UserManagementServiceApi userManagementServiceApi;
    private final CloneMapper cloneMapper;
    private final Validator validator;
    @Value("${path.to.default.phases:}")
    private String pathToDefaultPhases;
    private DefaultPhases defaultPhases;

    /**
     * Instantiates a new Training definition service.
     *
     * @param trainingDefinitionRepository the training definition repository
     * @param abstractPhaseRepository      the abstract phase repository
     * @param infoPhaseRepository          the info phase repository
     * @param trainingPhaseRepository      the training phase repository
     * @param questionnairePhaseRepository the questionnaire phase repository
     * @param trainingInstanceRepository   the training instance repository
     * @param userRefRepository            the user ref repository
     * @param userManagementServiceApi     the security service
     */
    @Autowired
    public TrainingDefinitionService(TrainingDefinitionRepository trainingDefinitionRepository,
                                     AbstractPhaseRepository abstractPhaseRepository,
                                     InfoPhaseRepository infoPhaseRepository,
                                     TrainingPhaseRepository trainingPhaseRepository,
                                     QuestionnairePhaseRepository questionnairePhaseRepository,
                                     TrainingInstanceRepository trainingInstanceRepository,
                                     UserRefRepository userRefRepository,
                                     UserManagementServiceApi userManagementServiceApi,
                                     CloneMapper cloneMapper,
                                     Validator validator) {
        this.trainingDefinitionRepository = trainingDefinitionRepository;
        this.abstractPhaseRepository = abstractPhaseRepository;
        this.trainingPhaseRepository = trainingPhaseRepository;
        this.infoPhaseRepository = infoPhaseRepository;
        this.questionnairePhaseRepository = questionnairePhaseRepository;
        this.trainingInstanceRepository = trainingInstanceRepository;
        this.userRefRepository = userRefRepository;
        this.userManagementServiceApi = userManagementServiceApi;
        this.cloneMapper = cloneMapper;
        this.validator = validator;
    }

    @PostConstruct
    private void loadDefaultPhases() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        try {
            InputStream inputStream = pathToDefaultPhases.isBlank() ? getClass().getResourceAsStream("/default-phases.json") : new FileInputStream(pathToDefaultPhases);
            defaultPhases = mapper.readValue(inputStream, DefaultPhases.class);
            Set<ConstraintViolation<DefaultPhases>> violations = this.validator.validate(defaultPhases);
            if (!violations.isEmpty()) {
                throw new InternalServerErrorException("Could not load the default phases. Reason: " + violations.stream()
                        .map(ConstraintViolation::getMessage).collect(Collectors.toList()));
            }
        } catch (IOException e) {
            throw new InternalServerErrorException("Could not load file with the default phases.", e);
        }
    }

    /**
     * Finds specific Training Definition by id
     *
     * @param id of a Training Definition that would be returned
     * @return specific {@link TrainingDefinition} by id
     * @throws EntityNotFoundException training definition cannot be found
     */
    public TrainingDefinition findById(Long id) {
        return trainingDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingDefinition.class, "id", Long.class, id)));
    }

    /**
     * Find all Training Definitions by author if user is designer or all Training Definitions if user is admin.
     *
     * @param predicate represents a predicate (boolean-valued function) of one argument.
     * @param pageable  pageable parameter with information about pagination.
     * @return all {@link TrainingDefinition}s
     */
    public Page<TrainingDefinition> findAll(Predicate predicate, Pageable pageable) {
        return trainingDefinitionRepository.findAll(predicate, pageable);
    }

    /**
     * Find all page.
     *
     * @param predicate      the predicate
     * @param pageable       the pageable
     * @param loggedInUserId the logged in user id
     * @return the page
     */
    public Page<TrainingDefinition> findAll(Predicate predicate, Pageable pageable, Long loggedInUserId) {
        return trainingDefinitionRepository.findAll(predicate, pageable, loggedInUserId);
    }

    /**
     * Finds all Training Definitions accessible to users with the role of organizer.
     *
     * @param state    represents a state of training definition if it is released or unreleased.
     * @param pageable pageable parameter with information about pagination.
     * @return all Training Definitions for organizers
     */
    public Page<TrainingDefinition> findAllForOrganizers(TDState state, Pageable pageable) {
        return trainingDefinitionRepository.findAllForOrganizers(state, pageable);
    }

    /**
     * Find all for designers and organizers unreleased page.
     *
     * @param loggedInUserId the logged in user id
     * @param pageable       the pageable
     * @return the page
     */
    public Page<TrainingDefinition> findAllForDesigner(Long loggedInUserId, Pageable pageable) {
        return trainingDefinitionRepository.findAllForDesigner(loggedInUserId, pageable);
    }

    /**
     * creates new training definition
     *
     * @param trainingDefinition to be created
     * @return new {@link TrainingDefinition}
     */
    public TrainingDefinition create(TrainingDefinition trainingDefinition, boolean createDefaultContent) {
        addLoggedInUserToTrainingDefinitionAsAuthor(trainingDefinition);
        if (createDefaultContent && defaultPhases != null) {
            this.createDefaultPhases(trainingDefinition);
        }
        LOG.info("Training definition with id: {} created.", trainingDefinition.getId());
        return this.auditAndSave(trainingDefinition);
    }

    /**
     * Updates given Training Definition
     *
     * @param trainingDefinitionToUpdate to be updated
     * @throws EntityNotFoundException training definition or one of the phase is not found.
     * @throws EntityConflictException released or archived training definition cannot be modified.
     */
    public void update(TrainingDefinition trainingDefinitionToUpdate) {
        TrainingDefinition trainingDefinition = findById(trainingDefinitionToUpdate.getId());
        checkIfCanBeUpdated(trainingDefinition);
        addLoggedInUserToTrainingDefinitionAsAuthor(trainingDefinitionToUpdate);
        trainingDefinitionToUpdate.setEstimatedDuration(trainingDefinition.getEstimatedDuration());
        this.auditAndSave(trainingDefinitionToUpdate);
        LOG.info("Training definition with id: {} updated.", trainingDefinitionToUpdate.getId());
    }

    /**
     * Creates new training definition by cloning existing one
     *
     * @param id    of definition to be cloned
     * @param title the title of the new cloned definition
     * @return cloned {@link TrainingDefinition}
     * @throws EntityNotFoundException training definition not found.
     * @throws EntityConflictException cannot clone unreleased training definition.
     */
    public TrainingDefinition clone(Long id, String title) {
        TrainingDefinition trainingDefinition = findById(id);
        TrainingDefinition clonedTrainingDefinition = cloneMapper.clone(trainingDefinition);
        clonedTrainingDefinition.setTitle(title);
        addLoggedInUserToTrainingDefinitionAsAuthor(clonedTrainingDefinition);
        clonedTrainingDefinition = this.auditAndSave(clonedTrainingDefinition);

        clonePhasesFromTrainingDefinition(trainingDefinition.getId(), clonedTrainingDefinition);
        LOG.info("Training definition with id: {} cloned.", trainingDefinition.getId());
        return clonedTrainingDefinition;
    }

    /**
     * Deletes specific training definition based on id
     *
     * @param definitionId of definition to be deleted
     * @throws EntityNotFoundException training definition or phase is not found.
     * @throws EntityConflictException released training definition cannot be deleted.
     */
    public void delete(Long definitionId) {
        TrainingDefinition definition = findById(definitionId);
        if (definition.getState().equals(TDState.RELEASED))
            throw new EntityConflictException(new EntityErrorDetail(TrainingDefinition.class, "id", definitionId.getClass(), definitionId,
                    "Cannot delete released training definition."));
        if (trainingInstanceRepository.existsAnyForTrainingDefinition(definitionId)) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingDefinition.class, "id", definitionId.getClass(), definitionId,
                    "Cannot delete training definition with already created training instance. " +
                            "Remove training instance/s before deleting training definition."));
        }
        List<AbstractPhase> abstractPhases = abstractPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(definitionId);
        abstractPhases.forEach(this::deletePhase);
        trainingDefinitionRepository.delete(definition);
    }


    /**
     * Finds all phase from single definition
     *
     * @param definitionId of definition
     * @return list of {@link AbstractPhase} associated with training definition
     */
    public List<AbstractPhase> findAllPhasesFromDefinition(Long definitionId) {
        return abstractPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(definitionId);
    }

    /**
     * Finds specific phase by id with associated training definition
     *
     * @param phaseId - id of wanted phase
     * @return wanted {@link AbstractPhase}
     * @throws EntityNotFoundException phase is not found.
     */
    public AbstractPhase findPhaseByIdWithDefinition(Long phaseId) {
        return abstractPhaseRepository.findByIdWithDefinition(phaseId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(AbstractPhase.class, "id", phaseId.getClass(), phaseId, PHASE_NOT_FOUND)));
    }

    /**
     * Finds specific phase by id
     *
     * @param phaseId - id of wanted phase
     * @return wanted {@link AbstractPhase}
     * @throws EntityNotFoundException phase is not found.
     */
    private AbstractPhase findPhaseById(Long phaseId) {
        return abstractPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(AbstractPhase.class, "id", phaseId.getClass(), phaseId, PHASE_NOT_FOUND)));
    }

    /**
     * Find all training instances associated with training definition by id.
     *
     * @param id the id of training definition
     * @return the list of all {@link TrainingInstance}s associated with wanted {@link TrainingDefinition}
     */
    public List<TrainingInstance> findAllTrainingInstancesByTrainingDefinitionId(Long id) {
        return trainingInstanceRepository.findAllByTrainingDefinitionId(id);
    }

    /**
     * Switch development state of definition from unreleased to released, or from released to archived or back to unreleased.
     *
     * @param definitionId - id of training definition
     * @param state        - new state of training definition
     */
    public void switchState(Long definitionId, TDState state) {
        TrainingDefinition trainingDefinition = findById(definitionId);
        if (trainingDefinition.getState().name().equals(state.name())) {
            return;
        }
        switch (trainingDefinition.getState()) {
            case UNRELEASED:
                if (state.equals(TDState.RELEASED))
                    trainingDefinition.setState(TDState.RELEASED);
                else
                    throw new EntityConflictException(new EntityErrorDetail(TrainingDefinition.class, "id", definitionId.getClass(), definitionId,
                            "Cannot switch from" + trainingDefinition.getState() + " to " + state));
                break;
            case RELEASED:
                if (state.equals(TDState.ARCHIVED))
                    trainingDefinition.setState(TDState.ARCHIVED);
                else if (state.equals(TDState.UNRELEASED)) {
                    if (trainingInstanceRepository.existsAnyForTrainingDefinition(definitionId)) {
                        throw new EntityConflictException(new EntityErrorDetail(TrainingDefinition.class, "id", definitionId.getClass(), definitionId,
                                "Cannot update training definition with already created training instance(s). " +
                                        "Remove training instance(s) before changing the state from released to unreleased training definition."));
                    }
                    trainingDefinition.setState((TDState.UNRELEASED));
                }
                break;
            default:
                throw new EntityConflictException(new EntityErrorDetail(TrainingDefinition.class, "id", definitionId.getClass(), definitionId,
                        "Cannot switch from " + trainingDefinition.getState() + " to " + state));
        }
        this.auditAndSave(trainingDefinition);
    }

    /**
     * Sets audit attributes to training definition and save.
     *
     * @param trainingDefinition the training definition to be saved.
     */
    public TrainingDefinition auditAndSave(TrainingDefinition trainingDefinition) {
        trainingDefinition.setLastEdited(getCurrentTimeInUTC());
        trainingDefinition.setLastEditedBy(userManagementServiceApi.getUserRefDTO().getUserRefFullName());
        return trainingDefinitionRepository.save(trainingDefinition);
    }

    private void clonePhasesFromTrainingDefinition(Long trainingDefinitionId, TrainingDefinition clonedTrainingDefinition) {
        List<AbstractPhase> phases = abstractPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingDefinitionId);
        if (phases == null || phases.isEmpty()) {
            return;
        }
        Map<Long, TrainingPhase> clonedTrainingPhases = new HashMap<>();
        phases.forEach(phase -> {
            if (phase instanceof InfoPhase) {
                cloneInfoPhase((InfoPhase) phase, clonedTrainingDefinition);
            }
            if (phase instanceof TrainingPhase) {
                TrainingPhase clonedTrainingPhase = cloneTrainingPhase((TrainingPhase) phase, clonedTrainingDefinition);
                clonedTrainingPhases.put(phase.getId(), clonedTrainingPhase);
            }
        });
        phases.forEach(phase -> {
            if (phase instanceof QuestionnairePhase) {
                cloneQuestionnairePhase((QuestionnairePhase) phase, clonedTrainingDefinition, clonedTrainingPhases);
            }
        });
    }

    private void cloneInfoPhase(InfoPhase infoPhase, TrainingDefinition trainingDefinition) {
        InfoPhase clonedInfoPhase = cloneMapper.clone(infoPhase);
        clonedInfoPhase.setTrainingDefinition(trainingDefinition);
        infoPhaseRepository.save(clonedInfoPhase);
    }

    private TrainingPhase cloneTrainingPhase(TrainingPhase trainingPhase, TrainingDefinition trainingDefinition) {
        TrainingPhase clonedTrainingPhase = cloneMapper.clone(trainingPhase);
        clonedTrainingPhase.setDecisionMatrix(trainingPhase.getDecisionMatrix()
                .stream()
                .map(matrix -> cloneDecisionMatrixRow(matrix, clonedTrainingPhase))
                .collect(Collectors.toList()));
        clonedTrainingPhase.setTasks(trainingPhase.getTasks()
                .stream()
                .map(task -> cloneTask(task, clonedTrainingPhase))
                .collect(Collectors.toList()));
        clonedTrainingPhase.setTrainingDefinition(trainingDefinition);
        return trainingPhaseRepository.save(clonedTrainingPhase);
    }

    private DecisionMatrixRow cloneDecisionMatrixRow(DecisionMatrixRow decisionMatrixRow, TrainingPhase trainingPhase) {
        DecisionMatrixRow clonedDecisionMatrixRow = cloneMapper.clone(decisionMatrixRow);
        clonedDecisionMatrixRow.setTrainingPhase(trainingPhase);
        return clonedDecisionMatrixRow;
    }

    private Task cloneTask(Task originalTask, TrainingPhase trainingPhase) {
        Task clonedTask = cloneMapper.clone(originalTask);
        clonedTask.setTrainingPhase(trainingPhase);
        return clonedTask;
    }

    private void cloneQuestionnairePhase(QuestionnairePhase questionnairePhase,
                                         TrainingDefinition trainingDefinition,
                                         Map<Long, TrainingPhase> clonedTrainingPhases) {
        QuestionnairePhase clonedQuestionnairePhase = cloneMapper.clone(questionnairePhase);
        clonedQuestionnairePhase.setTrainingDefinition(trainingDefinition);
        Map<Long, Question> clonedQuestions = questionnairePhase.getQuestions()
                .stream()
                .collect(Collectors.toMap(Question::getId, question -> this.cloneQuestion(question, clonedQuestionnairePhase)));
        clonedQuestionnairePhase.setQuestions(new ArrayList<>(clonedQuestions.values()));
        clonedQuestionnairePhase.setQuestionPhaseRelations(questionnairePhase.getQuestionPhaseRelations()
                .stream()
                .map(questionPhaseRelation -> this.cloneQuestionPhaseRelation(questionPhaseRelation, clonedQuestionnairePhase, clonedTrainingPhases, clonedQuestions))
                .collect(Collectors.toList()));
        questionnairePhaseRepository.save(clonedQuestionnairePhase);
    }

    private Question cloneQuestion(Question question, QuestionnairePhase clonedQuestionnairePhase) {
        Question clonedQuestion = cloneMapper.clone(question);
        clonedQuestion.setQuestionnairePhase(clonedQuestionnairePhase);
        clonedQuestion.setChoices(question.getChoices()
                .stream()
                .map(choice -> this.cloneQuestionChoice(choice, clonedQuestion))
                .collect(Collectors.toList()));
        return clonedQuestion;
    }

    private QuestionChoice cloneQuestionChoice(QuestionChoice choice, Question clonedQuestion) {
        QuestionChoice clonedChoice = cloneMapper.clone(choice);
        clonedChoice.setQuestion(clonedQuestion);
        return clonedChoice;
    }

    private QuestionPhaseRelation cloneQuestionPhaseRelation(QuestionPhaseRelation questionPhaseRelation,
                                                             QuestionnairePhase clonedQuestionnairePhase,
                                                             Map<Long, TrainingPhase> clonedTrainingPhases,
                                                             Map<Long, Question> clonedQuestions) {
        QuestionPhaseRelation clonedQuestionPhaseRelation = cloneMapper.clone(questionPhaseRelation);
        clonedQuestionPhaseRelation.setQuestionnairePhase(clonedQuestionnairePhase);
        clonedQuestionPhaseRelation.setRelatedTrainingPhase(clonedTrainingPhases.get(questionPhaseRelation.getRelatedTrainingPhase().getId()));
        clonedQuestionPhaseRelation.setQuestions(questionPhaseRelation.getQuestions()
                .stream()
                .map(question -> clonedQuestions.get(question.getId()))
                .collect(Collectors.toSet()));
        return clonedQuestionPhaseRelation;
    }

    public void checkIfCanBeUpdated(TrainingDefinition trainingDefinition) {
        if (!trainingDefinition.getState().equals(TDState.UNRELEASED)) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingDefinition.class, "id", trainingDefinition.getId().getClass(), trainingDefinition.getId(),
                    ARCHIVED_OR_RELEASED));
        }
        if (trainingInstanceRepository.existsAnyForTrainingDefinition(trainingDefinition.getId())) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingDefinition.class, "id", trainingDefinition.getId().getClass(), trainingDefinition.getId(),
                    "Cannot update training definition with already created training instance. " +
                            "Remove training instance/s before updating training definition."));
        }
    }

    private void deletePhase(AbstractPhase abstractPhase) {
        if (abstractPhase instanceof QuestionnairePhase) {
            questionnairePhaseRepository.delete((QuestionnairePhase) abstractPhase);
        } else if (abstractPhase instanceof InfoPhase) {
            infoPhaseRepository.delete((InfoPhase) abstractPhase);
        } else {
            trainingPhaseRepository.delete((TrainingPhase) abstractPhase);
        }
    }

    private LocalDateTime getCurrentTimeInUTC() {
        return LocalDateTime.now(Clock.systemUTC());
    }

    private void addLoggedInUserToTrainingDefinitionAsAuthor(TrainingDefinition trainingDefinition) {
        Optional<User> user = userRefRepository.findUserByUserRefId(userManagementServiceApi.getLoggedInUserRefId());
        if (user.isPresent()) {
            trainingDefinition.addAuthor(user.get());
        } else {
            User newUser = new User(userManagementServiceApi.getLoggedInUserRefId());
            trainingDefinition.addAuthor(newUser);
        }
    }

    private void createDefaultPhases(TrainingDefinition trainingDefinition) {
        InfoPhase introInfoPhase = new InfoPhase();
        introInfoPhase.setTitle(defaultPhases.getIntroInfoPhase().getTitle());
        introInfoPhase.setOrder(0);
        introInfoPhase.setTrainingDefinition(trainingDefinition);
        introInfoPhase.setContent(defaultPhases.getIntroInfoPhase().getContent());
        infoPhaseRepository.save(introInfoPhase);

        QuestionnairePhase questionnairePhase = new QuestionnairePhase();
        questionnairePhase.setQuestionnaireType(QuestionnaireType.ADAPTIVE);
        questionnairePhase.setOrder(1);
        questionnairePhase.setTrainingDefinition(trainingDefinition);
        questionnairePhase.setTitle("Pre-training questionnaire");
        questionnairePhase.setQuestions(new ArrayList<>(List.of(
                defaultMCQ(0, questionnairePhase),
                defaultFFQ(1, questionnairePhase),
                defaultRFQ(2, questionnairePhase)
        )));
        questionnairePhaseRepository.save(questionnairePhase);

        TrainingPhase getAccessPhase = new TrainingPhase();
        getAccessPhase.setTitle(defaultPhases.getGetAccessPhase().getTitle());
        getAccessPhase.setOrder(2);
        getAccessPhase.setTrainingDefinition(trainingDefinition);
        getAccessPhase.setEstimatedDuration(defaultPhases.getGetAccessPhase().getEstimatedDuration());
        getAccessPhase.setAllowedWrongAnswers(100);
        getAccessPhase.setAllowedCommands(100);

        DecisionMatrixRow decisionMatrixRow = new DecisionMatrixRow();
        decisionMatrixRow.setTrainingPhase(getAccessPhase);
        decisionMatrixRow.setOrder(0);

        Task accessPhaseTask = new Task();
        accessPhaseTask.setTitle(defaultPhases.getGetAccessPhase().getTitle());
        accessPhaseTask.setAnswer(defaultPhases.getGetAccessPhase().getAnswer());
        accessPhaseTask.setSolution(defaultPhases.getGetAccessPhase().getSolution());
        accessPhaseTask.setIncorrectAnswerLimit(100);
        accessPhaseTask.setContent(defaultPhases.getGetAccessPhase().getContent());
        accessPhaseTask.setOrder(0);
        accessPhaseTask.setTrainingPhase(getAccessPhase);

        getAccessPhase.setDecisionMatrix(new ArrayList<>(List.of(decisionMatrixRow)));
        getAccessPhase.setTasks(new ArrayList<>(List.of(accessPhaseTask)));
        trainingPhaseRepository.save(getAccessPhase);
        trainingDefinition.setEstimatedDuration(trainingDefinition.getEstimatedDuration() + getAccessPhase.getEstimatedDuration());
    }

    private Question defaultMCQ(Integer order, QuestionnairePhase questionnairePhase) {
        Question mcq = new Question();
        mcq.setQuestionType(QuestionType.MCQ);
        mcq.setQuestionnairePhase(questionnairePhase);
        mcq.setText("The city known as the \"IT capital of India\" is ");
        mcq.setOrder(order);
        mcq.setChoices(new ArrayList<>(List.of(
                createQuestionChoice("Bangalore", 0, true, mcq),
                createQuestionChoice("Karachi", 1, false, mcq),
                createQuestionChoice("Mumbai", 2, false, mcq)
        )));
        return mcq;
    }

    private Question defaultFFQ(Integer order, QuestionnairePhase questionnairePhase) {
        Question ffq = new Question();
        ffq.setQuestionType(QuestionType.FFQ);
        ffq.setQuestionnairePhase(questionnairePhase);
        ffq.setText("What is the example of the transport layer protocol?");
        ffq.setOrder(order);
        ffq.setChoices(new ArrayList<>(List.of(
                createQuestionChoice("SPX", 0, true, ffq),
                createQuestionChoice("TCP", 1, true, ffq),
                createQuestionChoice("UDP", 2, true, ffq)
        )));
        return ffq;
    }

    private Question defaultRFQ(Integer order, QuestionnairePhase questionnairePhase) {
        Question rfq = new Question();
        rfq.setQuestionType(QuestionType.RFQ);
        rfq.setQuestionnairePhase(questionnairePhase);
        rfq.setText("What is your level of skill in zip and unzip files in CLI?");
        rfq.setOrder(order);
        rfq.setChoices(new ArrayList<>(List.of(
                createQuestionChoice("High", 0, true, rfq),
                createQuestionChoice("Medium", 1, true, rfq),
                createQuestionChoice("Low", 3, false, rfq),
                createQuestionChoice("None", 2, false, rfq)
        )));
        return rfq;
    }

    private QuestionChoice createQuestionChoice(String text, Integer order, boolean correct, Question question) {
        QuestionChoice choice = new QuestionChoice();
        choice.setText(text);
        choice.setOrder(order);
        choice.setCorrect(correct);
        choice.setQuestion(question);
        return choice;
    }
}
