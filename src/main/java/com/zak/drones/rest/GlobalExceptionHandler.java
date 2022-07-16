package com.zak.drones.rest;

import com.zak.drones.rest.users.exceptions.ErrorDetails;
import com.zak.drones.rest.users.exceptions.InvalidEmailException;
import com.zak.drones.rest.users.exceptions.InvalidGenderException;
import com.zak.drones.rest.users.exceptions.InvalidLoginException;
import com.zak.drones.rest.users.exceptions.InvalidPermissionDataException;
import com.zak.drones.rest.users.exceptions.InvalidRoleDataException;
import com.zak.drones.rest.users.exceptions.InvalidRoleIdentifierException;
import com.zak.drones.rest.users.exceptions.InvalidUserDataException;
import com.zak.drones.rest.users.exceptions.InvalidUserIdentifierException;
import com.zak.drones.rest.users.exceptions.InvalidUsernameException;
import com.zak.drones.rest.users.exceptions.PermissionInUseException;
import com.zak.drones.rest.users.exceptions.PermissionNotFoundException;
import com.zak.drones.rest.users.exceptions.RoleInUseException;
import com.zak.drones.rest.users.exceptions.RoleNotFoundException;
import com.zak.drones.rest.users.exceptions.UserIsSecuredException;
import com.zak.drones.rest.users.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/** Handles the exceptions globally in this microservice */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidEmailException.class, InvalidGenderException.class, InvalidUserDataException.class,
            InvalidUserIdentifierException.class, InvalidRoleIdentifierException.class, InvalidUsernameException.class,
            InvalidLoginException.class, InvalidPermissionDataException.class, InvalidRoleDataException.class,
            RoleInUseException.class, PermissionInUseException.class})
    public ResponseEntity<ErrorDetails> handleAsBadRequest(RuntimeException ex) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RoleNotFoundException.class, UserNotFoundException.class, UserIsSecuredException.class,
            PermissionNotFoundException.class})
    public ResponseEntity<ErrorDetails> handleAsNotFound(RuntimeException ex) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

}
