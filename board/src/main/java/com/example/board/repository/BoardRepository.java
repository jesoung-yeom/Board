package com.example.board.repository;

import com.example.board.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAll();


    Page<Board> findAllByDeleted(String deleted, Pageable pageable);

    Board findById(long id);

    Board save(Board board);

    void deleteById(Long id);

}
