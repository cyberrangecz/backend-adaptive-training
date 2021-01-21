package com.example.demo.service;

import com.example.demo.domain.AssessmentLevel;
import com.example.demo.domain.Question;
import com.example.demo.domain.QuestionnaireLevel;
import com.example.demo.dto.QuestionDto;
import com.example.demo.dto.QuestionnaireLevelDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.BaseLevelRepository;
import com.example.demo.repository.QuestionnaireLevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionnaireLevelService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionnaireLevelService.class);

    @Autowired
    private QuestionnaireLevelRepository questionnaireLevelRepository;

    @Autowired
    private BaseLevelRepository baseLevelRepository;

    public QuestionnaireLevelDto createDefaultQuestionnaireLevel(Long trainingDefinitionId) {

        QuestionnaireLevel questionnaireLevel =new QuestionnaireLevel();
        questionnaireLevel.setTitle("Title of questionnaire level");
        questionnaireLevel.setTrainingDefinitionId(trainingDefinitionId);
        questionnaireLevel.setOrder(baseLevelRepository.getCurrentMaxOrder(trainingDefinitionId) + 1);

        QuestionnaireLevel persistedEntity = questionnaireLevelRepository.save(questionnaireLevel);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public QuestionnaireLevelDto updateQuestion(QuestionnaireLevel questionnaireLevel) {
        Optional<QuestionnaireLevel> persistedQuestion = questionnaireLevelRepository.findById(questionnaireLevel.getId());

        if (persistedQuestion.isEmpty()) {
            // TODO return 404
            LOG.error("No questionnaire level found with ID {}.", questionnaireLevel.getId());
            return new QuestionnaireLevelDto();
        }

        questionnaireLevel.setTrainingDefinitionId(persistedQuestion.get().getTrainingDefinitionId());
        questionnaireLevel.setQuestions(persistedQuestion.get().getQuestions());
        questionnaireLevel.setOrder(persistedQuestion.get().getOrder());

        QuestionnaireLevel savedEntity = questionnaireLevelRepository.save(questionnaireLevel);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }
}
