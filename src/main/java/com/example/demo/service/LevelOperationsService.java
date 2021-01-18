package com.example.demo.service;

import com.example.demo.domain.BaseLevel;
import com.example.demo.domain.Task;
import com.example.demo.domain.InfoLevel;
import com.example.demo.dto.BaseLevelDto;
import com.example.demo.dto.QuestionChoiceDto;
import com.example.demo.dto.QuestionDto;
import com.example.demo.dto.TaskDto;
import com.example.demo.dto.TaskUpdateDto;
import com.example.demo.dto.InfoLevelUpdateDto;
import com.example.demo.enums.LevelType;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.BaseLevelRepository;
import com.example.demo.repository.QuestionChoiceRepository;
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

    public void swapLevelsOrder(Long levelIdFrom, Long levelIdTo) {
        Optional<BaseLevel> levelFrom = baseLevelRepository.findById(levelIdFrom);
        Optional<BaseLevel> levelTo = baseLevelRepository.findById(levelIdTo);

        if (levelFrom.isEmpty() || levelTo.isEmpty()) {
            // TODO throw a proper exception
            return;
        }

        int fromOrder = levelFrom.get().getOrder();
        int toOrder = levelTo.get().getOrder();

        levelFrom.get().setOrder(toOrder);
        levelTo.get().setOrder(fromOrder);

        baseLevelRepository.save(levelFrom.get());
        baseLevelRepository.save(levelTo.get());
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

    public BaseLevelDto createLevel(Long trainingDefinitionId, LevelType levelType) {
        BaseLevelDto baseLevelDto;
        if (levelType.equals(LevelType.info)) {
            baseLevelDto = infoLevelService.createDefaultInfoLevel(trainingDefinitionId);
        } else if (levelType.equals(LevelType.assessment)) {
            baseLevelDto = assessmentLevelService.createDefaultAssessmentLevel(trainingDefinitionId);
        } else if (levelType.equals(LevelType.questionnaire)) {
            baseLevelDto = questionnaireLevelService.createDefaultQuestionnaireLevel(trainingDefinitionId);
        } else {
            baseLevelDto = phaseLevelService.createDefaultPhaseLevel(trainingDefinitionId);
        }

        baseLevelDto.setLevelType(levelType);

        return baseLevelDto;
    }

    public BaseLevelDto createTask(Long phaseId) {
        TaskDto createdTask = taskService.createDefaultTask(phaseId);
        createdTask.setLevelType(LevelType.task);

        return createdTask;
    }

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

    public void updateTask(TaskUpdateDto taskUpdateDto) {
        Task task = BeanMapper.INSTANCE.toEntity(taskUpdateDto);
        taskService.updateTask(task);
    }

    public QuestionDto createQuestion(Long questionnaireId) {
        QuestionDto createdQuestion = questionService.createDefaultQuestion(questionnaireId);

        return createdQuestion;
    }

    public QuestionChoiceDto createQuestionChoice(Long questionId) {
        QuestionChoiceDto createdQuestionChoice = questionChoiceService.createDefaultQuestionChoice(questionId);

        return createdQuestionChoice;
    }
}
