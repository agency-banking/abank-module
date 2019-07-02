/**
 *
 */
package com.agencybanking.core.data

import com.fasterxml.jackson.annotation.JsonFormat

/**
 * @author dubic
 */
// @JsonFormat(shape = JsonFormat.Shape.OBJECT)
interface BaseEnum {

    val description: String

    val name: String
}
