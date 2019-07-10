/**
 * 
 */
package com.agencybanking.core.web.lists;

import com.agencybanking.core.data.BaseEnum;
import com.agencybanking.core.data.NameValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * @author dubic
 *
 */
@Slf4j
@Component
public class EnumListProvider implements ListProvider{

	public List<NameValue> loadList(String type, String exclude) throws ClassNotFoundException {
		List<NameValue> result = new ArrayList<NameValue>();
		Assert.notNull(type, "List messageType cannot be null");
		Class enumClass = Class.forName(type);
		DataList e = (DataList) enumClass.getAnnotation(DataList.class);
		Assert.notNull(e, "Enum list must be annotated with @com.unionsystemsltd.optimus.core.web.lists.DataList");

		List enumList = new ArrayList<>(EnumSet.allOf(enumClass));

		for (Object o : enumList) {
			NameValue data = new NameValue(((BaseEnum) o).getName(), ((BaseEnum) o).getDescription());
			if (!(exclude != null && Arrays.asList(exclude.split(",")).contains(data.getId()))) {
				result.add(data);
			}
		}

		return result;
	}
}
