package com.example.demo.exceptions;

import java.io.IOException;

public class FileNotFoundInDB extends RuntimeException {
    public FileNotFoundInDB(String message) {
        super(message);
    }
}
