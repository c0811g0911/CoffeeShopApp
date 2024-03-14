package digital101.io.model;

import digital101.io.service.ShopService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.web.servlet.HandlerMapping;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import static java.lang.annotation.ElementType.*;


/**
 * Validate that the userName value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = ShopsUserNameUnique.ShopsUserNameUniqueValidator.class
)
public @interface ShopsUserNameUnique {

    String message() default "{Exists.shops.userName}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ShopsUserNameUniqueValidator implements ConstraintValidator<ShopsUserNameUnique, String> {

        private final ShopService shopsService;
        private final HttpServletRequest request;

        public ShopsUserNameUniqueValidator(final ShopService shopsService,
                final HttpServletRequest request) {
            this.shopsService = shopsService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(shopsService.get(Long.parseLong(currentId)).getUserName())) {
                // value hasn't changed
                return true;
            }
            return !shopsService.userNameExists(value);
        }

    }

}
