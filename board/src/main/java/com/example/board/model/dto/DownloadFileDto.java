package com.example.board.model.dto;

import com.example.board.model.AttachFile;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Slf4j
@Data
@NoArgsConstructor
public class DownloadFileDto {

    private String fileName;

    private byte[] file;

    private Long fileSize;

    public DownloadFileDto(AttachFile attachFile) {
        try {
            this.fileName = attachFile.getFileName();
            this.file = FileUtils.readFileToByteArray(new File(attachFile.getFilePath()));
            this.fileSize = attachFile.getFileSize();
        } catch (IOException e) {
            log.error("Occurred IOException during file conversion");
        }
    }
}
