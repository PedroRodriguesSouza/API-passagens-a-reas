package com.github.ata.integration.http.dto

import java.lang.IllegalArgumentException

sealed class StepResponse {

    data class StepSuccess(
        val payload:String,
        val headers: Map<String, List<String>>
    ): StepResponse(){
        fun getHeadersByKey(key: String): String{
            return headers[key]?.joinToString()?: throw IllegalArgumentException("Header with key: $key does not exist")
        }
    }

    data class StepError(val payload: String): StepResponse()
}