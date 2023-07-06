package com.example.board.repository;

import com.example.board.model.Board;
import com.sun.mail.imap.protocol.ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAll();

    Board findById(long id);

    Board save(Board board);

    void deleteById(Long id);

}
