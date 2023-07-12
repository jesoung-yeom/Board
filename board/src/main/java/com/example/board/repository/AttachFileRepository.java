package com.example.board.repository;

import com.example.board.model.AttachFile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {

    AttachFile findByBoardIdAndFileName(Long id, String fileName);
    List<AttachFile> findAllByBoardIdAndFileTypeAndDeleted(Long id, String type,String deleted);

    Optional<AttachFile> findByBoardIdAndFileNameAndFileSizeAndFileTypeAndDeleted(Long id, String fileName, Long fileSize, String type, String deleted);

    List<AttachFile> findAllByBoardIdAndDeleted(Long id,String deleted);

}
