package com.example.server.controller;

import com.example.server.dto.ApiResponse;
import com.example.server.dto.ConfigurationDTO;
import com.example.server.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/configuration")
@CrossOrigin
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;

    @GetMapping("/getAllConfigurations")
    public ResponseEntity<ApiResponse<List<ConfigurationDTO>>> getAllConfigurations() {
        return ResponseEntity.ok( new ApiResponse<>(configurationService.getAllConfigurations()));
    }

    @PostMapping("/saveUpdateConfiguration")
    public ResponseEntity<ApiResponse<ConfigurationDTO>> saveUpdateConfiguration(@RequestBody ConfigurationDTO configurationDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(configurationService.saveUpdateConfiguration(configurationDTO)));
    }

    @DeleteMapping("/deleteConfiguration/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteConfiguration(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(configurationService.deleteConfiguration(id)));
    }
}
