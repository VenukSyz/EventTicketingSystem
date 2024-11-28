package com.example.server.service;

import com.example.server.dto.ConfigurationDTO;
import com.example.server.entity.Configuration;
import com.example.server.repo.ConfigurationRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public ConfigurationDTO saveUpdateConfiguration(ConfigurationDTO configurationDTO) {
        configurationRepo.save(modelMapper.map(configurationDTO, Configuration.class));
        return configurationDTO;
    }

    public ConfigurationDTO getConfigurationById(long id) {
        Optional<Configuration> configuration = configurationRepo.findById(id);
        if (configuration.isPresent()) {
            return modelMapper.map(configuration.get(), ConfigurationDTO.class);
        } else {
            throw new EntityNotFoundException("Configuration not found with ID: " + id);
        }
    }

    public boolean deleteConfiguration(Long id) {
        configurationRepo.deleteById(id);
        return true;
    }
}
