/**
 * 
 */
package com.agencybanking.core.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author dubic
 *
 */
public class RuleValidator {
	private List<String> errorMsgs = new ArrayList<>();

	public <T> RuleValidator rule(Predicate<T> predicate, T t, String msg) {
		if (!predicate.test(t)) {
			errorMsgs.add(msg);
		}
		return this;
	}

	public List<String> errors() {
		return errorMsgs;
	}

	public void build() {
		if (!this.errorMsgs.isEmpty()) {
			for (String string : errorMsgs) {
				System.out.println("Errors are: "+string);
			}
			throw new InvalidPropertyException(this.errorMsgs);
		}
	}
}
