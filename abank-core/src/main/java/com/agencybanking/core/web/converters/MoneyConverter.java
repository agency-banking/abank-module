/**
 *
 */
package com.agencybanking.core.web.converters;

import com.agencybanking.core.data.Money;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * @author dubic
 *
 */
public class MoneyConverter implements Converter<String, Money> {

	private int scale = 2;

	public MoneyConverter(){

	}

	public MoneyConverter(int scale){
		this.scale = scale;
	}

	@Override
	public Money convert(String source) {
		return parseStringMoney(source);
	}

	public Money parseStringMoney(String strAmt) {
		return parseStringMoney(strAmt, scale);
	}

	public Money parseStringMoney(String strAmt, int scale) {
		return new Money(strAmt.replaceAll("[0-9., ]", ""), new BigDecimal(strAmt.replaceAll("[A-Z,a-z ]", "")), scale);
	}
}
