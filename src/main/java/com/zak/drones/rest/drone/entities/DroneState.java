package com.zak.drones.rest.drone.entities;

public enum DroneState {

    IDLE(1), LOADING(2), LOADED(3), DELIVERING(4), DELIVERED(5), RETURNING(6);

    private final int state;

    DroneState(int state) {
        this.state = state;
    }

    public int getDroneModel() {
        return state;
    }

    public static DroneState getValidDroneState(String modelName) {
        DroneState model;
        try {
            model = DroneState.valueOf(modelName);
        } catch(IllegalArgumentException ex) {
            throw new InvalidDroneModelException(String.format("Invalid drone state string %s. Supported models are : IDLE, LOADING, LOADED, DELIVERING, DELIVERED or RETURNING strings", modelName));
        }
        return model;
    }
}
