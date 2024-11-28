package com.example.server.controller;

import com.example.server.dto.ApiResponse;
import com.example.server.dto.ConfigurationDTO;
import com.example.server.dto.ControlPanelDTO;
import com.example.server.logic.EventTicketingLogic;
import com.example.server.service.ConfigurationService;
import com.example.server.service.LogBroadcaster;
import com.example.server.service.TicketStatusService;
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

    private final TicketStatusService ticketStatusService;

    private EventTicketingLogic system;

    public ControlPanelController(TicketStatusService ticketStatusService) {
        this.ticketStatusService = ticketStatusService;
    }

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<String>> startSystem(@RequestBody ControlPanelDTO controlPanelDTO) {
        ConfigurationDTO configurationDTO = configurationService.getConfigurationById(controlPanelDTO.getId());
        system = new EventTicketingLogic(logBroadcaster, configurationDTO,controlPanelDTO.getNoOfVendors(),controlPanelDTO.getNoOfCustomers());
        system.start();
        ticketStatusService.startTicketStatusUpdates(system.getTicketPool());
        return ResponseEntity.ok(new ApiResponse<>("System started successfully"));
    }

    @PostMapping("/resume")
    public ResponseEntity<ApiResponse<String>> resumeSystem() {
        system.start();
        ticketStatusService.startTicketStatusUpdates(system.getTicketPool());
        return ResponseEntity.ok(new ApiResponse<>("System resumed successfully"));
    }

    @PostMapping("/stop")
    public ResponseEntity<ApiResponse<String>> stopSystem() {
        system.stop();
        ticketStatusService.stopTicketStatusUpdates();
        return ResponseEntity.ok(new ApiResponse<>("System stopped successfully"));
    }
}
