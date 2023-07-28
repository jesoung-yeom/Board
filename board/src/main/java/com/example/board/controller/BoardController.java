package com.example.board.controller;

import com.example.board.global.EHome;
import com.example.board.model.AttachFile;
import com.example.board.model.Board;
import com.example.board.model.dto.*;
import com.example.board.service.BoardFileService;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("home")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final BoardFileService boardFileService;

    @GetMapping("")
    public String home(@RequestParam(defaultValue = "0") int page, HttpSession session, Model model) {
        if (session.getAttribute("user-id") != null) {
            Pageable pageable = PageRequest.of(page, EHome.EPage.PAGE.getPAGESIZE());
            Page<BoardDto> boardDtoList = this.boardService.findAll(pageable);
            List<BoardDto> contents = boardDtoList.getContent();
            int totalPages = boardDtoList.getTotalPages();
            model.addAttribute("boardDtoList", contents);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);

            return "home";
        } else {

            return "redirect:/";
        }
    }

    @GetMapping("/board")
    public String showBoard(BoardDto boardDto, Model model) {
        BoardDto resultBoardDto = this.boardService.findById(boardDto.getId(), this.boardFileService.convertToBase64(boardDto));

        if (ObjectUtils.isEmpty(resultBoardDto)) {
            log.error("Can't find board");

            return "page-404";
        }

        List<PreviewAttachFileDto> previewAttachFileDtoList = this.boardFileService.getPreviewAttachFileList(boardDto);
        List<CommentDto> commentDtoList = this.commentService.findByBoardId(boardDto.getId());
        model.addAttribute("boardDto", resultBoardDto);
        model.addAttribute("previewAttachFileDtoList", previewAttachFileDtoList);
        model.addAttribute("commentDtoList", commentDtoList);

        return "board";
    }

    @GetMapping("/board/download")
    public void downloadAttachFile(PreviewAttachFileDto previewAttachFileDto, HttpServletResponse response, Model model) {
        DownloadFileDto downloadFileDto = this.boardFileService.downloadFile(previewAttachFileDto);

        if (ObjectUtils.isEmpty(downloadFileDto)) {
            log.error("Can't download");
            //여기서 시도?
        }

        try {
            response.setContentType("application/octet-stream");
            response.setContentLength(Math.toIntExact(downloadFileDto.getFileSize()));
            response.setHeader("Content-Disposition", "attachment; fileName=\"" + UriUtils.encode(downloadFileDto.getFileName(), StandardCharsets.UTF_8) + "\";");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.getOutputStream().write(downloadFileDto.getFile());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (FileNotFoundException e) {
            log.error("Occurred FileNotFoundException during download");
        } catch (IOException e) {
            log.error("Occurred IOException during download");
        } catch (Exception e) {
            log.error("Occurred UnknownException during download");
        }
    }

    @GetMapping("/board/create")
    public String showWrite() {
        return "board-write";
    }

    @GetMapping("/board/update")
    public String showUpdateBoard(BoardDto boardDto, Model model) {
        BoardDto resultBoardDto = this.boardService.findById(boardDto.getId(), this.boardFileService.convertToBase64(boardDto));

        if (ObjectUtils.isEmpty(resultBoardDto)) {

            return "page-404";
        }

        List<PreviewAttachFileDto> previewAttachFileDtoList = this.boardFileService.getPreviewAttachFileList(boardDto);
        model.addAttribute("boardDto", resultBoardDto);
        model.addAttribute("previewAttachFileDtoList", previewAttachFileDtoList);

        return "board-update";
    }

    @PostMapping("/board/create")
    public String createBoard(HttpSession session, BoardDto boardDto, UploadFileDto uploadFileDto, Model model) {
        boardDto.setUserId(session.getAttribute("user-id").toString());
        Board board = this.boardService.create(boardDto);

        if (ObjectUtils.isEmpty(board)) {
            model.addAttribute("message", "생성오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }

        boardDto.setId(board.getId());
        uploadFileDto.setBoardId(boardDto.getId());

        ResponseDto createResponse = this.boardFileService.create(boardDto);

        if (!createResponse.getSuccess()) {
            model.addAttribute("message", "생성오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }

        FileSeparationDto fileSeparationDto = this.boardFileService.setAttachFileList(uploadFileDto);

        if (ObjectUtils.isEmpty(fileSeparationDto)) {

            return "redirect:/home";
        }

        List<AttachFile> attachFileList = fileSeparationDto.getAttachFileList();
        List<AttachFileDto> attachFileDtoList = fileSeparationDto.getAttachFileDtoList();
        ResponseDto fileSaveResponse = this.boardFileService.saveFile(attachFileDtoList);

        if (!fileSaveResponse.getSuccess()) {
            model.addAttribute("message", "생성오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }

        ResponseDto fileAttachResponse = this.boardFileService.fileAttach(attachFileList);

        if (!fileAttachResponse.getSuccess()) {
            model.addAttribute("message", "생성오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }

        return "redirect:/home";
    }

    @PostMapping("/board/delete")
    public String deleteBoard(BoardDto boardDto, Model model) {
        ResponseDto deleteResponse = this.boardService.delete(boardDto);

        if (!deleteResponse.getSuccess()) {
            model.addAttribute("message", "삭제오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }

        ResponseDto fileDeleteResponse = this.boardFileService.delete(boardDto);

        if (!fileDeleteResponse.getSuccess()) {
            model.addAttribute("message", "삭제오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
            model.addAttribute("replaceUrl", "/home");

            return "alert";

        }

        return "redirect:/home";
    }

    @PostMapping("/board/update")
    public String updateBoard(HttpSession session, BoardDto boardDto, UploadFileDto uploadFileDto, Model model) {
        boardDto.setUserId(session.getAttribute("user-id").toString());
        uploadFileDto.setBoardId(boardDto.getId());
        ResponseDto updateResponse = this.boardService.update(boardDto);

        if (!updateResponse.getSuccess()) {
            model.addAttribute("message", "수정오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }

        ResponseDto imageFileUpdateResponse = this.boardFileService.update(boardDto);

        if (!imageFileUpdateResponse.getSuccess()) {
            model.addAttribute("message", "수정오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }

        FileSeparationDto fileSeparationDto = this.boardFileService.setAttachFileList(uploadFileDto);
        List<AttachFile> attachFileList = fileSeparationDto.getAttachFileList();
        List<AttachFileDto> attachFileDtoList = fileSeparationDto.getAttachFileDtoList();

        ResponseDto fileSaveResponseDto = this.boardFileService.saveFile(attachFileDtoList);

        if (!fileSaveResponseDto.getSuccess()) {
            model.addAttribute("message", "수정오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }

        ResponseDto fileUpdateResponse = this.boardFileService.fileUpdate(uploadFileDto, attachFileList);

        if (!fileUpdateResponse.getSuccess()) {
            model.addAttribute("message", "수정오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }

        return "redirect:/home/board?id=" + boardDto.getId();
    }
}
