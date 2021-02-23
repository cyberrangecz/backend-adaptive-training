package cz.muni.ics.kypo.training.adaptive.facade;

import cz.muni.ics.kypo.training.adaptive.domain.QuestionAnswer;
import cz.muni.ics.kypo.training.adaptive.dto.run.QuestionnairePhaseAnswersDTO;
import cz.muni.ics.kypo.training.adaptive.service.QuestionnaireEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionnaireEvaluationFacade {

    private final QuestionnaireEvaluationService questionnaireEvaluationService;

    @Autowired
    public QuestionnaireEvaluationFacade(QuestionnaireEvaluationService questionnaireEvaluationService) {
        this.questionnaireEvaluationService = questionnaireEvaluationService;
    }

    public void evaluateAnswersToQuestionnaire(Long runId, QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO) {
        List<QuestionAnswer> savedAnswers = questionnaireEvaluationService.saveAnswersToQuestionnaire(runId, questionnairePhaseAnswersDTO);
        questionnaireEvaluationService.evaluateAnswersToQuestionnaire(runId, savedAnswers);
    }
}
