package com.example.board.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class AttachFileDto {

    private Long boardId;

    private MultipartFile multipartFile;

    private String filePath;
    
}
