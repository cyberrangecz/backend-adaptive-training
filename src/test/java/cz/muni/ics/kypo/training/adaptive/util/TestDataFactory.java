package cz.muni.ics.kypo.training.adaptive.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.muni.ics.kypo.training.adaptive.converter.LocalDateTimeUTCSerializer;
import cz.muni.ics.kypo.training.adaptive.domain.AccessToken;
import cz.muni.ics.kypo.training.adaptive.domain.User;
import cz.muni.ics.kypo.training.adaptive.domain.phase.*;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.Question;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionChoice;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.dto.BasicPhaseInfoDTO;
import cz.muni.ics.kypo.training.adaptive.dto.UserRefDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.training.TrainingInstanceArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.ImportTrainingDefinitionDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionAnswerDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionChoiceDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnaireUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.responses.LockedPoolInfo;
import cz.muni.ics.kypo.training.adaptive.dto.responses.PoolInfoDTO;
import cz.muni.ics.kypo.training.adaptive.dto.responses.SandboxInfo;
import cz.muni.ics.kypo.training.adaptive.dto.responses.SandboxPoolInfo;
import cz.muni.ics.kypo.training.adaptive.dto.training.DecisionMatrixRowDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TaskCopyDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TaskUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.technique.MitreTechniqueDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingdefinition.TrainingDefinitionByIdDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingdefinition.TrainingDefinitionCreateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingdefinition.TrainingDefinitionDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingdefinition.TrainingDefinitionInfoDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingdefinition.TrainingDefinitionUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.traininginstance.TrainingInstanceCreateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.traininginstance.TrainingInstanceDTO;
import cz.muni.ics.kypo.training.adaptive.dto.traininginstance.TrainingInstanceUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.TrainingRunByIdDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.TrainingRunDTO;
import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionType;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionnaireType;
import cz.muni.ics.kypo.training.adaptive.enums.TDState;
import cz.muni.ics.kypo.training.adaptive.enums.TRState;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Component
public class TestDataFactory {

    public static final String CORRECT_QUESTION_CHOICE = "Correct answer";
    public static final String WRONG_QUESTION_CHOICE = "Wrong answer";
    public static final String ANOTHER_CORRECT_QUESTION_CHOICE = "Another correct answer";

    private SimpleModule simpleModule = new SimpleModule("SimpleModule").addSerializer(new LocalDateTimeUTCSerializer());
    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(simpleModule)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private QuestionnairePhase general = generateQuestionnairePhase("Test", 50, QuestionnaireType.GENERAL);
    private QuestionnairePhase adaptive = generateQuestionnairePhase("Questionnaire", 0, QuestionnaireType.ADAPTIVE);

    private Question freeFormQuestion = generateQuestion("Free form question", 0, QuestionType.FFQ);
    private Question multipleChoiceQuestion = generateQuestion("Multiple choice question", 1, QuestionType.MCQ);
    private Question ratingFormQuestion = generateQuestion("Rating form question", 2, QuestionType.RFQ);

    private QuestionChoice correctQuestionChoice = generateQuestionChoice(CORRECT_QUESTION_CHOICE, 0, true);
    private QuestionChoice incorrectQuestionChoice = generateQuestionChoice(WRONG_QUESTION_CHOICE, 1, false);
    private QuestionChoice anotherCorrectQuestionChoice = generateQuestionChoice(ANOTHER_CORRECT_QUESTION_CHOICE, 2, true);

    private QuestionnaireUpdateDTO questionnaireUpdateDTO = generateQuestionnaireUpdateDTO("Updated questionnaire phase title");
    private QuestionDTO freeFormQuestionDTO = generateQuestionDTO("Free form question", 0, QuestionType.FFQ);
    private QuestionDTO multipleChoiceQuestionDTO = generateQuestionDTO("Multiple choice question", 1, QuestionType.MCQ);
    private QuestionDTO ratingFormQuestionDTO = generateQuestionDTO("Rating form question", 2, QuestionType.RFQ);

    private QuestionChoiceDTO correctQuestionChoiceDTO = generateQuestionChoiceDTO(CORRECT_QUESTION_CHOICE, 0, true);
    private QuestionChoiceDTO incorrectQuestionChoiceDTO = generateQuestionChoiceDTO(WRONG_QUESTION_CHOICE, 1, false);
    private QuestionChoiceDTO anotherCorrectQuestionChoiceDTO = generateQuestionChoiceDTO(ANOTHER_CORRECT_QUESTION_CHOICE, 2, true);

    private TrainingPhase trainingPhase1 = generateTrainingPhase("First Game Level", 100, 0, 3, 5);
    private TrainingPhase trainingPhase2 = generateTrainingPhase("Second Game Level", 100, 1, 3, 5);
    private TrainingPhase trainingPhase3 = generateTrainingPhase("Third Game Level", 100, 2, 3, 5);

    private TrainingPhaseUpdateDTO trainingPhaseUpdateDTO = generateTrainingPhaseUpdateDTO("Updated training phase title", 20, 5, 10);

    private DecisionMatrixRowDTO decisionMatrixRowDTO1 = generateDecisionMatrixRowDTO(0, 0.0, 0.0, 1.0, 0.0, 0.0);

    public Task getTask11() {
        return clone(task11, Task.class);
    }

    public Task getTask12() {
        return clone(task12, Task.class);
    }

    public Task getTask13() {
        return clone(task13, Task.class);
    }

    public Task getTask21() {
        return clone(task21, Task.class);
    }

    public Task getTask22() {
        return clone(task22, Task.class);
    }

    public Task getTask23() {
        return clone(task23, Task.class);
    }

    public Task getTask31() {
        return clone(task31, Task.class);
    }

    public Task getTask32() {
        return clone(task32, Task.class);
    }

    public Task getTask33() {
        return clone(task33, Task.class);
    }

    public TaskUpdateDTO getTaskUpdateDTO() {
        return clone(taskUpdateDTO, TaskUpdateDTO.class);
    }

    public TaskCopyDTO getTaskCopyDTO() {
        return clone(taskCopyDTO, TaskCopyDTO.class);
    }

