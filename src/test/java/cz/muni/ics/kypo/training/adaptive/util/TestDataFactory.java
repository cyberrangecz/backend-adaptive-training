package cz.muni.ics.kypo.training.adaptive.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.muni.ics.kypo.training.adaptive.converter.LocalDateTimeUTCSerializer;
import cz.muni.ics.kypo.training.adaptive.domain.AccessToken;
import cz.muni.ics.kypo.training.adaptive.domain.User;
import cz.muni.ics.kypo.training.adaptive.domain.phase.InfoPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.QuestionnairePhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.Task;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.Question;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionChoice;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.dto.BasicPhaseInfoDTO;
import cz.muni.ics.kypo.training.adaptive.dto.UserRefDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.training.TrainingInstanceArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.ImportTrainingDefinitionDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionAnswerDTO;
import cz.muni.ics.kypo.training.adaptive.dto.responses.LockedPoolInfo;
import cz.muni.ics.kypo.training.adaptive.dto.responses.PoolInfoDTO;
import cz.muni.ics.kypo.training.adaptive.dto.responses.SandboxInfo;
import cz.muni.ics.kypo.training.adaptive.dto.responses.SandboxPoolInfo;
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
    private ObjectMapper mapper = new ObjectMapper().registerModule( new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(simpleModule)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private QuestionnairePhase general = generateQuestionnairePhase("Test", 50, QuestionnaireType.GENERAL);
    private QuestionnairePhase adaptive = generateQuestionnairePhase("Questionnaire", 0, QuestionnaireType.ADAPTIVE);

    private Question freeFormQuestion = generateQuestion("Free form question", 0, QuestionType.FFQ);
    private Question multipleChoiceQuestion = generateQuestion("Multiple choice question", 1, QuestionType.MCQ);
    private Question ratingFormQuestion = generateQuestion("Rating form question", 0, QuestionType.RFQ);

    private QuestionChoice correctQuestionChoice = generateQuestionChoice(CORRECT_QUESTION_CHOICE, 0, true);
    private QuestionChoice incorrectQuestionChoice = generateQuestionChoice(WRONG_QUESTION_CHOICE, 1, false);
    private QuestionChoice anotherCorrectQuestionChoice = generateQuestionChoice(ANOTHER_CORRECT_QUESTION_CHOICE, 2, true);

    private TrainingPhase trainingPhase1 = generateTrainingPhase("First Game Level", 100, 0, 3, 5);
    private TrainingPhase trainingPhase2 = generateTrainingPhase("Second Game Level", 100, 1, 3, 5);
    private TrainingPhase trainingPhase3 = generateTrainingPhase("Third Game Level", 100, 2, 3, 5);

    public Task getTask11() { return clone(task11, Task.class);}
    public Task getTask12() { return clone(task12, Task.class);}
    public Task getTask13() { return clone(task13, Task.class);}
    public Task getTask21() { return clone(task21, Task.class);}
    public Task getTask22() { return clone(task22, Task.class);}
    public Task getTask23() { return clone(task23, Task.class);}
    public Task getTask31() { return clone(task31, Task.class);}
    public Task getTask32() { return clone(task32, Task.class);}
    public Task getTask33() { return clone(task33, Task.class);}

    private InfoPhase infoPhase1 = generateInfoPhase("Info phase 1", 7,  "Information");
    private InfoPhase infoPhase2 = generateInfoPhase("Info phase 2", 9,  "Content");

    private BasicPhaseInfoDTO basicTrainingPhaseInfoDTO = generateBasicPhaseInfoDTO("Basic Game phase info", PhaseType.TRAINING);
    private BasicPhaseInfoDTO basicInfoPhaseInfoDTO = generateBasicPhaseInfoDTO("Basic Info phase info", PhaseType.INFO);

    private AccessToken accessToken1 = generateAccessToken("test-0000");
    private AccessToken accessToken2 = generateAccessToken("token-9999");

    private TrainingDefinition unreleasedDefinition = generateTrainingDefinition("Unreleased definition", "Unreleased description",
            new String[]{"p1", "p2"}, new String[]{"o1", "o2"}, TDState.UNRELEASED, true,
            LocalDateTime.now(Clock.systemUTC()).minusHours(1));
    private TrainingDefinition releasedDefinition = generateTrainingDefinition("Released definition", "Released description",
            new String[]{"p3", "p4"}, new String[]{"o3"}, TDState.RELEASED, true,
            LocalDateTime.now(Clock.systemUTC()).minusHours(5));
    private TrainingDefinition archivedDefinition = generateTrainingDefinition("Archived definition", "Archived description",
            new String[]{"p5"}, new String[]{"o4", "o5", "o6"}, TDState.ARCHIVED, false,
            LocalDateTime.now(Clock.systemUTC()).minusHours(10));
    private TrainingDefinitionDTO unreleasedDefinitionDTO = generateTrainingDefinitionDTO(unreleasedDefinition);
    private TrainingDefinitionDTO releasedDefinitionDTO = generateTrainingDefinitionDTO(releasedDefinition);
    private TrainingDefinitionDTO archivedDefinitionDTO = generateTrainingDefinitionDTO(archivedDefinition);
    private TrainingDefinitionInfoDTO unreleasedDefinitionInfoDTO = generateTrainingDefinitionInfoDTO(unreleasedDefinition);
    private TrainingDefinitionInfoDTO releasedDefinitionInfoDTO = generateTrainingDefinitionInfoDTO(releasedDefinition);
    private TrainingDefinitionInfoDTO archivedDefinitionInfoDTO = generateTrainingDefinitionInfoDTO(archivedDefinition);
    private TrainingDefinitionCreateDTO trainingDefinitionCreateDTO = generateTrainingDefinitionCreateDTO("Training definition create DTO",
            "Creation of definition", new String[]{"p8", "p9"}, new String[]{"o8", "o9"}, TDState.UNRELEASED,
            true);
    private TrainingDefinitionUpdateDTO trainingDefinitionUpdateDTO = generateTrainingDefinitionUpdateDTO("Training definition updaet DTO",
            "Update of definition", new String[]{"p6", "p7"}, new String[]{"o6", "o7"}, TDState.UNRELEASED,
            false, 7L);
    private ImportTrainingDefinitionDTO importTrainingDefinitionDTO = generateImportTrainingDefinitionDTO("Imported definition", "Imported description",
            new String[]{"ip1", "ip2"}, TDState.UNRELEASED, true);
    private TrainingDefinitionByIdDTO trainingDefinitionByIdDTO = generateTrainingDefinitionByIdDTO("TDbyId", "Definition by id",  new String[]{"p8", "p9"},
            TDState.UNRELEASED,false, false,
            20L, LocalDateTime.now(Clock.systemUTC()).minusHours(15));

    private TrainingInstance futureInstance = generateTrainingInstance(LocalDateTime.now(Clock.systemUTC()).plusHours(10),
            LocalDateTime.now(Clock.systemUTC()).plusHours(20), "Future Instance", 1L, "future-1111");
    private TrainingInstance ongoingInstance = generateTrainingInstance(LocalDateTime.now(Clock.systemUTC()).minusHours(10),
            LocalDateTime.now(Clock.systemUTC()).plusHours(10), "Ongoing Instance", 2L, "ongoing-2222");
    private TrainingInstance concludedInstance = generateTrainingInstance(LocalDateTime.now(Clock.systemUTC()).minusHours(20),
            LocalDateTime.now(Clock.systemUTC()).minusHours(5), "Concluded Instance", 3L, "concluded-3333");
    private TrainingInstanceCreateDTO trainingInstanceCreateDTO = generateTrainingInstanceCreateDTO(LocalDateTime.now(Clock.systemUTC()).plusHours(15),
            LocalDateTime.now(Clock.systemUTC()).plusHours(22), "Create Instance", "create");
    private TrainingInstanceUpdateDTO trainingInstanceUpdateDTO = generateTrainingInstanceUpdateDTO(LocalDateTime.now(Clock.systemUTC()).plusHours(5),
            LocalDateTime.now(Clock.systemUTC()).plusHours(7), "Update Instance", "update");
    private TrainingInstanceDTO trainingInstanceDTO = generateTrainingInstanceDTO(LocalDateTime.now(Clock.systemUTC()).plusHours(11),
            LocalDateTime.now(Clock.systemUTC()).plusHours(22), "Instance DTO", "DTO-5555", 10L);
    private TrainingInstanceArchiveDTO trainingInstanceArchiveDTO = generateTrainingInstanceArchiveDTO(LocalDateTime.now(Clock.systemUTC()).minusHours(20),
            LocalDateTime.now(Clock.systemUTC()).minusHours(10), "Archived instance", "archived-6666");

    private TrainingRun runningRun = generateTrainingRun(LocalDateTime.now(Clock.systemUTC()).minusHours(2), LocalDateTime.now(Clock.systemUTC()).plusHours(2),
            TRState.RUNNING, 2, true, 1L, true,
            20L);
    private TrainingRun finishedRun = generateTrainingRun(LocalDateTime.now(Clock.systemUTC()).minusHours(10), LocalDateTime.now(Clock.systemUTC()).minusHours(5),
            TRState.FINISHED, 4, false, 3L, true, 30L);
    private TrainingRun archivedRun = generateTrainingRun(LocalDateTime.now(Clock.systemUTC()).minusHours(20), LocalDateTime.now(Clock.systemUTC()).minusHours(10),
            TRState.ARCHIVED, 0, false, 5L, false, 60L);
    private TrainingRunByIdDTO trainingRunByIdDTO = generateTrainingRunByIdDTO(LocalDateTime.now(Clock.systemUTC()).minusHours(2), LocalDateTime.now(Clock.systemUTC()).plusHours(2),
             TRState.RUNNING, 5L);
    private TrainingRunDTO trainingRunDTO = generateTrainingRunDTO(LocalDateTime.now(Clock.systemUTC()).minusHours(9), LocalDateTime.now(Clock.systemUTC()).minusHours(5),
             TRState.FINISHED, 7L);
//    private AccessedTrainingRunDTO accessedTrainingRunDTO = generateAccessedTrainingRunDTO("Accessed run", LocalDateTime.now(Clock.systemUTC()).minusHours(8), LocalDateTime.now(Clock.systemUTC()).minusHours(4), 5,
//            6, Actions.RESUME);

    private PoolInfoDTO poolInfoDTO = generatePoolInfoDTO(1L, 1L, 5L, 10L, 5L, "sha", "revSha");
    private SandboxInfo sandboxInfo = generateSandboxInfo(1L, 1, 4);
    private SandboxPoolInfo sandboxPoolInfo = generateSandboxPoolInfo(1L, 1L, 10L, 5L);
    private LockedPoolInfo lockedPoolInfo = generateLockedPoolInfo(1L, 1L);

    private UserRefDTO userRefDTO1 = generateUserRefDTO(10L, "Michael Bolt", "Bolt", "Michael", "mail1@muni.cz", "https://oidc.muni.cz/oidc", null);
    private UserRefDTO userRefDTO2 = generateUserRefDTO(12L, "Peter Most", "Most", "Peter", "mail2@muni.cz", "https://oidc.muni.cz/oidc", null);
    private UserRefDTO userRefDTO3 = generateUserRefDTO(14L, "John Nevel", "Nevel", "John", "mail38@muni.cz", "https://oidc.muni.cz/oidc", null);
    private UserRefDTO userRefDTO4 = generateUserRefDTO(17L, "Ted Mosby", "Mosby", "Ted", "mail4@muni.cz", "https://oidc.muni.cz/oidc", null);

    private User user1 = generateUser( 10L);
    private User user2 = generateUser(12L);
    private User user3 = generateUser(14L);
    private User user4 = generateUser(17L);

    public QuestionnairePhase getGeneral(){
        return clone(general, QuestionnairePhase.class);
    }

    public QuestionnairePhase getAdaptive(){
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

    public TrainingPhase getTrainingPhase1(){
        return clone(trainingPhase1, TrainingPhase.class);
    }
    public TrainingPhase getTrainingPhase2(){
        return clone(trainingPhase2, TrainingPhase.class);
    }
    public TrainingPhase getTrainingPhase3(){
        return clone(trainingPhase3, TrainingPhase.class);
    }

    private Task task11 = generateTask("Task11", "Content of task11", 0, "answer11", 2, true, 20);
    private Task task12 = generateTask("Task12", "Content of task12", 1, "answer12", 5, false, 15);
    private Task task13 = generateTask("Task13", "Content of task13", 2, "answer13", 4, true, 10);
    private Task task21 = generateTask("Task21", "Content of task21", 0, "answer21", 2, true, 35);
    private Task task22 = generateTask("Task22", "Content of task22", 1, "answer22", 3, false, 22);
    private Task task23 = generateTask("Task23", "Content of task23", 2, "answer23", 5, false, 50);
    private Task task31 = generateTask("Task31", "Content of task31", 0, "answer31", 9, true, 60);
    private Task task32 = generateTask("Task32", "Content of task32", 1, "answer32", 5, false, 40);
    private Task task33 = generateTask("Task33", "Content of task33", 2, "answer33", 3, true, 20);

    public InfoPhase getInfoLevel1(){
        return clone(infoPhase1, InfoPhase.class);
    }

    public InfoPhase getInfoLevel2(){
        return clone(infoPhase2 , InfoPhase.class);
    }

    public AccessToken getAccessToken1(){
        return clone(accessToken1, AccessToken.class);
    }

    public AccessToken getAccessToken2(){
        return clone(accessToken2, AccessToken.class);
    }

    public TrainingDefinition getUnreleasedDefinition(){
        return clone(unreleasedDefinition, TrainingDefinition.class);
    }

    public TrainingDefinition getReleasedDefinition(){
        return clone(releasedDefinition, TrainingDefinition.class);
    }

    public TrainingDefinition getArchivedDefinition() {
        return clone(archivedDefinition, TrainingDefinition.class);
    }

    public TrainingDefinitionDTO getUnreleasedDefinitionDTO(){
        return clone(unreleasedDefinitionDTO, TrainingDefinitionDTO.class);
    }

    public TrainingDefinitionDTO getReleasedDefinitionDTO(){
        return clone(releasedDefinitionDTO, TrainingDefinitionDTO.class);
    }

    public TrainingDefinitionDTO getArchivedDefinitionDTO(){
        return clone(archivedDefinitionDTO, TrainingDefinitionDTO.class);
    }

    public TrainingDefinitionInfoDTO getUnreleasedDefinitionInfoDTO(){
        return clone(unreleasedDefinitionInfoDTO, TrainingDefinitionInfoDTO.class);
    }

    public TrainingDefinitionInfoDTO getReleasedDefinitionInfoDTO(){
        return clone(releasedDefinitionInfoDTO, TrainingDefinitionInfoDTO.class);
    }

    public TrainingDefinitionInfoDTO getArchivedDefinitionInfoDTO(){
        return clone(archivedDefinitionInfoDTO, TrainingDefinitionInfoDTO.class);
    }

    public TrainingInstance getFutureInstance(){
        return clone(futureInstance, TrainingInstance.class);
    }

    public TrainingInstance getOngoingInstance(){
        return clone(ongoingInstance, TrainingInstance.class);
    }

    public TrainingInstance getConcludedInstance(){
        return clone(concludedInstance, TrainingInstance.class);
    }

    public TrainingRun getRunningRun(){
        return clone(runningRun, TrainingRun.class);
    }

    public TrainingRun getFinishedRun(){
        return clone(finishedRun, TrainingRun.class);
    }

    public TrainingRun getArchivedRun(){
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

    public TrainingRunByIdDTO getTrainingRunByIdDTO(){
        return clone(trainingRunByIdDTO, TrainingRunByIdDTO.class);
    }


    public TrainingRunDTO getTrainingRunDTO(){
        return clone(trainingRunDTO, TrainingRunDTO.class);
    }

    public ImportTrainingDefinitionDTO getImportTrainingDefinitionDTO(){
        return clone(importTrainingDefinitionDTO, ImportTrainingDefinitionDTO.class);
    }

    public TrainingDefinitionByIdDTO getTrainingDefinitionByIdDTO(){
        return clone(trainingDefinitionByIdDTO, TrainingDefinitionByIdDTO.class);
    }

    public TrainingInstanceDTO getTrainingInstanceDTO(){
        return clone(trainingInstanceDTO, TrainingInstanceDTO.class);
    }

//    public AccessedTrainingRunDTO getAccessedTrainingRunDTO(){
//        return clone(accessedTrainingRunDTO, AccessedTrainingRunDTO.class);
//    }

    public TrainingInstanceArchiveDTO getTrainingInstanceArchiveDTO(){
        return clone(trainingInstanceArchiveDTO, TrainingInstanceArchiveDTO.class);
    }

    public PoolInfoDTO getPoolInfoDTO(){
        return clone(poolInfoDTO, PoolInfoDTO.class);
    }

    public SandboxInfo getSandboxInfo(){
        return clone(sandboxInfo, SandboxInfo.class);
    }

    public SandboxPoolInfo getSandboxPoolInfo(){
        return clone(sandboxPoolInfo, SandboxPoolInfo.class);
    }

    public LockedPoolInfo getLockedPoolInfo(){
        return clone(lockedPoolInfo, LockedPoolInfo.class);
    }

    public UserRefDTO getUserRefDTO1() { return clone(userRefDTO1, UserRefDTO.class);}
    public UserRefDTO getUserRefDTO2() { return clone(userRefDTO2, UserRefDTO.class);}
    public UserRefDTO getUserRefDTO3() { return clone(userRefDTO3, UserRefDTO.class);}
    public UserRefDTO getUserRefDTO4() { return clone(userRefDTO4, UserRefDTO.class);}


    public User getUser1() { return clone(user1, User.class);}
    public User getUser2() { return clone(user2, User.class);}
    public User getUser3() { return clone(user3, User.class);}
    public User getUser4() { return clone(user4, User.class);}

    private QuestionnairePhase generateQuestionnairePhase(String title, int order, QuestionnaireType questionnaireType){
        QuestionnairePhase newQuestionnairePhase = new QuestionnairePhase();
        newQuestionnairePhase.setTitle(title);
        newQuestionnairePhase.setOrder(order);
        newQuestionnairePhase.setQuestionnaireType(questionnaireType);
        return newQuestionnairePhase;
    }

    private TrainingPhase generateTrainingPhase(String title, int estimatedDuration, int order, int allowedCommands, int allowedWrongAnswers){
        TrainingPhase newTrainingPhase = new TrainingPhase();
        newTrainingPhase.setTitle(title);
        newTrainingPhase.setEstimatedDuration(estimatedDuration);
        newTrainingPhase.setOrder(order);
        newTrainingPhase.setAllowedCommands(allowedCommands);
        newTrainingPhase.setAllowedWrongAnswers(allowedWrongAnswers);
        return newTrainingPhase;
    }

    private InfoPhase generateInfoPhase(String title, int order, String content){
        InfoPhase newInfoPhase = new InfoPhase();
        newInfoPhase.setTitle(title);
        newInfoPhase.setOrder(order);
        newInfoPhase.setContent(content);
        return newInfoPhase;
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

    private AccessToken generateAccessToken(String accessToken){
        AccessToken newAccessToken = new AccessToken();
        newAccessToken.setAccessToken(accessToken);
        return newAccessToken;
    }

    private Task generateTask(String title, String content, Integer order, String answer, int incorrectAnswerLimit, boolean modifySandbox, int sandboxChangeExpectedDuration){
        Task newTask = new Task();
        newTask.setTitle(title);
        newTask.setContent(content);
        newTask.setOrder(order);
        newTask.setAnswer(answer);
        newTask.setIncorrectAnswerLimit(incorrectAnswerLimit);
        newTask.setModifySandbox(modifySandbox);
        newTask.setSandboxChangeExpectedDuration(sandboxChangeExpectedDuration);
        return newTask;
    }

    private TrainingDefinition generateTrainingDefinition(String title, String description, String[] prerequisites,
                                                          String[] outcomes, TDState state, boolean showStepperBar,
                                                          LocalDateTime lastEdited){
        TrainingDefinition newTrainingDefinition = new TrainingDefinition();
        newTrainingDefinition.setTitle(title);
        newTrainingDefinition.setDescription(description);
        newTrainingDefinition.setOutcomes(outcomes);
        newTrainingDefinition.setState(state);
        newTrainingDefinition.setShowStepperBar(showStepperBar);
        newTrainingDefinition.setLastEdited(lastEdited);
        return newTrainingDefinition;
    }

    private TrainingDefinitionDTO generateTrainingDefinitionDTO(TrainingDefinition trainingDefinition){
        TrainingDefinitionDTO trainingDefinitionDTO = new TrainingDefinitionDTO();
        trainingDefinitionDTO.setTitle(trainingDefinition.getTitle());
        trainingDefinitionDTO.setDescription(trainingDefinition.getDescription());
        trainingDefinitionDTO.setOutcomes(trainingDefinition.getOutcomes());
        trainingDefinitionDTO.setState(trainingDefinition.getState());
        trainingDefinitionDTO.setShowStepperBar(trainingDefinition.isShowStepperBar());
        trainingDefinitionDTO.setLastEdited(trainingDefinition.getLastEdited());
        return trainingDefinitionDTO;
    }

    private TrainingDefinitionInfoDTO generateTrainingDefinitionInfoDTO(TrainingDefinition trainingDefinition){
        TrainingDefinitionInfoDTO trainingDefinitionInfoDTO = new TrainingDefinitionInfoDTO();
        trainingDefinitionInfoDTO.setTitle(trainingDefinition.getTitle());
        trainingDefinitionInfoDTO.setState(trainingDefinition.getState());
        return trainingDefinitionInfoDTO;
    }

    private TrainingInstance generateTrainingInstance(LocalDateTime starTime, LocalDateTime endTime, String title,
                                                      Long poolId, String accessToken){
        TrainingInstance newTrainingInstance = new TrainingInstance();
        newTrainingInstance.setStartTime(starTime);
        newTrainingInstance.setEndTime(endTime);
        newTrainingInstance.setTitle(title);
        newTrainingInstance.setPoolId(poolId);
        newTrainingInstance.setAccessToken(accessToken);
        return newTrainingInstance;
    }

    private TrainingRun generateTrainingRun(LocalDateTime startTime, LocalDateTime endTime, TRState state,
                                            int incorrectAnswerCount, boolean solutionTaken, Long SBIRefId, boolean phaseAnswered, Long previousSBIRefId){
        TrainingRun newTrainingRun = new TrainingRun();
        newTrainingRun.setStartTime(startTime);
        newTrainingRun.setEndTime(endTime);
        newTrainingRun.setState(state);
        newTrainingRun.setIncorrectAnswerCount(incorrectAnswerCount);
        newTrainingRun.setSolutionTaken(solutionTaken);
        newTrainingRun.setSandboxInstanceRefId(SBIRefId);
        newTrainingRun.setPhaseAnswered(phaseAnswered);
        newTrainingRun.setPreviousSandboxInstanceRefId(previousSBIRefId);
        return newTrainingRun;
    }

    private TrainingDefinitionCreateDTO generateTrainingDefinitionCreateDTO(String title, String description, String[] prerequisites,
                                                                            String[] outcomes, TDState state,
                                                                            boolean showStepperBar){
        TrainingDefinitionCreateDTO trainingDefinitionCreateDTO = new TrainingDefinitionCreateDTO();
        trainingDefinitionCreateDTO.setTitle(title);
        trainingDefinitionCreateDTO.setDescription(description);
        trainingDefinitionCreateDTO.setOutcomes(outcomes);
        trainingDefinitionCreateDTO.setState(state);
        trainingDefinitionCreateDTO.setShowStepperBar(showStepperBar);
        return trainingDefinitionCreateDTO;
    }

    private TrainingDefinitionUpdateDTO generateTrainingDefinitionUpdateDTO(String title, String description, String[] prerequisites,
                                                                            String[] outcomes, TDState state,
                                                                            boolean showStepperBar, Long SDRefId){
        TrainingDefinitionUpdateDTO trainingDefinitionUpdateDTO = new TrainingDefinitionUpdateDTO();
        trainingDefinitionUpdateDTO.setTitle(title);
        trainingDefinitionUpdateDTO.setDescription(description);
        trainingDefinitionUpdateDTO.setOutcomes(outcomes);
        trainingDefinitionUpdateDTO.setState(state);
        trainingDefinitionUpdateDTO.setShowStepperBar(showStepperBar);
        return trainingDefinitionUpdateDTO;
    }

    private TrainingInstanceCreateDTO generateTrainingInstanceCreateDTO(LocalDateTime startTime, LocalDateTime endTime,
                                                                        String title, String accessToken){
        TrainingInstanceCreateDTO trainingInstanceCreateDTO = new TrainingInstanceCreateDTO();
        trainingInstanceCreateDTO.setStartTime(startTime);
        trainingInstanceCreateDTO.setEndTime(endTime);
        trainingInstanceCreateDTO.setTitle(title);
        trainingInstanceCreateDTO.setAccessToken(accessToken);
        return trainingInstanceCreateDTO;
    }

    private TrainingInstanceUpdateDTO generateTrainingInstanceUpdateDTO(LocalDateTime startTime, LocalDateTime endTime,
                                                                        String title, String accessToken){
        TrainingInstanceUpdateDTO trainingInstanceUpdateDTO = new TrainingInstanceUpdateDTO();
        trainingInstanceUpdateDTO.setStartTime(startTime);
        trainingInstanceUpdateDTO.setEndTime(endTime);
        trainingInstanceUpdateDTO.setTitle(title);
        trainingInstanceUpdateDTO.setAccessToken(accessToken);
        return trainingInstanceUpdateDTO;
    }


    private ImportTrainingDefinitionDTO generateImportTrainingDefinitionDTO(String title, String description, String[] outcomes, TDState state,
                                                                            boolean showStepperBar){
        ImportTrainingDefinitionDTO importTrainingDefinitionDTO = new ImportTrainingDefinitionDTO();
        importTrainingDefinitionDTO.setTitle(title);
        importTrainingDefinitionDTO.setDescription(description);
        importTrainingDefinitionDTO.setOutcomes(outcomes);
        importTrainingDefinitionDTO.setState(state);
        importTrainingDefinitionDTO.setShowStepperBar(showStepperBar);
        return importTrainingDefinitionDTO;
    }

    private TrainingDefinitionByIdDTO generateTrainingDefinitionByIdDTO(String title, String description, String[] outcomes, TDState state,
                                                                        boolean showStepperBar, boolean canBeArchived, long estimatedDuration, LocalDateTime lastEdited){
        TrainingDefinitionByIdDTO trainingDefinitionByIdDTO = new TrainingDefinitionByIdDTO();
        trainingDefinitionByIdDTO.setTitle(title);
        trainingDefinitionByIdDTO.setDescription(description);
        trainingDefinitionByIdDTO.setOutcomes(outcomes);
        trainingDefinitionByIdDTO.setState(state);
        trainingDefinitionByIdDTO.setShowStepperBar(showStepperBar);
        trainingDefinitionByIdDTO.setCanBeArchived(canBeArchived);
        trainingDefinitionByIdDTO.setEstimatedDuration(estimatedDuration);
        trainingDefinitionByIdDTO.setLastEdited(lastEdited);
        return trainingDefinitionByIdDTO;
    }

    private BasicPhaseInfoDTO generateBasicPhaseInfoDTO(String title, PhaseType phaseType){
        BasicPhaseInfoDTO basicPhaseInfoDTO = new BasicPhaseInfoDTO();
        basicPhaseInfoDTO.setTitle(title);
        basicPhaseInfoDTO.setPhaseType(phaseType);
        return basicPhaseInfoDTO;
    }

    private TrainingInstanceDTO generateTrainingInstanceDTO(LocalDateTime start, LocalDateTime end, String title,
                                                            String accessToken, Long poolId){
        TrainingInstanceDTO trainingInstanceDTO = new TrainingInstanceDTO();
        trainingInstanceDTO.setStartTime(start);
        trainingInstanceDTO.setEndTime(end);
        trainingInstanceDTO.setTitle(title);
        trainingInstanceDTO.setAccessToken(accessToken);
        trainingInstanceDTO.setPoolId(poolId);
        return trainingInstanceDTO;
    }

    private TrainingRunByIdDTO generateTrainingRunByIdDTO(LocalDateTime start, LocalDateTime end, TRState state, Long SBIId){
        TrainingRunByIdDTO trainingRunByIdDTO = new TrainingRunByIdDTO();
        trainingRunByIdDTO.setStartTime(start);
        trainingRunByIdDTO.setEndTime(end);
        trainingRunByIdDTO.setState(state);
        trainingRunByIdDTO.setSandboxInstanceRefId(SBIId);
        return trainingRunByIdDTO;
    }

    private TrainingRunDTO generateTrainingRunDTO(LocalDateTime start, LocalDateTime end, TRState state, Long SBIId){
        TrainingRunDTO trainingRunDTO = new TrainingRunDTO();
        trainingRunDTO.setStartTime(start);
        trainingRunDTO.setEndTime(end);
        trainingRunDTO.setState(state);
        trainingRunDTO.setSandboxInstanceRefId(SBIId);
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

    private TrainingInstanceArchiveDTO generateTrainingInstanceArchiveDTO(LocalDateTime start, LocalDateTime end, String title, String accessToken){
        TrainingInstanceArchiveDTO trainingInstanceArchiveDTO = new TrainingInstanceArchiveDTO();
        trainingInstanceArchiveDTO.setStartTime(start);
        trainingInstanceArchiveDTO.setEndTime(end);
        trainingInstanceArchiveDTO.setTitle(title);
        trainingInstanceArchiveDTO.setAccessToken(accessToken);
        return trainingInstanceArchiveDTO;
    }

    private PoolInfoDTO generatePoolInfoDTO(Long id, Long definitionId, Long lockId, Long maxSize, Long size, String sha, String revSha){
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

    private SandboxInfo generateSandboxInfo(Long id, Integer lockId, Integer allocationUnit){
        SandboxInfo sandboxInfo = new SandboxInfo();
        sandboxInfo.setId(id);
        sandboxInfo.setAllocationUnitId(allocationUnit);
        sandboxInfo.setLockId(lockId);
        return sandboxInfo;
    }

    private SandboxPoolInfo generateSandboxPoolInfo(Long id, Long definitionId, Long maxSize, Long size){
        SandboxPoolInfo sandboxPoolInfo = new SandboxPoolInfo();
        sandboxPoolInfo.setId(id);
        sandboxPoolInfo.setDefinitionId(definitionId);
        sandboxPoolInfo.setMaxSize(maxSize);
        sandboxPoolInfo.setSize(size);
        return sandboxPoolInfo;
    }

    private LockedPoolInfo generateLockedPoolInfo(Long id, Long poolId){
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

    private <T> T clone(Object object, Class<T> tClass){
        try {
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            String json = mapper.writeValueAsString(object);
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
