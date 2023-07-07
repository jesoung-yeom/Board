package com.example.board.service;

import com.example.board.model.AttachFile;
import com.example.board.model.dto.AttachFileDto;
import com.example.board.model.dto.BoardDto;
import com.example.board.repository.AttachFileRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardFileService {

    private final AttachFileRepository attachFileRepository;

    public boolean create(BoardDto boardDto) {
        if (!boardFileList.isEmpty()) {
        ArrayList<AttachFile> attachFileList = convertToBoardFile(boardDto);
                this.attachFileRepository.saveAll(attachFileList);

            return true;
        } else {

            return false;
        }
    }

    public boolean update(BoardDto boardDto) {
        if(!boardFileList.isEmpty()) {
        ArrayList<AttachFile> attachFileList = convertToBoardFile(boardDto);
            this.attachFileRepository.deleteAllByBoardId(boardDto.getId());
            if (!attachFileList.isEmpty()) {
                this.attachFileRepository.saveAll(attachFileList);
            }

            return true;
        } else {
            this.boardFileRepository.deleteAllByBoardId(boardDto.getId());

            return true;
        }
    }

    public boolean delete(BoardDto boardDto) {
            this.attachFileRepository.deleteAllByBoardId(boardDto.getId());

        return true;
    }

    public ArrayList<String> convertToBase64(BoardDto boardDto) {
        List<AttachFile> attachFileList = this.attachFileRepository.findAllByBoardId(boardDto.getId());
        ArrayList<String> convertList = new ArrayList<String>();
        for (int i = 0; i < attachFileList.size(); i++) {
            try {
                File file = new File(attachFileList.get(i).getFilePath());
                FileInputStream fis = new FileInputStream(file);
                byte[] imageBytes = new byte[(int) file.length()];
                fis.read(imageBytes);
                fis.close();
                convertList.add("data:image/" + attachFileList.get(i).getFileExtension() + ";base64," + Base64.getEncoder().encodeToString(imageBytes));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return convertList;
    }

    public ArrayList<AttachFile> convertToBoardFile(BoardDto boardDto) {
        ArrayList<AttachFile> boarFileList = new ArrayList<AttachFile>();
        Document doc = Jsoup.parse(boardDto.getContent());
        Elements imgTags = doc.select("img");
        for (Element imgTag : imgTags) {
            AttachFile attachFile = new AttachFile();
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
                attachFile.setBoardId(boardDto.getId())
                        .setFileName(fileName)
                        .setFileType("board")
                        .setFileSize((long) imageBytes.length)
                        .setFilePath(filePath)
                        .setFileExtension(fileExtension)
                        .setUploadedAt(LocalDateTime.now());
                boarFileList.add(attachFile);
                bis.close();
            } catch (IOException e) {
                // 예외 처리
            }
        }

        return boarFileList;
    }
}
