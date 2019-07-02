package com.agencybanking.core.storage;

import com.agencybanking.core.data.BaseEnum;

public enum StorageType implements BaseEnum {
	LOCAL("local");
	
	private String description;
	StorageType(String d) {
		this.description = d;
	}

	@Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getName() {
        return name();
    }
}
