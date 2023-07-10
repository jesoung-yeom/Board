package com.example.board.global;

public class PageNationEnum {

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
}
