/**
 * 
 */
package com.agencybanking.core.web;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.io.Serializable;

/**
 * @author Dubic
 *
 */
@Data
@ToString
public class Paging implements Serializable{
	private int limit = 10;
	private String order;
	private int page = 1;


	public Paging() {
	}

	public Sort getSort() {
		Direction dir = Direction.ASC;
		if(this.order == null){
			return null;
		}
		if (this.order.startsWith("-")) {
			dir = Direction.DESC;
			this.order = this.order.substring(1);
		}
		
		return new Sort(dir, this.order);
	}

	public int getPage() {
		return this.page - 1;
	}

	public PageRequest getPageRequest(){
        return PageRequest.of(this.getPage(), this.limit, this.getSort());
    }
}
