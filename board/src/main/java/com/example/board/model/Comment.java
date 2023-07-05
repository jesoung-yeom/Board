package com.example.board.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Date;

@Entity
@Data
@Table(name = "board_comment")
@Accessors(chain = true)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "board_info_id")
    private Long boardId;

    @Column(name = "account_email")
    private String userEmail;

    @Column(name = "content")
    private String contentOfComment;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

}
