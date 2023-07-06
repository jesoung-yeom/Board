package com.example.board.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;

@Data
@Builder
public class BoardDto {

    private Long id;

    private String userId;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
