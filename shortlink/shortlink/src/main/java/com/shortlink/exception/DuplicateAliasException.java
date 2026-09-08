package com.shortlink.exception;

public class DuplicateAliasException extends RuntimeException {

    public DuplicateAliasException(String message) {
        super(message);
    }
}