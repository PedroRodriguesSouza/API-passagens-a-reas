package usecase.retrieve

import com.github.ata.usecase.retrieve.RetrieveTicket
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import com.github.ata.usecase.integration.AirlineTicketIntegration
import com.github.ata.usecase.retrieve.dto.RetrieveTicketOutput.RetrieveTicketSuccess
import com.github.ata.usecase.integration.dto.IntegrationOutput.IntegrationSuccess
import com.github.ata.usecase.integration.dto.IntegrationOutput.IntegrationError
import com.github.ata.usecase.retrieve.dto.RetrieveTicketOutput.RetrieveTicketError
import kotlin.test.Test
import util.Modelbuilder.createAirlineTicketIntegrationInput
import util.Modelbuilder.createRetrieveTicketInput
import util.Modelbuilder.createAirlineTicketIntegrationOutput
import util.Modelbuilder.createTicket

class RetrieveTicketTest{
    private val firstIntegration: AirlineTicketIntegration = mockk()
    private val secondIntegration: AirlineTicketIntegration = mockk()
    private val retrieveTicket = RetrieveTicket(
        listOf(firstIntegration, secondIntegration)
    )
     // retrieve success
    @Test
    fun `should return retrieve success with smallest price`(){
        val integrationInput  = createAirlineTicketIntegrationInput()
        val retriveTicketInput = createRetrieveTicketInput()

         val firstIntegrationResponse = IntegrationSuccess(
             data = createAirlineTicketIntegrationOutput(
                 companyName = "LATAM",
                 lowestPrice = 1100.00,
             )
         )

         val secondIntegrationResponse = IntegrationSuccess(
             data = createAirlineTicketIntegrationOutput(
                 companyName = "GO",
                 lowestPrice = 1500.00,
             )
         )

         every {
            firstIntegration.integrate(integrationInput)
         }  returns firstIntegrationResponse

         every {
            secondIntegration.integrate(integrationInput)
         }  returns secondIntegrationResponse

         val expected = RetrieveTicketSuccess (
            ticket = createTicket(
                companyName = "LATAM",
                airportOrigin = "FLN",
                airportDestination = "GRU",
                date = "21-11-2023",
                lowestPrice = 1100.00
            )
         )
        val actual = retrieveTicket.retrieve(retriveTicketInput)

         assertEquals(expected, actual)
    }

     // retrieve success with one integration fail

    @Test
    fun `should return retrieve success with smallest price and one integration fail`(){
        val integrationInput  = createAirlineTicketIntegrationInput()
        val retriveTicketInput = createRetrieveTicketInput()

        val firstIntegrationResponse = IntegrationSuccess(
            data = createAirlineTicketIntegrationOutput(
                companyName = "LATAM",
                lowestPrice = 1100.00,
            )
        )

        val secondIntegrationResponse = IntegrationError(
            message = "Error"
        )

        every {
            firstIntegration.integrate(integrationInput)
        }  returns firstIntegrationResponse

        every {
            secondIntegration.integrate(integrationInput)
        }  returns secondIntegrationResponse

        val expected = RetrieveTicketSuccess (
            ticket = createTicket(
                companyName = "LATAM",
                airportOrigin = "FLN",
                airportDestination = "GRU",
                date = "21-11-2023",
                lowestPrice = 1100.00
            )
        )
        val actual = retrieveTicket.retrieve(retriveTicketInput)

        assertEquals(expected, actual)
    }

     // retrieve success with two valid integration, ticket with same price
     @Test
     fun `should return retrieve success with two valid integration, ticket with same price`(){
         val integrationInput  = createAirlineTicketIntegrationInput()
         val retriveTicketInput = createRetrieveTicketInput()

         val firstIntegrationResponse = IntegrationSuccess(
             data = createAirlineTicketIntegrationOutput(
                 companyName = "LATAM",
                 lowestPrice = 1500.00,
             )
         )

         val secondIntegrationResponse = IntegrationSuccess(
             data = createAirlineTicketIntegrationOutput(
                 companyName = "GO",
                 lowestPrice = 1500.00,
             )
         )

         every {
             firstIntegration.integrate(integrationInput)
         }  returns firstIntegrationResponse

         every {
             secondIntegration.integrate(integrationInput)
         }  returns secondIntegrationResponse

         val expected = RetrieveTicketSuccess (
             ticket = createTicket(
                 companyName = "LATAM",
                 airportOrigin = "FLN",
                 airportDestination = "GRU",
                 date = "21-11-2023",
                 lowestPrice = 1500.00
             )
         )
         val actual = retrieveTicket.retrieve(retriveTicketInput)

         assertEquals(expected, actual)
     }

     // retrieve error all integration fail
     @Test
     fun `should return retrieve error all integration fail`(){
         val integrationInput  = createAirlineTicketIntegrationInput()
         val retriveTicketInput = createRetrieveTicketInput()

         val firstIntegrationResponse = IntegrationError(
             message = "Error"
         )

         val secondIntegrationResponse = IntegrationError(
             message = "Error"
         )

         every {
             firstIntegration.integrate(integrationInput)
         }  returns firstIntegrationResponse

         every {
             secondIntegration.integrate(integrationInput)
         }  returns secondIntegrationResponse

         val expected = RetrieveTicketError(
             message = "Could not extract tickets for FLN to GRU"
         )
         val actual = retrieveTicket.retrieve(retriveTicketInput)

         assertEquals(expected, actual)
     }
}