    private InfoPhase infoPhase1 = generateInfoPhase("Info phase 1", 7, "Information");
    private InfoPhase infoPhase2 = generateInfoPhase("Info phase 2", 9, "Content");

    private InfoPhaseUpdateDTO infoPhaseUpdateDTO = generateInfoPhaseUpdateDTO("Updated info phase title", "Updated info phase content");

    private AccessPhase accessPhase = generateAccessPhase("Access phase", 10, "Cloud content.",
            "Local content. Command: ${USER_ID} ${ACCESS_PHASE} ${CENTRAL_SYSLOG_IP}", "start-training");

    private BasicPhaseInfoDTO basicTrainingPhaseInfoDTO = generateBasicPhaseInfoDTO("Basic Game phase info", PhaseType.TRAINING);
    private BasicPhaseInfoDTO basicInfoPhaseInfoDTO = generateBasicPhaseInfoDTO("Basic Info phase info", PhaseType.INFO);

    private MitreTechnique mitreTechnique1 = generateMitreTechnique("T1548.002");
    private MitreTechnique mitreTechnique2 = generateMitreTechnique("T2451.004");

    private MitreTechniqueDTO mitreTechniqueDTO1 = generateMitreTechniqueDTO("T3548.003");
    private MitreTechniqueDTO mitreTechniqueDTO2 = generateMitreTechniqueDTO("T5791.011");

    private AccessToken accessToken1 = generateAccessToken("test-0000");
    private AccessToken accessToken2 = generateAccessToken("token-9999");

    private TrainingDefinition unreleasedDefinition = generateTrainingDefinition("Unreleased definition", "Unreleased description",
            new String[]{"p1", "p2"}, new String[]{"o1", "o2"}, TDState.UNRELEASED,
            LocalDateTime.now(Clock.systemUTC()).minusHours(1), "John Doe", LocalDateTime.now(Clock.systemUTC()).minusHours(1));
    private TrainingDefinition releasedDefinition = generateTrainingDefinition("Released definition", "Released description",
            new String[]{"p3", "p4"}, new String[]{"o3"}, TDState.RELEASED,
            LocalDateTime.now(Clock.systemUTC()).minusHours(5), "John Doe", LocalDateTime.now(Clock.systemUTC()).minusHours(5));
    private TrainingDefinition archivedDefinition = generateTrainingDefinition("Archived definition", "Archived description",
            new String[]{"p5"}, new String[]{"o4", "o5", "o6"}, TDState.ARCHIVED,
            LocalDateTime.now(Clock.systemUTC()).minusHours(10), "Jane Doe", LocalDateTime.now(Clock.systemUTC()).minusHours(10));
    private TrainingDefinitionDTO unreleasedDefinitionDTO = generateTrainingDefinitionDTO(unreleasedDefinition);
    private TrainingDefinitionDTO releasedDefinitionDTO = generateTrainingDefinitionDTO(releasedDefinition);
    private TrainingDefinitionDTO archivedDefinitionDTO = generateTrainingDefinitionDTO(archivedDefinition);
    private TrainingDefinitionInfoDTO unreleasedDefinitionInfoDTO = generateTrainingDefinitionInfoDTO(unreleasedDefinition);
    private TrainingDefinitionInfoDTO releasedDefinitionInfoDTO = generateTrainingDefinitionInfoDTO(releasedDefinition);
    private TrainingDefinitionInfoDTO archivedDefinitionInfoDTO = generateTrainingDefinitionInfoDTO(archivedDefinition);
    private TrainingDefinitionCreateDTO trainingDefinitionCreateDTO = generateTrainingDefinitionCreateDTO("Training definition create DTO",
            "Creation of definition", new String[]{"p8", "p9"}, new String[]{"o8", "o9"}, TDState.UNRELEASED);
    private TrainingDefinitionUpdateDTO trainingDefinitionUpdateDTO = generateTrainingDefinitionUpdateDTO("Training definition updaet DTO",
            "Update of definition", new String[]{"p6", "p7"}, new String[]{"o6", "o7"}, TDState.UNRELEASED,
            7L);
    private ImportTrainingDefinitionDTO importTrainingDefinitionDTO = generateImportTrainingDefinitionDTO("Imported definition", "Imported description",
            new String[]{"ip1", "ip2"}, TDState.UNRELEASED);
    private TrainingDefinitionByIdDTO trainingDefinitionByIdDTO = generateTrainingDefinitionByIdDTO("TDbyId", "Definition by id", new String[]{"p8", "p9"},
            TDState.UNRELEASED, false,
            20L, LocalDateTime.now(Clock.systemUTC()).minusHours(15));

    private TrainingInstance futureInstance = generateTrainingInstance(LocalDateTime.now(Clock.systemUTC()).plusHours(10),
            LocalDateTime.now(Clock.systemUTC()).plusHours(20), "Future Instance", 1L, "future-1111",
            LocalDateTime.now(Clock.systemUTC()).minusHours(12), "Peter White");
    private TrainingInstance ongoingInstance = generateTrainingInstance(LocalDateTime.now(Clock.systemUTC()).minusHours(10),
            LocalDateTime.now(Clock.systemUTC()).plusHours(10), "Ongoing Instance", 2L, "ongoing-2222",
            LocalDateTime.now(Clock.systemUTC()).minusHours(14), "Michael Black");
    private TrainingInstance concludedInstance = generateTrainingInstance(LocalDateTime.now(Clock.systemUTC()).minusHours(20),
            LocalDateTime.now(Clock.systemUTC()).minusHours(5), "Concluded Instance", 3L, "concluded-3333",
            LocalDateTime.now(Clock.systemUTC()).minusHours(11), "John Doe");
    private TrainingInstanceCreateDTO trainingInstanceCreateDTO = generateTrainingInstanceCreateDTO(LocalDateTime.now(Clock.systemUTC()).plusHours(15),
            LocalDateTime.now(Clock.systemUTC()).plusHours(22), "Create Instance", "create");
    private TrainingInstanceUpdateDTO trainingInstanceUpdateDTO = generateTrainingInstanceUpdateDTO(LocalDateTime.now(Clock.systemUTC()).plusHours(5),
            LocalDateTime.now(Clock.systemUTC()).plusHours(7), "Update Instance", "update");
    private TrainingInstanceDTO trainingInstanceDTO = generateTrainingInstanceDTO(LocalDateTime.now(Clock.systemUTC()).plusHours(11),
            LocalDateTime.now(Clock.systemUTC()).plusHours(22), "Instance DTO", "DTO-5555", 10L);
    private TrainingInstanceArchiveDTO trainingInstanceArchiveDTO = generateTrainingInstanceArchiveDTO(LocalDateTime.now(Clock.systemUTC()).minusHours(20),
            LocalDateTime.now(Clock.systemUTC()).minusHours(10), "Archived instance", "archived-6666");

