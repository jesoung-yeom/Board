package com.example.board.service;

import com.example.board.factory.AttachFileFactory;
import com.example.board.global.EBoard;
import com.example.board.global.EResponse;
import com.example.board.model.AttachFile;
import com.example.board.model.dto.*;
import com.example.board.repository.AttachFileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
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
@Slf4j
public class BoardFileService {

    private final AttachFileRepository attachFileRepository;

    @Value("${storage.path}")
    private String localPath;

    @Transactional
    public ResponseDto fileAttach(List<AttachFile> attachFileList) {
        EResponse.EResponseValue response = EResponse.EResponseValue.OK;

        try {
            this.attachFileRepository.saveAll(attachFileList);
        } catch (CannotGetJdbcConnectionException e) {
            log.error("Occurred CannotGetJdbcConnectionException during attach file");
            response = EResponse.EResponseValue.CNGJCE;
        } catch (DataAccessException e) {
            log.error("Occurred DataAccessException during attach file");
            response = EResponse.EResponseValue.DAE;
        } catch (Exception e) {
            log.error("Occurred UnknownException during attach file");
            response = EResponse.EResponseValue.UNE;
        } finally {

            return new ResponseDto(response);
        }
    }


    public List<PreviewAttachFileDto> getPreviewAttachFileList(BoardDto boardDto) {
        Optional<List<AttachFile>> attachList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(boardDto.getId(), EBoard.EFileType.ATTACH.getFileType(), EBoard.EDeletionStatus.EXIST.getStatus());

        if (!attachList.isPresent()) {

            return Collections.emptyList();
        }

        List<PreviewAttachFileDto> previewList = new ArrayList<>();

        for (AttachFile attachFile : attachList.get()) {
            PreviewAttachFileDto previewAttachFileDto = AttachFileFactory.convertPreviewAttachFileDto(attachFile);
            previewList.add(previewAttachFileDto);
        }

        return previewList;
    }

