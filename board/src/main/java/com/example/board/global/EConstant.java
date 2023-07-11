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
        board("board"),attach("attach");

        private String type;

        private EFileType(String type) {
            this.type = type;
        }

        public String getFileType() {
            return type;
        }
    }
}
