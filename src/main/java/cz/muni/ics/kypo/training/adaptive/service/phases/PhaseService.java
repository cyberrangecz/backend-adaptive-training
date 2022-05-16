package cz.muni.ics.kypo.training.adaptive.service.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.AccessPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.InfoPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.QuestionnairePhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.Question;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionChoice;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionType;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionnaireType;
import cz.muni.ics.kypo.training.adaptive.enums.TDState;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityConflictException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.repository.phases.AbstractPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.service.training.TrainingDefinitionService;
import cz.muni.ics.kypo.training.adaptive.startup.DefaultPhasesLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PhaseService {

    public static final String PHASE_NOT_FOUND = "Phase not found.";

    private final AbstractPhaseRepository abstractPhaseRepository;
    private final TrainingPhaseService trainingPhaseService;
    private final TrainingDefinitionService trainingDefinitionService;
    private final DefaultPhasesLoader defaultPhasesLoader;

    @Autowired
    public PhaseService(AbstractPhaseRepository abstractPhaseRepository,
                        TrainingPhaseService trainingPhaseService,
                        TrainingDefinitionService trainingDefinitionService,
                        DefaultPhasesLoader defaultPhasesLoader) {
        this.abstractPhaseRepository = abstractPhaseRepository;
        this.trainingPhaseService = trainingPhaseService;
        this.trainingDefinitionService = trainingDefinitionService;
        this.defaultPhasesLoader = defaultPhasesLoader;
    }

    /**
     * Deletes specific phase based on id
     *
     * @param phaseId - id of phase to be deleted
     * @return training definition from which the phase has been deleted.
     * @throws EntityNotFoundException training definition or phase is not found.
     * @throws EntityConflictException phase cannot be deleted in released or archived training definition.
     */
    public TrainingDefinition deletePhase(Long phaseId) {
        AbstractPhase phaseToDelete = this.getPhase(phaseId);
        TrainingDefinition trainingDefinition = phaseToDelete.getTrainingDefinition();
        if (!trainingDefinition.getState().equals(TDState.UNRELEASED)) {
            throw new EntityConflictException(new EntityErrorDetail(AbstractPhase.class, "id", trainingDefinition.getId().getClass(), trainingDefinition.getId(), TrainingDefinitionService.ARCHIVED_OR_RELEASED));
        }
        if (phaseToDelete instanceof TrainingPhase) {
            trainingDefinition.setEstimatedDuration(trainingDefinition.getEstimatedDuration() - ((TrainingPhase) phaseToDelete).getEstimatedDuration());
        }
        abstractPhaseRepository.delete(phaseToDelete);
        int phaseOrder = phaseToDelete.getOrder();
        abstractPhaseRepository.decreaseOrderAfterPhaseWasDeleted(trainingDefinition.getId(), phaseOrder);
        return trainingDefinition;
    }

    public AbstractPhase getPhase(Long phaseId) {
        return abstractPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(AbstractPhase.class, "id", phaseId.getClass(), phaseId, PHASE_NOT_FOUND)));
    }

    public List<AbstractPhase> getPhases(Long trainingDefinitionId) {
        return abstractPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingDefinitionId);
    }

    /**
     * Move phase to the different position and modify orders of phase between moved phase and new position.
     *
     * @param phaseIdFrom - id of the phase to be moved to the new position
     * @param toOrder     - position where phase will be moved
     * @return phase that has been moved to the specific position
     * @throws EntityNotFoundException training definition or one of the phase is not found.
     * @throws EntityConflictException released or archived training definition cannot be modified.
     */
    public AbstractPhase movePhaseToSpecifiedOrder(Long phaseIdFrom, int toOrder) {
        AbstractPhase phaseFrom = abstractPhaseRepository.findById(phaseIdFrom)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(AbstractPhase.class, "id", phaseIdFrom.getClass(), phaseIdFrom, PHASE_NOT_FOUND)));
        toOrder = getCorrectToOrder(phaseFrom.getTrainingDefinition().getId(), toOrder);
        int fromOrder = phaseFrom.getOrder();
        int trainingPhaseFromOrder = 0;
        int trainingPhaseToOrder = 0;
        if (phaseFrom instanceof TrainingPhase) {
            List<AbstractPhase> abstractPhases = getPhases(phaseFrom.getTrainingDefinition().getId());
            trainingPhaseFromOrder = this.getTrainingPhaseOrder(fromOrder, abstractPhases, false);
            trainingPhaseToOrder = this.getTrainingPhaseOrder(toOrder, abstractPhases, fromOrder < toOrder);
        }

        if (fromOrder == toOrder) {
            return phaseFrom;
        } else if (fromOrder > toOrder) {
            abstractPhaseRepository.increaseOrderOfPhasesOnInterval(phaseFrom.getTrainingDefinition().getId(), toOrder, fromOrder);
        } else {
            abstractPhaseRepository.decreaseOrderOfPhasesOnInterval(phaseFrom.getTrainingDefinition().getId(), fromOrder, toOrder);
        }
        phaseFrom.setOrder(toOrder);
        trainingPhaseService.alignDecisionMatrixOfTrainingPhasesAfterMove(phaseFrom.getTrainingDefinition().getId(), trainingPhaseFromOrder, trainingPhaseToOrder);
        return abstractPhaseRepository.save(phaseFrom);
    }

    private int getCorrectToOrder(Long trainingDefinitionId, int order) {
        Integer maxOrderOfPhase = abstractPhaseRepository.getCurrentMaxOrder(trainingDefinitionId);
        if (order < 0) {
            order = 0;
        } else if (order > maxOrderOfPhase) {
            order = maxOrderOfPhase;
        }
        return order;
    }

    private int getTrainingPhaseOrder(int phaseOrder, List<AbstractPhase> abstractPhases, boolean lowerToUpper) {
        int trainingPhaseCounter = 0;
        for (AbstractPhase abstractPhase : abstractPhases) {
            if (abstractPhase.getOrder() == phaseOrder) {
                return !lowerToUpper || abstractPhase instanceof TrainingPhase ? trainingPhaseCounter : trainingPhaseCounter - 1;
            }
            if (abstractPhase instanceof TrainingPhase) {
                trainingPhaseCounter++;
            }
        }
        return -1;
    }

    public void createPredefinedPhases(Long definitionId) {
        TrainingDefinition trainingDefinition = trainingDefinitionService.findById(definitionId);

        InfoPhase infoPhase = new InfoPhase();
        infoPhase.setOrder(0);
        infoPhase.setTitle(defaultPhasesLoader.getDefaultInfoPhase().getTitle());
        infoPhase.setContent(defaultPhasesLoader.getDefaultInfoPhase().getContent());
        infoPhase.setTrainingDefinition(trainingDefinition);
        abstractPhaseRepository.save(infoPhase);

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
        abstractPhaseRepository.save(questionnairePhase);

        AccessPhase accessPhase = new AccessPhase();
        accessPhase.setOrder(2);
        accessPhase.setTitle(defaultPhasesLoader.getDefaultAccessPhase().getTitle());
        accessPhase.setPasskey(defaultPhasesLoader.getDefaultAccessPhase().getPasskey());
        accessPhase.setLocalContent(defaultPhasesLoader.getDefaultAccessPhase().getLocalContent());
        accessPhase.setCloudContent(defaultPhasesLoader.getDefaultAccessPhase().getCloudContent());
        accessPhase.setTrainingDefinition(trainingDefinition);
        abstractPhaseRepository.save(accessPhase);
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
