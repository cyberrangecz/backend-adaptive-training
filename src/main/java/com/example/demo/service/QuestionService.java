package com.example.demo.service;

import com.example.demo.domain.Question;
import com.example.demo.domain.QuestionChoice;
import com.example.demo.domain.QuestionnaireLevel;
import com.example.demo.dto.QuestionDto;
import com.example.demo.enums.QuestionType;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.QuestionnaireLevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private QuestionnaireLevelRepository questionnaireLevelRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public QuestionDto createDefaultQuestion(Long questionnaireId, QuestionType questionType) {
        Optional<QuestionnaireLevel> questionnaireLevel = questionnaireLevelRepository.findById(questionnaireId);
        if (questionnaireLevel.isEmpty()) {
            // TODO return 404
            return null;
        }

        Question question = new Question();
        question.setText("Title of question");
        question.setQuestionType(questionType);
        question.setQuestionnaireLevel(questionnaireLevel.get());
        question.setOrder(questionRepository.getCurrentMaxOrder(questionnaireId) + 1);

        Question persistedEntity = questionRepository.save(question);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public QuestionDto updateQuestion(Question question) {
        Optional<Question> persistedQuestion = questionRepository.findById(question.getId());

        if (persistedQuestion.isEmpty()) {
            // TODO return 404
            LOG.error("No question found with ID {}.", question.getId());
            return new QuestionDto();
        }

        question.setChoices(persistedQuestion.get().getChoices());
        question.setQuestionnaireLevel(persistedQuestion.get().getQuestionnaireLevel());
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
        questionRepository.decreaseOrderAfterQuestionWasDeleted(questionOrder, question.get().getQuestionnaireLevel().getId());

        questionRepository.delete(question.get());
    }
}
