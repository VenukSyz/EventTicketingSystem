package com.example.server.controller;

import com.example.server.dto.VendorDTO;
import com.example.server.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/vendor")
@CrossOrigin
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping("/saveVendor")
    public ResponseEntity<VendorDTO> saveVendor(@RequestBody VendorDTO vendorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendorService.saveVendor(vendorDTO));
    }
}
