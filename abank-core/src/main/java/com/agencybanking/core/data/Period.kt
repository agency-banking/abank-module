package com.agencybanking.core.data

import javax.xml.bind.annotation.XmlEnum
import javax.xml.bind.annotation.XmlType

@XmlType(name = "period")
@XmlEnum(String::class)
enum class Period(override val description: String) : BaseEnum {
    DAYS("DAYS"),
    HOURS("HOURS"),
    MINUTES("MINUTES");
}
