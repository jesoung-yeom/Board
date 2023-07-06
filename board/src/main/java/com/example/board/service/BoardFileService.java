package com.example.board.service;

import com.example.board.model.BoardFile;
import com.example.board.model.dto.BoardDto;
import com.example.board.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardFileService {

    private final BoardFileRepository boardFileRepository;

    public boolean create(BoardDto boardDto) {
        ArrayList<BoardFile> boardFileList = convertToBoardFile(boardDto);
        if (!boardFileList.isEmpty()) {
            boardFileRepository.saveAll(convertToBoardFile(boardDto));

            return true;
        } else {

            return false;
        }
    }

    public boolean delete(BoardDto boardDto) {
        this.boardFileRepository.deleteAllByBoardId(boardDto.getId());

        return true;
    }

    public ArrayList<String> convertToBase64(BoardDto boardDto) {
        List<BoardFile> boardFileList = this.boardFileRepository.findAllByBoardId(boardDto.getId());
        ArrayList<String> convertList = new ArrayList<String>();
        for (int i = 0; i < boardFileList.size(); i++) {
            try {
                File file = new File(boardFileList.get(i).getFilePath());
                FileInputStream fis = new FileInputStream(file);
                byte[] imageBytes = new byte[(int) file.length()];
                fis.read(imageBytes);
                fis.close();
                convertList.add("data:image/" + boardFileList.get(i).getFileExtension() + ";base64," + Base64.getEncoder().encodeToString(imageBytes));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return convertList;
    }

    public ArrayList<BoardFile> convertToBoardFile(BoardDto boardDto) {
        ArrayList<BoardFile> boarFileList = new ArrayList<BoardFile>();
        Document doc = Jsoup.parse(boardDto.getContent());
        Elements imgTags = doc.select("img");
        for (Element imgTag : imgTags) {
            BoardFile boardFile = new BoardFile();
            String base64Data = imgTag.attr("src");
            String base64Image = base64Data.split(",")[1];
            String fileName = imgTag.attr("data-filename");
            String[] parts = base64Data.split(";");
            String mimeType = parts[0].split(":")[1];
            String fileExtension = mimeType.split("/")[1];
            String style = imgTag.attr("style");

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                BufferedImage bufferedImage = ImageIO.read(bis);
                String filePath = "/Users/jesoung/desktop/filestorage/" + fileName;
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                ImageIO.write(bufferedImage, fileExtension, bos);
                boardFile.setBoardId(boardDto.getId())
                        .setFileName(fileName)
                        .setFileType("board")
                        .setFileSize((long) imageBytes.length)
                        .setFilePath(filePath)
                        .setFileExtension(fileExtension)
                        .setUploadedAt(Date.valueOf(LocalDate.now()));
                boarFileList.add(boardFile);
                bis.close();
            } catch (IOException e) {
                // 예외 처리
            }
        }

        return boarFileList;
    }
}
