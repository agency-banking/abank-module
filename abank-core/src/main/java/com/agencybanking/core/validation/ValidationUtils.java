/**
 * 
 */
package com.agencybanking.core.validation;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;

import javax.validation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author dubic
 *
 */
public class ValidationUtils {
	private static Validator validator;

	public static Validator getValidator() {
		if (validator != null) {
			return validator;
		}
		HibernateValidatorConfiguration config = Validation.byProvider(HibernateValidator.class).configure();
		// config.messageInterpolator(new ResourceBundleMessageInterpolator())
		// .traversableResolver(new DefaultTraversableResolver())
		// .constraintValidatorFactory(new ConstraintValidatorFactoryImpl());
		ValidatorFactory factory = config.buildValidatorFactory();
		validator = factory.getValidator();
		return validator;
	}

	public static <T> List<String> getErrors(T t, Class<?>... types) throws ConstraintViolationException {
		return getValidator().validate(t).stream().map(v -> v.getMessage()).collect(Collectors.toList());
	}

	public static <T> void validate(T t, Class<?>... types) throws ConstraintViolationException {
		Set<ConstraintViolation<T>> results = getValidator().validate(t);
		if (!results.isEmpty()) {
			throw new ConstraintViolationException("Invalid Data [" + t.getClass().getName() + "]", results);
		}
	}

	public static PropertyValidator property() {
		return new PropertyValidator();
	}
	
	public static RuleValidator ruleValidator() {
		return new RuleValidator();
	}
}
