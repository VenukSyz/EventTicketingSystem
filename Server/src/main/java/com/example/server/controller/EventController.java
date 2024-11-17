package com.example.server.controller;

import com.example.server.dto.EventDTO;
import com.example.server.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/event")
@CrossOrigin
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping("/saveEvent")
    public ResponseEntity<String> saveEvent(@RequestBody EventDTO eventDTO) {
        eventDTO = eventService.saveEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event Created Successfully");
    }
}
