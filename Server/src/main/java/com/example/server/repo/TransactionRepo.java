package com.example.server.repo;

import com.example.server.entity.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<TransactionRecord, Long> {
}
