package com.example.server.controller;

import com.example.server.dto.ApiResponse;
import com.example.server.dto.EventDTO;
import com.example.server.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing events in the ticketing system.
 * Provides endpoints for retrieving and deleting events.
 */
@RestController
@RequestMapping(value = "api/event")
@CrossOrigin(origins = {"http://localhost:4200", "https://event-ticketing-system-eight.vercel.app"})
public class EventController {
    @Autowired
    private EventService eventService;

    /**
     * Retrieves a list of all events.
     *
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with a list of {@link EventDTO} objects
     */
    @GetMapping("/getAllEvents")
    public ResponseEntity<ApiResponse<List<EventDTO>>> getAllEvents() {
        return ResponseEntity.ok(new ApiResponse<>(eventService.getAllEvents()));
    }

    /**
     * Deletes an event by its ID.
     *
     * @param id the ID of the event to be deleted
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with a boolean indicating the success of the deletion
     */
    @DeleteMapping("/deleteEvent/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteEvent(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(eventService.deleteEvent(id)));
    }
}
