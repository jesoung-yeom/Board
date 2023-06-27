package com.example.board.repository;

import com.example.board.domain.Board;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class BoardRepository {

    private final EntityManager em;

    public List<Board> findAllBoards() {
        return em.createQuery("select c from Board c",Board.class).getResultList();
    }

    public Board findOneById(Long id) {
        return this.em.find(Board.class,id);
    }
    @Transactional
    public boolean create(Board board) {

            this.em.persist(board);
            return true;
    }
}
