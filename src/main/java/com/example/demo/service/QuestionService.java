package com.example.demo.service;

import com.example.demo.domain.Question;
import com.example.demo.domain.QuestionnaireLevel;
import com.example.demo.dto.QuestionDto;
import com.example.demo.enums.QuestionType;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.QuestionnaireLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionnaireLevelRepository questionnaireLevelRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public QuestionDto createDefaultQuestion(Long questionnaireId) {
        Optional<QuestionnaireLevel> questionnaireLevel = questionnaireLevelRepository.findById(questionnaireId);
        if (questionnaireLevel.isEmpty()) {
            // TODO return 404
            return null;
        }

        Question question = new Question();
        question.setText("Title of question");
        question.setQuestionType(QuestionType.MCQ);
        question.setQuestionnaireLevel(questionnaireLevel.get());
        question.setOrder(questionRepository.getCurrentMaxOrder(questionnaireId) + 1);

        Question persistedEntity = questionRepository.save(question);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }
}
