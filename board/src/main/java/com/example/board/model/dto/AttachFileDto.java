package com.example.board.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AttachFileDto {

    private Long boardId;

    private List<MultipartFile> attachFiles;
}
