package com.example.demo.model.token;

import com.example.demo.model.user.CustomerUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity

public class Token {

    @Id
    @GeneratedValue
    private Integer id;
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "customer_user_id")
    private CustomerUser customerUser;

}
