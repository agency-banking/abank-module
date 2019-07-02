/**
 *
 */
package com.agencybanking.core.data

import com.agencybanking.core.el.Label
import javax.persistence.Embeddable

/**
 * Store a name/value pair
 *
 * @author dubic
 */
@Embeddable
class NameValue : Data {

    @Label(value = "Code")
    var id: String? = null
    @Label(value = "Name")
    var name: String? = null

    constructor() {}

    constructor(id: String, name: String) {
        this.id = id
        this.name = name
    }

    constructor(baseEnum: BaseEnum) {
        this.id = baseEnum.name
        this.name = baseEnum.description
    }
}
