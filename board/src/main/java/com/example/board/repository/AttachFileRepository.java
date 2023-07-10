package com.example.board.repository;

import com.example.board.model.AttachFile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {

    AttachFile findByBoardIdAndFileName(Long id, String fileName);
    List<AttachFile> findAllByBoardIdAndFileType(Long id, String type);

    List<AttachFile> findAllByBoardId(Long id);

    void deleteAllByBoardId(Long id);
    
    void deleteAllByBoardIdAndFileType(Long id,String fileType);

}
