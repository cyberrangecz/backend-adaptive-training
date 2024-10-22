package cz.cyberrange.platform.training.adaptive.api.mapping;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.Question;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionChoice;
import cz.cyberrange.platform.training.adaptive.api.dto.archive.phases.questionnaire.QuestionArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.archive.phases.questionnaire.QuestionChoiceArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.export.phases.questionnaire.QuestionChoiceExportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.export.phases.questionnaire.QuestionExportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.questionnaire.QuestionChoiceImportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.questionnaire.QuestionImportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionChoiceDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.preview.QuestionChoicePreviewDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.preview.QuestionPreviewDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.view.QuestionChoiceViewDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.view.QuestionViewDTO;
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
