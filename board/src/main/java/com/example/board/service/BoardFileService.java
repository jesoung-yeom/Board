package com.example.board.service;

import com.example.board.global.EConstant;
import com.example.board.model.AttachFile;
import com.example.board.model.dto.BoardDto;
import com.example.board.model.dto.DownloadFileDto;
import com.example.board.model.dto.PreviewAttachFileDto;
import com.example.board.model.dto.UploadFileDto;
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
        if (uploadFileDto.getAttachFileList().get(0).getSize() != 0) {
            List<AttachFile> attachFileList = this.setAttachFileList(uploadFileDto);

            this.attachFileRepository.saveAll(attachFileList);
        }

        return true;
    }

    public List<PreviewAttachFileDto> getPreviewAttachFileList(BoardDto boardDto) {
        List<AttachFile> attachList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(boardDto.getId(), EConstant.EFileType.attach.getFileType(), EConstant.EDeletionStatus.exist.getStatus());
        List<PreviewAttachFileDto> previewList = new ArrayList<>();
        for(AttachFile attachFile : attachList) {
            PreviewAttachFileDto previewAttachFileDto = PreviewAttachFileDto.builder()
                    .attachFileId(attachFile.getId())
                    .boardId(attachFile.getBoardId())
                    .fileName(attachFile.getFileName())
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

        List<AttachFile> existAttachFileList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(boardDto.getId(), EConstant.EFileType.board.getFileType(), EConstant.EDeletionStatus.exist.getStatus());
        List<AttachFile> resultAttachFileList = new ArrayList<>();

        for(AttachFile attachFile : existAttachFileList) {
            attachFile.setDeleted(EConstant.EDeletionStatus.delete.getStatus());
        }


        if (attachFileList.size() > 0 && attachFileList.get(0).getFileName() != null) {
            resultAttachFileList.addAll(attachFileList);
        }

        try {

            this.attachFileRepository.saveAll(resultAttachFileList);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public boolean fileUpdate(UploadFileDto uploadFileDto) {
        if (uploadFileDto.getAttachFileList() != null) {
            List<AttachFile> existAttachFileList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(uploadFileDto.getBoardId(), EConstant.EFileType.attach.getFileType(), EConstant.EDeletionStatus.exist.getStatus());
            List<AttachFile> resultAttachFileList = new ArrayList<>();

            List<AttachFile> attachFileList = attachFileList = this.setAttachFileList(uploadFileDto);

            for (int i = 0; i < existAttachFileList.size(); i++) {
                existAttachFileList.get(i).setDeleted(EConstant.EDeletionStatus.delete.getStatus());
            }

            resultAttachFileList.addAll(existAttachFileList);

            if (attachFileList.size() > 0 && attachFileList.get(0).getFileName().isEmpty() != true) {
                resultAttachFileList.addAll(attachFileList);
            }

            this.attachFileRepository.saveAll(resultAttachFileList);

        }
        return true;
    }

    public boolean delete(BoardDto boardDto) {
        try {
            List<AttachFile> attachFileList = this.attachFileRepository.findAllByBoardIdAndDeleted(boardDto.getId(), EConstant.EDeletionStatus.exist.getStatus());
            for(AttachFile attachFile : attachFileList) {
                attachFile.setDeleted(EConstant.EDeletionStatus.delete.getStatus());
            }
            this.attachFileRepository.saveAll(attachFileList);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public ArrayList<String> convertToBase64(BoardDto boardDto) {
        List<AttachFile> attachFileList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(boardDto.getId(), EConstant.EFileType.board.getFileType(), EConstant.EDeletionStatus.exist.getStatus());
        ArrayList<String> convertList = new ArrayList<>();
        for(AttachFile attachFile : attachFileList) {
            try {
                File file = new File(attachFile.getFilePath());
                FileInputStream fis = new FileInputStream(file);
                byte[] imageBytes = new byte[(int) file.length()];
                fis.read(imageBytes);
                fis.close();
                convertList.add("data:image/" + attachFile.getFileExtension() + ";base64," + Base64.getEncoder().encodeToString(imageBytes));
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

    public List<AttachFile> setAttachFileList(UploadFileDto uploadFileDto) {
        List<MultipartFile> uploadFileList = uploadFileDto.getAttachFileList();
        List<AttachFile> attachFileList = new ArrayList<AttachFile>();

        for(MultipartFile multipartFile : uploadFileList) {
            AttachFile attachFile = new AttachFile();
            attachFile.setBoardId(uploadFileDto.getBoardId())
                    .setFileName(multipartFile.getOriginalFilename())
                    .setFileType(EConstant.EFileType.attach.getFileType())
                    .setFileSize(multipartFile.getSize())
                    .setFilePath(saveFile(multipartFile))
                    .setFileExtension(extractFileExtension(multipartFile.getOriginalFilename()))
                    .setUploadedAt(LocalDateTime.now())
                    .setDeleted(EConstant.EDeletionStatus.exist.getStatus());
            attachFileList.add(attachFile);
        }
        return attachFileList;
    }
}
