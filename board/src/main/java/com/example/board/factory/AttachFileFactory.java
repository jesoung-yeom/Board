package com.example.board.factory;

import com.example.board.global.EBoard;
import com.example.board.model.AttachFile;
import com.example.board.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class AttachFileFactory {

    public static AttachFile convertAttachFile(AttachFileDto attachFileDto) {
        AttachFile attachFile = new AttachFile();
        attachFile.setBoardId(attachFileDto.getBoardId())
                .setFileName(attachFileDto.getMultipartFile().getOriginalFilename())
                .setFileType(EBoard.EFileType.ATTACH.getFileType())
                .setFilePath(attachFileDto.getFilePath())
                .setFileSize(attachFileDto.getMultipartFile().getSize())
                .setFileExtension(extractFileExtension(attachFileDto.getMultipartFile().getOriginalFilename()))
                .setUploadedAt(LocalDateTime.now())
                .setDeleted(EBoard.EDeletionStatus.EXIST.getStatus());

        return attachFile;
    }

    public static AttachFile convertAttachFile(BoardDto boardDto, String fileName, int fileSize, String filePath, String fileExtension) {
        AttachFile attachFile = new AttachFile();
        attachFile.setBoardId(boardDto.getId())
                .setFileName(fileName)
                .setFileType(EBoard.EFileType.BOARD.getFileType())
                .setFilePath(filePath)
                .setFileSize((long) fileSize)
                .setFileExtension(fileExtension)
                .setUploadedAt(LocalDateTime.now())
                .setDeleted(EBoard.EDeletionStatus.EXIST.getStatus());

        return attachFile;
    }

    public static String extractFileExtension(String target) {
        String[] result = target.split("[.]");

        return result[result.length - 1];
    }

    public static AttachFileDto convertAttachFileDto(UploadFileDto uploadFileDto, MultipartFile multipartFile, String filePath) {
        AttachFileDto attachFileDto = AttachFileDto.builder().boardId(uploadFileDto.getBoardId())
                .multipartFile(multipartFile)
                .filePath(filePath).build();

        return attachFileDto;
    }

    public static DownloadFileDto convertDownloadFileDto(AttachFile attachFile) {
        try {
            DownloadFileDto downloadFileDto = DownloadFileDto.builder()
                    .fileName(attachFile.getFileName())
                    .file(FileUtils.readFileToByteArray(new File(attachFile.getFilePath())))
                    .fileSize(attachFile.getFileSize())
                    .build();

            return downloadFileDto;
        } catch (FileNotFoundException e) {
            log.error("Occurred FileNotFoundException during download file");

            return new DownloadFileDto();
        } catch (IOException e) {
            log.error("Occurred IOException during download file");

            return new DownloadFileDto();
        } catch (Exception e) {
            log.error("Occurred UnknownException during download file");

            return new DownloadFileDto();
        }
    }

    public static PreviewAttachFileDto convertPreviewAttachFileDto(AttachFile attachFile) {
        PreviewAttachFileDto previewAttachFileDto = PreviewAttachFileDto.builder()
                .attachFileId(attachFile.getId())
                .boardId(attachFile.getBoardId())
                .fileName(attachFile.getFileName())
                .build();

        return previewAttachFileDto;
    }

    public static FileSeparationDto convertFileSeparationDto(List<AttachFile> attachFileList, List<AttachFileDto> attachFileDtoList) {
        FileSeparationDto fileSeparationDto = FileSeparationDto.builder()
                .attachFileList(attachFileList)
                .attachFileDtoList(attachFileDtoList)
                .build();
        return fileSeparationDto;
    }
}
