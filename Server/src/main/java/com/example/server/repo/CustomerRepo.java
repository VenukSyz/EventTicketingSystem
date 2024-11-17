package com.example.server.repo;

import com.example.server.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepo extends JpaRepository<Customer, Long> { //entity name, primary key type

    @Query(value = "SELECT * FROM CUSTOMER WHERE ID = ?1", nativeQuery = true)
    Customer getCustomerById(String id);

    @Query(value = "SELECT * FROM CUSTOMER WHERE EMAIL = ?1", nativeQuery = true)
    Customer getCustomerByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNum(String phoneNum);
}
