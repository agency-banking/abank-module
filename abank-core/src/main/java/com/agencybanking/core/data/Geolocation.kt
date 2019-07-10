package com.agencybanking.core.data

import com.agencybanking.core.el.Label
import javax.persistence.Embeddable

@Embeddable
class Geolocation {
    @Label("Lat")
    var lat: Double? = null

    @Label("Lon")
    var lon: Double? = null
}