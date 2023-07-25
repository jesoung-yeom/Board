package com.example.board.model;

import com.example.board.global.EConstant;
import com.example.board.model.dto.BoardDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "board_info")
@NoArgsConstructor
@Accessors(chain = true)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_id")
    private String userId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted")
    private String deleted;

    public Board(BoardDto boardDto) {
        if (boardDto.getId() != null) {
            this.id = boardDto.getId();
        }

        this.title = boardDto.getTitle();
        this.content = extractContent(boardDto.getContent());

        if (this.createdAt != null) {
            this.createdAt = boardDto.getCreatedAt();
        } else {
            this.createdAt = LocalDateTime.now();
        }

        this.userId = boardDto.getUserId();
        this.deleted = EConstant.EDeletionStatus.exist.getStatus();

        if (boardDto.getUpdatedAt() != null) {
            this.updatedAt = boardDto.getUpdatedAt();
        }
    }

    public String extractContent(String content) {
        Document doc = Jsoup.parse(content);
        Elements images = doc.select("img");

        for (int i = 0; i < images.size(); i++) {
            images.get(i).attr("src", "[image-" + i + "]");
        }

        return doc.toString();
    }

}
