package com.example.board.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Data
@Builder
public class BoardFileDto {

    private Long id;

    private Long boardId;

    private Long commentId;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private String filePath;

    private String fileExtension;

    private Date uploadedAt;
}
