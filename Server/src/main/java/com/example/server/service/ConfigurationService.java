package com.example.server.service;

import com.example.server.dto.ConfigurationDTO;
import com.example.server.entity.Configuration;
import com.example.server.repo.ConfigurationRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ConfigurationService {
    @Autowired
    private ConfigurationRepo configurationRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<ConfigurationDTO> getAllConfigurations() {
        List<Configuration> configurations = configurationRepo.findAll();
        return modelMapper.map(configurations, new TypeToken<List<ConfigurationDTO>>(){}.getType());
    }
}
