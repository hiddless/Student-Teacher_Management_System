package com.hiddless.exceptions;

public class TeacherNotFoundException extends RuntimeException {

    public TeacherNotFoundException() {
        super("No Teacher Found");
    }


    public TeacherNotFoundException(String message) {
        super(message);
    }
}