    private TrainingRun runningRun = generateTrainingRun(LocalDateTime.now(Clock.systemUTC()).minusHours(2), LocalDateTime.now(Clock.systemUTC()).plusHours(2),
            TRState.RUNNING, 2, true, "1L", 1, true,
            "20L");
    private TrainingRun finishedRun = generateTrainingRun(LocalDateTime.now(Clock.systemUTC()).minusHours(10), LocalDateTime.now(Clock.systemUTC()).minusHours(5),
            TRState.FINISHED, 4, false, "3L", 3, true, "30L");
    private TrainingRun archivedRun = generateTrainingRun(LocalDateTime.now(Clock.systemUTC()).minusHours(20), LocalDateTime.now(Clock.systemUTC()).minusHours(10),
            TRState.ARCHIVED, 0, false, "5L", 5, false, "60L");
    private TrainingRunByIdDTO trainingRunByIdDTO = generateTrainingRunByIdDTO(LocalDateTime.now(Clock.systemUTC()).minusHours(2), LocalDateTime.now(Clock.systemUTC()).plusHours(2),
            TRState.RUNNING, "5L");
    private TrainingRunDTO trainingRunDTO = generateTrainingRunDTO(LocalDateTime.now(Clock.systemUTC()).minusHours(9), LocalDateTime.now(Clock.systemUTC()).minusHours(5),
            TRState.FINISHED, "7L", 7);
//    private AccessedTrainingRunDTO accessedTrainingRunDTO = generateAccessedTrainingRunDTO("Accessed run", LocalDateTime.now(Clock.systemUTC()).minusHours(8), LocalDateTime.now(Clock.systemUTC()).minusHours(4), 5,
//            6, Actions.RESUME);

    private PoolInfoDTO poolInfoDTO = generatePoolInfoDTO(1L, 1L, 5L, 10L, 5L, "sha", "revSha");
    private SandboxInfo sandboxInfo = generateSandboxInfo("1L", 1, 4);
    private SandboxPoolInfo sandboxPoolInfo = generateSandboxPoolInfo(1L, 1L, 10L, 5L);
    private LockedPoolInfo lockedPoolInfo = generateLockedPoolInfo(1L, 1L);

    private UserRefDTO userRefDTO1 = generateUserRefDTO(10L, "Michael Bolt", "Bolt", "Michael", "mail1@muni.cz", "https://oidc.muni.cz/oidc", null);
    private UserRefDTO userRefDTO2 = generateUserRefDTO(12L, "Peter Most", "Most", "Peter", "mail2@muni.cz", "https://oidc.muni.cz/oidc", null);
    private UserRefDTO userRefDTO3 = generateUserRefDTO(14L, "John Nevel", "Nevel", "John", "mail38@muni.cz", "https://oidc.muni.cz/oidc", null);
    private UserRefDTO userRefDTO4 = generateUserRefDTO(17L, "Ted Mosby", "Mosby", "Ted", "mail4@muni.cz", "https://oidc.muni.cz/oidc", null);

    private User user1 = generateUser(10L);
    private User user2 = generateUser(12L);
    private User user3 = generateUser(14L);
    private User user4 = generateUser(17L);

    public QuestionnairePhase getGeneral() {
        return clone(general, QuestionnairePhase.class);
    }

    public QuestionnairePhase getAdaptive() {
        return clone(adaptive, QuestionnairePhase.class);
    }

    public Question getFreeFormQuestion() {
        return clone(freeFormQuestion, Question.class);
    }

    public Question getMultipleChoiceQuestion() {
        return clone(multipleChoiceQuestion, Question.class);
    }

    public Question getRatingFormQuestion() {
        return clone(ratingFormQuestion, Question.class);
    }

    public QuestionChoice getCorrectQuestionChoice() {
        return clone(correctQuestionChoice, QuestionChoice.class);
    }

    public QuestionChoice getIncorrectQuestionChoice() {
        return clone(incorrectQuestionChoice, QuestionChoice.class);
    }

    public QuestionChoice getAnotherCorrectQuestionChoice() {
        return clone(anotherCorrectQuestionChoice, QuestionChoice.class);
    }

    public QuestionAnswerDTO getCorrectAnswer() {
        QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
        questionAnswerDTO.setAnswers(Set.of(CORRECT_QUESTION_CHOICE));
        return questionAnswerDTO;
    }

    public QuestionAnswerDTO getWrongAnswer() {
        QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
        questionAnswerDTO.setAnswers(Set.of(WRONG_QUESTION_CHOICE));
        return questionAnswerDTO;
    }

    public QuestionAnswerDTO getEmptyAnswer() {
        QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
        questionAnswerDTO.setAnswers(Collections.emptySet());
        return questionAnswerDTO;
    }

    public QuestionnaireUpdateDTO getQuestionnaireUpdateDTO() {
        return clone(questionnaireUpdateDTO, QuestionnaireUpdateDTO.class);
    }

    public QuestionDTO getFreeFormQuestionDTO() {
        return clone(freeFormQuestionDTO, QuestionDTO.class);
    }

    public QuestionDTO getMultipleChoiceQuestionDTO() {
        return clone(multipleChoiceQuestionDTO, QuestionDTO.class);
    }

    public QuestionDTO getRatingFormQuestionDTO() {
        return clone(ratingFormQuestionDTO, QuestionDTO.class);
    }

    public QuestionChoiceDTO getCorrectQuestionChoiceDTO() {
        return clone(correctQuestionChoiceDTO, QuestionChoiceDTO.class);
    }

    public QuestionChoiceDTO getIncorrectQuestionChoiceDTO() {
        return clone(incorrectQuestionChoiceDTO, QuestionChoiceDTO.class);
    }

