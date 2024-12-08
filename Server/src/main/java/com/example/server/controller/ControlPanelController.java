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

@RestController
@RequestMapping(value = "api/controlPanel")
@CrossOrigin
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


    public ControlPanelController(TicketStatusService ticketStatusService) {
        this.ticketStatusService = ticketStatusService;
    }

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<String>> startSystem(@RequestBody ControlPanelDTO controlPanelDTO) {
        ConfigurationDTO configurationDTO = configurationService.getConfigurationById(controlPanelDTO.getId());
        eventDTO = eventService.saveUpdateEvent(new EventDTO(0, controlPanelDTO.getEventName(), controlPanelDTO.getTicketPrice(), configurationDTO.getMaxTicketCapacity(), 0));
        system = new EventTicketingLogic(logBroadcaster, configurationDTO,controlPanelDTO.getNoOfVendors(),controlPanelDTO.getNoOfCustomers());
        system.start();
        ticketStatusService.startTicketStatusUpdates(system.getTicketPool(),eventDTO);
        return ResponseEntity.ok(new ApiResponse<>("System started successfully"));
    }

    @PostMapping("/resume")
    public ResponseEntity<ApiResponse<String>> resumeSystem() {
        system.start();
        ticketStatusService.startTicketStatusUpdates(system.getTicketPool(), eventDTO);
        return ResponseEntity.ok(new ApiResponse<>("System resumed successfully"));
    }

    @PostMapping("/stop")
    public ResponseEntity<ApiResponse<String>> stopSystem() {
        system.stop();
        ticketStatusService.stopTicketStatusUpdates();
        return ResponseEntity.ok(new ApiResponse<>("System stopped successfully"));
    }
}
