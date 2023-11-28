package com.github.ata.http.ktor.controller

import com.github.ata.http.dto.response.RetrieveSolicitationsResponse.Companion.fromSolicitationOutput
import com.github.ata.usecase.solicitation.SolicitationHandler
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Location("/retrieve_solicitations")
class RetrieveSolicitations {
}

fun Route.retrieveSolicitations(
    service: SolicitationHandler
) = get<RetrieveSolicitations> {
    val output = service.retrieveAllSolicitation()
    val  response = fromSolicitationOutput(output)
    call.respond(response)
}