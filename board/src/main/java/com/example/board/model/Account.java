package com.example.board.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@NoArgsConstructor
@Table(name = "account")
@Accessors(chain = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "login_id")
    private String userId;

    @Column(name = "email")
    private String userEmail;

    @Column(name = "password")
    private String userPw;

    @Column(name = "name")
    private String userName;

}
