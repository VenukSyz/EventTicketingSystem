package com.example.server.service;

import com.example.server.dto.CustomerDTO;
import com.example.server.entity.Customer;
import com.example.server.exceptions.DuplicateFieldException;
import com.example.server.repo.CustomerRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ModelMapper modelMapper;

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        if (customerRepo.existsByEmail(customerDTO.getEmail())) {
            throw new DuplicateFieldException("Email already exists: " + customerDTO.getEmail());
        }
        if (customerRepo.existsByPhoneNum(customerDTO.getPhoneNum())) {
            throw new DuplicateFieldException("Phone number already exists: " + customerDTO.getPhoneNum());
        }
        customerRepo.save(modelMapper.map(customerDTO, Customer.class));
        return customerDTO;
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepo.findAll();
        return modelMapper.map(customers, new TypeToken<List<CustomerDTO>>(){}.getType());
    }

    public Optional<CustomerDTO> getCustomerById(String id){
        return Optional.ofNullable(customerRepo.getCustomerById(id))
                .map(customer -> modelMapper.map(customer, CustomerDTO.class));
    }

    public Optional<CustomerDTO> getCustomerByEmail(String email){
        return Optional.ofNullable(customerRepo.getCustomerByEmail(email))
                .map(customer -> modelMapper.map(customer,CustomerDTO.class));
    }
}
