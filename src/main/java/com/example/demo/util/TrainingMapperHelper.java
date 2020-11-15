package com.example.demo.util;

import com.example.demo.domain.AssessmentLevel;
import com.example.demo.domain.BaseLevel;
import com.example.demo.domain.GameLevel;
import com.example.demo.domain.InfoLevel;
import com.example.demo.domain.TrainingDefinition;
import com.example.demo.domain.UnityLevel;
import com.example.demo.dto.input.GameDefinitionCreateDto;
import com.example.demo.mapper.BeanMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class TrainingMapperHelper {

    public List<GameDefinitionCreateDto> getLevelsFrom(TrainingDefinition trainingDefinition) {
        final List<GameDefinitionCreateDto> levels = new ArrayList<>();
        for (BaseLevel level : trainingDefinition.getLevels()) {
            if (Objects.isNull(level.getUnityLevel())) {
                levels.add(getLevelDto(level));
            }
        }

        return levels;
    }


    private <T extends BaseLevel> GameDefinitionCreateDto getLevelDto(T level) {
        if (level instanceof GameLevel) {
            return BeanMapper.INSTANCE.toLevelDefinitionDto((GameLevel) level);
        } else if (level instanceof InfoLevel) {
            return BeanMapper.INSTANCE.toLevelDefinitionDto((InfoLevel) level);
        } else if (level instanceof AssessmentLevel) {
            return BeanMapper.INSTANCE.toLevelDefinitionDto((AssessmentLevel) level);
        } else if (level instanceof UnityLevel) {
            return BeanMapper.INSTANCE.toLevelDefinitionDto((UnityLevel) level);
        }
        // TODO again, this is very ugly, refactor needed
        return null;
    }
}