    public QuestionChoiceDTO getAnotherCorrectQuestionChoiceDTO() {
        return clone(anotherCorrectQuestionChoiceDTO, QuestionChoiceDTO.class);
    }

    public TrainingPhase getTrainingPhase1() {
        return clone(trainingPhase1, TrainingPhase.class);
    }

    public TrainingPhase getTrainingPhase2() {
        return clone(trainingPhase2, TrainingPhase.class);
    }

    public TrainingPhase getTrainingPhase3() {
        return clone(trainingPhase3, TrainingPhase.class);
    }

    public TrainingPhaseUpdateDTO getTrainingPhaseUpdateDTO() {
        return clone(trainingPhaseUpdateDTO, TrainingPhaseUpdateDTO.class);
    }

    public DecisionMatrixRowDTO getDecisionMatrixRowDTO1() {
        return clone(decisionMatrixRowDTO1, DecisionMatrixRowDTO.class);
    }

    private Task task11 = generateTask("Task11", "Content of task11", 0, "answer11", "solution11", 2, true, 20);
    private Task task12 = generateTask("Task12", "Content of task12", 1, "answer12", "solution12", 5, false, 15);
    private Task task13 = generateTask("Task13", "Content of task13", 2, "answer13", "solution13", 4, true, 10);
    private Task task21 = generateTask("Task21", "Content of task21", 0, "answer21", "solution21", 2, true, 35);
    private Task task22 = generateTask("Task22", "Content of task22", 1, "answer22", "solution22", 3, false, 22);
    private Task task23 = generateTask("Task23", "Content of task23", 2, "answer23", "solution23", 5, false, 50);
    private Task task31 = generateTask("Task31", "Content of task31", 0, "answer31", "solution31", 9, true, 60);
    private Task task32 = generateTask("Task32", "Content of task32", 1, "answer32", "solution32", 5, false, 40);
    private Task task33 = generateTask("Task33", "Content of task33", 2, "answer33", "solution33", 3, true, 20);

    private TaskUpdateDTO taskUpdateDTO = generateTaskUpdateDTO("Updated task title", "Updated task content", "Updated answer", "Updated solution", 10, false, 10);
    private TaskCopyDTO taskCopyDTO = generateTaskCopyDTO("Copied task title", "Copied task content", "Copied answer", "Copied solution", 5, true, 20);

    public InfoPhase getInfoPhase1() {
        return clone(infoPhase1, InfoPhase.class);
    }

    public InfoPhase getInfoPhase2() {
        return clone(infoPhase2, InfoPhase.class);
    }

    public InfoPhaseUpdateDTO getInfoPhaseUpdateDTO() {
        return clone(infoPhaseUpdateDTO, InfoPhaseUpdateDTO.class);
    }

    public AccessPhase getAccessPhase() {
        return clone(accessPhase, AccessPhase.class);
    }

    public MitreTechnique getMitreTechnique1() {
        return clone(mitreTechnique1, MitreTechnique.class);
    }

    public MitreTechnique getMitreTechnique2() {
        return clone(mitreTechnique2, MitreTechnique.class);
    }

    public MitreTechniqueDTO getMitreTechniqueDTO1() {
        return clone(mitreTechniqueDTO1, MitreTechniqueDTO.class);
    }

    public MitreTechniqueDTO getMitreTechniqueDTO2() {
        return clone(mitreTechniqueDTO2, MitreTechniqueDTO.class);
    }

    public AccessToken getAccessToken1() {
        return clone(accessToken1, AccessToken.class);
    }

    public AccessToken getAccessToken2() {
        return clone(accessToken2, AccessToken.class);
    }

    public TrainingDefinition getUnreleasedDefinition() {
        return clone(unreleasedDefinition, TrainingDefinition.class);
    }

    public TrainingDefinition getReleasedDefinition() {
        return clone(releasedDefinition, TrainingDefinition.class);
    }

    public TrainingDefinition getArchivedDefinition() {
        return clone(archivedDefinition, TrainingDefinition.class);
    }

    public TrainingDefinitionDTO getUnreleasedDefinitionDTO() {
        return clone(unreleasedDefinitionDTO, TrainingDefinitionDTO.class);
    }

    public TrainingDefinitionDTO getReleasedDefinitionDTO() {
        return clone(releasedDefinitionDTO, TrainingDefinitionDTO.class);
    }

    public TrainingDefinitionDTO getArchivedDefinitionDTO() {
        return clone(archivedDefinitionDTO, TrainingDefinitionDTO.class);
    }

    public TrainingDefinitionInfoDTO getUnreleasedDefinitionInfoDTO() {
        return clone(unreleasedDefinitionInfoDTO, TrainingDefinitionInfoDTO.class);
    }

    public TrainingDefinitionInfoDTO getReleasedDefinitionInfoDTO() {
        return clone(releasedDefinitionInfoDTO, TrainingDefinitionInfoDTO.class);
    }

    public TrainingDefinitionInfoDTO getArchivedDefinitionInfoDTO() {
        return clone(archivedDefinitionInfoDTO, TrainingDefinitionInfoDTO.class);
    }

    public TrainingInstance getFutureInstance() {
        return clone(futureInstance, TrainingInstance.class);
    }

    public TrainingInstance getOngoingInstance() {
        return clone(ongoingInstance, TrainingInstance.class);
    }

    public TrainingInstance getConcludedInstance() {
        return clone(concludedInstance, TrainingInstance.class);
    }

    public TrainingRun getRunningRun() {
        return clone(runningRun, TrainingRun.class);
    }

    public TrainingRun getFinishedRun() {
        return clone(finishedRun, TrainingRun.class);
    }

    public TrainingRun getArchivedRun() {
        return clone(archivedRun, TrainingRun.class);
    }

    public TrainingDefinitionCreateDTO getTrainingDefinitionCreateDTO() {
        return clone(trainingDefinitionCreateDTO, TrainingDefinitionCreateDTO.class);
    }

    public TrainingDefinitionUpdateDTO getTrainingDefinitionUpdateDTO() {
        return clone(trainingDefinitionUpdateDTO, TrainingDefinitionUpdateDTO.class);
    }

