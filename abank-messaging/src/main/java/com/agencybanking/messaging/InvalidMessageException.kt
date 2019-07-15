package com.agencybanking.messaging

class InvalidMessageException(val messages: MutableList<String>) : RuntimeException() {
    companion object {
        const val TEMPLATE_TEXT_REQUIRED = "Template or Text is required"
        const val RECIPIENTS_REQUIRED: String = "Message must contain recipients"
    }
}