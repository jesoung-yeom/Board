package com.example.board.model.dto;

import com.example.board.model.Board;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@NoArgsConstructor
public class BoardDto {

    private Long id;

    private String userId;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String deleted;

    public BoardDto(Board board, ArrayList<String> convertList) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.userId = board.getUserId();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        if (board.getUpdatedAt() != null) {
            this.updatedAt = board.getUpdatedAt();
        }
        if (convertList.size() > 0 && convertList.get(0).isEmpty() != true) {
            this.content = combineContent(board.getContent(), convertList);
        }
    }

    public BoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.userId = board.getUserId();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        if (board.getUpdatedAt() != null) {
            this.updatedAt = board.getUpdatedAt();
        }
    }

    public String combineContent(String content, ArrayList<String> convertList) {
        Document doc = Jsoup.parse(content);
        Elements images = doc.select("img");
        for (int i = 0; i < images.size(); i++) {
            images.get(i).attr("src", convertList.get(i).toString());

        return doc.toString();
    }

}
