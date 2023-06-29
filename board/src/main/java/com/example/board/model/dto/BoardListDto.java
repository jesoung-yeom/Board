package com.example.board.model.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class BoardListDto {

    private String title;

    private String content;

    private String name;

    private Date date;

}
