package com.hiddless.exceptions;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException() {
        super("No Student found");
    }


    public StudentNotFoundException(String message) {
        super(message);
    }

}
