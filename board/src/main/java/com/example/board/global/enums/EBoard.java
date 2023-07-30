package com.example.board.global.enums;

public class EBoard {

    public enum EFileType {
        BOARD("board"), ATTACH("attach");

        private String type;

        private EFileType(String type) {
            this.type = type;
        }

        public String getFileType() {
            return type;
        }
    }

    public enum EDeletionStatus {
        EXIST("N"), DELETE("Y");

        private String status;

        private EDeletionStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }
    }


}