    @Transactional
    public ResponseDto saveFile(List<AttachFileDto> attachFileDtoList) {
        EResponse.EResponseValue response = EResponse.EResponseValue.OK;

        try {
            for (AttachFileDto attachFileDto : attachFileDtoList) {
                Path filepath = Path.of(localPath, attachFileDto.getMultipartFile().getOriginalFilename());
                Files.copy(attachFileDto.getMultipartFile().getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (FileNotFoundException e) {
            log.error("Occurred FileNotFoundException during save file");
            response = EResponse.EResponseValue.FNFE;
        } catch (IOException e) {
            log.error("Occurred IOException during save file");
            response = EResponse.EResponseValue.IOE;
        } catch (Exception e) {
            log.error("Unknown Exception");
            response = EResponse.EResponseValue.UNE;
        } finally {

            return new ResponseDto(response);
        }
    }


    public DownloadFileDto downloadFile(PreviewAttachFileDto previewAttachFileDto) {
        Optional<AttachFile> attachFile = this.attachFileRepository.findById(previewAttachFileDto.getAttachFileId());

        if (!attachFile.isPresent()) {
            log.error("Can't find attachFile");

            return new DownloadFileDto();
        }

        DownloadFileDto downloadFileDto = AttachFileFactory.convertDownloadFileDto(attachFile.get());

        return downloadFileDto;

    }

    @Transactional
    public ResponseDto create(BoardDto boardDto) {
        List<AttachFile> attachFileList = convertToBoardFile(boardDto);
        EResponse.EResponseValue response = EResponse.EResponseValue.OK;

        if (attachFileList.isEmpty()) {
            log.info("empty list");
            response = EResponse.EResponseValue.ETL;

            return new ResponseDto(response);
        }

        try {
            this.attachFileRepository.saveAll(attachFileList);
        } catch (CannotGetJdbcConnectionException e) {
            log.error("Occurred CannotGetJdbcConnectionException during create file");
            response = EResponse.EResponseValue.CNGJCE;
        } catch (DataAccessException e) {
            log.error("Occurred DataAccessException during create file");
            response = EResponse.EResponseValue.DAE;
        } catch (Exception e) {
            log.error("Occurred UnknownException during create file");
            response = EResponse.EResponseValue.UNE;
        } finally {

            return new ResponseDto(response);
        }
    }

    @Transactional
    public ResponseDto update(BoardDto boardDto) {
        List<AttachFile> attachFileList = convertToBoardFile(boardDto);
        Optional<List<AttachFile>> existAttachFileList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(boardDto.getId(), EBoard.EFileType.BOARD.getFileType(), EBoard.EDeletionStatus.EXIST.getStatus());
        EResponse.EResponseValue response = EResponse.EResponseValue.OK;

        List<AttachFile> resultAttachFileList = new ArrayList<>();

        for (AttachFile attachFile : existAttachFileList.get()) {
            attachFile.setDeleted(EBoard.EDeletionStatus.DELETE.getStatus());
        }

        if (!existAttachFileList.isPresent()) {
            response = EResponse.EResponseValue.ETL;

            return new ResponseDto(response);
        }

        if (!attachFileList.isEmpty()) {
            resultAttachFileList.addAll(attachFileList);
        }

        try {
            this.attachFileRepository.saveAll(resultAttachFileList);
        } catch (CannotGetJdbcConnectionException e) {
            log.error("Occurred CannotGetJdbcConnectionException during update file");
            response = EResponse.EResponseValue.CNGJCE;
        } catch (DataAccessException e) {
            log.error("Occurred DataAccessException during update file");
            response = EResponse.EResponseValue.DAE;
        } catch (Exception e) {
            log.error("Occurred UnknownException during update file");
            response = EResponse.EResponseValue.UNE;
        } finally {

            return new ResponseDto(response);
        }
    }

    @Transactional
    public ResponseDto fileUpdate(UploadFileDto uploadFileDto, List<AttachFile> attachFileList) {
        EResponse.EResponseValue response = EResponse.EResponseValue.OK;

        Optional<List<AttachFile>> existAttachFileList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(uploadFileDto.getBoardId(), EBoard.EFileType.ATTACH.getFileType(), EBoard.EDeletionStatus.EXIST.getStatus());

        List<AttachFile> resultAttachFileList = new ArrayList<>();

        for (AttachFile attachFile : existAttachFileList.get()) {
            attachFile.setDeleted(EBoard.EDeletionStatus.DELETE.getStatus());
        }

        if (!existAttachFileList.isPresent()) {
            log.info("empty list");
            response = EResponse.EResponseValue.ETL;

            return new ResponseDto(response);
        }

        resultAttachFileList.addAll(existAttachFileList.get());

        if (!attachFileList.isEmpty()) {
            resultAttachFileList.addAll(attachFileList);
        }

        try {
            this.attachFileRepository.saveAll(resultAttachFileList);
        } catch (CannotGetJdbcConnectionException e) {
            log.error("Occurred CannotGetJdbcConnectionException during update file");
            response = EResponse.EResponseValue.CNGJCE;
        } catch (DataAccessException e) {
            log.error("Occurred DataAccessException during update file");
            response = EResponse.EResponseValue.DAE;
        } catch (Exception e) {
            log.error("Occurred UnknownException during update file");
            response = EResponse.EResponseValue.UNE;
        } finally {

            return new ResponseDto(response);
        }

    }

    @Transactional
    public ResponseDto delete(BoardDto boardDto) {
        Optional<List<AttachFile>> attachFileList = this.attachFileRepository.findAllByBoardIdAndDeleted(boardDto.getId(), EBoard.EDeletionStatus.EXIST.getStatus());
        EResponse.EResponseValue response = EResponse.EResponseValue.OK;

        if (!attachFileList.isPresent()) {
            response = EResponse.EResponseValue.ETL;

            return new ResponseDto(response);
        }

        for (AttachFile attachFile : attachFileList.get()) {
            attachFile.setDeleted(EBoard.EDeletionStatus.DELETE.getStatus());
        }

        try {
            this.attachFileRepository.saveAll(attachFileList.get());
        } catch (CannotGetJdbcConnectionException e) {
            log.error("Occurred CannotGetJdbcConnectionException during delete file");
            response = EResponse.EResponseValue.CNGJCE;
        } catch (DataAccessException e) {
            log.error("Occurred DataAccessException during delete file");
            response = EResponse.EResponseValue.DAE;
        } catch (Exception e) {
            log.error("Occurred UnknownException during delete file");
            response = EResponse.EResponseValue.UNE;
        } finally {

            return new ResponseDto(response);
        }
    }

    public List<String> convertToBase64(BoardDto boardDto) {
        Optional<List<AttachFile>> attachFileList = this.attachFileRepository.findAllByBoardIdAndFileTypeAndDeleted(boardDto.getId(), EBoard.EFileType.BOARD.getFileType(), EBoard.EDeletionStatus.EXIST.getStatus());

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
            } catch (FileNotFoundException e) {
                log.error("Occurred FileNotFoundException during read file");

                return Collections.emptyList();
            } catch (IOException e) {
                log.error("Occurred IOException during conversion");

                return Collections.emptyList();
            } catch (Exception e) {
                log.error("Occurred UnknownException");

                return Collections.emptyList();
            }
        }

        return convertList;
    }

    public List<AttachFile> convertToBoardFile(BoardDto boardDto) {
        List<AttachFile> boardFileList = new ArrayList<AttachFile>();
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
                AttachFile attachFile = AttachFileFactory.convertAttachFile(boardDto, fileName, imageBytes.length, filePath, fileExtension);
                boardFileList.add(attachFile);
                bis.close();
            } catch (IOException e) {
                log.error("Occurred IOException during conversion");

                return Collections.emptyList();
            }
        }

        return boardFileList;
    }

    public FileSeparationDto setAttachFileList(UploadFileDto uploadFileDto) {
        List<MultipartFile> uploadFileList = uploadFileDto.getAttachFileList();
        List<AttachFile> attachFileList = new ArrayList<AttachFile>();
        List<AttachFileDto> attachFileDtoList = new ArrayList<>();

        for (MultipartFile multipartFile : uploadFileList) {
            Path filepath = Path.of(localPath, multipartFile.getOriginalFilename());
            AttachFileDto attachFileDto = AttachFileFactory.convertAttachFileDto(uploadFileDto, multipartFile, filepath.toString());
            AttachFile attachFile = AttachFileFactory.convertAttachFile(attachFileDto);

            attachFileDtoList.add(attachFileDto);
            attachFileList.add(attachFile);
        }

        Optional<FileSeparationDto> fileSeparationDto = Optional.ofNullable(AttachFileFactory.convertFileSeparationDto(attachFileList, attachFileDtoList));

        if (!fileSeparationDto.isPresent()) {
            return new FileSeparationDto();
        }

        return fileSeparationDto.get();
    }
}