    public TrainingInstanceCreateDTO getTrainingInstanceCreateDTO() {
        return clone(trainingInstanceCreateDTO, TrainingInstanceCreateDTO.class);
    }

    public TrainingInstanceUpdateDTO getTrainingInstanceUpdateDTO() {
        return clone(trainingInstanceUpdateDTO, TrainingInstanceUpdateDTO.class);
    }

    public TrainingRunByIdDTO getTrainingRunByIdDTO() {
        return clone(trainingRunByIdDTO, TrainingRunByIdDTO.class);
    }


    public TrainingRunDTO getTrainingRunDTO() {
        return clone(trainingRunDTO, TrainingRunDTO.class);
    }

    public ImportTrainingDefinitionDTO getImportTrainingDefinitionDTO() {
        return clone(importTrainingDefinitionDTO, ImportTrainingDefinitionDTO.class);
    }

    public TrainingDefinitionByIdDTO getTrainingDefinitionByIdDTO() {
        return clone(trainingDefinitionByIdDTO, TrainingDefinitionByIdDTO.class);
    }

    public TrainingInstanceDTO getTrainingInstanceDTO() {
        return clone(trainingInstanceDTO, TrainingInstanceDTO.class);
    }

//    public AccessedTrainingRunDTO getAccessedTrainingRunDTO(){
//        return clone(accessedTrainingRunDTO, AccessedTrainingRunDTO.class);
//    }

    public TrainingInstanceArchiveDTO getTrainingInstanceArchiveDTO() {
        return clone(trainingInstanceArchiveDTO, TrainingInstanceArchiveDTO.class);
    }

    public PoolInfoDTO getPoolInfoDTO() {
        return clone(poolInfoDTO, PoolInfoDTO.class);
    }

    public SandboxInfo getSandboxInfo() {
        return clone(sandboxInfo, SandboxInfo.class);
    }

    public SandboxPoolInfo getSandboxPoolInfo() {
        return clone(sandboxPoolInfo, SandboxPoolInfo.class);
    }

    public LockedPoolInfo getLockedPoolInfo() {
        return clone(lockedPoolInfo, LockedPoolInfo.class);
    }

    public UserRefDTO getUserRefDTO1() {
        return clone(userRefDTO1, UserRefDTO.class);
    }

    public UserRefDTO getUserRefDTO2() {
        return clone(userRefDTO2, UserRefDTO.class);
    }

    public UserRefDTO getUserRefDTO3() {
        return clone(userRefDTO3, UserRefDTO.class);
    }

    public UserRefDTO getUserRefDTO4() {
        return clone(userRefDTO4, UserRefDTO.class);
    }


    public User getUser1() {
        return clone(user1, User.class);
    }

    public User getUser2() {
        return clone(user2, User.class);
    }

    public User getUser3() {
        return clone(user3, User.class);
    }

    public User getUser4() {
        return clone(user4, User.class);
    }

    private QuestionnairePhase generateQuestionnairePhase(String title, int order, QuestionnaireType questionnaireType) {
        QuestionnairePhase newQuestionnairePhase = new QuestionnairePhase();
        newQuestionnairePhase.setTitle(title);
        newQuestionnairePhase.setOrder(order);
        newQuestionnairePhase.setQuestionnaireType(questionnaireType);
        return newQuestionnairePhase;
    }

    private TrainingPhase generateTrainingPhase(String title, int estimatedDuration, int order, int allowedCommands, int allowedWrongAnswers) {
        TrainingPhase newTrainingPhase = new TrainingPhase();
        newTrainingPhase.setTitle(title);
        newTrainingPhase.setEstimatedDuration(estimatedDuration);
        newTrainingPhase.setOrder(order);
        newTrainingPhase.setAllowedCommands(allowedCommands);
        newTrainingPhase.setAllowedWrongAnswers(allowedWrongAnswers);
        return newTrainingPhase;
    }

    private InfoPhase generateInfoPhase(String title, int order, String content) {
        InfoPhase newInfoPhase = new InfoPhase();
        newInfoPhase.setTitle(title);
        newInfoPhase.setOrder(order);
        newInfoPhase.setContent(content);
        return newInfoPhase;
    }

    private AccessPhase generateAccessPhase(String title, int order, String cloudContent, String localContent, String passkey) {
        AccessPhase newAccessPhase = new AccessPhase();
        newAccessPhase.setTitle(title);
        newAccessPhase.setOrder(order);
        newAccessPhase.setCloudContent(cloudContent);
        newAccessPhase.setLocalContent(localContent);
        newAccessPhase.setPasskey(passkey);
        return newAccessPhase;
    }

    private InfoPhaseUpdateDTO generateInfoPhaseUpdateDTO(String title, String content) {
        InfoPhaseUpdateDTO infoPhaseUpdateDTO = new InfoPhaseUpdateDTO();
        infoPhaseUpdateDTO.setPhaseType(PhaseType.INFO);
        infoPhaseUpdateDTO.setTitle(title);
        infoPhaseUpdateDTO.setContent(content);
        return infoPhaseUpdateDTO;
    }

    private TrainingPhaseUpdateDTO generateTrainingPhaseUpdateDTO(String title, int allowedCommands, int allowedWrongAnswers, int estimatedDuration) {
        TrainingPhaseUpdateDTO trainingPhaseUpdateDTO = new TrainingPhaseUpdateDTO();
        trainingPhaseUpdateDTO.setPhaseType(PhaseType.TRAINING);
        trainingPhaseUpdateDTO.setTitle(title);
        trainingPhaseUpdateDTO.setAllowedCommands(allowedCommands);
        trainingPhaseUpdateDTO.setAllowedWrongAnswers(allowedWrongAnswers);
        trainingPhaseUpdateDTO.setEstimatedDuration(estimatedDuration);
        return trainingPhaseUpdateDTO;
    }

