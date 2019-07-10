/**
 * 
 */
package com.agencybanking.core.web.converters;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dubic
 *
 */
public class DateConverter implements Converter<String, Date> {
	private final SimpleDateFormat fommatter;

	public DateConverter(String pattern) {
		this.fommatter = new SimpleDateFormat(pattern);
	}

	@Override
	public Date convert(String source) {
		System.out.println(source);
		try {
			return fommatter.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
