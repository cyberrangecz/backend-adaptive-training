package com.example.demo.service;

import com.example.demo.domain.QuestionnairePhase;
import com.example.demo.dto.QuestionnairePhaseDto;
import com.example.demo.dto.QuestionnaireUpdateDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.AbstractPhaseRepository;
import com.example.demo.repository.QuestionnairePhaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionnairePhaseService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionnairePhaseService.class);

    @Autowired
    private QuestionnairePhaseRepository questionnairePhaseRepository;

    @Autowired
    private AbstractPhaseRepository abstractPhaseRepository;

    public QuestionnairePhaseDto createDefaultQuestionnairePhase(Long trainingDefinitionId) {

        QuestionnairePhase questionnairePhase =new QuestionnairePhase();
        questionnairePhase.setTitle("Title of questionnaire level");
        questionnairePhase.setTrainingDefinitionId(trainingDefinitionId);
        questionnairePhase.setOrder(abstractPhaseRepository.getCurrentMaxOrder(trainingDefinitionId) + 1);

        QuestionnairePhase persistedEntity = questionnairePhaseRepository.save(questionnairePhase);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public QuestionnairePhaseDto updateQuestion(QuestionnairePhase questionnairePhase) {
        Optional<QuestionnairePhase> persistedQuestion = questionnairePhaseRepository.findById(questionnairePhase.getId());

        if (persistedQuestion.isEmpty()) {
            // TODO return 404
            LOG.error("No questionnaire level found with ID {}.", questionnairePhase.getId());
            return new QuestionnairePhaseDto();
        }

        questionnairePhase.setTrainingDefinitionId(persistedQuestion.get().getTrainingDefinitionId());
        questionnairePhase.setQuestions(persistedQuestion.get().getQuestions());
        questionnairePhase.setOrder(persistedQuestion.get().getOrder());

        QuestionnairePhase savedEntity = questionnairePhaseRepository.save(questionnairePhase);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }

    public QuestionnairePhaseDto updateQuestionnairePhase(Long definitionId, Long phaseId, QuestionnaireUpdateDto questionnaireUpdateDto) {
        QuestionnairePhase questionnairePhase = BeanMapper.INSTANCE.toEntity(questionnaireUpdateDto);
        questionnairePhase.setId(phaseId);

        QuestionnairePhase persistedQuestionnairePhase = questionnairePhaseRepository.findById(questionnairePhase.getId())
                .orElseThrow(() -> new RuntimeException("Questionnaire phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed);


        questionnairePhase.setTrainingDefinitionId(persistedQuestionnairePhase.getTrainingDefinitionId());
        questionnairePhase.setOrder(persistedQuestionnairePhase.getOrder());

        // TODO questions, their choices and realtions among questions and training phases must be set here

        QuestionnairePhase savedEntity = questionnairePhaseRepository.save(questionnairePhase);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }
}
