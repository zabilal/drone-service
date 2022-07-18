package com.zak.drones.rest.drone.services.validation;

import com.google.common.base.Strings;
import com.zak.drones.rest.drone.exceptions.MedicationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MedicationNameValidator {

    private static final String NAME_REGEX = "^[A-Za-z0-9_-]*$";

    private Pattern pattern;

    public MedicationNameValidator() {
        pattern = Pattern.compile(NAME_REGEX);
    }

    public void checkName(String medicationName) {
        if (Strings.isNullOrEmpty(medicationName)) {
            throw new MedicationException("The Medication name cannot be null or empty");
        }

        Matcher matcher = pattern.matcher(medicationName);
        if (!matcher.matches()) {
            throw new MedicationException(String.format("The medication name provided %s is not valid", medicationName));
        }
    }
}
