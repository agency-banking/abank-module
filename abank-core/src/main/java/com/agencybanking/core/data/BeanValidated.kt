/**
 *
 */
package com.agencybanking.core.data

import javax.validation.ConstraintViolationException

/**
 * @author dubic
 */
interface BeanValidated {
    fun validateForErrors(): List<String>

    @Throws(ConstraintViolationException::class)
    fun validate()

}
