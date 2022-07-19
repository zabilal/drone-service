package com.zak.drones.rest.drone.services.validation;

import com.google.common.base.Strings;
import com.zak.drones.rest.drone.exceptions.DroneException;
import com.zak.drones.rest.users.exceptions.InvalidUserDataException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerialNumberValidator {

    private static final int MAX_SERIAL_NUMBER_LENGTH = 100;

    private static final String SERIAL_REGEX = "^\\d{5}$";

    private Pattern pattern;

    public SerialNumberValidator() {
        pattern = Pattern.compile(SERIAL_REGEX);
    }

    public void checkSerialNumber(String serialNumber) {
        if (Strings.isNullOrEmpty(serialNumber)) {
            throw new DroneException("The serial number cannot be null or empty");
        }

        // check max serial number length
        if (serialNumber.length() > MAX_SERIAL_NUMBER_LENGTH) {
            throw new DroneException(String.format("The serial number is too long: max number of chars is %s",
                    MAX_SERIAL_NUMBER_LENGTH));
        }

        Matcher matcher = pattern.matcher(serialNumber);
        if (!matcher.matches()) {
            throw new DroneException(String.format("The serial number provided %s is not valid", serialNumber));
        }
    }

}
