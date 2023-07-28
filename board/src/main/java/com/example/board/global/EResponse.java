package com.example.board.global;

public class EResponse {

    public enum EResponseValue {

        OK(true, 000, "Success"),
        ETL(true, 001, "List is empty, but no problem"),
        UNE(false, 101, "Occurred UnknownException"),
        IOE(false, 102, "Occurred IOException"),
        FNFE(false, 103, "Occurred FileNotFoundException"),
        NPE(false, 105, "Occurred Null PointException"),
        CNGJCE(false, 200, "Occurred CannotGetJdbcConnectionException"),
        DAE(false, 201, "Occurred DataAccessException"),
        CNF(false, 210, "Can't Find Board"),
        CFC(false, 211, "Can't find Comment");


        private boolean success;
        private int status;
        private String message;

        EResponseValue(boolean success, int status, String message) {
            this.success = success;
            this.status = status;
            this.message = message;
        }

        public boolean getSuccess() {
            return this.success;
        }

        public int getStatus() {
            return this.status;
        }

        public String getMessage() {
            return this.message;
        }
    }
}
