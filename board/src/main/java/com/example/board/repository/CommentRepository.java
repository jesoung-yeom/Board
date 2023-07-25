package com.example.board.repository;

import com.example.board.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long id);

    Comment save(Comment comment);

    Optional<List<Comment>> findByBoardIdAndDeleted(Long id, String deleted);

    void deleteById(Long id);

}
