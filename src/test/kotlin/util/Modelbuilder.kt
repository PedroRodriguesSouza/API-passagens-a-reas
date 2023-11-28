package util

import com.github.ata.integration.ticket.latam.dto.*
import com.github.ata.entity.ticket.Airport
import com.github.ata.entity.ticket.Company
import com.github.ata.entity.ticket.Ticket
import com.github.ata.usecase.integration.dto.AirlineTicketIntegrationInput
import com.github.ata.usecase.integration.dto.AirlineTicketIntegrationOutput
import com.github.ata.usecase.retrieve.dto.RetrieveTicketInput

object Modelbuilder {
    fun createTicket(
        companyName: String  = "Company",
        airportOrigin: String = "FLB",
        airportDestination: String = "GRU",
        date: String = "21-11-2023",
        lowestPrice: Double = 1100.00
    ) = Ticket(
        company = Company(companyName),
        origin  = Airport(airportOrigin),
        destination = Airport(airportDestination),
        date =  date,
        price = lowestPrice
    )
    fun createAirlineTicketIntegrationInput(
        origin:  String = "FLN",
        destination: String = "GRU",
        date: String = "21-11-2023"
    ) = AirlineTicketIntegrationInput(
        origin = origin,
        destination = destination,
        date = date
    )
    fun createRetrieveTicketInput(
        origin:  String = "FLN",
        destination: String = "GRU",
        date: String = "21-11-2023"
    ) = RetrieveTicketInput(
        origin = origin,
        destination = destination,
        date = date
    )
    fun createAirlineTicketIntegrationOutput(
        companyName: String = "Company",
        lowestPrice: Double = 1100.00,
        departureDate: String = "21-11-2023",
        airportOrigin: String = "FLN",
        airportDestination: String = "GRU"
    ) = AirlineTicketIntegrationOutput(
        companyName = companyName,
        lowestPrice = lowestPrice,
        departureDate = departureDate,
        airportOrigin = airportOrigin,
        airportDestination = airportDestination
    )

    fun createLatamTicketRequest(
        origin: String = "FLN",
        destination: String = "GRU",
        outFrom: String = "2023-11-27"
    ) = LatamTicketRequest(
        origin = origin,
        destination  = destination,
        outFrom = outFrom
    )

    fun createLatamTicketResponse() = LatamTicketResponse(
        content = listOf(createLatamContentResponse())
    )

    fun createLatamContentResponse(
        departure: String = "2022-07-01T18:40:00",
        originIataCode: String = "FLN",
        destinationIataCode: String = "GRU",
        amount: Double = 532.48
    ) = Content(
        summary =  Summary(
            origin = Origin(
                departure = departure,
                iataCode = originIataCode
            ),
            destination = Destination(
                iataCode = destinationIataCode,
            ),
            lowestPrice = LowestPrice(
                amount = amount,
            )
        )
    )
}



































