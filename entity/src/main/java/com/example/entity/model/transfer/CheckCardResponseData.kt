package com.example.entity.model.transfer

import com.example.entity.model.auth.ApiError

data class CheckCardResponseData (
    val cardNumber: String,
    val ownerName : String
)
data class CheckCardResponse(
    val success : Boolean,
    val data: CheckCardResponseData?,
    val error : ApiError
)
