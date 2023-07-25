package com.example.board.global;

public class EConstant {

    public enum EPage {
        page(10);

        private int pageSize;

        private EPage(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageSize() {
            return pageSize;
        }
    }

    public enum EFileType {
        board("board"), attach("attach");

        private String type;

        private EFileType(String type) {
            this.type = type;
        }

        public String getFileType() {
            return type;
        }
    }

    public enum EDeletionStatus {
        exist("N"), delete("Y");

        private String status;

        private EDeletionStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }
    }
}
