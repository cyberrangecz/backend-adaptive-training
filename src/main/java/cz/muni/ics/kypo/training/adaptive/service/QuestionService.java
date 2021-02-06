package cz.muni.ics.kypo.training.adaptive.service;

import cz.muni.ics.kypo.training.adaptive.domain.Question;
import cz.muni.ics.kypo.training.adaptive.domain.QuestionnairePhase;
import cz.muni.ics.kypo.training.adaptive.dto.QuestionDTO;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionType;
import cz.muni.ics.kypo.training.adaptive.mapper.BeanMapper;
import cz.muni.ics.kypo.training.adaptive.repository.QuestionRepository;
import cz.muni.ics.kypo.training.adaptive.repository.QuestionnairePhaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private QuestionnairePhaseRepository questionnairePhaseRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public QuestionDTO createDefaultQuestion(Long questionnaireId, QuestionType questionType) {
        Optional<QuestionnairePhase> questionnairePhase = questionnairePhaseRepository.findById(questionnaireId);
        if (questionnairePhase.isEmpty()) {
            // TODO return 404
            return null;
        }

        Question question = new Question();
        question.setText("Title of question");
        question.setQuestionType(questionType);
        question.setQuestionnairePhase(questionnairePhase.get());
        question.setOrder(questionRepository.getCurrentMaxOrder(questionnaireId) + 1);

        Question persistedEntity = questionRepository.save(question);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public QuestionDTO updateQuestion(Question question) {
        Optional<Question> persistedQuestion = questionRepository.findById(question.getId());

        if (persistedQuestion.isEmpty()) {
            // TODO return 404
            LOG.error("No question found with ID {}.", question.getId());
            return new QuestionDTO();
        }

        question.setChoices(persistedQuestion.get().getChoices());
        question.setQuestionnairePhase(persistedQuestion.get().getQuestionnairePhase());
        question.setOrder(persistedQuestion.get().getOrder());

        Question savedEntity = questionRepository.save(question);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }

    public void deleteQuestion(Long questionId) {
        Optional<Question> question = questionRepository.findById(questionId);

        if (question.isEmpty()) {
            // TODO throw a proper exception
            return;
        }

        int questionOrder = question.get().getOrder();
        questionRepository.decreaseOrderAfterQuestionWasDeleted(questionOrder, question.get().getQuestionnairePhase().getId());

        questionRepository.delete(question.get());
    }
}
