package com.example.personalfinancesapi;

public class Exceptions {

    public static class ExpenseNotFoundException extends RuntimeException {

        public ExpenseNotFoundException(String message) {
            super(message);
        }

        public ExpenseNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
