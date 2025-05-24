package com.example.demo.repository;

import com.example.demo.model.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("select t from Token t where t.customerUser.id = :id and t.expired = false and t.revoked = false")
    List<Token> findAllValidToken(Integer id);

    Optional<Token> findByToken(String token);


}
