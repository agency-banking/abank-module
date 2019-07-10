package com.agencybanking.core.data

import java.lang.RuntimeException

class UniqueFieldExistsException : RuntimeException() {
    var messages = mutableMapOf<String, List<Any>>()
}