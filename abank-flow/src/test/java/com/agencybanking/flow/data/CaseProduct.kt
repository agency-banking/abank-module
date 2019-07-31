package com.agencybanking.flow.data

import com.agencybanking.core.data.BaseEntity

class CaseProduct : BaseEntity(){
    var name = ""
    var registered = false

    override fun product(): String {
        return "caseproduct"
    }

    override fun module(): String {
        return "tests"
    }
}