package com.example.server.repo;

import com.example.server.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepo extends JpaRepository<Configuration, Long> {
}
