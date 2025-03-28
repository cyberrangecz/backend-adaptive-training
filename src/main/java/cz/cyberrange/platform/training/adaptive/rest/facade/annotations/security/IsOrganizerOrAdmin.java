package cz.cyberrange.platform.training.adaptive.rest.facade.annotations.security;


import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The custom annotation <i>@IsOrganizerOrAdmin<i/>. All methods annotated with this annotation expect the user has a role <strong>ROLE_TRAINING_ADMINISTRATOR<strong/>
 * or <strong>ROLE_TRAINING_ORGANIZER<strong/>.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR, " +
        "T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ORGANIZER)")
public @interface IsOrganizerOrAdmin {
}
