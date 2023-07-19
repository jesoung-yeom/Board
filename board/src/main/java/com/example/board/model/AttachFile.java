package com.example.board.model;

import com.example.board.global.EConstant;
import com.example.board.model.dto.AttachFileDto;
import com.example.board.model.dto.BoardDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "file_attachment")
@NoArgsConstructor
@Accessors(chain = true)
public class AttachFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "board_info_id")
    private Long boardId;

    @Column(name = "name")
    private String fileName;

    @Column(name = "type")
    private String fileType;

    @Column(name = "size")
    private Long fileSize;

    @Column(name = "path")
    private String filePath;

    @Column(name = "extension")
    private String fileExtension;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "deleted")
    private String deleted;

    public AttachFile(AttachFileDto attachFileDto) {
        this.boardId = attachFileDto.getBoardId();
        this.fileName = attachFileDto.getMultipartFile().getOriginalFilename();
        this.fileType = EConstant.EFileType.attach.getFileType();
        this.filePath = attachFileDto.getFilePath();
        this.fileSize = attachFileDto.getMultipartFile().getSize();
        this.fileExtension = extractFileExtension(attachFileDto.getMultipartFile().getOriginalFilename());
        this.uploadedAt = LocalDateTime.now();
        this.deleted = EConstant.EDeletionStatus.exist.getStatus();
    }

    public AttachFile(BoardDto boardDto, String fileName, int fileSize, String filePath, String fileExtension) {
        this.boardId = boardDto.getId();
        this.fileName = fileName;
        this.fileType = EConstant.EFileType.board.getFileType();
        this.fileSize = (long) fileSize;
        this.filePath = filePath;
        this.fileExtension = fileExtension;
        this.uploadedAt = LocalDateTime.now();
        this.deleted = EConstant.EDeletionStatus.exist.getStatus();
    }

    public String extractFileExtension(String target) {
        String[] result = target.split("[.]");

        return result[result.length - 1];
    }

}
