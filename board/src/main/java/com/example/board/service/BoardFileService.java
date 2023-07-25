package com.example.board.service;

import com.example.board.global.EConstant;
import com.example.board.model.AttachFile;
import com.example.board.model.dto.*;
import com.example.board.repository.AttachFileRepository;
import lombok.RequiredArgsConstructor;
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
import java.util.*;

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

            return true;
        }

        return false;
    }

    public List<PreviewAttachFileDto> getPreviewAttachFileList(BoardDto boardDto) {
        Optional<List<AttachFile>> attachList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(boardDto.getId(), EConstant.EFileType.attach.getFileType(), EConstant.EDeletionStatus.exist.getStatus());

        if (!attachList.isPresent()) {

            return Collections.emptyList();
        }

        List<PreviewAttachFileDto> previewList = new ArrayList<>();

        for (AttachFile attachFile : attachList.get()) {
            PreviewAttachFileDto previewAttachFileDto = new PreviewAttachFileDto(attachFile);
            previewList.add(previewAttachFileDto);
        }

        return previewList;
    }


    public void saveFile(MultipartFile file, Path filePath) {
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
        }
    }

    public DownloadFileDto downloadFile(PreviewAttachFileDto previewAttachFileDto) {
        Optional<AttachFile> attachFile = this.attachFileRepository.findById(previewAttachFileDto.getAttachFileId());

        if (!attachFile.isPresent()) {

            return new DownloadFileDto();
        }

        DownloadFileDto downloadFileDto = new DownloadFileDto(attachFile.get());

        return downloadFileDto;

    }

    public boolean create(BoardDto boardDto) {
        List<AttachFile> attachFileList = convertToBoardFile(boardDto);

        if (!attachFileList.isEmpty()) {
            this.attachFileRepository.saveAll(attachFileList);

            return true;
        }

        return false;
    }

    public boolean update(BoardDto boardDto) {
        List<AttachFile> attachFileList = convertToBoardFile(boardDto);
        Optional<List<AttachFile>> existAttachFileList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(boardDto.getId(), EConstant.EFileType.board.getFileType(), EConstant.EDeletionStatus.exist.getStatus());

        if (!existAttachFileList.isPresent()) {

            return true;
        }

        List<AttachFile> resultAttachFileList = new ArrayList<>();

        for (AttachFile attachFile : existAttachFileList.get()) {
            attachFile.setDeleted(EConstant.EDeletionStatus.delete.getStatus());
        }

        if (attachFileList.size() > 0 && attachFileList.get(0).getFileName() != null) {
            resultAttachFileList.addAll(attachFileList);
        }
        this.attachFileRepository.saveAll(resultAttachFileList);

        return true;
    }

    public boolean fileUpdate(UploadFileDto uploadFileDto) {
        if (uploadFileDto.getAttachFileList() != null) {
            Optional<List<AttachFile>> existAttachFileList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(uploadFileDto.getBoardId(), EConstant.EFileType.attach.getFileType(), EConstant.EDeletionStatus.exist.getStatus());

            if (!existAttachFileList.isPresent()) {

                return true;
            }

            List<AttachFile> resultAttachFileList = new ArrayList<>();
            List<AttachFile> attachFileList = this.setAttachFileList(uploadFileDto);

            for (AttachFile attachFile : existAttachFileList.get()) {
                attachFile.setDeleted(EConstant.EDeletionStatus.delete.getStatus());
            }

            resultAttachFileList.addAll(existAttachFileList.get());

            if (attachFileList.size() > 0 && attachFileList.get(0).getFileName().isEmpty() != true) {
                resultAttachFileList.addAll(attachFileList);
            }
            this.attachFileRepository.saveAll(resultAttachFileList);

            return true;
        }

        return true;
    }

    public boolean delete(BoardDto boardDto) {
        Optional<List<AttachFile>> attachFileList = this.attachFileRepository.findAllByBoardIdAndDeleted(boardDto.getId(), EConstant.EDeletionStatus.exist.getStatus());

        if (!attachFileList.isPresent()) {

            return true;
        }

        for (AttachFile attachFile : attachFileList.get()) {
            attachFile.setDeleted(EConstant.EDeletionStatus.delete.getStatus());
        }

        this.attachFileRepository.saveAll(attachFileList.get());

        return true;
    }

    public List<String> convertToBase64(BoardDto boardDto) {
        Optional<List<AttachFile>> attachFileList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(boardDto.getId(), EConstant.EFileType.board.getFileType(), EConstant.EDeletionStatus.exist.getStatus());

        if (!attachFileList.isPresent()) {

            return Collections.emptyList();
        }

        List<String> convertList = new ArrayList<>();

        for (AttachFile attachFile : attachFileList.get()) {
            try {
                File file = new File(attachFile.getFilePath());
                FileInputStream fis = new FileInputStream(file);
                byte[] imageBytes = new byte[(int) file.length()];
                fis.read(imageBytes);
                fis.close();
                convertList.add("data:image/" + attachFile.getFileExtension() + ";base64," + Base64.getEncoder().encodeToString(imageBytes));
            } catch (IOException e) {

                return Collections.emptyList();
            }
        }

        return convertList;
    }

    public List<AttachFile> convertToBoardFile(BoardDto boardDto) {
        List<AttachFile> boarFileList = new ArrayList<AttachFile>();
        Document doc = Jsoup.parse(boardDto.getContent());
        Elements imgTags = doc.select("img");

        for (Element imgTag : imgTags) {
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
                AttachFile attachFile = new AttachFile(boardDto, fileName, imageBytes.length, filePath, fileExtension);
                boarFileList.add(attachFile);
                bis.close();
            } catch (IOException e) {

                return Collections.emptyList();
            }
        }

        return boarFileList;
    }

    public List<AttachFile> setAttachFileList(UploadFileDto uploadFileDto) {
        List<MultipartFile> uploadFileList = uploadFileDto.getAttachFileList();
        List<AttachFile> attachFileList = new ArrayList<AttachFile>();

        for (MultipartFile multipartFile : uploadFileList) {
            Path filepath = Path.of(localPath, multipartFile.getOriginalFilename());
            AttachFileDto attachFileDto = new AttachFileDto(uploadFileDto, multipartFile, filepath.toString());
            AttachFile attachFile = new AttachFile(attachFileDto);
            this.saveFile(multipartFile, filepath);
            attachFileList.add(attachFile);
        }

        return attachFileList;
    }
}
