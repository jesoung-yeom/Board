package com.example.board.factory;

import com.example.board.global.EBoard;
import com.example.board.model.Board;
import com.example.board.model.dto.BoardDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.List;

public class BoardFactory {

    public static Board convertBoard(BoardDto boardDto) {
        Board board = new Board();

        board.setTitle(boardDto.getTitle())
                .setContent(extractContent(boardDto.getContent()))
                .setUserId(boardDto.getUserId())
                .setDeleted(EBoard.EDeletionStatus.EXIST.getStatus());

        if (boardDto.getId() != null) {
            board.setId(boardDto.getId());
        }

        if (boardDto.getCreatedAt() != null) {
            board.setCreatedAt(boardDto.getCreatedAt());
        } else {
            board.setCreatedAt(LocalDateTime.now());
        }

        if (boardDto.getUpdatedAt() != null) {
            board.setUpdatedAt(boardDto.getUpdatedAt());
        }

        return board;
    }

    public static String extractContent(String content) {
        Document doc = Jsoup.parse(content);
        Elements images = doc.select("img");

        for (int i = 0; i < images.size(); i++) {
            images.get(i).attr("src", "[image-" + i + "]");
        }

        return doc.toString();
    }

    public static BoardDto convertBoardDto(Board board, List<String> convertList) {
        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .userId(board.getUserId())
                .content(board.getContent())
                .createdAt(board.getCreatedAt()).build();

        if (board.getUpdatedAt() != null) {
            boardDto.setUpdatedAt(board.getUpdatedAt());
        }

        if (convertList.size() > 0 && convertList.get(0).isEmpty() != true) {
            boardDto.setContent(combineContent(board.getContent(), convertList));
        }

        return boardDto;
    }

    public static BoardDto convertBoardDto(Board board) {
        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .userId(board.getUserId())
                .content(board.getContent())
                .createdAt(board.getCreatedAt()).build();

        if (board.getUpdatedAt() != null) {
            boardDto.setUpdatedAt(board.getUpdatedAt());
        }

        return boardDto;
    }


    public static String combineContent(String content, List<String> convertList) {
        Document doc = Jsoup.parse(content);
        Elements images = doc.select("img");

        for (int i = 0; i < images.size(); i++) {
            images.get(i).attr("src", convertList.get(i).toString());
        }

        return doc.toString();
    }
}
