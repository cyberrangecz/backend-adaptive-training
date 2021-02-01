package com.example.demo.service;

import com.example.demo.domain.QuestionnaireLevel;
import com.example.demo.dto.QuestionnaireLevelDto;
import com.example.demo.dto.QuestionnaireUpdateDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.AbstractPhaseRepository;
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
    private AbstractPhaseRepository abstractPhaseRepository;

    public QuestionnaireLevelDto createDefaultQuestionnaireLevel(Long trainingDefinitionId) {

        QuestionnaireLevel questionnaireLevel =new QuestionnaireLevel();
        questionnaireLevel.setTitle("Title of questionnaire level");
        questionnaireLevel.setTrainingDefinitionId(trainingDefinitionId);
        questionnaireLevel.setOrder(abstractPhaseRepository.getCurrentMaxOrder(trainingDefinitionId) + 1);

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

    public QuestionnaireLevelDto updateQuestionnairePhase(Long definitionId, Long phaseId, QuestionnaireUpdateDto questionnaireUpdateDto) {
        QuestionnaireLevel questionnaireLevel = BeanMapper.INSTANCE.toEntity(questionnaireUpdateDto);
        questionnaireLevel.setId(phaseId);

        QuestionnaireLevel persistedQuestionnairePhase = questionnaireLevelRepository.findById(questionnaireLevel.getId())
                .orElseThrow(() -> new RuntimeException("Questionnaire phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed);


        questionnaireLevel.setTrainingDefinitionId(persistedQuestionnairePhase.getTrainingDefinitionId());
        questionnaireLevel.setOrder(persistedQuestionnairePhase.getOrder());

        // TODO questions, their choices and realtions among questions and training phases must be set here

        QuestionnaireLevel savedEntity = questionnaireLevelRepository.save(questionnaireLevel);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }
}
