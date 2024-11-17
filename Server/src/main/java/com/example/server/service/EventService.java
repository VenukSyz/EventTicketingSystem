package com.example.server.service;

import com.example.server.dto.EventDTO;
import com.example.server.entity.Event;
import com.example.server.repo.EventRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EventService {
    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private ModelMapper modelMapper;

    public EventDTO saveEvent(EventDTO eventDTO) {
        eventRepo.save(modelMapper.map(eventDTO, Event.class));
        return eventDTO;
    }
}
