package com.example.board.model.mapper;

import com.example.board.model.Board;
import com.example.board.model.dto.BoardDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BoardMapper {

    BoardMapper MAPPER = Mappers.getMapper(BoardMapper.class);


    @Mapping(target = "id", source = "boardDto.id")
    @Mapping(target = "updatedAt", source = "boardDto.updatedAt")
    @Mapping(target = "content", expression = "java(com.example.board.global.utils.StringUtil.extractContent(boardDto.getContent()))")
    @Mapping(target = "createdAt", expression = "java(boardDto.getCreatedAt() != null ? boardDto.getCreatedAt() : java.time.LocalDateTime.now())")
    @Mapping(target = "deleted", expression = "java(com.example.board.global.enums.EBoard.EDeletionStatus.EXIST.getStatus())")
    Board toBoard(BoardDto boardDto);

    @Mapping(target = "updatedAt", source = "board.updatedAt")
    BoardDto toBoardDto(Board board);

    @Mapping(target = "updatedAt", source = "board.updatedAt")
    @Mapping(target = "content", expression = "java(convertList.isEmpty() ? board.getContent() : com.example.board.global.utils.StringUtil.combineContent(board.getContent(),convertList))")
    BoardDto toBoardDto(Board board, List<String> convertList);

}
