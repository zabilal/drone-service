package com.zak.drones.rest.drone.entities;

import com.zak.drones.rest.drone.exceptions.DroneException;

public enum DroneModel {

    LIGHTWEIGHT(1), MIDDLEWEIGHT(2), CRUISERWEIGHT(3), HEAVYWEIGHT(4);

    private final int model;

    DroneModel(int model) {
        this.model = model;
    }

    public int getDroneModel() {
        return model;
    }

    public static DroneModel getValidDroneModel(String modelName) {
        DroneModel model;
        try {
            model = DroneModel.valueOf(modelName);
        } catch(IllegalArgumentException ex) {
            throw new DroneException(String.format("Invalid drone model string %s. Supported models are : LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT or HEAVYWEIGHT strings", modelName));
        }
        return model;
    }

}
