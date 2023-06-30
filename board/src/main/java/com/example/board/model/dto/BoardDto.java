package com.example.board.model.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class BoardDto {

    private String title;

    private String content;

    private String userName;

    private Date createdAt;

    private Date updatedAt;

}
