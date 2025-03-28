package cz.cyberrange.platform.training.adaptive.api.annotations.validation;

import cz.cyberrange.platform.training.adaptive.api.dto.PhaseCreateDTO;
import cz.cyberrange.platform.training.adaptive.persistence.enums.PhaseType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNullQuestionnaireTypeValidator implements ConstraintValidator<NotNullQuestionnaireType, PhaseCreateDTO> {

    @Override
    public void initialize(NotNullQuestionnaireType constraintAnnotation) {
    }

    @Override
    public boolean isValid(PhaseCreateDTO phaseCreateDTO, ConstraintValidatorContext context) {
        if (phaseCreateDTO == null) {
            return true;
        }
        if (phaseCreateDTO.getPhaseType() == PhaseType.QUESTIONNAIRE && phaseCreateDTO.getQuestionnaireType() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Field 'questionnaire_type' cannot be null.").addConstraintViolation();
            return false;
        }
        return true;
    }
}
