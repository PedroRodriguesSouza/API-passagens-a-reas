package com.github.ata.usecase.solicitation.dto

import com.github.ata.entity.ticket.Ticket

data class SolicitationOutput(
    val cheapestTicket: List<Ticket>,
    val summary: Summary
) {
}

data class Summary(
    val error: List<String?>
)