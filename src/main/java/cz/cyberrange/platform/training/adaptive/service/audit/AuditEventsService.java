package cz.cyberrange.platform.training.adaptive.service.audit;

import cz.cyberrange.platform.events.adaptive.trainings.AbstractAuditAdaptivePOJO;
import cz.cyberrange.platform.events.adaptive.trainings.CorrectAnswerSubmitted;
import cz.cyberrange.platform.events.adaptive.trainings.CorrectPasskeySubmitted;
import cz.cyberrange.platform.events.adaptive.trainings.PhaseCompleted;
import cz.cyberrange.platform.events.adaptive.trainings.PhaseStarted;
import cz.cyberrange.platform.events.adaptive.trainings.QuestionnaireAnswers;
import cz.cyberrange.platform.events.adaptive.trainings.SolutionDisplayed;
import cz.cyberrange.platform.events.adaptive.trainings.TrainingRunEnded;
import cz.cyberrange.platform.events.adaptive.trainings.TrainingRunResumed;
import cz.cyberrange.platform.events.adaptive.trainings.TrainingRunStarted;
import cz.cyberrange.platform.events.adaptive.trainings.WrongAnswerSubmitted;
import cz.cyberrange.platform.events.adaptive.trainings.WrongPasskeySubmitted;
import cz.cyberrange.platform.events.adaptive.trainings.enums.PhaseType;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AbstractPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AccessPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.InfoPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.QuestionnairePhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.Task;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.TrainingPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingInstance;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * The type Audit events service.
 */
@Service
public class AuditEventsService {

    private final AuditService auditService;

    /**
     * Instantiates a new Audit events service.
     *
     * @param auditService the audit service
     */
    @Autowired
    public AuditEventsService(AuditService auditService) {
        this.auditService = auditService;
    }


    /**
     * Audit training run started action.
     *
     * @param trainingRun the training run
     */
    public void auditTrainingRunStartedAction(TrainingRun trainingRun) {
        TrainingRunStarted.TrainingRunStartedBuilder<?, ?> trainingRunStartedBuilder = (TrainingRunStarted.TrainingRunStartedBuilder<?, ?>)
                fillInCommonBuilderFields(trainingRun, TrainingRunStarted.builder());

        TrainingRunStarted trainingRunStarted = trainingRunStartedBuilder
                .trainingTime(0L)
                .build();
        auditService.saveTrainingRunEvent(trainingRunStarted, 0L);
    }

    /**
     * Audit phase started action.
     *
     * @param trainingRun the training run
     */
    public void auditPhaseStartedAction(TrainingRun trainingRun) {
        PhaseStarted.PhaseStartedBuilder<?, ?> phaseStartedBuilder = (PhaseStarted.PhaseStartedBuilder<?, ?>)
                fillInCommonBuilderFields(trainingRun, PhaseStarted.builder());

        PhaseStarted phaseStarted = phaseStartedBuilder
                .phaseType(getPhaseType(trainingRun.getCurrentPhase()))
                .phaseTitle(trainingRun.getCurrentPhase().getTitle())
                .build();
        auditService.saveTrainingRunEvent(phaseStarted, 10L);
    }

    /**
     * Audit phase completed action.
     *
     * @param trainingRun the training run
     */
    public void auditPhaseCompletedAction(TrainingRun trainingRun) {
        PhaseCompleted.PhaseCompletedBuilder<?, ?> phaseCompletedBuilder = (PhaseCompleted.PhaseCompletedBuilder<?, ?>)
                fillInCommonBuilderFields(trainingRun, PhaseCompleted.builder());

        PhaseCompleted phaseCompleted = phaseCompletedBuilder
                .phaseType(getPhaseType(trainingRun.getCurrentPhase()))
                .build();
        auditService.saveTrainingRunEvent(phaseCompleted, 5L);
    }

