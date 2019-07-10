package com.agencybanking.core.web.messages;

import com.agencybanking.core.data.BaseEnum;
import com.agencybanking.core.web.lists.DataList;

import java.util.Arrays;

@DataList
public enum MessageTypes implements BaseEnum {

	EMAIL("Email"),
	WEB("Web"),
	SMS("SMS");
	
	private String description;

	MessageTypes(String description) {
		this.description = description;
	}
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name();
	}
	
	public static MessageTypes fromValue(String val) {
		return Arrays.stream(MessageTypes.values())
				.filter(l -> l.getDescription().equalsIgnoreCase(val))
				.findFirst().orElse(null);
	}
}
