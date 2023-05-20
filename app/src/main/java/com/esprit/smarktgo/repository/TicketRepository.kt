package com.esprit.smarktgo.repository

import com.esprit.smarktgo.model.Ticket
import com.esprit.smarktgo.model.UpdateTicket
import com.esprit.smarktgo.utils.ApiInterface
import com.esprit.smarktgo.utils.RetrofitInstance

class TicketRepository {

    val api = RetrofitInstance.getRetroInstance().create(ApiInterface::class.java)

    suspend fun getAll(): MutableList<Ticket>?
    {
        val request =api.getAllTickets()

        if (request.code()!=200) return null
        return request.body()
    }

    suspend fun update(updateTicket: UpdateTicket): Ticket?
    {
        val request =api.updateTicket(updateTicket)

        if (request.code()!=200) return null
        return request.body()
    }

}