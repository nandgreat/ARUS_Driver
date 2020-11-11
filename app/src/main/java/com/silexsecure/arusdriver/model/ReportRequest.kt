package com.silexsecure.arusdriver.model

data class ReportRequest(
        val customerID: Int? = null,
        val operatorID: Int? = null,
        var serviceTypeID: Int,
        val classOfServiceID: Int? = null,
        val ratingID: Int? = null,
        val area: String? = null,
        val description: String? = null,
        val iMEI: String? = null,
        val statesID: Int? = null,
        val latitude: Any? = null,
        val longitude: Any? = null,
        val frequencyOfOccurenceID: Int? = null
) {
    var _serviceTypeID: Int = serviceTypeID
}