    private DecisionMatrixRowDTO generateDecisionMatrixRowDTO(int order, double completedInTime, double keywordUser, double questionnaireAnswered, double solutionDisplayed, double wrongAnswers) {
        DecisionMatrixRowDTO decisionMatrixRowDTO = new DecisionMatrixRowDTO();
        decisionMatrixRowDTO.setOrder(order);
        decisionMatrixRowDTO.setCompletedInTime(completedInTime);
        decisionMatrixRowDTO.setKeywordUsed(keywordUser);
        decisionMatrixRowDTO.setQuestionnaireAnswered(questionnaireAnswered);
        decisionMatrixRowDTO.setSolutionDisplayed(solutionDisplayed);
        decisionMatrixRowDTO.setWrongAnswers(wrongAnswers);
        return decisionMatrixRowDTO;
    }

    private Question generateQuestion(String text, int order, QuestionType questionType) {
        Question newQuestion = new Question();
        newQuestion.setText(text);
        newQuestion.setOrder(order);
        newQuestion.setQuestionType(questionType);
        return newQuestion;
    }

    private QuestionChoice generateQuestionChoice(String text, int order, boolean correct) {
        QuestionChoice newQuestionChoice = new QuestionChoice();
        newQuestionChoice.setText(text);
        newQuestionChoice.setOrder(order);
        newQuestionChoice.setCorrect(correct);
        return newQuestionChoice;
    }

    private QuestionnaireUpdateDTO generateQuestionnaireUpdateDTO(String title) {
        QuestionnaireUpdateDTO questionnaireUpdateDTO = new QuestionnaireUpdateDTO();
        questionnaireUpdateDTO.setPhaseType(PhaseType.QUESTIONNAIRE);
        questionnaireUpdateDTO.setTitle(title);
        return questionnaireUpdateDTO;
    }

