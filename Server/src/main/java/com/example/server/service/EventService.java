package com.example.server.service;

import com.example.server.dto.EventDTO;
import com.example.server.entity.Event;
import com.example.server.repo.EventRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing event data.
 * This class handles CRUD operations related to events.
 */
@Service
@Transactional
public class EventService {
    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Saves or updates an event in the database.
     *
     * @param eventDTO the event data to be saved or updated
     * @return the saved or updated EventDTO object
     */
    public EventDTO saveUpdateEvent(EventDTO eventDTO) {
        Event event = eventRepo.save(modelMapper.map(eventDTO, Event.class));
        return modelMapper.map(event, EventDTO.class);
    }

    /**
     * Retrieves all events from the database.
     *
     * @return a list of EventDTO objects representing all events
     */
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepo.findAll();
        return modelMapper.map(events, new TypeToken<List<EventDTO>>(){}.getType());
    }

    /**
     * Deletes an event by its ID.
     *
     * @param id the ID of the event to be deleted
     * @return true if the event was successfully deleted, false otherwise
     */
    public boolean deleteEvent(Long id) {
        eventRepo.deleteById(id);
        return true;
    }
}
