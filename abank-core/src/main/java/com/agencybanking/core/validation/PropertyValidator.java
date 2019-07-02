/**
 * 
 */
package com.agencybanking.core.validation;

import com.agencybanking.core.data.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Validates class properties for general validations
 * 
 * @author dubic
 *
 */
public class PropertyValidator {
	private List<String> errorMsgs = new ArrayList<>();

	public PropertyValidator notNull(Object property, String msg) {
		if (property == null) {
			errorMsgs.add(msg);
		}
		return this;
	}
	
	public PropertyValidator notEmpty(Collection<?> property, String msg) {
		if (property.isEmpty() || property.size() == 0) {
			errorMsgs.add(msg);
		}
		return this;
	}

	public PropertyValidator notBlank(String property, String msg) {
		if (property == null) {
			errorMsgs.add(msg);
			return this;
		}
		if (property.isEmpty()) {
			errorMsgs.add(msg);
		}
		return this;
	}

	public PropertyValidator equals(Object property, Object property2, String msg) {
		if (property == null || property2 == null) {
			errorMsgs.add(msg);
			return this;
		}
		if (!property.equals(property2)) {
			errorMsgs.add(msg);
		}
		return this;
	}

	public void build() throws IllegalArgumentException {
		if (!this.errorMsgs.isEmpty()) {
			throw new InvalidPropertyException(this.errorMsgs);
		}
	}

//	public static Predicate<BaseEntity> isAdultMale() {
//		return p -> p.getId() > 21 && p.getActive().equals(Boolean.TRUE);
//	}

}
