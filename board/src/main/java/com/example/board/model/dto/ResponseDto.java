package com.example.board.model.dto;

import com.example.board.global.EResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto {

    private Boolean success;

    private int status;

    private String message;

    public ResponseDto(EResponse.EResponseValue response) {
        this.success = response.getSuccess();
        this.status = response.getStatus();
        this.message = response.getMessage();
    }
}
