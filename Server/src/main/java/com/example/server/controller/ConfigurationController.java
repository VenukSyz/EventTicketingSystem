package com.example.server.controller;

import com.example.server.dto.ApiResponse;
import com.example.server.dto.ConfigurationDTO;
import com.example.server.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling API requests related to system configurations.
 * Provides endpoints for retrieving, creating/updating, and deleting configurations.
 */
@RestController
@RequestMapping(value = "api/configuration")
@CrossOrigin(origins = {"http://localhost:4200", "https://event-ticketing-system-eight.vercel.app"})
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;

    /**
     * Retrieves all configurations in the system.
     *
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with a list of {@link ConfigurationDTO} objects
     */
    @GetMapping("/getAllConfigurations")
    public ResponseEntity<ApiResponse<List<ConfigurationDTO>>> getAllConfigurations() {
        return ResponseEntity.ok( new ApiResponse<>(configurationService.getAllConfigurations()));
    }

    /**
     * Creates or updates a configuration in the system.
     *
     * @param configurationDTO the configuration data to save or update
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with the saved/updated {@link ConfigurationDTO}
     */
    @PostMapping("/saveUpdateConfiguration")
    public ResponseEntity<ApiResponse<ConfigurationDTO>> saveUpdateConfiguration(@RequestBody ConfigurationDTO configurationDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(configurationService.saveUpdateConfiguration(configurationDTO)));
    }

    /**
     * Deletes a configuration based on its unique identifier.
     *
     * @param id the ID of the configuration to delete
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with a boolean indicating success or failure
     */
    @DeleteMapping("/deleteConfiguration/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteConfiguration(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(configurationService.deleteConfiguration(id)));
    }
}
