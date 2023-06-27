package com.example.board.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "uid", nullable = false)
    private Long uid;
    private String id;
    private String pw;
    private String name;
}
