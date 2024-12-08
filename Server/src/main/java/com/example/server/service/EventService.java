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

@Service
@Transactional
public class EventService {
    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private ModelMapper modelMapper;

    public EventDTO saveUpdateEvent(EventDTO eventDTO) {
        Event event = eventRepo.save(modelMapper.map(eventDTO, Event.class));
        return modelMapper.map(event, EventDTO.class);
    }

    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepo.findAll();
        return modelMapper.map(events, new TypeToken<List<EventDTO>>(){}.getType());
    }

    public boolean deleteEvent(Long id) {
        eventRepo.deleteById(id);
        return true;
    }
}
