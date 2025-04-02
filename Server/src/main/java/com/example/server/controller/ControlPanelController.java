package com.example.server.controller;

import com.example.server.dto.ApiResponse;
import com.example.server.dto.ConfigurationDTO;
import com.example.server.dto.ControlPanelDTO;
import com.example.server.dto.EventDTO;
import com.example.server.logic.EventTicketingLogic;
import com.example.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing the control panel of the ticketing system.
 * Provides endpoints to start, resume, and stop the ticketing system.
 */
@RestController
@RequestMapping(value = "api/controlPanel")
@CrossOrigin(origins = {"http://localhost:4200", "https://event-ticketing-system-eight.vercel.app"})
public class ControlPanelController {
    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private LogBroadcaster logBroadcaster;

    @Autowired
    private EventService eventService;

    private final TicketStatusService ticketStatusService;

    private EventTicketingLogic system;

    private EventDTO eventDTO;

    /**
     * Constructor for ControlPanelController.
     *
     * @param ticketStatusService the service for handling ticket status updates
     */
    public ControlPanelController(TicketStatusService ticketStatusService) {
        this.ticketStatusService = ticketStatusService;
    }

    /**
     * Starts the ticketing system with the provided configuration and event details.
     *
     * @param controlPanelDTO the control panel data containing system configuration and event details
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with a success message
     */
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<String>> startSystem(@RequestBody ControlPanelDTO controlPanelDTO) {
        ConfigurationDTO configurationDTO = configurationService.getConfigurationById(controlPanelDTO.getConfigId());
        eventDTO = eventService.saveUpdateEvent(new EventDTO(0, controlPanelDTO.getEventName(), controlPanelDTO.getTicketPrice(), configurationDTO.getMaxTicketCapacity(), 0));
        system = new EventTicketingLogic(logBroadcaster, configurationDTO,controlPanelDTO.getNoOfVendors(),controlPanelDTO.getNoOfCustomers());
        system.start(false);
        ticketStatusService.startTicketStatusUpdates(system.getTicketPool(),eventDTO);
        return ResponseEntity.ok(new ApiResponse<>("System started successfully"));
    }

    /**
     * Resumes the ticketing system if it was previously stopped.
     *
     * @param controlPanelDTO the control panel data containing system configuration and event details
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with a success message
     */
    @PostMapping("/resume")
    public ResponseEntity<ApiResponse<String>> resumeSystem(@RequestBody ControlPanelDTO controlPanelDTO) {
        system.resume(controlPanelDTO.getNoOfVendors(), controlPanelDTO.getNoOfCustomers());
        ticketStatusService.startTicketStatusUpdates(system.getTicketPool(), eventDTO);
        return ResponseEntity.ok(new ApiResponse<>("System resumed successfully"));
    }

    /**
     * Stops the ticketing system, halting all operations.
     *
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with a success message
     */
    @PostMapping("/stop")
    public ResponseEntity<ApiResponse<String>> stopSystem() {
        system.stop();
        ticketStatusService.stopTicketStatusUpdates();
        return ResponseEntity.ok(new ApiResponse<>("System stopped successfully"));
    }
}
