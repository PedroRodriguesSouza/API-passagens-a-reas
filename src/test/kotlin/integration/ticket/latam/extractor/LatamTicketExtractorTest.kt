package integration.ticket.latam.extractor

import com.github.ata.integration.exception.ExtractorException
import com.github.ata.integration.ticket.latam.extractor.LatamTicketExtractor
import com.github.ata.integration.http.dto.StepResponse.*
import com.github.ata.integration.ticket.latam.step.GetCookiesStep
import com.github.ata.integration.ticket.latam.step.GetTicketStep
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import com.github.ata.shared.exception.ObjectConversionException
import util.Modelbuilder.createLatamContentResponse
import util.Modelbuilder.createLatamTicketRequest
import util.TestUtils.getFileContent
import kotlin.test.Test

class LatamTicketExtractorTest{

    private val getCookiesStep: GetCookiesStep = mockk()
    private val getTicketStep: GetTicketStep = mockk()
    private val latamTicketExtractor = LatamTicketExtractor(getCookiesStep, getTicketStep)

    //success
    @Test
    fun `should extract latam ticket response successfully`(){
        val request = createLatamTicketRequest()
        val cookies ="Cookie 1, Cookie 2"
        val latamTicketResponse = getFileContent("/tickets/Latam_tickets_response.json")

        every {
            getCookiesStep.doRequest()
        } returns StepSuccess(
            payload = "SUCCESS",
            headers = mapOf(
                "Set-Cookie" to listOf("Cookie 1", "Cookie 2")
            )
        )

        every {
            getTicketStep.doRequest(request, cookies)
        } returns StepSuccess(
            payload = latamTicketResponse,
            headers = emptyMap()
        )

        val expected = createLatamContentResponse()
        val actual = latamTicketExtractor.extract(request).content.first()

        assertEquals(expected, actual)
    }

    @Test
    fun `should throw ExtractorException when get cookies step returns an error`(){
        val request = createLatamTicketRequest()

        every {
            getCookiesStep.doRequest()
        } returns StepError(
            payload = "Error"
        )

        assertThrows<ExtractorException> {
            latamTicketExtractor.extract(request)
        }
    }

    @Test
    fun `should throw IllegalArgumentsException when get cookies does not returns valid headers`(){
        val request = createLatamTicketRequest()

        every {
            getCookiesStep.doRequest()
        } returns StepSuccess(
            payload = "SUCCESS",
            headers = mapOf(
                "Invaid key" to emptyList()
            )
        )

        assertThrows<IllegalArgumentException> {
            latamTicketExtractor.extract(request)
        }
    }

    @Test
    fun `should throw ExtractorException when get ticket step returns an error`(){
        val request = createLatamTicketRequest()
        val cookies ="Cookie 1, Cookie 2"

        every {
            getCookiesStep.doRequest()
        } returns StepSuccess(
            payload = "SUCCESS",
            headers = mapOf(
                "Set-Cookie" to listOf("Cookie 1", "Cookie 2")
            )
        )

        every {
            getTicketStep.doRequest(request, cookies)
        } returns StepError(
            payload = "Error"
        )

        assertThrows<ExtractorException> {
            latamTicketExtractor.extract(request)
        }
    }

    @Test
    fun `should throw ObjectConversionException when get ticket step returns invalid response`(){
        val request = createLatamTicketRequest()
        val cookies ="Cookie 1, Cookie 2"

        every {
            getCookiesStep.doRequest()
        } returns StepSuccess(
            payload = "SUCCESS",
            headers = mapOf(
                "Set-Cookie" to listOf("Cookie 1", "Cookie 2")
            )
        )

        every {
            getTicketStep.doRequest(request, cookies)
        } returns StepSuccess(
            payload = "INVALID RESPONSE",
            headers = emptyMap()
        )

        assertThrows<ObjectConversionException> {
            latamTicketExtractor.extract(request)
        }
    }

}