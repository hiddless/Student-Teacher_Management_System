package com.hiddless.exceptions;

public class RegisterNotFoundException extends RuntimeException {

    public RegisterNotFoundException() {
        super("No register found");
    }

    public RegisterNotFoundException(String message) {
        super(message);
    }
}
}
