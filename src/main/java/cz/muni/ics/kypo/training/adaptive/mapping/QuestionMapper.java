package cz.muni.ics.kypo.training.adaptive.mapping;

import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.Question;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionChoice;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.questionnaire.QuestionArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.questionnaire.QuestionChoiceArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.phases.questionnaire.QuestionChoiceExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.phases.questionnaire.QuestionExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.questionnaire.QuestionChoiceImportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.questionnaire.QuestionImportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionChoiceDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.preview.QuestionChoicePreviewDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.preview.QuestionPreviewDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.view.QuestionChoiceViewDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.view.QuestionViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * The HintMapper is an utility class to map items into data transfer objects. It provides the implementation of mappings between Java bean type HintMapper and
 * DTOs classes. Code is generated during compile time.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionMapper extends ParentMapper {
    //    QUESTIONS
    Question mapToEntity(QuestionDTO dto);

    Question mapToEntity(QuestionImportDTO dto);

    QuestionArchiveDTO mapToQuestionArchiveDTO(Question entity);

    QuestionExportDTO mapToQuestionExportDTO(Question entity);

    QuestionDTO mapToQuestionDTO(Question entity);

    QuestionViewDTO mapToQuestionViewDTO(Question entity);

    QuestionPreviewDTO mapToQuestionPreviewDTO(Question entity);

    List<Question> mapToList(Collection<QuestionDTO> dtos);

    List<QuestionDTO> mapToListDTO(Collection<Question> entities);

    Set<Question> mapToSet(Collection<QuestionDTO> dtos);

    Set<QuestionDTO> mapToSetDTO(Collection<Question> entities);

    //    QUESTION CHOICES
    QuestionChoice mapToEntity(QuestionChoiceDTO dto);

    QuestionChoice mapToEntity(QuestionChoiceImportDTO dto);

    QuestionChoiceArchiveDTO mapToQuestionArchiveDTO(QuestionChoice entity);

    QuestionChoiceExportDTO mapToQuestionExportDTO(QuestionChoice entity);

    QuestionChoiceDTO mapToQuestionDTO(QuestionChoice entity);

    QuestionChoiceViewDTO mapToQuestionViewDTO(QuestionChoice entity);

    QuestionChoicePreviewDTO mapToQuestionPreviewDTO(QuestionChoice entity);

    List<QuestionChoice> mapChoicesToList(Collection<QuestionChoiceDTO> dtos);

    List<QuestionDTO> mapToChoicesListDTO(Collection<QuestionChoice> entities);

    Set<QuestionChoice> mapChoicesToSet(Collection<QuestionChoiceDTO> dtos);

    Set<QuestionDTO> mapToChoicesSetDTO(Collection<QuestionChoice> entities);
}
