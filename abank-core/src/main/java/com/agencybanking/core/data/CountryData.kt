package com.agencybanking.core.data

import java.util.*

class CountryData : Comparator<CountryData> {

    var name: String? = null
        set(name) {
            field = this.name
        }
    var displayName: String? = null
        set(displayName) {
            field = this.displayName
        }

    override fun compare(c1: CountryData, c2: CountryData): Int {
        return c1.name!!.compareTo(c2.name!!)
    }

}