    /**
     * Audit solution displayed action.
     *
     * @param trainingRun the training run
     */
    public void auditSolutionDisplayedAction(TrainingRun trainingRun) {
        SolutionDisplayed.SolutionDisplayedBuilder<?, ?> solutionDisplayedBuilder = (SolutionDisplayed.SolutionDisplayedBuilder<?, ?>)
                fillInCommonBuilderFields(trainingRun, SolutionDisplayed.builder());

        SolutionDisplayed solutionDisplayed = solutionDisplayedBuilder
                .build();
        auditService.saveTrainingRunEvent(solutionDisplayed, 0L);
    }

    /**
     * Audit correct answer submitted action.
     *
     * @param trainingRun the training run
     * @param answer      the answer
     */
    public void auditCorrectAnswerSubmittedAction(TrainingRun trainingRun, String answer) {
        CorrectAnswerSubmitted.CorrectAnswerSubmittedBuilder<?, ?> correctAnswerSubmittedBuilder = (CorrectAnswerSubmitted.CorrectAnswerSubmittedBuilder<?, ?>)
                fillInCommonBuilderFields(trainingRun, CorrectAnswerSubmitted.builder());

        CorrectAnswerSubmitted correctAnswerSubmitted = correctAnswerSubmittedBuilder
                .answerContent(answer)
                .build();
        auditService.saveTrainingRunEvent(correctAnswerSubmitted, 0L);
    }

    /**
     * Audit wrong answer submitted action.
     *
     * @param trainingRun the training run
     * @param answer      the answer
     */
    public void auditWrongAnswerSubmittedAction(TrainingRun trainingRun, String answer) {
        WrongAnswerSubmitted.WrongAnswerSubmittedBuilder<?, ?> wrongAnswerSubmittedBuilder = (WrongAnswerSubmitted.WrongAnswerSubmittedBuilder<?, ?>)
                fillInCommonBuilderFields(trainingRun, WrongAnswerSubmitted.builder());

        WrongAnswerSubmitted wrongAnswerSubmitted = wrongAnswerSubmittedBuilder
                .answerContent(answer)
                .count(trainingRun.getIncorrectAnswerCount())
                .build();
        auditService.saveTrainingRunEvent(wrongAnswerSubmitted, 0L);
    }

    /**
     * Audit correct passkey submitted action.
     *
     * @param trainingRun the training run
     * @param passkey     the passkey
     */
    public void auditCorrectPasskeySubmittedAction(TrainingRun trainingRun, String passkey) {
        CorrectPasskeySubmitted.CorrectPasskeySubmittedBuilder<?, ?> correctPasskeySubmittedBuilder = (CorrectPasskeySubmitted.CorrectPasskeySubmittedBuilder<?, ?>)
                fillInCommonBuilderFields(trainingRun, CorrectPasskeySubmitted.builder());

        CorrectPasskeySubmitted correctPasskeySubmitted = correctPasskeySubmittedBuilder
                .passkeyContent(passkey)
                .build();
        auditService.saveTrainingRunEvent(correctPasskeySubmitted, 0L);
    }

    /**
     * Audit wrong passkey submitted action.
     *
     * @param trainingRun the training run
     * @param passkey      the passkey
     */
    public void auditWrongPasskeySubmittedAction(TrainingRun trainingRun, String passkey) {
        WrongPasskeySubmitted.WrongPasskeySubmittedBuilder<?, ?> wrongPasskeySubmittedBuilder = (WrongPasskeySubmitted.WrongPasskeySubmittedBuilder<?, ?>)
                fillInCommonBuilderFields(trainingRun, WrongPasskeySubmitted.builder());

        WrongPasskeySubmitted wrongPasskeySubmitted = wrongPasskeySubmittedBuilder
                .passkeyContent(passkey)
                .build();
        auditService.saveTrainingRunEvent(wrongPasskeySubmitted, 0L);
    }

