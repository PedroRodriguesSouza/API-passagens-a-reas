package integration.ticket.latam.step

import com.github.ata.integration.http.ConnectionHttpClient
import com.github.ata.integration.http.dto.ClientHttpResponse
import com.github.ata.integration.http.dto.StepResponse.StepSuccess
import com.github.ata.integration.http.dto.StepResponse.StepError
import com.github.ata.integration.ticket.latam.dto.Constants.BASE_SERVICE_URL
import com.github.ata.integration.ticket.latam.step.GetCookiesStep
import io.mockk.every
import io.mockk.mockk
import java.net.HttpURLConnection
import kotlin.test.Test
import kotlin.test.assertEquals

class GetCookiesStepTest{
    private val httpClient: ConnectionHttpClient = mockk()
    private val step = GetCookiesStep(httpClient)

    //success StepSeccess
    @Test
    fun `should return StepSuccess when execute step successfully`(){
        val headers = mapOf(
            "Set-Cookie" to listOf("Cookie 1", "Cookie 2")
        )

        every {
            httpClient.get(BASE_SERVICE_URL)
        }returns ClientHttpResponse(
            statusCode = HttpURLConnection.HTTP_OK,
            body = "SUCCESS RESPONSE",
            headers = headers
        )
        val expected = StepSuccess(payload = "SUCCESS RESPONSE", headers = headers)
        val actual = step.doRequest()
        assertEquals(expected, actual)
    }

    //success StepError
    @Test
    fun `should return StepError when not execute step successfully`(){

        every {
            httpClient.get(BASE_SERVICE_URL)
        }returns ClientHttpResponse(
            statusCode = HttpURLConnection.HTTP_BAD_REQUEST,
            body = "ERROR RESPONSE",
            headers = emptyMap()
        )
        val expected = StepError(payload = "ERROR RESPONSE")
        val actual = step.doRequest()
        assertEquals(expected, actual)
    }
}