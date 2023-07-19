package com.example.board.model.dto;

import com.example.board.model.AttachFile;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Data
@NoArgsConstructor
public class DownloadFileDto {

    private String fileName;

    private byte[] file;

    private Long fileSize;

    public DownloadFileDto(AttachFile attachFile) throws IOException {
        this.fileName = attachFile.getFileName();
        this.file = FileUtils.readFileToByteArray(new File(attachFile.getFilePath()));
        this.fileSize = attachFile.getFileSize();
    }
}
