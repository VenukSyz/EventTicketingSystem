package com.example.server.service;

import com.example.server.dto.VendorDTO;
import com.example.server.entity.Vendor;
import com.example.server.repo.VendorRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class VendorService {

    @Autowired
    private VendorRepo vendorRepo;

    @Autowired
    private ModelMapper modelMapper;

    public VendorDTO saveVendor(VendorDTO vendorDTO) {
        vendorRepo.save(modelMapper.map(vendorDTO, Vendor.class));
        return vendorDTO;
    }
}
