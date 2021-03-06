package com.zak.drones.rest.drone.services.validation;

import com.zak.drones.rest.drone.exceptions.DroneException;

public class WeightValidator {

    private static final int MAX_WEIGHT = 500;

    public void checkWeight(int weight) {
        if (weight <= 0) {
            throw new DroneException("The weight cannot be 0");
        }

        // check max weight
        if (weight > MAX_WEIGHT) {
            throw new DroneException(String.format("The weight is too large: max weight is %s",
                    MAX_WEIGHT));
        }
    }
}
