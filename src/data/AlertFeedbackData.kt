package com.jj.smarthouseserver.data

data class AlertFeedbackData(
    val deviceName: String? = null,
    val timeFromStart: String,
    val additionalInfo: List<String>
)