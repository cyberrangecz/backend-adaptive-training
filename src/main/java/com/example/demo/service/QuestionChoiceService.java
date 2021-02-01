package com.example.demo.service;

import com.example.demo.domain.Question;
import com.example.demo.domain.QuestionChoice;
import com.example.demo.dto.QuestionChoiceDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.QuestionChoiceRepository;
import com.example.demo.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionChoiceService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionChoiceService.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionChoiceRepository questionChoiceRepository;

    public QuestionChoiceDto createDefaultQuestionChoice(Long questionId) {
        Optional<Question> question = questionRepository.findById(questionId);
        if (question.isEmpty()) {
            // TODO return 404
            return null;
        }

        QuestionChoice questionChoice = new QuestionChoice();
        questionChoice.setText("Title of question choice");
        questionChoice.setQuestion(question.get());
        questionChoice.setOrder(questionChoiceRepository.getCurrentMaxOrder(questionId) + 1);

        QuestionChoice persistedEntity = questionChoiceRepository.save(questionChoice);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public QuestionChoiceDto updateQuestionChoice(QuestionChoice questionChoice) {
        Optional<QuestionChoice> persistedQuestionChoice = questionChoiceRepository.findById(questionChoice.getId());

        if (persistedQuestionChoice.isEmpty()) {
            // TODO return 404
            LOG.error("No question choice found with ID {}.", questionChoice.getId());
            return new QuestionChoiceDto();
        }

        questionChoice.setQuestion(persistedQuestionChoice.get().getQuestion());
        questionChoice.setOrder(persistedQuestionChoice.get().getOrder());

        QuestionChoice savedEntity = questionChoiceRepository.save(questionChoice);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }

    public void deleteQuestionChoice(Long questionChoiceId) {
        Optional<QuestionChoice> questionChoice = questionChoiceRepository.findById(questionChoiceId);

        if (questionChoice.isEmpty()) {
            // TODO throw a proper exception
            return;
        }

        int questionChoiceOrder = questionChoice.get().getOrder();
        questionChoiceRepository.decreaseOrderAfterQuestionChoiceWasDeleted(questionChoiceOrder, questionChoice.get().getQuestion().getId());

        questionChoiceRepository.delete(questionChoice.get());
    }
}
