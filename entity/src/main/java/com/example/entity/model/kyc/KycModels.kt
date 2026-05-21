package com.example.entity.model.kyc

data class KycStatusData(
    val status: String,
    val rejectionReason: String?
)

data class KycSubmitRequest(
    val passportSeries: String,
    val passportNumber: String,
    val birthDate: String,
    val selfieBase64: String
)
