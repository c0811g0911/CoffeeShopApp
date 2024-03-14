package digital101.io.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import digital101.io.service.ShopService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the id value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = ShopsQueueUnique.ShopsQueueUniqueValidator.class
)
public @interface ShopsQueueUnique {

    String message() default "{Exists.shops.queue}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ShopsQueueUniqueValidator implements ConstraintValidator<ShopsQueueUnique, Long> {

        private final ShopService shopsService;
        private final HttpServletRequest request;

        public ShopsQueueUniqueValidator(final ShopService shopsService,
                final HttpServletRequest request) {
            this.shopsService = shopsService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Long value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(shopsService.get(Long.parseLong(currentId)).getQueue())) {
                // value hasn't changed
                return true;
            }
            return !shopsService.queueExists(value);
        }

    }

}
