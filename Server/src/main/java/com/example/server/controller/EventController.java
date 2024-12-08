package com.example.server.controller;

import com.example.server.dto.ApiResponse;
import com.example.server.dto.EventDTO;
import com.example.server.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/event")
@CrossOrigin
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/getAllEvents")
    public ResponseEntity<ApiResponse<List<EventDTO>>> getAllEvents() {
        return ResponseEntity.ok(new ApiResponse<>(eventService.getAllEvents()));
    }

    @DeleteMapping("/deleteEvent/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteEvent(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(eventService.deleteEvent(id)));
    }
}
