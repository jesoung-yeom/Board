package com.example.board.model.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class BoardDto {

    private Long id;

    private String title;

    private String content;

    private String userId;

    private Date createdAt;

    private Date updatedAt;

}
