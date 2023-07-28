package com.example.board.global;

public class EHome {
    public enum EPage {
        PAGE(10);

        private int PAGESIZE;

        private EPage(int PAGESIZE) {
            this.PAGESIZE = PAGESIZE;
        }

        public int getPAGESIZE() {
            return PAGESIZE;
        }
    }
}
