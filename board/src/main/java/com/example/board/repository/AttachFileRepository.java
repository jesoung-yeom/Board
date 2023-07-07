package com.example.board.repository;

import com.example.board.model.AttachFile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {

    List<AttachFile> findAllByBoardId(Long id);
    void deleteAllByBoardId(Long id);

}
