package com.zak.drones.rest.drone.dtos;

import com.zak.drones.rest.drone.entities.Drone;
import lombok.Data;

import java.io.Serializable;

@Data
public class DroneDTO implements Serializable {

    public DroneDTO(){}

    public DroneDTO(Drone drone){
        if (drone != null){
            this.id = drone.getId();
            this.serialNumber = drone.getSerialNumber();
            this.model = drone.getModel().name();
            this.weightLimit = drone.getWeightLimit();
            this.batteryCapacity = drone.getBatteryCapacity();
            this.state = drone.getState().name();
        }
    }

    private Long id;
    private String serialNumber;
    private String model;
    private int weightLimit;
    private int batteryCapacity;
    private String state;
}
