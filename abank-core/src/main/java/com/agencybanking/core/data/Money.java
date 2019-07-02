package com.agencybanking.core.data;

import com.agencybanking.core.el.Label;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author dubic
 */

/**
 * @author dubic
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class Money extends Data implements Comparable<Money> {

    public static final int DEFAULT_CURRENCY_SCALE = 2;
    public static final RoundingMode CURRENCY_ROUNDING_MODE = RoundingMode.HALF_UP;
    public static final MathContext OPERATION_MATH_CONTEXT = MathContext.DECIMAL128;
    private static final long serialVersionUID = 2230697437606720760L;
    public static final String LOCAL_CCY = "NGN";
    public static final String USD_CCY = "USD";
    public static final String EUR_CCY = "EUR";
    public static final String GBP_CCY = "GBP";

    @JsonView(DataViews.UIView.class)
    @Transient
    public int scale = DEFAULT_CURRENCY_SCALE;

    //	@Column(length = 3)
    @Label("Currency")
    @XmlElement(name = "Currency")
    public String currency;

    @Label("Amount")
    @XmlElement(name = "Amount")
    public BigDecimal amount;

    @Transient
    public String value;

    public Money() {
        this(Money.LOCAL_CCY, BigDecimal.ZERO);
    }

    public Money(String value) {
        this(value.replaceAll("[0-9., ]", ""), new BigDecimal(value.replaceAll("[A-Z,a-z ]", "")));
    }

    public Money(String currency, BigDecimal amount) {
        this(currency, amount, DEFAULT_CURRENCY_SCALE);
    }

    public Money(String currency, BigDecimal amount, int scale) {
        Assert.notNull(amount, "amount is required");
        this.scale = scale;
        amount = amount.setScale(this.scale, CURRENCY_ROUNDING_MODE);
        this.currency = currency;
        if (StringUtils.isEmpty(currency)) {
            this.currency = LOCAL_CCY;
        } else {
            this.currency = currency.trim().toUpperCase();
        }
        this.amount = amount;
    }

    public Money(String currency, Double amount) {
        this(currency, BigDecimal.valueOf(amount));
    }

    public Money(Double amount) {
        this(null, BigDecimal.valueOf(amount));
    }

    public Money(int amount) {
        this(null, BigDecimal.valueOf(amount));
    }

    public Money(Long amount) {
        this(null, BigDecimal.valueOf(amount));
    }

    public static Money of(BigDecimal amount, String currency, int scale) {
        return new Money(currency, amount);
    }

    public static Money of(BigDecimal amount, String currency) {
        return new Money(currency, amount);
    }

    public static Money of(double amount, String currency) {
        return new Money(currency, amount);
    }

    public static Money of(double amount) {
        return new Money(amount);
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // @Transient
    // public boolean equals(Money test) {
    // if (test == null) {
    // return false;
    // }
    // return test.getCurrency().equalsIgnoreCase(this.getCurrency())
    // && test.getAmount().compareTo(this.getAmount()) == 0;
    // }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        result = prime * result + scale;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Money other = (Money) obj;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (currency == null) {
            if (other.currency != null)
                return false;
        } else if (!currency.equals(other.currency))
            return false;
        if (scale != other.scale)
            return false;
        return true;
    }

    public String getValue() {
        return formatAmount() + " " + this.currency;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String formatAmount() {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(this.scale);
        return df.format(this.amount);
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "Money [scale=" + scale + ", currency=" + currency + ", amount=" + amount + "]";
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Money m) {
        if (!this.currency.equalsIgnoreCase(m.getCurrency())) {
            return -2;
        }
        return this.getAmount().compareTo(m.getAmount());
    }

    /**
     * Convert this money recipients the equivalent in this currency
     *
     * @param ccy  the currency recipients convert recipients
     * @param rate the rate used for the conversion
     * @return the converted money with the equivalent amount and the converted
     * ccy
     */
    public Money convertTo(String ccy, BigDecimal rate) {
        if (this.getCurrency().equalsIgnoreCase(ccy)) {
            return this;
        }
        BigDecimal camt = rate.multiply(this.getAmount());
        return new Money(ccy, camt, this.getScale());
    }

    /**
     * Add this money recipients the specified
     *
     * @param m
     * @return
     */
    public Money add(Money m) {
        Assert.isTrue(this.currency.equalsIgnoreCase(m.getCurrency()),
                "cannot add money of different currencies.Convert first");
        return new Money(this.currency, this.amount.add(m.getAmount(), OPERATION_MATH_CONTEXT), this.scale);
    }

    /**
     * Add this amount recipients the specified
     *
     * @param amt the amount
     * @return
     * @throws IllegalArgumentException if amt is null
     */
    public Money add(BigDecimal amt) {
        Assert.notNull(amt, "Amount required for add");
        return new Money(this.currency, this.amount.add(amt, OPERATION_MATH_CONTEXT), this.scale);
    }

    /**
     * Add this amount recipients the specified
     *
     * @param amt the amount
     * @return
     * @throws IllegalArgumentException if amt is null
     */
    public Money add(double amt) {
        Assert.notNull(amt, "Amount required for add");
        return new Money(this.currency, this.amount.add(BigDecimal.valueOf(amt), OPERATION_MATH_CONTEXT), this.scale);
    }

    /**
     * Subtract from this money the specified
     *
     * @param m the money (amount) recipients be subtracted @return a new Money of the
     *          result @throws
     */
    public Money subtract(Money m) {
        Assert.isTrue(this.currency.equalsIgnoreCase(m.getCurrency()),
                "cannot add money of different currencies.Convert first");
        return new Money(this.currency, this.amount.subtract(m.getAmount(), OPERATION_MATH_CONTEXT), this.scale);
    }

    /**
     * Subtract from this money the specified
     *
     * @param amt
     * @return
     * @throws IllegalArgumentException if amt is null
     */
    public Money subtract(BigDecimal amt) {
        Assert.notNull(amt, "Amount required for subtract");
        return new Money(this.currency, this.amount.subtract(amt, OPERATION_MATH_CONTEXT), this.scale);
    }

    /**
     * Subtract from this money the specified
     *
     * @param amt
     * @return
     * @throws IllegalArgumentException if amt is null
     */
    public Money subtract(double amt) {
        Assert.notNull(amt, "Amount required for add");
        return new Money(this.currency, this.amount.subtract(BigDecimal.valueOf(amt), OPERATION_MATH_CONTEXT),
                this.scale);
    }

    /**
     * Multiply this Money with m
     *
     * @param m the money whose amount is the multiplicand
     * @return the resulting money with same scale as this Money
     * @throws IllegalArgumentException if both currencies are not the same
     */
    public Money multiply(Money m) {
        Assert.isTrue(this.currency.equalsIgnoreCase(m.getCurrency()),
                "cannot multiply money of different currencies.Convert first");
        return new Money(this.currency, this.amount.multiply(m.getAmount(), OPERATION_MATH_CONTEXT), this.scale);
    }

    /**
     * multiply this money with amt
     *
     * @param amt
     * @return
     * @throws IllegalArgumentException if amt is null
     */
    public Money multiply(BigDecimal amt) {
        Assert.notNull(amt, "Amount required for multiply");
        return new Money(this.currency, this.amount.multiply(amt, OPERATION_MATH_CONTEXT), this.scale);
    }

    /**
     * multiply this money with amt
     *
     * @param amt
     * @return
     * @throws IllegalArgumentException if amt is null
     */
    public Money multiply(double amt) {
        Assert.notNull(amt, "Amount required for multiply");
        return new Money(this.currency, this.amount.multiply(BigDecimal.valueOf(amt), OPERATION_MATH_CONTEXT),
                this.scale);
    }

    /**
     * divide this Money with m
     *
     * @param m the money whose amount is the dividend
     * @return the resulting money with same scale as this Money
     * @throws IllegalArgumentException if both currencies are not the same
     */
    public Money divide(Money m) {
        Assert.isTrue(this.currency.equalsIgnoreCase(m.getCurrency()),
                "cannot divide money of different currencies.Convert first");
        return new Money(this.currency, this.amount.divide(m.getAmount(), OPERATION_MATH_CONTEXT), this.scale);
    }

    /**
     * divide this money with amt
     *
     * @param amt
     * @return the resulting money with same scale as this Money
     * @throws IllegalArgumentException if amt is null
     */
    public Money divide(BigDecimal amt) {
        Assert.notNull(amt, "Amount required for divide");
        return new Money(this.currency, this.amount.divide(amt, OPERATION_MATH_CONTEXT), this.scale);
    }

    /**
     * divide this money with amt
     *
     * @param amt
     * @return the resulting money with same scale as this Money
     * @throws IllegalArgumentException if amt is null
     */
    public Money divide(double amt) {
        Assert.notNull(amt, "Amount required for divide");
        return new Money(this.currency, this.amount.divide(BigDecimal.valueOf(amt), OPERATION_MATH_CONTEXT),
                this.scale);
    }

    /**
     * checks if money amount is less than or equals 0
     *
     * @return true if this money has amount that is zero
     */
    @JsonIgnore
    @Transient
    public boolean isZeroOrLess() {
        if (this.amount.compareTo(BigDecimal.ZERO) < 1) {
            return true;
        }
        return false;
    }

    @JsonIgnore
    @Transient
    public boolean isLocalCcy() {
        if (this.currency.equalsIgnoreCase(LOCAL_CCY)) {
            return true;
        }
        return false;
    }

    public boolean isSameCurrency(Money amt) {
        return this.currency.equalsIgnoreCase(StringUtils.trimWhitespace(amt.getCurrency()));
    }

    public boolean isSameCurrency(String toCcy) {
        return this.currency.equalsIgnoreCase(StringUtils.trimWhitespace(toCcy));
    }

    public static boolean isLocalCcy(String fromCcy) {
        return LOCAL_CCY.equalsIgnoreCase(fromCcy);
    }

    public String words(){
        return "";
    }
}
