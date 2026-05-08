package com.example.usecase.mapper

import com.example.entity.local.NetworkLogEntity
import com.example.usecase.model.NetworkLog

fun NetworkLogEntity.toDomain(): NetworkLog = NetworkLog(
    id = id,
    method = method,
    url = url,
    code = code,
    requestBody = requestBody,
    responseBody = responseBody,
    message = message,
    timeStamp = timeStamp,
    duration = duration,
)