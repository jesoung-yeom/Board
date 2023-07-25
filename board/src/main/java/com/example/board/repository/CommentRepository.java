package com.example.board.repository;

import com.example.board.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    Comment save(Comment comment);

    List<Comment> findByBoardIdAndDeleted(Long id,String deleted);

    void deleteById(Long id);

}