    private QuestionDTO generateQuestionDTO(String text, int order, QuestionType questionType) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setText(text);
        questionDTO.setOrder(order);
        questionDTO.setQuestionType(questionType);
        return questionDTO;
    }

    private QuestionChoiceDTO generateQuestionChoiceDTO(String text, int order, boolean correct) {
        QuestionChoiceDTO questionChoiceDTO = new QuestionChoiceDTO();
        questionChoiceDTO.setText(text);
        questionChoiceDTO.setOrder(order);
        questionChoiceDTO.setCorrect(correct);
        return questionChoiceDTO;
    }

    private MitreTechnique generateMitreTechnique(String techniqueKey) {
        MitreTechnique newMitreTechnique = new MitreTechnique();
        newMitreTechnique.setTechniqueKey(techniqueKey);
        return newMitreTechnique;
    }

    private MitreTechniqueDTO generateMitreTechniqueDTO(String techniqueKey) {
        MitreTechniqueDTO mitreTechniqueDTO = new MitreTechniqueDTO();
        mitreTechniqueDTO.setTechniqueKey(techniqueKey);
        return mitreTechniqueDTO;
    }

    private AccessToken generateAccessToken(String accessToken) {
        AccessToken newAccessToken = new AccessToken();
        newAccessToken.setAccessToken(accessToken);
        return newAccessToken;
    }

    private Task generateTask(String title, String content, Integer order, String answer, String solution, int incorrectAnswerLimit, boolean modifySandbox, int sandboxChangeExpectedDuration) {
        Task newTask = new Task();
        newTask.setTitle(title);
        newTask.setContent(content);
        newTask.setOrder(order);
        newTask.setAnswer(answer);
        newTask.setSolution(solution);
        newTask.setIncorrectAnswerLimit(incorrectAnswerLimit);
        newTask.setModifySandbox(modifySandbox);
        newTask.setSandboxChangeExpectedDuration(sandboxChangeExpectedDuration);
        return newTask;
    }

    private TaskUpdateDTO generateTaskUpdateDTO(String title, String content, String answer, String solution, int incorrectAnswerLimit, boolean modifySandbox, int sandboxChangeExpectedDuration) {
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setTitle(title);
        taskUpdateDTO.setContent(content);
        taskUpdateDTO.setAnswer(answer);
        taskUpdateDTO.setSolution(solution);
        taskUpdateDTO.setIncorrectAnswerLimit(incorrectAnswerLimit);
        taskUpdateDTO.setModifySandbox(modifySandbox);
        taskUpdateDTO.setSandboxChangeExpectedDuration(sandboxChangeExpectedDuration);
        return taskUpdateDTO;
    }

    private TaskCopyDTO generateTaskCopyDTO(String title, String content, String answer, String solution, int incorrectAnswerLimit, boolean modifySandbox, int sandboxChangeExpectedDuration) {
        TaskCopyDTO taskCopyDTO = new TaskCopyDTO();
        taskCopyDTO.setTitle(title);
        taskCopyDTO.setContent(content);
        taskCopyDTO.setAnswer(answer);
        taskCopyDTO.setSolution(solution);
        taskCopyDTO.setIncorrectAnswerLimit(incorrectAnswerLimit);
        taskCopyDTO.setModifySandbox(modifySandbox);
        taskCopyDTO.setSandboxChangeExpectedDuration(sandboxChangeExpectedDuration);
        return taskCopyDTO;
    }

    private TrainingDefinition generateTrainingDefinition(String title, String description, String[] prerequisites,
                                                          String[] outcomes, TDState state,
                                                          LocalDateTime lastEdited, String lastEditedBy,
                                                          LocalDateTime createdAt) {
        TrainingDefinition newTrainingDefinition = new TrainingDefinition();
        newTrainingDefinition.setTitle(title);
        newTrainingDefinition.setDescription(description);
        newTrainingDefinition.setOutcomes(outcomes);
        newTrainingDefinition.setState(state);
        newTrainingDefinition.setLastEdited(lastEdited);
        newTrainingDefinition.setLastEditedBy(lastEditedBy);
        newTrainingDefinition.setCreatedAt(createdAt);
        return newTrainingDefinition;
    }

    private TrainingDefinitionDTO generateTrainingDefinitionDTO(TrainingDefinition trainingDefinition) {
        TrainingDefinitionDTO trainingDefinitionDTO = new TrainingDefinitionDTO();
        trainingDefinitionDTO.setTitle(trainingDefinition.getTitle());
        trainingDefinitionDTO.setDescription(trainingDefinition.getDescription());
        trainingDefinitionDTO.setOutcomes(trainingDefinition.getOutcomes());
        trainingDefinitionDTO.setState(trainingDefinition.getState());
        trainingDefinitionDTO.setLastEdited(trainingDefinition.getLastEdited());
        trainingDefinitionDTO.setCreatedAt(trainingDefinition.getCreatedAt());
        return trainingDefinitionDTO;
    }

    private TrainingDefinitionInfoDTO generateTrainingDefinitionInfoDTO(TrainingDefinition trainingDefinition) {
        TrainingDefinitionInfoDTO trainingDefinitionInfoDTO = new TrainingDefinitionInfoDTO();
        trainingDefinitionInfoDTO.setTitle(trainingDefinition.getTitle());
        trainingDefinitionInfoDTO.setState(trainingDefinition.getState());
        return trainingDefinitionInfoDTO;
    }

    private TrainingInstance generateTrainingInstance(LocalDateTime starTime, LocalDateTime endTime, String title,
                                                      Long poolId, String accessToken, LocalDateTime lastEdited, String lastEditedBy) {
        TrainingInstance newTrainingInstance = new TrainingInstance();
        newTrainingInstance.setStartTime(starTime);
        newTrainingInstance.setEndTime(endTime);
        newTrainingInstance.setTitle(title);
        newTrainingInstance.setPoolId(poolId);
        newTrainingInstance.setAccessToken(accessToken);
        newTrainingInstance.setLastEdited(lastEdited);
        newTrainingInstance.setLastEditedBy(lastEditedBy);
        return newTrainingInstance;
    }

    private TrainingRun generateTrainingRun(LocalDateTime startTime, LocalDateTime endTime, TRState state,
                                            int incorrectAnswerCount, boolean solutionTaken, String SBIRefId, Integer SBIAllocId, boolean phaseAnswered, String previousSBIRefId) {
        TrainingRun newTrainingRun = new TrainingRun();
        newTrainingRun.setStartTime(startTime);
        newTrainingRun.setEndTime(endTime);
        newTrainingRun.setState(state);
        newTrainingRun.setIncorrectAnswerCount(incorrectAnswerCount);
        newTrainingRun.setSolutionTaken(solutionTaken);
        newTrainingRun.setSandboxInstanceRefId(SBIRefId);
        newTrainingRun.setSandboxInstanceAllocationId(SBIAllocId);
        newTrainingRun.setPhaseAnswered(phaseAnswered);
        newTrainingRun.setPreviousSandboxInstanceRefId(previousSBIRefId);
        return newTrainingRun;
    }

    private TrainingDefinitionCreateDTO generateTrainingDefinitionCreateDTO(String title, String description, String[] prerequisites,
                                                                            String[] outcomes, TDState state) {
        TrainingDefinitionCreateDTO trainingDefinitionCreateDTO = new TrainingDefinitionCreateDTO();
        trainingDefinitionCreateDTO.setTitle(title);
        trainingDefinitionCreateDTO.setDescription(description);
        trainingDefinitionCreateDTO.setOutcomes(outcomes);
        trainingDefinitionCreateDTO.setState(state);
        return trainingDefinitionCreateDTO;
    }

    private TrainingDefinitionUpdateDTO generateTrainingDefinitionUpdateDTO(String title, String description, String[] prerequisites,
                                                                            String[] outcomes, TDState state, Long SDRefId) {
        TrainingDefinitionUpdateDTO trainingDefinitionUpdateDTO = new TrainingDefinitionUpdateDTO();
        trainingDefinitionUpdateDTO.setTitle(title);
        trainingDefinitionUpdateDTO.setDescription(description);
        trainingDefinitionUpdateDTO.setOutcomes(outcomes);
        trainingDefinitionUpdateDTO.setState(state);
        return trainingDefinitionUpdateDTO;
    }

    private TrainingInstanceCreateDTO generateTrainingInstanceCreateDTO(LocalDateTime startTime, LocalDateTime endTime,
                                                                        String title, String accessToken) {
        TrainingInstanceCreateDTO trainingInstanceCreateDTO = new TrainingInstanceCreateDTO();
        trainingInstanceCreateDTO.setStartTime(startTime);
        trainingInstanceCreateDTO.setEndTime(endTime);
        trainingInstanceCreateDTO.setTitle(title);
        trainingInstanceCreateDTO.setAccessToken(accessToken);
        return trainingInstanceCreateDTO;
    }

    private TrainingInstanceUpdateDTO generateTrainingInstanceUpdateDTO(LocalDateTime startTime, LocalDateTime endTime,
                                                                        String title, String accessToken) {
        TrainingInstanceUpdateDTO trainingInstanceUpdateDTO = new TrainingInstanceUpdateDTO();
        trainingInstanceUpdateDTO.setStartTime(startTime);
        trainingInstanceUpdateDTO.setEndTime(endTime);
        trainingInstanceUpdateDTO.setTitle(title);
        trainingInstanceUpdateDTO.setAccessToken(accessToken);
        return trainingInstanceUpdateDTO;
    }


    private ImportTrainingDefinitionDTO generateImportTrainingDefinitionDTO(String title, String description, String[] outcomes, TDState state) {
        ImportTrainingDefinitionDTO importTrainingDefinitionDTO = new ImportTrainingDefinitionDTO();
        importTrainingDefinitionDTO.setTitle(title);
        importTrainingDefinitionDTO.setDescription(description);
        importTrainingDefinitionDTO.setOutcomes(outcomes);
        importTrainingDefinitionDTO.setState(state);
        return importTrainingDefinitionDTO;
    }

    private TrainingDefinitionByIdDTO generateTrainingDefinitionByIdDTO(String title, String description, String[] outcomes, TDState state,
                                                                        boolean canBeArchived, long estimatedDuration, LocalDateTime lastEdited) {
        TrainingDefinitionByIdDTO trainingDefinitionByIdDTO = new TrainingDefinitionByIdDTO();
        trainingDefinitionByIdDTO.setTitle(title);
        trainingDefinitionByIdDTO.setDescription(description);
        trainingDefinitionByIdDTO.setOutcomes(outcomes);
        trainingDefinitionByIdDTO.setState(state);
        trainingDefinitionByIdDTO.setCanBeArchived(canBeArchived);
        trainingDefinitionByIdDTO.setEstimatedDuration(estimatedDuration);
        trainingDefinitionByIdDTO.setLastEdited(lastEdited);
        return trainingDefinitionByIdDTO;
    }

    private BasicPhaseInfoDTO generateBasicPhaseInfoDTO(String title, PhaseType phaseType) {
        BasicPhaseInfoDTO basicPhaseInfoDTO = new BasicPhaseInfoDTO();
        basicPhaseInfoDTO.setTitle(title);
        basicPhaseInfoDTO.setPhaseType(phaseType);
        return basicPhaseInfoDTO;
    }

    private TrainingInstanceDTO generateTrainingInstanceDTO(LocalDateTime start, LocalDateTime end, String title,
                                                            String accessToken, Long poolId) {
        TrainingInstanceDTO trainingInstanceDTO = new TrainingInstanceDTO();
        trainingInstanceDTO.setStartTime(start);
        trainingInstanceDTO.setEndTime(end);
        trainingInstanceDTO.setTitle(title);
        trainingInstanceDTO.setAccessToken(accessToken);
        trainingInstanceDTO.setPoolId(poolId);
        return trainingInstanceDTO;
    }

    private TrainingRunByIdDTO generateTrainingRunByIdDTO(LocalDateTime start, LocalDateTime end, TRState state, String SBIId) {
        TrainingRunByIdDTO trainingRunByIdDTO = new TrainingRunByIdDTO();
        trainingRunByIdDTO.setStartTime(start);
        trainingRunByIdDTO.setEndTime(end);
        trainingRunByIdDTO.setState(state);
        trainingRunByIdDTO.setSandboxInstanceRefId(SBIId);
        return trainingRunByIdDTO;
    }

    private TrainingRunDTO generateTrainingRunDTO(LocalDateTime start, LocalDateTime end, TRState state, String SBIRefId, Integer SBIAllocId) {
        TrainingRunDTO trainingRunDTO = new TrainingRunDTO();
        trainingRunDTO.setStartTime(start);
        trainingRunDTO.setEndTime(end);
        trainingRunDTO.setState(state);
        trainingRunDTO.setSandboxInstanceRefId(SBIRefId);
        trainingRunDTO.setSandboxInstanceAllocationId(SBIAllocId);
        return trainingRunDTO;
    }

