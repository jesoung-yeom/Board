package com.example.board.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Date;

@Entity
@Data
@Table
@Accessors(chain = true)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "content_of_comment")
    private String contentOfComment;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

}
