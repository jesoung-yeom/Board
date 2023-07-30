package com.example.board.model.mapper;

import com.example.board.model.AttachFile;
import com.example.board.model.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper
public interface AttachFileMapper {
    AttachFileMapper MAPPER = Mappers.getMapper(AttachFileMapper.class);


    @Mapping(target = "fileName", expression = "java(attachFileDto.getMultipartFile().getOriginalFilename())")
    @Mapping(target = "fileType", expression = "java(com.example.board.global.enums.EBoard.EFileType.ATTACH.getFileType())")
    @Mapping(target = "fileSize", expression = "java(attachFileDto.getMultipartFile().getSize())")
    @Mapping(target = "fileExtension", expression = "java(com.example.board.global.utils.StringUtil.extractFileExtension(attachFileDto.getMultipartFile().getOriginalFilename()))")
    @Mapping(target = "uploadedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deleted", expression = "java(com.example.board.global.enums.EBoard.EDeletionStatus.EXIST.getStatus())")
    AttachFile toAttachFile(AttachFileDto attachFileDto);

    @Mapping(target = "boardId", source = "boardDto.id")
    @Mapping(target = "fileType", expression = "java(com.example.board.global.enums.EBoard.EFileType.BOARD.getFileType())")
    @Mapping(target = "fileSize", expression = "java((long)fileSize)")
    @Mapping(target = "uploadedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deleted", expression = "java(com.example.board.global.enums.EBoard.EDeletionStatus.EXIST.getStatus())")
    AttachFile toAttachFile(BoardDto boardDto, String fileName, int fileSize, String filePath, String fileExtension);

    AttachFileDto toAttachFileDto(UploadFileDto uploadFileDto, MultipartFile multipartFile, String filePath);

    @Mapping(target = "file", expression = "java(org.apache.commons.io.FileUtils.readFileToByteArray(new java.io.File(attachFile.getFilePath())))")
    DownloadFileDto toDownloadFileDto(AttachFile attachFile) throws IOException;

    @Mapping(target = "attachFileId", source = "attachFile.id")
    PreviewAttachFileDto toPreviewAttachFileDto(AttachFile attachFile);

}