//    private AccessedTrainingRunDTO generateAccessedTrainingRunDTO(String title, LocalDateTime start, LocalDateTime end, int currentLevelOrder,
//                                                                  int numberOfLevels, Actions possibleAction){
//        AccessedTrainingRunDTO accessedTrainingRunDTO = new AccessedTrainingRunDTO();
//        accessedTrainingRunDTO.setTitle(title);
//        accessedTrainingRunDTO.setTrainingInstanceStartDate(start);
//        accessedTrainingRunDTO.setTrainingInstanceEndDate(end);
//        accessedTrainingRunDTO.setCurrentLevelOrder(currentLevelOrder);
//        accessedTrainingRunDTO.setNumberOfLevels(numberOfLevels);
//        accessedTrainingRunDTO.setPossibleAction(possibleAction);
//        return accessedTrainingRunDTO;
//    }

    private TrainingInstanceArchiveDTO generateTrainingInstanceArchiveDTO(LocalDateTime start, LocalDateTime end, String title, String accessToken) {
        TrainingInstanceArchiveDTO trainingInstanceArchiveDTO = new TrainingInstanceArchiveDTO();
        trainingInstanceArchiveDTO.setStartTime(start);
        trainingInstanceArchiveDTO.setEndTime(end);
        trainingInstanceArchiveDTO.setTitle(title);
        trainingInstanceArchiveDTO.setAccessToken(accessToken);
        return trainingInstanceArchiveDTO;
    }

    private PoolInfoDTO generatePoolInfoDTO(Long id, Long definitionId, Long lockId, Long maxSize, Long size, String sha, String revSha) {
        PoolInfoDTO poolInfoDTO = new PoolInfoDTO();
        poolInfoDTO.setId(id);
        poolInfoDTO.setDefinitionId(definitionId);
        poolInfoDTO.setLockId(lockId);
        poolInfoDTO.setMaxSize(maxSize);
        poolInfoDTO.setSize(size);
        poolInfoDTO.setRev(sha);
        poolInfoDTO.setRevSha(revSha);
        return poolInfoDTO;
    }

    private SandboxInfo generateSandboxInfo(String id, Integer lockId, Integer allocationUnit) {
        SandboxInfo sandboxInfo = new SandboxInfo();
        sandboxInfo.setId(id);
        sandboxInfo.setAllocationUnitId(allocationUnit);
        sandboxInfo.setLockId(lockId);
        return sandboxInfo;
    }

    private SandboxPoolInfo generateSandboxPoolInfo(Long id, Long definitionId, Long maxSize, Long size) {
        SandboxPoolInfo sandboxPoolInfo = new SandboxPoolInfo();
        sandboxPoolInfo.setId(id);
        sandboxPoolInfo.setDefinitionId(definitionId);
        sandboxPoolInfo.setMaxSize(maxSize);
        sandboxPoolInfo.setSize(size);
        return sandboxPoolInfo;
    }

    private LockedPoolInfo generateLockedPoolInfo(Long id, Long poolId) {
        LockedPoolInfo lockedPoolInfo = new LockedPoolInfo();
        lockedPoolInfo.setId(id);
        lockedPoolInfo.setPoolId(poolId);
        return lockedPoolInfo;
    }

    private User generateUser(Long userRefId) {
        User user = new User();
        user.setUserRefId(userRefId);
        return user;
    }

    private UserRefDTO generateUserRefDTO(Long userRefId, String fullName, String familyName, String givenName, String sub, String iss, byte[] picture) {
        UserRefDTO userRefDTO = new UserRefDTO();
        userRefDTO.setUserRefId(userRefId);
        userRefDTO.setUserRefFullName(fullName);
        userRefDTO.setUserRefFamilyName(familyName);
        userRefDTO.setUserRefGivenName(givenName);
        userRefDTO.setUserRefSub(sub);
        userRefDTO.setIss(iss);
        userRefDTO.setPicture(picture);
        return userRefDTO;
    }

    private <T> T clone(Object object, Class<T> tClass) {
        try {
            mapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
            String json = mapper.writeValueAsString(object);
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
