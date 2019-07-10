/**
 * 
 */
package com.agencybanking.core.web;

import com.agencybanking.core.web.messages.Message;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dubic
 *
 */
@Data
//@JsonRootName(value = "errorpack")
public class ErrorData {
	List<Message> errors = new ArrayList<>();

	public ErrorData() {

	}

}