    /**
     * Audit questionnaire answers action.
     *
     * @param trainingRun the training run
     * @param answers     the answers
     */
    public void auditQuestionnaireAnswersAction(TrainingRun trainingRun, String answers) {
        QuestionnaireAnswers.QuestionnaireAnswersBuilder<?, ?> questionnaireAnswersBuilder = (QuestionnaireAnswers.QuestionnaireAnswersBuilder<?, ?>)
                fillInCommonBuilderFields(trainingRun, QuestionnaireAnswers.builder());

        QuestionnaireAnswers questionnaireAnswers = questionnaireAnswersBuilder
                .answers(answers)
                .build();
        auditService.saveTrainingRunEvent(questionnaireAnswers, 0L);
    }

    /**
     * Audit training run ended action.
     *
     * @param trainingRun the training run
     */
    public void auditTrainingRunEndedAction(TrainingRun trainingRun) {
        TrainingRunEnded.TrainingRunEndedBuilder<?, ?> trainingRunEndedBuilder = (TrainingRunEnded.TrainingRunEndedBuilder<?, ?>)
                fillInCommonBuilderFields(trainingRun, TrainingRunEnded.builder());

        TrainingRunEnded trainingRunEnded = trainingRunEndedBuilder
                .startTime(trainingRun.getStartTime().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli())
                .endTime(System.currentTimeMillis())
                .build();
        auditService.saveTrainingRunEvent(trainingRunEnded, 10L);
    }

    /**
     * Audit training run resumed action.
     *
     * @param trainingRun the training run
     */
    public void auditTrainingRunResumedAction(TrainingRun trainingRun) {
        TrainingRunResumed.TrainingRunResumedBuilder<?, ?> trainingRunResumedBuilder = (TrainingRunResumed.TrainingRunResumedBuilder<?, ?>)
                fillInCommonBuilderFields(trainingRun, TrainingRunResumed.builder());
        TrainingRunResumed trainingRunResumed = trainingRunResumedBuilder
                .build();
        auditService.saveTrainingRunEvent(trainingRunResumed, 0L);
    }

    private AbstractAuditAdaptivePOJO.AbstractAuditAdaptivePOJOBuilder<?, ?> fillInCommonBuilderFields(TrainingRun trainingRun, AbstractAuditAdaptivePOJO.AbstractAuditAdaptivePOJOBuilder<?, ?> builder) {
        TrainingInstance trainingInstance = trainingRun.getTrainingInstance();
        Task trainingRunTask = trainingRun.getCurrentTask();
        AbstractPhase trainingRunPhase = trainingRun.getCurrentPhase();
        builder.trainingRunId(trainingRun.getId())
                .poolId(trainingInstance.getPoolId())
                .sandboxId(trainingRun.getSandboxInstanceRefId())
                .trainingInstanceId(trainingInstance.getId())
                .trainingDefinitionId(trainingInstance.getTrainingDefinition().getId())
                .trainingTime(computeTrainingTime(trainingRun.getStartTime()))
                .userRefId(trainingRun.getParticipantRef().getUserRefId())
                .phaseId(trainingRunPhase.getId())
                .phaseOrder(trainingRunPhase.getOrder())
                .taskId(trainingRunTask == null ? null : trainingRunTask.getId())
                .taskOrder(trainingRunTask == null ? null : trainingRunTask.getOrder());
        return builder;
    }

    private long computeTrainingTime(LocalDateTime trainingStartedTime) {
        return ChronoUnit.MILLIS.between(trainingStartedTime, LocalDateTime.now(Clock.systemUTC()));
    }

    private PhaseType getPhaseType(AbstractPhase abstractPhase) {
        if (abstractPhase instanceof TrainingPhase) {
            return PhaseType.TRAINING;
        } else if (abstractPhase instanceof InfoPhase) {
            return PhaseType.INFO;
        } else if (abstractPhase instanceof QuestionnairePhase) {
            return PhaseType.QUESTIONNAIRE;
        } else if (abstractPhase instanceof AccessPhase) {
            return PhaseType.ACCESS;
        }
        return null;
    }
}
