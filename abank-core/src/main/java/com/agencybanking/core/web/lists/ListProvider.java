/**
 * 
 */
package com.agencybanking.core.web.lists;

import java.util.List;

/**
 * @author dubic
 *
 */
public interface ListProvider {
	/**
	 * 
	 * @param type
	 * @param exclude comma separated string
	 * @return
	 * @throws ClassNotFoundException
	 */
	public List loadList(String type, String exclude) throws ClassNotFoundException;
}
