package com.vitalykalenik.topsecret.models.request

/**
 * @author Vitaly Kalenik
 */
data class LoginRequest(
    val grant_type: String,
    val login: String,
    val password: String
)