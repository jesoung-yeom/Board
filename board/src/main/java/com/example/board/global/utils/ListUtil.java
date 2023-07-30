package com.example.board.global.utils;

import com.example.board.model.AttachFile;
import com.example.board.model.dto.AttachFileDto;
import com.example.board.model.dto.FileSeparationDto;

import java.util.List;

public class ListUtil {
    public static FileSeparationDto convertFileSeparationDto(List<AttachFile> attachFileList, List<AttachFileDto> attachFileDtoList) {
        FileSeparationDto fileSeparationDto = FileSeparationDto.builder()
                .attachFileList(attachFileList)
                .attachFileDtoList(attachFileDtoList)
                .build();
        return fileSeparationDto;
    }
}
