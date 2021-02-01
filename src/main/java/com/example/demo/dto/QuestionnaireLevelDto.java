package com.example.demo.dto;

import java.util.List;

public class QuestionnaireLevelDto extends AbstractPhaseDto {

    private List<QuestionDto> questions;

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }
}
