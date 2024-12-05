package com.example.server.service;

import com.example.server.dto.TicketStatusDTO;
import com.example.server.logic.TicketPoolLogic;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;

@Service
public class TicketStatusService {

    private final LogBroadcaster logBroadcaster;
    private final ThreadPoolTaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;

    public TicketStatusService(LogBroadcaster logBroadcaster, ThreadPoolTaskScheduler taskScheduler) {
        this.logBroadcaster = logBroadcaster;
        this.taskScheduler = taskScheduler;
    }

    public void startTicketStatusUpdates(TicketPoolLogic ticketPool) {
        if (scheduledTask == null || scheduledTask.isCancelled()) {
            scheduledTask = taskScheduler.scheduleAtFixedRate(() -> sendTicketStatus(ticketPool), 2000);
            System.out.println("System started: Ticket updates are now running.");
        }
    }

    public void stopTicketStatusUpdates() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            System.out.println("System stopped: Ticket updates have been halted.");
        }
    }

    public void sendTicketStatus(TicketPoolLogic ticketPool) {
        TicketStatusDTO status = new TicketStatusDTO(ticketPool.getAvailableTicketsInPool(), ticketPool.getSoldOutTickets(), ticketPool.getToBeSoldOutTickets(), ticketPool.getTicketsAddedByVendors());
        logBroadcaster.sendStatusUpdate(status);
    }
}
