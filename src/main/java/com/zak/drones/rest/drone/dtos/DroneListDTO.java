package com.zak.drones.rest.drone.dtos;

import lombok.Data;

import java.util.ArrayList;

/**
 * DTO for the List of drones
 */
@Data
public class DroneListDTO implements java.io.Serializable {

    private ArrayList<DroneDTO> droneList;

    public DroneListDTO() {
        droneList = new ArrayList<>();
    }
}