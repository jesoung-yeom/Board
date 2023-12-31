package com.example.board.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AttachFileDto {

    private Long boardId;

    private MultipartFile multipartFile;

    private String filePath;

}
