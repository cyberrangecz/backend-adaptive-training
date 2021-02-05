package com.example.demo.service;

import com.example.demo.domain.Question;
import com.example.demo.domain.QuestionPhaseRelation;
import com.example.demo.domain.QuestionnairePhase;
import com.example.demo.domain.TrainingPhase;
import com.example.demo.dto.PhaseCreateDTO;
import com.example.demo.dto.QuestionPhaseRelationDto;
import com.example.demo.dto.QuestionnairePhaseDto;
import com.example.demo.dto.QuestionnaireUpdateDto;
import com.example.demo.enums.PhaseType;
import com.example.demo.enums.QuestionnaireType;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.AbstractPhaseRepository;
import com.example.demo.repository.QuestionPhaseRelationRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.QuestionnairePhaseRepository;
import com.example.demo.repository.TrainingPhaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class QuestionnairePhaseService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionnairePhaseService.class);

    @Autowired
    private QuestionnairePhaseRepository questionnairePhaseRepository;

    @Autowired
    private AbstractPhaseRepository abstractPhaseRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TrainingPhaseRepository trainingPhaseRepository;

    @Autowired
    private QuestionPhaseRelationRepository questionPhaseRelationRepository;

    public QuestionnairePhaseDto createDefaultQuestionnairePhase(Long trainingDefinitionId, PhaseCreateDTO phaseCreateDTO) {

        QuestionnairePhase questionnairePhase = new QuestionnairePhase();
        questionnairePhase.setTitle("Title of questionnaire level");
        questionnairePhase.setTrainingDefinitionId(trainingDefinitionId);
        questionnairePhase.setOrder(abstractPhaseRepository.getCurrentMaxOrder(trainingDefinitionId) + 1);

        if (PhaseType.QUESTIONNAIRE_ADAPTIVE.equals(phaseCreateDTO.getPhaseType())) {
            questionnairePhase.setQuestionnaireType(QuestionnaireType.ADAPTIVE);
        } else {
            questionnairePhase.setQuestionnaireType(QuestionnaireType.GENERAL);
        }

        QuestionnairePhase persistedEntity = questionnairePhaseRepository.save(questionnairePhase);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
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

        questionnairePhase.getQuestionPhaseRelations().clear();
        questionnairePhase.getQuestionPhaseRelations().addAll(resolveQuestionnairePhaseRelationsUpdate(questionnairePhase, questionnaireUpdateDto));

        if (!CollectionUtils.isEmpty(questionnairePhase.getQuestions())) {
            questionnairePhase.getQuestions().forEach(x -> x.setQuestionnairePhase(questionnairePhase));
            for (Question question : questionnairePhase.getQuestions()) {
                question.getChoices().forEach(x -> x.setQuestion(question));
            }
        }

        QuestionnairePhase savedEntity = questionnairePhaseRepository.save(questionnairePhase);

        QuestionnairePhaseDto result = BeanMapper.INSTANCE.toDto(savedEntity);
        if (QuestionnaireType.ADAPTIVE.equals(savedEntity.getQuestionnaireType())) {
            result.setPhaseType(PhaseType.QUESTIONNAIRE_ADAPTIVE);
        } else {
            result.setPhaseType(PhaseType.QUESTIONNAIRE_GENERAL);
        }

        return result;
    }

    private List<QuestionPhaseRelation> resolveQuestionnairePhaseRelationsUpdate(QuestionnairePhase questionnairePhase, QuestionnaireUpdateDto questionnaireUpdateDto) {
        List<QuestionPhaseRelation> questionnairePhaseRelations = new ArrayList<>();

        if (!CollectionUtils.isEmpty(questionnaireUpdateDto.getPhaseRelations())) {
            int order = 0;
            for (QuestionPhaseRelationDto phaseRelation : questionnaireUpdateDto.getPhaseRelations()) {
                Set<Question> questionsInPhaseRelation = Set.copyOf(questionRepository.findAllById(phaseRelation.getQuestionIds()));

                QuestionPhaseRelation questionPhaseRelation;
                if (Objects.isNull(phaseRelation.getId())) {
                    questionPhaseRelation = new QuestionPhaseRelation();
                    questionPhaseRelation.setQuestions(questionsInPhaseRelation);
                } else {
                    questionPhaseRelation = questionPhaseRelationRepository.findById(phaseRelation.getId())
                            .orElseThrow(() -> new RuntimeException("Question phase relation was not found"));
                    // TODO throw proper exception once kypo2-training is migrated

                    questionPhaseRelation.getQuestions().clear();
                    questionPhaseRelation.getQuestions().addAll(questionsInPhaseRelation);
                }

                TrainingPhase trainingPhase = trainingPhaseRepository.findById(phaseRelation.getPhaseId())
                        .orElseThrow(() -> new RuntimeException("Training phase was not found"));
                // TODO throw proper exception once kypo2-training is migrated

                questionPhaseRelation.setOrder(order);
                questionPhaseRelation.setSuccessRate(phaseRelation.getSuccessRate());
                questionPhaseRelation.setRelatedTrainingPhase(trainingPhase);
                questionPhaseRelation.setQuestionnairePhase(questionnairePhase);

                questionnairePhaseRelations.add(questionPhaseRelation);
                order++;
            }
        }

        return questionnairePhaseRelations;
    }

}
