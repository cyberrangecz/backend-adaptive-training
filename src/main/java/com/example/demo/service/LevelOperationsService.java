package com.example.demo.service;

import com.example.demo.domain.BaseLevel;
import com.example.demo.domain.InfoLevel;
import com.example.demo.domain.PhaseLevel;
import com.example.demo.domain.Question;
import com.example.demo.domain.QuestionChoice;
import com.example.demo.domain.QuestionnaireLevel;
import com.example.demo.domain.Task;
import com.example.demo.dto.BaseLevelDto;
import com.example.demo.dto.InfoLevelUpdateDto;
import com.example.demo.dto.PhaseCreateDTO;
import com.example.demo.dto.PhaseLevelUpdateDto;
import com.example.demo.dto.QuestionChoiceDto;
import com.example.demo.dto.QuestionChoiceUpdateDto;
import com.example.demo.dto.QuestionDto;
import com.example.demo.dto.QuestionUpdateDto;
import com.example.demo.dto.QuestionnaireUpdateDto;
import com.example.demo.dto.TaskUpdateDto;
import com.example.demo.enums.PhaseType;
import com.example.demo.enums.QuestionType;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.BaseLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LevelOperationsService {

    @Autowired
    private BaseLevelRepository baseLevelRepository;

    @Autowired
    private InfoLevelService infoLevelService;

    @Autowired
    private AssessmentLevelService assessmentLevelService;

    @Autowired
    private QuestionnaireLevelService questionnaireLevelService;

    @Autowired
    private PhaseLevelService phaseLevelService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionChoiceService questionChoiceService;

    @Transactional
    public void moveLevelToSpecifiedOrder(Long levelIdFrom, int newPosition) {
        Optional<BaseLevel> levelFrom = baseLevelRepository.findById(levelIdFrom);

        if (levelFrom.isEmpty()) {
            // TODO throw a proper exception
            return;
        }

        int fromOrder = levelFrom.get().getOrder();

        if (fromOrder < newPosition) {
            baseLevelRepository.decreaseOrderOfLevelsOnInterval(levelFrom.get().getTrainingDefinitionId(), fromOrder, newPosition);
        } else if (fromOrder > newPosition) {
            baseLevelRepository.increaseOrderOfLevelsOnInterval(levelFrom.get().getTrainingDefinitionId(), newPosition, fromOrder);
        } else {
            // nothing should be changed, no further actions needed
            return;
        }

        levelFrom.get().setOrder(newPosition);
        baseLevelRepository.save(levelFrom.get());

        phaseLevelService.alignDecisionMatrixForPhasesInTrainingDefinition(levelFrom.get().getTrainingDefinitionId());
    }

    @Transactional
    public void deleteLevel(Long levelId) {
        Optional<BaseLevel> levelEntity = baseLevelRepository.findById(levelId);

        if (levelEntity.isEmpty()) {
            // TODO throw a proper exception
            return;
        }

        int levelOrder = levelEntity.get().getOrder();
        Long phaseId = levelEntity.get().getPhaseLevel() == null ? null : levelEntity.get().getPhaseLevel().getId();
        baseLevelRepository.decreaseOrderAfterLevelWasDeleted(levelEntity.get().getTrainingDefinitionId(), levelOrder, phaseId);

        baseLevelRepository.delete(levelEntity.get());
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        questionService.deleteQuestion(questionId);
    }

    @Transactional
    public void deleteQuestionChoice(Long questionChoiceId) {
        questionChoiceService.deleteQuestionChoice(questionChoiceId);
    }

    public BaseLevelDto createLevel(Long trainingDefinitionId, PhaseCreateDTO phaseCreateDTO) {
        BaseLevelDto baseLevelDto;
        if (PhaseType.INFO.equals(phaseCreateDTO.getPhaseType())) {
            baseLevelDto = infoLevelService.createDefaultInfoLevel(trainingDefinitionId);
        } else if (PhaseType.QUESTIONNAIRE.equals(phaseCreateDTO.getPhaseType())) {
            baseLevelDto = questionnaireLevelService.createDefaultQuestionnaireLevel(trainingDefinitionId);
        } else {
            baseLevelDto = phaseLevelService.createDefaultPhaseLevel(trainingDefinitionId);
        }

        baseLevelDto.setPhaseType(phaseCreateDTO.getPhaseType());

        return baseLevelDto;
    }

//    public BaseLevelDto createTask(Long phaseId) {
//        TaskDto createdTask = taskService.createDefaultTask(phaseId);
//        createdTask.setPhaseType(PhaseType.task);
//
//        return createdTask;
//    }

    public BaseLevelDto getLevel(Long levelId) {
        Optional<BaseLevel> level = baseLevelRepository.findById(levelId);

        if (level.isEmpty()) {
            // TODO throw 404
            return null;
        }

        return BeanMapper.INSTANCE.toDto(level.get());
    }

    public void updateInfoLevel(InfoLevelUpdateDto infoLevelUpdateDto) {
        InfoLevel infoLevel = BeanMapper.INSTANCE.toEntity(infoLevelUpdateDto);
        infoLevelService.updateInfoLevel(infoLevel);
    }

    public void updatePhaseLevel(PhaseLevelUpdateDto phaseLevelUpdateDto) {
        PhaseLevel phaseLevel = BeanMapper.INSTANCE.toEntity(phaseLevelUpdateDto);
        phaseLevelService.updatePhaseLevel(phaseLevel);
    }

    public void updateTask(TaskUpdateDto taskUpdateDto) {
        Task task = BeanMapper.INSTANCE.toEntity(taskUpdateDto);
        taskService.updateTask(task);
    }

    public QuestionDto createQuestion(Long questionnaireId, QuestionType questionType) {
        QuestionDto createdQuestion = questionService.createDefaultQuestion(questionnaireId, questionType);

        return createdQuestion;
    }

    public QuestionChoiceDto createQuestionChoice(Long questionId) {
        QuestionChoiceDto createdQuestionChoice = questionChoiceService.createDefaultQuestionChoice(questionId);

        return createdQuestionChoice;
    }

    public void updateQuestionnaire(QuestionnaireUpdateDto questionnaireUpdateDto) {
        QuestionnaireLevel questionnaireLevel = BeanMapper.INSTANCE.toEntity(questionnaireUpdateDto);
        questionnaireLevelService.updateQuestion(questionnaireLevel);
    }

    public void updateQuestion(QuestionUpdateDto questionUpdateDto) {
        Question question = BeanMapper.INSTANCE.toEntity(questionUpdateDto);
        questionService.updateQuestion(question);
    }

    public void updateQuestionChoice(QuestionChoiceUpdateDto questionChoiceUpdateDto) {
        QuestionChoice questionChoice = BeanMapper.INSTANCE.toEntity(questionChoiceUpdateDto);
        questionChoiceService.updateQuestionChoice(questionChoice);
    }
}
