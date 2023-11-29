package com.github.ata.integration.ticket.latam.extractor

import com.github.ata.integration.exception.ExtractorException
import com.github.ata.integration.http.dto.StepResponse.StepSuccess
import com.github.ata.integration.ticket.latam.dto.LatamTicketRequest
import com.github.ata.integration.ticket.latam.dto.LatamTicketResponse
import com.github.ata.integration.ticket.latam.step.GetCookiesStep
import com.github.ata.integration.ticket.latam.step.GetTicketStep
import com.github.ata.shared.extension.StringExtensions.convertTo

class LatamTicketExtractor(
    private val getCookiesStep: GetCookiesStep,
    private val getTicketStep: GetTicketStep
) {
    fun extract(request: LatamTicketRequest): LatamTicketResponse {
        val cookies  = when(val response  = getCookiesStep.doRequest()){
            is StepSuccess -> response.getHeadersByKey(SET_COOKIE_KEY)
            else -> throw ExtractorException("Error when extract response from GetCookieStep $response")
        }
        return when(val response  = getTicketStep.doRequest(request, cookies)){
            is StepSuccess -> response.payload.convertTo()
            else -> throw ExtractorException("Error when extract response from GetTicketStep $response")
        }
    }

    companion object{
        const val SET_COOKIE_KEY = "Set-Cookie"

    }
}
