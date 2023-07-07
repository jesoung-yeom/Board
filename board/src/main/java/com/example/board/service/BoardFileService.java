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
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardFileService {

    private final AttachFileRepository attachFileRepository;

    public boolean fileAttach(AttachFileDto attachFileDto) {

        for (int i = 0; i < attachFileDto.getAttachFiles().size(); i++) {
            AttachFile attachFile = new AttachFile();
            attachFile.setBoardId(attachFileDto.getBoardId())
                    .setFileName(attachFileDto.getAttachFiles().get(i).getName())
                    .setFileType("attach")
                    .setFilePath(attachFileDto.getAttachFiles().get(i).getOriginalFilename())
                    .setUploadedAt(LocalDateTime.now());
        }

        return false;
    }

    public String extractFileExtension(String target) {
        String[] result = target.split("[.]");
        return result[0];
    }

    public boolean saveFile(MultipartFile file) {
        try {
            Path filepath = Path.of("/Users/jesoung/desktop/filestorage/", file.getName());
            Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean create(BoardDto boardDto) {
        ArrayList<AttachFile> attachFileList = convertToBoardFile(boardDto);
        try {
            if (!attachFileList.isEmpty()) {
                this.attachFileRepository.saveAll(attachFileList);

                return true;
            }
        } catch (Exception e) {

            return false;
        }

        return true;
    }

    public boolean update(BoardDto boardDto) {
        ArrayList<AttachFile> attachFileList = convertToBoardFile(boardDto);
        try {
            this.attachFileRepository.deleteAllByBoardId(boardDto.getId());
            if (!attachFileList.isEmpty()) {
                this.attachFileRepository.saveAll(attachFileList);
            }

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public boolean delete(BoardDto boardDto) {
        try {
            this.attachFileRepository.deleteAllByBoardId(boardDto.getId());

            return true;
        } catch (Exception e) {

            return false;
        }
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
                return null;
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
                return null;
            }
        }

        return boarFileList;
    }
}
