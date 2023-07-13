package com.example.board.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "file_attachment")
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

}
