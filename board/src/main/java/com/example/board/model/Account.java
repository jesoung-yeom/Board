package com.example.board.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Data
@Table
@Accessors(chain = true)
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "uid")
    private Long uid;

    @Column(name = "id")
    private String id;

    @Column(name = "pw")
    private String pw;

    @Column(name = "name")
    private String name;

}
