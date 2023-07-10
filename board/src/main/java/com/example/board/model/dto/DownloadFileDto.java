package com.example.board.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DownloadFileDto {

    private String fileName;

    private byte[] file;

    private Long fileSize;

}
