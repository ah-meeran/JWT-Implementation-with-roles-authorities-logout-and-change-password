package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.user.CustomerUser;

public interface CustomerUserRepository extends JpaRepository<CustomerUser, Integer> {
    Optional<CustomerUser> findByEmail(String email);
}