package com.zak.drones.rest.users.exceptions;

public class RoleInUseException extends RuntimeException {

    public RoleInUseException(String message) {
        super(message);
    }

}
