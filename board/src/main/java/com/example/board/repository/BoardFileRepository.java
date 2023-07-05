package com.example.board.repository;

import com.example.board.model.Account;
import com.example.board.model.BoardFile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {

    List<BoardFile> findAllByBoardId(Long id);
    void deleteAllByBoardId(Long id);

}
