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

/**
 * Service class for managing configuration data.
 * This class handles CRUD operations related to configurations.
 */
@Service
@Transactional
public class ConfigurationService {
    @Autowired
    private ConfigurationRepo configurationRepo;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Retrieves all configurations from the database.
     *
     * @return a list of ConfigurationDTO objects representing all configurations
     */
    public List<ConfigurationDTO> getAllConfigurations() {
        List<Configuration> configurations = configurationRepo.findAll();
        return modelMapper.map(configurations, new TypeToken<List<ConfigurationDTO>>(){}.getType());
    }

    /**
     * Saves or updates a configuration in the database.
     *
     * @param configurationDTO the configuration data to be saved or updated
     * @return the saved or updated ConfigurationDTO object
     */
    public ConfigurationDTO saveUpdateConfiguration(ConfigurationDTO configurationDTO) {
        Configuration configuration = configurationRepo.save(modelMapper.map(configurationDTO, Configuration.class));
        return modelMapper.map(configuration, ConfigurationDTO.class);
    }

    /**
     * Retrieves a configuration by its ID.
     *
     * @param id the ID of the configuration to be retrieved
     * @return the ConfigurationDTO object representing the configuration
     * @throws EntityNotFoundException if no configuration is found with the provided ID
     */
    public ConfigurationDTO getConfigurationById(long id) {
        Optional<Configuration> configuration = configurationRepo.findById(id);
        if (configuration.isPresent()) {
            return modelMapper.map(configuration.get(), ConfigurationDTO.class);
        } else {
            throw new EntityNotFoundException("Configuration not found with ID: " + id);
        }
    }

    /**
     * Deletes a configuration by its ID.
     *
     * @param id the ID of the configuration to be deleted
     * @return true if the configuration was successfully deleted, false otherwise
     */
    public boolean deleteConfiguration(Long id) {
        configurationRepo.deleteById(id);
        return true;
    }
}
