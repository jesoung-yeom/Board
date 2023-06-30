package com.example.board.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Date;

@Entity
@Data
@Table
@Accessors(chain = true)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

}
