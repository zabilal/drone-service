package com.zak.drones.rest.drone.dtos;

import com.zak.drones.rest.drone.entities.Drone;
import com.zak.drones.rest.drone.entities.Medication;
import lombok.Data;

import java.util.ArrayList;

@Data
public class DispatchListDTO {

    private Drone drone;
    private ArrayList<Medication> medications;

    public DispatchListDTO() {
        medications = new ArrayList<>();
    }
}
