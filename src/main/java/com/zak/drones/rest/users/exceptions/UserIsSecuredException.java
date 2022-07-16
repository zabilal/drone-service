package com.zak.drones.rest.users.exceptions;

public class UserIsSecuredException extends RuntimeException {

    public UserIsSecuredException(String message) {
        super(message);
    }

}
