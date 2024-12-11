package com.example.server.service;

import com.example.server.dto.EventDTO;
import com.example.server.dto.TicketStatusDTO;
import com.example.server.logic.TicketPoolLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;

/**
 * The TicketStatusService class is responsible for managing the periodic updates of the ticket status
 * for an event. It uses a {@link ThreadPoolTaskScheduler} to schedule tasks that periodically
 * retrieve and broadcast the ticket pool status to connected clients via the {@link LogBroadcaster}.
 * The status updates include details such as the number of available tickets, sold-out tickets,
 * and total ticket sales for the event.
 */
@Service
public class TicketStatusService {
    @Autowired
    private EventService eventService;
    private final LogBroadcaster logBroadcaster;
    private final ThreadPoolTaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;

    /**
     * Constructs a new TicketStatusService with the specified {@link LogBroadcaster} and
     * {@link ThreadPoolTaskScheduler} to handle ticket status updates.
     *
     * @param logBroadcaster the broadcaster to send ticket status updates
     * @param taskScheduler the task scheduler for scheduling periodic tasks
     */
    public TicketStatusService(LogBroadcaster logBroadcaster, ThreadPoolTaskScheduler taskScheduler) {
        this.logBroadcaster = logBroadcaster;
        this.taskScheduler = taskScheduler;
    }

    /**
     * Starts periodic updates of the ticket status for the given {@link TicketPoolLogic} and {@link EventDTO}.
     * The status updates are scheduled to run every 2000 milliseconds (2 seconds).
     *
     * @param ticketPool the ticket pool whose status is to be updated
     * @param eventDTO the event data transfer object containing event details
     */
    public void startTicketStatusUpdates(TicketPoolLogic ticketPool, EventDTO eventDTO) {
        if (scheduledTask == null || scheduledTask.isCancelled()) {
            scheduledTask = taskScheduler.scheduleAtFixedRate(() -> sendTicketStatus(ticketPool, eventDTO), 2000);
            System.out.println("System started: Ticket updates are now running.");
        }
    }

    /**
     * Stops the periodic ticket status updates.
     * Cancels the scheduled task and halts further updates.
     */
    public void stopTicketStatusUpdates() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            System.out.println("System stopped: Ticket updates have been halted.");
        }
    }

    /**
     * Sends the current ticket status to connected clients via the {@link LogBroadcaster}.
     * This includes the number of available tickets in the pool, sold-out tickets,
     * tickets to be sold out, and the total ticket sales based on the event's ticket price.
     *
     * The method also updates the event's sold-out ticket count and saves the updated event information.
     *
     * @param ticketPool the ticket pool from which the status is derived
     * @param eventDTO the event data transfer object containing event details
     */
    public void sendTicketStatus(TicketPoolLogic ticketPool, EventDTO eventDTO) {
        TicketStatusDTO status = new TicketStatusDTO(
                ticketPool.getAvailableTicketsInPool(),
                ticketPool.getSoldOutTickets(), ticketPool.
                getToBeSoldOutTickets(),
                ticketPool.getTicketsAddedByVendors(),
                ticketPool.getSoldOutTickets() * eventDTO.getTicketPrice());
        eventDTO.setSoldOutTickets(ticketPool.getSoldOutTickets());
        eventService.saveUpdateEvent(eventDTO);
        logBroadcaster.sendStatusUpdate(status);
    }
}
