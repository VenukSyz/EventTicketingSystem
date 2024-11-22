package com.example.server.controller;

import com.example.server.dto.ApiResponse;
import com.example.server.dto.ConfigurationDTO;
import com.example.server.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
