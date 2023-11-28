package com.github.ata.usecase.solicitation

import com.github.ata.entity.ticket.Ticket
import com.github.ata.shared.extension.StringExtensions.convertTo
import com.github.ata.shared.file.fileUtils.loadResource
import com.github.ata.usecase.retrieve.RetrieveTicket
import com.github.ata.usecase.retrieve.dto.RetrieveTicketInput
import com.github.ata.usecase.retrieve.dto.RetrieveTicketOutput
import com.github.ata.usecase.retrieve.dto.RetrieveTicketOutput.RetrieveTicketSuccess
import com.github.ata.usecase.retrieve.dto.RetrieveTicketOutput.RetrieveTicketError
import com.github.ata.usecase.solicitation.dto.SolicitationOutput
import com.github.ata.usecase.solicitation.dto.Summary

class SolicitationHandler(
    private val retrieveTicket: RetrieveTicket
) {
    fun retrieveAllSolicitation(): SolicitationOutput {
        val inputs = loadResource(SOLICITATIONS_RESOURCE_PATH).convertTo<List<RetrieveTicketInput>>()
        val retrieveOutputs = inputs.map { input -> retrieveTicket.retrieve(input)  }

        return SolicitationOutput(
            cheapestTicket = getRetrieveSuccess(retrieveOutputs),
            summary = Summary(
                error = getRetrieveError(retrieveOutputs)
            )
        )
    }

    private fun getRetrieveSuccess(output: List<RetrieveTicketOutput>) : List<Ticket>{
        return output
            .asSequence()
            .filterIsInstance<RetrieveTicketSuccess>()
            .map { it.ticket }
            .toList()
    }

    private fun getRetrieveError(output: List<RetrieveTicketOutput>) : List<String?> {
        return output
            .asSequence()
            .filterIsInstance<RetrieveTicketError>()
            .map { it.message }
            .toList()
    }

    companion object {
        const val SOLICITATIONS_RESOURCE_PATH = "/solicitations/solicitations.json"
    }

}