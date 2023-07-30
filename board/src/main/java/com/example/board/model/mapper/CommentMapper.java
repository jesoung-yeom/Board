package com.example.board.model.mapper;

import com.example.board.model.Comment;
import com.example.board.model.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper MAPPER = Mappers.getMapper(CommentMapper.class);

    //convertComment
    @Mapping(target = "id", source = "commentDto.id")
    @Mapping(target = "updatedAt", source = "commentDto.updatedAt")
    @Mapping(target = "createdAt", expression = "java(commentDto.getCreatedAt() != null ? commentDto.getCreatedAt() : java.time.LocalDateTime.now())")
    @Mapping(target = "deleted", expression = "java(com.example.board.global.enums.EBoard.EDeletionStatus.EXIST.getStatus())")
    Comment toComment(CommentDto commentDto);

    @Mapping(target = "updatedAt", source = "comment.updatedAt")
    CommentDto toCommentDto(Comment comment);

}
