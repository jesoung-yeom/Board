package com.example.board.service;

import com.example.board.model.AttachFile;
import com.example.board.model.dto.BoardDto;
import com.example.board.model.dto.DownloadFileDto;
import com.example.board.model.dto.PreviewAttachFileDto;
import com.example.board.model.dto.UploadFileDto;
import com.example.board.global.EConstant;
import com.example.board.repository.AttachFileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class BoardFileService {

    private final AttachFileRepository attachFileRepository;

    @Value("${storage.path}")
    private String localPath;

    public boolean fileAttach(UploadFileDto uploadFileDto) {
        if (!uploadFileDto.getAttachFileList().get(0).equals("")) {
            List<AttachFile> fileList = new ArrayList<AttachFile>();
            for (int i = 0; i < uploadFileDto.getAttachFileList().size(); i++) {
                AttachFile attachFile = new AttachFile();
                attachFile.setBoardId(uploadFileDto.getBoardId())
                        .setFileName(uploadFileDto.getAttachFileList().get(i).getOriginalFilename())
                        .setFileType(EConstant.EFileType.attach.getFileType())
                        .setFileSize(uploadFileDto.getAttachFileList().get(i).getSize())
                        .setFilePath(saveFile(uploadFileDto.getAttachFileList().get(i)))
                        .setFileExtension(extractFileExtension(uploadFileDto.getAttachFileList().get(i).getOriginalFilename()))
                        .setUploadedAt(LocalDateTime.now())
                        .setDeleted(EConstant.EDeletionStatus.exist.getStatus());
                fileList.add(attachFile);
            }
            this.attachFileRepository.saveAll(fileList);
            //예외처리
        }
        return true;
    }

    public List<PreviewAttachFileDto> getPreviewAttachFileList(BoardDto boardDto) {
        List<AttachFile> attachList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(boardDto.getId(), EConstant.EFileType.attach.getFileType(), EConstant.EDeletionStatus.exist.getStatus());
        List<PreviewAttachFileDto> previewList = new ArrayList<>();
        for (int i = 0; i < attachList.size(); i++) {
            PreviewAttachFileDto previewAttachFileDto = PreviewAttachFileDto.builder()
                    .attachFileId(attachList.get(i).getId())
                    .boardId(attachList.get(i).getBoardId())
                    .fileName(attachList.get(i).getFileName())
                    .build();
            previewList.add(previewAttachFileDto);
        }
        return previewList;
    }

    public String extractFileExtension(String target) {
        String[] result = target.split("[.]");
        return result[result.length - 1];
    }

    public String saveFile(MultipartFile file) {
        Path filepath = Path.of(localPath, file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {

            return "";
        }

        return filepath.toString();
    }

    public DownloadFileDto downloadFile(PreviewAttachFileDto previewAttachFileDto) {
        Optional<AttachFile> attachFile = Optional.ofNullable(this.attachFileRepository.findById(previewAttachFileDto.getAttachFileId()).orElse(null));
        try {
            DownloadFileDto downloadFileDto = DownloadFileDto.builder()
                    .fileName(attachFile.get().getFileName())
                    .fileSize(attachFile.get().getFileSize())
                    .file(FileUtils.readFileToByteArray(new File(attachFile.get().getFilePath())))
                    .build();

            return downloadFileDto;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        List<AttachFile> attachFileList = convertToBoardFile(boardDto);

        try {

            this.attachFileRepository.deleteAllByBoardIdAndFileType(boardDto.getId(), EConstant.EFileType.board.getFileType());
            if (!attachFileList.isEmpty()) {
                this.attachFileRepository.saveAll(attachFileList);
            }

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public boolean removeAllFile(BoardDto boardDto) {
        List<AttachFile> removeList = this.attachFileRepository.findAllByBoardId(boardDto.getId());
        for (int i = 0; i < removeList.size(); i++) {
            File file = new File(removeList.get(i).getFilePath());
            file.delete();
        }
        return true;
    }

    public boolean removeAllAttachFile(UploadFileDto uploadFileDto) {
        List<AttachFile> removeList = this.attachFileRepository.findAllByBoardIdAndFileType(uploadFileDto.getBoardId(),  EConstant.EFileType.attach.getFileType());
        for (int i = 0; i < removeList.size(); i++) {
            File file = new File(removeList.get(i).getFilePath());
            file.delete();
        }
        return true;
    }

    public boolean fileUpdate(UploadFileDto uploadFileDto) {
        this.removeAllAttachFile(uploadFileDto);
        this.attachFileRepository.deleteAllByBoardIdAndFileType(uploadFileDto.getBoardId(),  EConstant.EFileType.attach.getFileType());
        List<AttachFile> fileList = new ArrayList<>();
        if (uploadFileDto.getAttachFileList() != null) {
            for (int i = 0; i < uploadFileDto.getAttachFileList().size(); i++) {
                AttachFile attachFile = new AttachFile();
                attachFile.setBoardId(uploadFileDto.getBoardId())
                        .setFileName(uploadFileDto.getAttachFileList().get(i).getOriginalFilename())
                        .setFileType( EConstant.EFileType.attach.getFileType())
                        .setFileSize(uploadFileDto.getAttachFileList().get(i).getSize())
                        .setFilePath(saveFile(uploadFileDto.getAttachFileList().get(i)))
                        .setFileExtension(extractFileExtension(uploadFileDto.getAttachFileList().get(i).getOriginalFilename()))
                        .setUploadedAt(LocalDateTime.now());
                fileList.add(attachFile);
            }
            this.attachFileRepository.saveAll(fileList);
        }
        return true;
    }

    public boolean delete(BoardDto boardDto) {
        try {
            this.removeAllFile(boardDto);
            this.attachFileRepository.deleteAllByBoardId(boardDto.getId());

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public ArrayList<String> convertToBase64(BoardDto boardDto) {
        List<AttachFile> attachFileList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(boardDto.getId(), EConstant.EFileType.board.getFileType(), EConstant.EDeletionStatus.exist.getStatus());
        ArrayList<String> convertList = new ArrayList<>();
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
                String filePath = localPath + fileName;
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                ImageIO.write(bufferedImage, fileExtension, bos);
                attachFile.setBoardId(boardDto.getId())
                        .setFileName(fileName)
                        .setFileType(EConstant.EFileType.board.getFileType())
                        .setFileSize((long) imageBytes.length)
                        .setFilePath(filePath)
                        .setFileExtension(fileExtension)
                        .setUploadedAt(LocalDateTime.now())
                        .setDeleted(EConstant.EDeletionStatus.exist.getStatus());
                boarFileList.add(attachFile);
                bis.close();
            } catch (IOException e) {
                return null;
            }
        }

        return boarFileList;
    }
}
