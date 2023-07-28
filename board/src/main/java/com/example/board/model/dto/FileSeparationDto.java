package com.example.board.model.dto;

import com.example.board.model.AttachFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileSeparationDto {

    private List<AttachFileDto> attachFileDtoList;

    private List<AttachFile> attachFileList;

}
