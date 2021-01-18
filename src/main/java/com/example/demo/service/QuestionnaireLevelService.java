package com.example.demo.service;

import com.example.demo.domain.AssessmentLevel;
import com.example.demo.domain.QuestionnaireLevel;
import com.example.demo.dto.QuestionnaireLevelDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.BaseLevelRepository;
import com.example.demo.repository.QuestionnaireLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionnaireLevelService {

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
}
